import org.junit.Assert;

import java.util.concurrent.Callable;

import thedrover.androidapiintegration.ResultHandler;

/**
 * Event listener that can be set up as a future that will be interrogated until
 * the asynchronous event is received or the test times out.
 * <p/>
 * A special result...apis need to allow this to be injected...can
 * <p/>
 * note that it doesn't wait the entire interval to keep things speedy.
 */
public class TestEventListener extends ResultHandler implements Callable<Boolean> {

  private static final int CHECK_INTERVAL_MILLIS = 100;

  private boolean mEventReceived = false;
  private final int mMaxCheckCount;

  /**
   *
   * @param timeoutMillis
   */
  public TestEventListener(int timeoutMillis) {
    // Approximate timeout is fine
    mMaxCheckCount = timeoutMillis * 1000 / CHECK_INTERVAL_MILLIS;
  }

  @Override
  public Boolean call() throws Exception {
    int i = 0;
    while (!mSuccess) {
      synchronized (this) {
        wait(CHECK_INTERVAL_MILLIS);
        i++;
        if (i > mMaxCheckCount) {
          return false;
        }
      }
    }
    return mSuccess;
  }


  @Override
  public String toString() {
    int waitDurationMs = CHECK_INTERVAL_MILLIS * mMaxCheckCount;
    return "TestEventListener - Async event wait time = " + waitDurationMs;
  }

  /**
   *
   */
  public void assertSuccessResult() {
    Assert.assertTrue(mSuccess);
  }

  public void assertFailureResult() {
    Assert.assertFalse(mSuccess);
  }
}
