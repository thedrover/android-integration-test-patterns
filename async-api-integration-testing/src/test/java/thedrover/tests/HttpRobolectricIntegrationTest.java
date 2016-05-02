package thedrover.tests;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import thedrover.androidapiintegration.thedrover.testsupport.http.HttpClientWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.HttpRequestWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.HttpURLConnectionWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.OkHttpWrapper;

/**
 *
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class HttpRobolectricIntegrationTest {

  @Rule
  public Timeout timeout = Timeout.seconds(50);


  // TODO maybe use factory instead of sub-classsing? parameterised junit tests to be clevah...
  protected HttpRequestWrapper mHttpRequestor;
  private TestEventListener result;


  @ParameterizedRobolectricTestRunner.Parameters(name = "request wrapper = {0}")
  public static List<String[]> data() {
    return Arrays.asList(TestUtil.getArgs(HttpClientWrapper.class.getName()), TestUtil.getArgs(OkHttpWrapper.class.getName()), TestUtil.getArgs(HttpURLConnectionWrapper.class.getName()));
  }


  public HttpRobolectricIntegrationTest(String requestor) {

    if (HttpClientWrapper.class.getName().equals(requestor)) {
      mHttpRequestor = new HttpClientWrapper();
    } else if (OkHttpWrapper.class.getName().equals(requestor)) {
      mHttpRequestor = new OkHttpWrapper();
    } else if (HttpURLConnectionWrapper.class.getName().equals(requestor)) {
      mHttpRequestor = new HttpURLConnectionWrapper();
    }
    Assert.assertNotNull(mHttpRequestor);
  }


  @Test
  public void testSimpleGet() throws ExecutionException, InterruptedException, JSONException {


    // COMPARE ACTUAL RESULT WITH EXPECTED
    // TODO pass timeout in ctor.
    result = new TestEventListener();
    mHttpRequestor.setResultHandler(result);

    //makeRequests posts a result that the TestEventListener receivers.


    // Set up a future that will be interrogated until the asynchronous event is
    // received or the test times out.
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<Boolean> mFuture = executorService.submit(result);

    // MUT
    mHttpRequestor.makeRequestOnThread("http://httpbin.org/headers");

    TestUtil.assertFutureCompleted(mFuture);
    result.assertSuccessResult();
    String payload = result.getPayload();
    TestUtil.checkHeaders(payload);


  }


  @Test
  public void testSimpleGetFailure() throws ExecutionException, InterruptedException {


    // COMPARE ACTUAL RESULT WITH EXPECTED
    // TODO pass timeout in ctor.
    result = new TestEventListener();
    mHttpRequestor.setResultHandler(result);

    //makeRequests posts a result that the TestEventListener receivers.


    // Set up a future that will be interrogated until the asynchronous event is
    // received or the test times out.
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<Boolean> mFuture = executorService.submit(result);

    // MUT
    mHttpRequestor.makeRequestOnThread("http://httpbin.org/headersnot");

    TestUtil.assertFutureCompleted(mFuture);
    result.assertFailureResult();

    String payload = result.getPayload();
    Assert.assertNotNull(payload);
    Assert.assertEquals("payload = " + payload, "NOT FOUND", payload);
  }

}
