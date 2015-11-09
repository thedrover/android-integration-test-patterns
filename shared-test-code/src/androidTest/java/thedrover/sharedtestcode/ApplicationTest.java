package thedrover.sharedtestcode;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Example instrumentation test using UiThreadTestRule because there is no Activity.
 * <p/>
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApplicationTest extends SharedTests {

  @Rule
  public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  @Test
  public void testAdd() {
    Assert.assertEquals(5, TestUtils.add(3, 2));
    Assert.assertEquals(7, TestUtils.subtract(10, 3));

  }


  @Test
  public void testLoadAsset() throws IOException {
    // using the application context
    AssetManager assetManager = InstrumentationRegistry.getTargetContext().getAssets();
    doX(assetManager);
  }


  @Test
  public void testAndroidTestResource() throws IOException {
    InputStream is = getClass().getClassLoader().getResourceAsStream("androidTest.txt");
    Assert.assertNotNull("Resource not found", is);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = reader.readLine();
    Assert.assertEquals("Android test", line);
    reader.close();
    is.close();
  }


  @Test
  public void testCommonTestResource() throws IOException {




    InputStream is = getClass().getClassLoader().getResourceAsStream("common.txt");
    Assert.assertNotNull("Resource not found", is);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = reader.readLine();
    Assert.assertEquals("common", line);
    reader.close();
    is.close();
  }

}