package thedrover.androidapiintegration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dougwright on 3/04/2015.
 */
public class HttpURLConnectionRequestor extends HttpRequestor {

  /**
   * Inject for test....general listener is better...
   * @param url
   */
  public void makeRequest(String url) {


    // TODO do a successful get URL connection to httpbin.org.

    URL urlx;
    try {
      urlx = new URL(url);



      HttpURLConnection urlConnection;
      urlConnection = (HttpURLConnection) urlx.openConnection();

      int responseCode = urlConnection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {

        InputStream in = urlConnection.getInputStream();

        InputStreamReader isw = new InputStreamReader(in);

        int data = isw.read();
        while (data != -1) {
          char current = (char) data;
          data = isw.read();
          System.out.print(current);
        }

        // TODO could decode JSON etc...herer
        Object decode = decodeResponse();
        mResultHandler.onSuccess();
      } else {
        mResultHandler.onFailure(responseCode);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
