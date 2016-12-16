package test.com.qzt360.service;

import com.qzt360.service.MyTestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * MyTestService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Dec 16, 2016</pre>
 */
@Slf4j
public class MyTestServiceTest {
    @Before
    public void before() throws Exception {
        log.info("before test MyTestServiceTest");
    }

    @After
    public void after() throws Exception {
        log.info("after test MyTestServiceTest");
    }

    /**
     * Method: startJob()
     */
    @Test
    public void testStartJob() throws Exception {
//TODO: Test goes here... 
    }


    /**
     * Method: doSomething()
     */
    @Test
    public void testDoSomething() throws Exception {
        MyTestService myTestService = new MyTestService();
        myTestService.doSomething();
    }

} 
