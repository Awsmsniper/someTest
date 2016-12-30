package test.com.qzt360.service;

import com.qzt360.service.RedisService;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RedisService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Dec 27, 2016</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisService.class)
public class RedisServiceTest {
    @Autowired
    private RedisService redis;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: someTest()
     */
    @Test
    public void testSomeTest() throws Exception {
        redis.someTest();
    }


} 
