package thedrover.androidapiintegration;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
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
import thedrover.tests.TestEventListener;
import thedrover.tests.TestUtil;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@RunWith(Parameterized.class)
public class HttpInstrumentationTest {

  @Rule
  public Timeout timeout = Timeout.seconds(TestUtil.TIMEOUT_MILLIS);

  private final HttpRequestWrapper mHttpRequestor;
  private TestEventListener result;
  
  @Parameterized.Parameters(name = "request wrapper = {0}")
  public static List<String[]> data() {
    return Arrays.asList(TestUtil.getArgs(HttpClientWrapper.class.getName()), TestUtil.getArgs(OkHttpWrapper.class.getName()), TestUtil.getArgs(HttpURLConnectionWrapper.class.getName()));
  }

  public HttpInstrumentationTest(String requestor) {
    mHttpRequestor = TestUtil.buildHttpRequestWrapper(requestor);
  }

  @Test
  public void testSimpleGet() throws ExecutionException, InterruptedException, JSONException {


    // COMPARE ACTUAL RESULT WITH EXPECTED

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
    Assert.assertEquals(HttpURLConnection.HTTP_OK, result.getResponseCode());
    String payload = result.getPayload();
    TestUtil.checkHeaders(payload);
  }

  @Test
  public void testSimpleGetFailure() throws ExecutionException, InterruptedException {


//    // COMPARE ACTUAL RESULT WITH EXPECTED
//
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
    Assert.assertEquals(HttpURLConnection.HTTP_NOT_FOUND, result.getResponseCode());

    String payload = result.getPayload();
    Assert.assertNotNull(payload);
    Assert.assertEquals("payload = " + payload, "NOT FOUND", payload);


  }

}