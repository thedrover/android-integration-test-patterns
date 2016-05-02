package thedrover.tests;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import thedrover.androidapiintegration.thedrover.testsupport.http.HttpClientWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.HttpRequestWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.HttpURLConnectionWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.OkHttpWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.HttpResult;

/**
 *
 */
@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class HttpRobolectricIntegrationTest {

  @Rule
  public Timeout timeout = Timeout.seconds(TestUtil.TIMEOUT_MILLIS);

  private final HttpRequestWrapper mHttRequestWrapper;

  @ParameterizedRobolectricTestRunner.Parameters(name = "request wrapper = {0}")
  public static List<String[]> data() {
    return Arrays.asList(TestUtil.getArgs(HttpClientWrapper.class.getName()), TestUtil.getArgs(OkHttpWrapper.class.getName()), TestUtil.getArgs(HttpURLConnectionWrapper.class.getName()));
  }

  public HttpRobolectricIntegrationTest(String requestor) {
    mHttRequestWrapper = TestUtil.buildHttpRequestWrapper(requestor);
  }

  @Test
  public void testSimpleGet() throws ExecutionException, InterruptedException, JSONException, TimeoutException {
    HttpResult resultHandler = new HttpResult();

    //makeRequests posts a result that the HttpResultWatcherTask receivers.


    // Set up a future that will be interrogated until the asynchronous event is
    // received or the test times out.
    Callable<HttpResult> result = new HttpResultWatcherTask(resultHandler);
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<HttpResult> future = executorService.submit(result);

    // MUT
    mHttRequestWrapper.makeRequestOnThread("http://httpbin.org/headers", resultHandler);

   // TestUtil.assertFutureCompleted(mFuture);
    HttpResult r = future.get(TestUtil.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    Assert.assertEquals(HttpURLConnection.HTTP_OK, r.getResponseCode());
    String payload = r.getPayload();
    TestUtil.checkHeaders(payload);
  }


  @Test
  public void testSimpleGetFailure() throws ExecutionException, InterruptedException, TimeoutException {
    HttpResult resultHandler = new HttpResult();





    //makeRequests posts a result that the HttpResultWatcherTask receivers.


    // Set up a future that will be interrogated until the asynchronous event is
    // received or the test times out.
    Callable<HttpResult> result = new HttpResultWatcherTask(resultHandler);
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<HttpResult> mFuture = executorService.submit(result);

    // MUT
    mHttRequestWrapper.makeRequestOnThread("http://httpbin.org/headersnot", resultHandler);

    HttpResult r = mFuture.get(TestUtil.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    Assert.assertEquals(HttpURLConnection.HTTP_NOT_FOUND, r.getResponseCode());

    String payload = r.getPayload();
    Assert.assertNotNull(payload);
    Assert.assertEquals("payload = " + payload, "NOT FOUND", payload);
  }

}
