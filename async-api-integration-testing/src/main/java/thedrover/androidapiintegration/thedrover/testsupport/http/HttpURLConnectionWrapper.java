package thedrover.androidapiintegration.thedrover.testsupport.http;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dougwright on 3/04/2015.
 */
public class HttpURLConnectionWrapper extends HttpRequestWrapper {

  @Override
  protected void makeRequest(String url) {

    URL urlx;
    try {
      urlx = new URL(url);


      HttpURLConnection urlConnection;
      urlConnection = (HttpURLConnection) urlx.openConnection();

      int responseCode = urlConnection.getResponseCode();
      // TODO 206 etc...

      int firstDigit = responseCode / 100;
      if (firstDigit == 2) {

        InputStream in = urlConnection.getInputStream();

        mResultHandler.onResult(responseCode, HttpClientWrapper.convertStreamToString(in));
      } else {
        mResultHandler.onResult(responseCode, urlConnection.getResponseMessage());
      }
    } catch (Exception e) {
      mResultHandler.onResult(e);
    }

  }

}
