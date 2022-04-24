// eslint-disable-next-line import/no-extraneous-dependencies
import { NativeEventEmitter, NativeModules } from 'react-native';

const { RNBackgroundTimer } = NativeModules;
const Emitter = new NativeEventEmitter(RNBackgroundTimer);
const EVENT_NAME = 'BackgroundPollingCallback';

class BackgroundTimer {
  constructor() {
    this.eventListener = null;
    this.tags = {};
  }

  /**
   *
   * @param {number} delay
   * @param {string} pollingWorkerTag
   * @param {requestCallback} callback
   */
  start(delay, pollingWorkerTag, callback) {
    if (delay) {
      if (this.tags[pollingWorkerTag]) {
        if (this.eventListener) {
          this.eventListener.remove();
        }
        delete this.tags[pollingWorkerTag];
      }

      this.tags[pollingWorkerTag] = true;
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
