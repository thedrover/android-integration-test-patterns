package thedrover.androidapiintegration.thedrover.testsupport.http;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dougwright on 4/04/2015.
 */
public class OkHttpWrapper extends HttpRequestWrapper {

  @Override
  protected void makeRequest(String url) {

    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder().url(url).build();

    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        String result = response.body().string();

        mResultHandler.onSuccess(result);
      } else {
        mResultHandler.onFailure(response.code(), response.message());
      }
    } catch (IOException e) {

      mResultHandler.onFailure(e);
    }

  }
}

// USe built-in async callback examples.

