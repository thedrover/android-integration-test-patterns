package thedrover.androidapiintegration;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by dougwright on 4/04/2015.
 */
public class OkHttpRequestor extends HttpRequestor {
  @Override
  public void makeRequest(String s_url) {

  System.out.println("Yah");
    OkHttpClient client = new OkHttpClient();


    Request request = new Request.Builder()
        .url(s_url)
        .build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      String result = response.body().string();

      mResultHandler.onSuccess();
    } catch (IOException e) {
      e.printStackTrace();
      //mResultHandler.onFailure
    }

  }
}

// USe built-in async callback examples.

