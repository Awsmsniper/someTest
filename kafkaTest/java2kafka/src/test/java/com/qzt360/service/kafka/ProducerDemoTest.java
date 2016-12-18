package test.com.qzt360.service.kafka; 

import com.qzt360.service.kafka.ProducerDemo;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* ProducerDemo Tester. 
* 
* @author <Authors name> 
* @since <pre>Dec 17, 2016</pre> 
* @version 1.0 
*/ 
public class ProducerDemoTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: doSend() 
* 
*/ 
@Test
public void testDoSend() throws Exception {
    ProducerDemo demo = new ProducerDemo();
    demo.doSend();
} 


} 
