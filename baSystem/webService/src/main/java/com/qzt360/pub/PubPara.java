package com.qzt360.pub;

import java.util.*;

public class PubPara {
    // key:strSN,value:actionTime
    public static Map<String, Long> hashOperatorLogin = new HashMap<String, Long>();
    public static List<Map<String, Object>> listSystemLog = Collections
            .synchronizedList(new LinkedList<Map<String, Object>>());
}
