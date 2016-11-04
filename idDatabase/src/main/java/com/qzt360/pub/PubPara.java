package com.qzt360.pub;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PubPara {
    public static List<Map<String, Object>> listSystemLog = Collections
            .synchronizedList(new LinkedList<Map<String, Object>>());
}
