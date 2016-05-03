package thedrover.tests;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;

import thedrover.androidapiintegration.thedrover.testsupport.http.HttpClientWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.HttpRequestWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.HttpURLConnectionWrapper;
import thedrover.androidapiintegration.thedrover.testsupport.http.OkHttpWrapper;

/**
 * Created by dougwright on 2/05/2016.
 */
public class TestUtil {

  /**
   * How long to wait for an HTTP response when running the tests.
   */
  public static final int TIMEOUT_MILLIS = 10000;

  /**
   * HttpRequestWrapper implementation class names.
   */
  public static final List<String[]> REQUEST_WRAPPER_NAMES = Arrays.asList(getArgs(HttpClientWrapper.class.getName()), getArgs(OkHttpWrapper.class.getName()), getArgs(HttpURLConnectionWrapper.class.getName()));

  public static void checkHeaders(String payload) throws JSONException {
    JSONObject json = new JSONObject(payload);
    String host = json.getJSONObject("headers").getString("Host");
    Assert.assertEquals("httpbin.org", host);
  }


  /**
   * Convenience method to build the parameters with less clutter.
   *
   * @param requestWrapperId
   * @return class constructor arguments for parameterized JUnit test.
   */
  public static String[] getArgs(String requestWrapperId) {
    // See https://github.com/robolectric/robolectric/issues/871 for why the
    // string values are used.
    return new String[]{requestWrapperId};
  }

  public static HttpRequestWrapper buildHttpRequestWrapper(String requestor) {
    HttpRequestWrapper httpRequestWrapper = null;
    if (HttpClientWrapper.class.getName().equals(requestor)) {
      httpRequestWrapper = new HttpClientWrapper();
    } else if (OkHttpWrapper.class.getName().equals(requestor)) {
      httpRequestWrapper = new OkHttpWrapper();
    } else if (HttpURLConnectionWrapper.class.getName().equals(requestor)) {
      httpRequestWrapper = new HttpURLConnectionWrapper();
    }
    Assert.assertNotNull(httpRequestWrapper);
    return httpRequestWrapper;
  }
}
