package test.com.qzt360.service.kafka; 

import com.qzt360.service.kafka.TMacService;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* TMacService Tester. 
* 
* @author <Authors name> 
* @since <pre>Dec 18, 2016</pre> 
* @version 1.0 
*/ 
public class TMacServiceTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: tmac2Kafka() 
* 
*/ 
@Test
public void testTmac2Kafka() throws Exception {
    TMacService tmac = new TMacService();
    tmac.tmac2Kafka();
} 


} 
