package com.ninelook.wecard.tests;

import com.ninelook.wecard.tests.api.AllSocketApiTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
* 所有测试
* User: Simon
* Date: 13-7-1 下午4:49
*/
@RunWith(Suite.class)
@Suite.SuiteClasses({
//        AllUnitTests.class,
        AllSocketApiTests.class,
})
public class AllTests {

}
