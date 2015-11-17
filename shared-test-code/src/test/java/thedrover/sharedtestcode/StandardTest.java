package thedrover.sharedtestcode;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by dougwright on 15/11/2015.
 */
public class StandardTest extends SharedTest {

  @Test
  public void testAdd() throws InterruptedException {
    Assert.assertEquals(5, TestUtils.add(3, 2));
    Assert.assertEquals(7, TestUtils.subtract(10, 3));
  }

  /**
   * Load a test resource in the unit test directory tree and verify the contents are as expected.
   */
  @Test
  public void testLoadUnitTestResource() throws IOException {
    checkFile(COM_EXAMPLE_UNITTEST_UNIT_TEST_RESOURCE_TXT, "unit test");
  }

  /**
   * Load a test resource in the common test directory tree and verify the contents are as expected.
   */
  @Test
  public void testLoadCommonResource() throws IOException {
    checkFile(COM_EXAMPLE_COMMON_COMMON_RESOURCE_TXT, "common");
  }



}
