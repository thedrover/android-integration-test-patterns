package thedrover.tests;

import java.util.concurrent.Callable;

import thedrover.androidapiintegration.thedrover.testsupport.http.HttpResult;

/**
 * Event listener that can be set up as a future that will be interrogated until
 * the asynchronous event is received or the test times out.
 * <p/>
 * A special result...apis need to allow this to be injected...can
 * <p/>
 * note that it doesn't wait the entire interval to keep things speedy.
 */
public class HttpResultWatcherTask implements Callable<HttpResult> {

  private static final int CHECK_INTERVAL_MILLIS = 100;

  private final HttpResult mResultHandler;

  public HttpResultWatcherTask(HttpResult resultHandler) {
    mResultHandler = resultHandler;
  }

  @Override
  public HttpResult call() throws Exception {

    while (mResultHandler.getResponseCode() == -1) {
      synchronized (this) {
        wait(CHECK_INTERVAL_MILLIS);
      }
    }
    return mResultHandler;
  }


}
