package thedrover.androidapiintegration;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import thedrover.androidapiintegration.thedrover.testsupport.http.HttpRequestWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.HttpResult;
import thedrover.tests.HttpResultWatcherTask;
import thedrover.tests.TestUtil;

@RunWith(Parameterized.class)
public class HttpInstrumentationTest {

  private final HttpRequestWrapper mHttpRequestor;

  @Parameterized.Parameters(name = "request wrapper = {0}")
  public static List<String[]> data() {
    return TestUtil.REQUEST_WRAPPER_NAMES;
  }

  public HttpInstrumentationTest(String requestWrapper) {
    mHttpRequestor = TestUtil.buildHttpRequestWrapper(requestWrapper);
  }

  /**
   * Perform an HTTP GET request to http://httpbin.org/headers and check the simple JSON response is as expected.
   */
  @Test
  public void testSimpleGet() throws ExecutionException, InterruptedException, JSONException, TimeoutException {

    HttpResult resultHandler = new HttpResult();
    Callable<HttpResult> result = new HttpResultWatcherTask(resultHandler);


    //makeRequests posts a result that the HttpResultWatcherTask receivers.


    // Set up a future that will be interrogated until the asynchronous event is
    // received or the test times out.
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<HttpResult> mFuture = executorService.submit(result);

    // MUT
    mHttpRequestor.makeRequestOnThread("http://httpbin.org/headers", resultHandler);

    HttpResult r = mFuture.get(TestUtil.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    Assert.assertEquals(HttpURLConnection.HTTP_OK, r.getResponseCode());

    String payload = r.getPayload();
    TestUtil.checkHeaders(payload);
  }

  /**
   * Perform an HTTP GET request for a non-existent URL and confirm the 404 response is handled.
   */
  @Test
  public void testSimpleGetFailure() throws ExecutionException, InterruptedException, TimeoutException {


//    // COMPARE ACTUAL RESULT WITH EXPECTED
    HttpResult resultHandler = new HttpResult();
    Callable<HttpResult> result = new HttpResultWatcherTask(resultHandler);


    //makeRequests posts a result that the HttpResultWatcherTask receivers.


    // Set up a future that will be interrogated until the asynchronous event is
    // received or the test times out.
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<HttpResult> futureResult = executorService.submit(result);


    // MUT
    mHttpRequestor.makeRequestOnThread("http://httpbin.org/headersnot", resultHandler);

    HttpResult r = futureResult.get(TestUtil.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    Assert.assertEquals(HttpURLConnection.HTTP_NOT_FOUND, r.getResponseCode());

    String payload = r.getPayload();
    Assert.assertNotNull(payload);
    Assert.assertEquals("payload = " + payload, "NOT FOUND", payload);


  }

}