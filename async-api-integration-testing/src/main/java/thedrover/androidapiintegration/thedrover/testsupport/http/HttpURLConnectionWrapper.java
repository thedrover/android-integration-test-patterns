package thedrover.androidapiintegration.thedrover.testsupport.http;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dougwright on 3/04/2015.
 */
public class HttpURLConnectionWrapper extends HttpRequestWrapper {

  /**
   * Inject for test....general listener is better...
   *
   * @param url
   */
  @Override
  protected void makeRequest(String url) {


    // TODO this is synchronous.


    // TODO do a successful get URL connection to httpbin.org.

    URL urlx;
    try {
      urlx = new URL(url);


      HttpURLConnection urlConnection;
      urlConnection = (HttpURLConnection) urlx.openConnection();

      int responseCode = urlConnection.getResponseCode();
      // TODO 206 etc...
      if (responseCode == HttpURLConnection.HTTP_OK) {

        InputStream in = urlConnection.getInputStream();

        mResultHandler.onSuccess(HttpClientWrapper.convertStreamToString(in));
      } else {
        mResultHandler.onFailure(responseCode, urlConnection.getResponseMessage());
      }
    } catch (Exception e) {
      mResultHandler.onFailure(e);
    }

  }

}
