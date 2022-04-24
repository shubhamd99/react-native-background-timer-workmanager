package com.shubhamd99.timer;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.shubhamd99.timer.polling.PollingWorker;

public class BackgroundTimerModule extends ReactContextBaseJavaModule {

    private static ReactContext reactContext;
    private final static String BACKGROUND_TIMER_CALLBACK_EVENT = "BackgroundPollingCallback";

    public BackgroundTimerModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return "RNBackgroundTimer";
    }

    @ReactMethod
    public void startPolling(final int delay, final String pollingWorkerTag) {
        PollingWorker.clearPollingWorker(
                reactContext,
                pollingWorkerTag
        );
        PollingWorker.startPollingWorker(
                reactContext,
                delay,
                pollingWorkerTag
        );
    }

    @ReactMethod
    public void stopPolling(final String pollingWorkerTag) {
        PollingWorker.clearPollingWorker(
                reactContext,
                pollingWorkerTag
        );
    }

    public static void sendEvent() {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(BACKGROUND_TIMER_CALLBACK_EVENT, null);
    }

}
