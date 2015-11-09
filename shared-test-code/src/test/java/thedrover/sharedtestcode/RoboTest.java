package thedrover.sharedtestcode;

import android.content.res.AssetManager;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Example unit test.
 *
 * TODO manifest path from command line
 */
@Config(manifest = "shared-test-code/src/main/AndroidManifest.xml", sdk=21)
@RunWith(RobolectricTestRunner.class)
public class RoboTest extends SharedTests {


  @Test
  public void testAdd() throws InterruptedException {
    Assert.assertEquals(5, TestUtils.add(3, 2));
    Assert.assertEquals(7, TestUtils.subtract(10, 3));

  }

  @Test
  public void testLoadAsset() throws IOException {
    // using the application context
    AssetManager assetManager = RuntimeEnvironment.application.getAssets();
    Assert.assertNotNull(assetManager);
    doX(assetManager);
  }


  @Test
  public void testLoadUnitTestResource() throws IOException {
    // TODO add a path
    checkFile("/test.txt", "unit test");


  }

  @Test
  public void testLoadCommonResource() throws IOException {
    checkFile("/common.txt", "common");
  }

  private void checkFile(String s, String common) throws IOException {


    InputStream is = getClass().getClassLoader().getResourceAsStream(s);
    Assert.assertNotNull("Resource not found", is);
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    String line = reader.readLine();
    Assert.assertEquals(common, line);
    reader.close();
    is.close();
  }

}
