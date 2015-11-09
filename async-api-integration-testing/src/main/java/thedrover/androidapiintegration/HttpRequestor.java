package thedrover.androidapiintegration;

/**
 * Created by dougwright on 4/04/2015.
 */
public abstract class HttpRequestor {

  protected ResultHandler mResultHandler;

  /**
   * Make asynchronous HTTP request.
   * @param url
   */
  public abstract void makeRequest(String url);

  /**
   * Decode/process the result of the asynchronous action. App-specific. Consider creating method that can be overridden...
   *
   * @return
   */
  protected Object decodeResponse() {
    return null;
  }

  public void setResultHandler(ResultHandler resultHandler) {
    mResultHandler = resultHandler;

  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
