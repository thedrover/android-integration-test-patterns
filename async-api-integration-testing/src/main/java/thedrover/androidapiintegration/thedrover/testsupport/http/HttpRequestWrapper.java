package thedrover.androidapiintegration.thedrover.testsupport.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple wrapper that provides a consistent interface for using HttpURLConnection,
 * OK HTTP, or HTTPClient to make requests.
 */
public abstract class HttpRequestWrapper {


  protected ResultHandler mResultHandler;
  protected ExecutorService mGetConnectionThreadPool;

  public HttpRequestWrapper() {
    mGetConnectionThreadPool = Executors.newFixedThreadPool(2);
  }

  /**
   * Make asynchronous HTTP request.
   *
   * @param url
   */
  protected abstract void makeRequest(String url);

  /**
   *
   * @param url
   */
  public void makeRequestOnThread(final String url) {
    // TODO add HTTP method
    mGetConnectionThreadPool.submit(new Runnable() {

      @Override
      public void run() {
        makeRequest(url);
      }
    });
  }

  public void setResultHandler(ResultHandler resultHandler) {
    mResultHandler = resultHandler;

  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
