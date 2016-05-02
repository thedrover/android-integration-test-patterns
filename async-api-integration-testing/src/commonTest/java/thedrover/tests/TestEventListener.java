package thedrover.tests;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.concurrent.Callable;

import thedrover.androidapiintegration.thedrover.testsupport.http.ResultHandler;

/**
 * Event listener that can be set up as a future that will be interrogated until
 * the asynchronous event is received or the test times out.
 * <p/>
 * A special result...apis need to allow this to be injected...can
 * <p/>
 * note that it doesn't wait the entire interval to keep things speedy.
 */
public class TestEventListener extends ResultHandler implements Callable<Boolean> {

  public static final int TIMEOUT_MILLIS = 100000;
  private static final int CHECK_INTERVAL_MILLIS = 100;

  @Rule
  public Timeout timeout = Timeout.seconds(10);

  private final int mMaxCheckCount;

  /**
   *
   */
  public TestEventListener() {
    // Approximate timeout is fine
    mMaxCheckCount = TIMEOUT_MILLIS * 1000 / CHECK_INTERVAL_MILLIS;
  }



  @Override
  public Boolean call() throws Exception {
    int i = 0;
    while (!mIsComplete) {
      synchronized (this) {
        wait(CHECK_INTERVAL_MILLIS);
        i++;
        if (i > mMaxCheckCount) {
          return false;
        }
      }
    }
    return mIsComplete;
  }


  @Override
  public String toString() {
    int waitDurationMs = CHECK_INTERVAL_MILLIS * mMaxCheckCount;
    return "thedrover.tests.TestEventListener - Async event wait time = " + waitDurationMs;
  }

  /**
   *
   */
  public void assertSuccessResult() {
    Assert.assertTrue(mSuccess);
  }

  public void assertFailureResult() {
    // TODO add failure messages etc to these...
    Assert.assertFalse(mSuccess);
  }


}
