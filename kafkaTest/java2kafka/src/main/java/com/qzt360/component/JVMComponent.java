package com.qzt360.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by zhaogj on 05/11/2016.
 */
@Slf4j
@Component
public class JVMComponent {
    public void outputJVMInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("\njvm内存使用情况\n最大可用内存:");
        sb.append(Runtime.getRuntime().maxMemory());
        sb.append("\n当前JVM空闲内存:");
        sb.append(Runtime.getRuntime().freeMemory());
        sb.append("\n当前JVM占用的内存总数:");
        sb.append(Runtime.getRuntime().totalMemory());
        sb.append("\n内存使用率:");
        long nMemRate = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) * 100 / Runtime.getRuntime().maxMemory();
        sb.append(nMemRate);
        sb.append("%");
        log.info(sb.toString());
    }
}
