package com.shubhamd99.timer.polling;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.shubhamd99.timer.BackgroundTimerModule;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

public class PollingWorker extends Worker {

    private final static String DELAY_KEY = "initialDelayDuration";
    private final static String TAG_POLLING_WORK_KEY = "background_polling_tag";
    private final static int DEFAULT_DELAY = 20000;

    private final WorkerParameters params;
    private final Context context;

    public PollingWorker(
            @Nonnull Context ctx,
            @Nonnull WorkerParameters params) {
        super(ctx, params);
        this.params = params;
        this.context = ctx;
    }

    @NonNull
    @Override
    public Result doWork() {
        int pollingFrequency = params.getInputData().getInt(DELAY_KEY, DEFAULT_DELAY);
        String pollingWorkerKey = params.getInputData().getString(TAG_POLLING_WORK_KEY);

        Constraints constraints = new Constraints.Builder()
                .setRequiresStorageNotLow(true)
                .build();

        OneTimeWorkRequest pollingWorkerRequest =
                new OneTimeWorkRequest.Builder(PollingWorker.class)
                        .setConstraints(constraints)
                        .addTag(pollingWorkerKey)
                        .setInitialDelay(pollingFrequency, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager.getInstance(context).enqueueUniqueWork(
                pollingWorkerKey,
                ExistingWorkPolicy.APPEND,
                pollingWorkerRequest
        );

        BackgroundTimerModule.sendEvent();
        return Result.success();
    }

    public static void startPollingWorker(
            @Nonnull Context context,
            final int delay,
            final String pollingWorkerTag
    ) {

        Constraints constraints = new Constraints.Builder()
                .setRequiresStorageNotLow(true)
                .build();

        Data data = new Data.Builder()
                .putInt(DELAY_KEY, delay)
                .putString(TAG_POLLING_WORK_KEY, pollingWorkerTag)
                .build();

        OneTimeWorkRequest pollingWorkerRequest =
                new OneTimeWorkRequest.Builder(PollingWorker.class)
                        .setConstraints(constraints)
                        .setInputData(data)
                        .addTag(pollingWorkerTag)
                        .build();

        WorkManager.getInstance(context).enqueueUniqueWork(
                pollingWorkerTag,
                ExistingWorkPolicy.REPLACE,
                pollingWorkerRequest
        );
    }

    public static void clearPollingWorker(
            @Nonnull Context context,
            final String pollingWorkerTag
    ) {
        WorkManager.getInstance(context).cancelAllWorkByTag(
                pollingWorkerTag
        );
    }
}
