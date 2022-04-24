// eslint-disable-next-line import/no-extraneous-dependencies
import { NativeEventEmitter, NativeModules } from 'react-native';

const { RNBackgroundTimer } = NativeModules;
const Emitter = new NativeEventEmitter(RNBackgroundTimer);
const EVENT_NAME = 'BackgroundPollingCallback';

class BackgroundTimer {
  /**
   *
   * @param {number} delay
   * @param {string} pollingWorkerTag
   * @param {requestCallback} callback
   */
  start(delay, pollingWorkerTag, callback) {
    if (delay) {
      RNBackgroundTimer.startPolling(delay, pollingWorkerTag);
      this.eventListener = Emitter.addListener(EVENT_NAME, () => {
        callback();
      });
    }
  }

  /**
   *
   * @param {string} pollingWorkerTag
   */
  stop(pollingWorkerTag) {
    if (this.eventListener) {
      this.eventListener.remove();
    }
    RNBackgroundTimer.stopPolling(pollingWorkerTag);
  }
}

export default new BackgroundTimer();
