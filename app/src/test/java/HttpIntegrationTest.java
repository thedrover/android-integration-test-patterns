import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import thedrover.androidapiintegration.HttpClientRequestor;
import thedrover.androidapiintegration.HttpRequestor;
import thedrover.androidapiintegration.HttpURLConnectionRequestor;
import thedrover.androidapiintegration.OkHttpRequestor;

/**
 * Created by dougwright on 4/04/2015.
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class HttpIntegrationTest {

  protected static final int REQUESTOR_HTTP_CLIENT = 0;
  protected static final int REQUESTOR_OK = 1;
  protected static final int REQUESTOR_URL_CONNECTION = 2;
  public static final int TIMEOUT_MILLIS = 10000;


  // TODO maybe use factory instead of sub-classsing? parameterised junit tests to be clevah...
  protected HttpRequestor mHttpRequestor;
  private TestEventListener result;


  @ParameterizedRobolectricTestRunner.Parameters(name = "initialDB = {0}")
  public static List<Integer[]> data() {
    return Arrays.asList(getArgs(REQUESTOR_HTTP_CLIENT), getArgs(REQUESTOR_OK), getArgs(REQUESTOR_URL_CONNECTION));
  }

  /**
   * Convenience method to build the parameters with less clutter.
   *
   * @param initialDB the DB script to create the DB to upgrade.
   * @return class constructor arguments for parameterized JUnit test.
   */
  private static Integer[] getArgs(int initialDB) {
    // See https://github.com/robolectric/robolectric/issues/871 for why the
    // string values are used.
    return new Integer[]{initialDB};
  }


  public HttpIntegrationTest(Integer requestor) {
    switch (requestor)
    {
      case REQUESTOR_HTTP_CLIENT:
        mHttpRequestor = new HttpClientRequestor();
      case REQUESTOR_OK:
        mHttpRequestor = new OkHttpRequestor();
      case REQUESTOR_URL_CONNECTION:
        mHttpRequestor = new HttpURLConnectionRequestor();
      default:
    }
    Assert.assertNotNull(mHttpRequestor);
  }

  @Test
  public void testSimpleGet() throws ExecutionException, InterruptedException {


    // COMPARE ACTUAL RESULT WITH EXPECTED
    // TODO pass timeout in ctor.
    result = new TestEventListener(TIMEOUT_MILLIS);
    mHttpRequestor.setResultHandler(result);

    //makeRequests posts a result that the TestEventListener receivers.


    // Set up a future that will be interrogated until the asynchronous event is
    // received or the test times out.
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<Boolean> mFuture = executorService.submit(result);

    // MUT
    mHttpRequestor.makeRequest("http://httpbin.org/headers");

    assertFutureCompleted(mFuture);
    result.assertSuccessResult();
  }

  private void assertFutureCompleted(Future<Boolean> future) throws InterruptedException, ExecutionException {
    // TODO better message
    Assert.assertTrue("Future did not return a successful connection result: ", future.get());
  }

  @Test
  public void testSimpleGetFailure() throws ExecutionException, InterruptedException {


    // COMPARE ACTUAL RESULT WITH EXPECTED
    // TODO pass timeout in ctor.
    result = new TestEventListener(TIMEOUT_MILLIS);
    mHttpRequestor.setResultHandler(result);

    //makeRequests posts a result that the TestEventListener receivers.


    // Set up a future that will be interrogated until the asynchronous event is
    // received or the test times out.
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<Boolean> mFuture = executorService.submit(result);

    // MUT
    mHttpRequestor.makeRequest("http://httpbin.org/headersnot");

    assertFutureCompleted(mFuture);
    result.assertFailureResult();
  }

//  @Before
//  public void setup() {
//    mHttpRequestor = buildHttpRequestor();
//    // Use real HTTP layer with Robolectric
//    //Robolectric.getFakeHttpLayer().interceptHttpRequests(false);
//
//  }


 // protected abstract HttpRequestor buildHttpRequestor();

}
