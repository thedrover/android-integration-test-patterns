package thedrover.androidapiintegration.thedrover.testsupport.http;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

/**
 * {@link org.apache.http.client.HttpClient} has been deprecated in the Android SDK. This test pattern may provde a useful
 * approach to create tests and migrate to an alternative.
 * <p/>
 * Created by dougwright on 4/04/2015.
 */
@SuppressWarnings("deprecation")
public class HttpClientWrapper extends HttpRequestWrapper {

  private static final String UTF_8 = "UTF-8";

  private static final String cHttp = "http";
  private static final String cHttps = "https";
  private static final int cHttpPort = 80;
  private static final int cHttpsPort = 443;

  // The timeout in milliseconds to wait until a connection is established.
  private static final int cTimeoutConnection = 60000;

  // The timeout in milliseconds to wait for data (socket timeout SO_TIMEOUT)
  private static final int cTimeoutSocket = 60000;


  private AbstractHttpClient mHttpClient;

  public HttpClientWrapper() {
    super();


    SchemeRegistry lSchemeRegistry = new SchemeRegistry();
    lSchemeRegistry.register(new Scheme(cHttp, PlainSocketFactory.getSocketFactory(), cHttpPort));
    lSchemeRegistry.register(new Scheme(cHttps, SSLSocketFactory.getSocketFactory(), cHttpsPort));

    HttpParams lParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(lParams, cTimeoutConnection);
    HttpConnectionParams.setSoTimeout(lParams, cTimeoutSocket);
    ClientConnectionManager lConnMgr = new ThreadSafeClientConnManager(lParams, lSchemeRegistry);
    mHttpClient = new DefaultHttpClient(lConnMgr, lParams);
    mHttpClient.setReuseStrategy(new NoConnectionReuseStrategy());
  }

  @Override
  protected void makeRequest(final String url) {

    InputStream response = null;
    try {


      Header[] headers = {new BasicHeader("Accept-Encoding", "gzip"),};// {


      HttpRequestBase httpRequest = new HttpGet(url);


      for (Header header : headers) {
        if (header != null) {
          httpRequest.addHeader(header);
        }
      }


      // The underlying HTTP connection is still held by the response
      // object
      // to allow the response content to be streamed directly from the
      // network socket.
      // In order to ensure correct deallocation of system resources
      // the user MUST either fully consume the response content or abort
      // request
      // execution by calling HttpGet#releaseConnection().
      // sLogger.info("Making request " + httpRequest);

      HttpResponse response1 = mHttpClient.execute(httpRequest);
      InputStream instream = null;
      int statusCode = response1.getStatusLine().getStatusCode();

      // TODO 206 etc
      if (statusCode != HttpStatus.SC_OK) {


        mResultHandler.onResult(statusCode, response1.getStatusLine().getReasonPhrase());

      } else {

        // Success
        instream = response1.getEntity().getContent();
        Header contentEncoding = response1.getFirstHeader("Content-Encoding");
        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
          instream = new GZIPInputStream(instream);
        }

        mResultHandler.onResult(statusCode, convertStreamToString(instream));
      }

      response = instream;

      Object responsePayload = null;


    } catch (Exception e) {
      mResultHandler.onResult(e);
    } finally {
      if (response != null) {
        try {
          response.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  /**
   * To convert the InputStream to String we use the BufferedReader.readLine()
   * method. We iterate until the BufferedReader return null which means there's
   * no more data to read. Each line will appended to a StringBuilder and
   * returned as String.
   *
   * @throws UnsupportedEncodingException
   */
  public static String convertStreamToString(InputStream is) throws IOException {

    BufferedReader reader = new BufferedReader(new InputStreamReader(is, UTF_8));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
      reader.close();
    } catch (IOException e) {

      throw e;
    } finally {
      try {
        is.close();
        reader.close();
      } catch (IOException e) {

        throw e;
      }
    }

    return sb.toString();
  }


}
