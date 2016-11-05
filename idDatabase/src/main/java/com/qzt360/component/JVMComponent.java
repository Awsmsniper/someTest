package com.qzt360.component;

import com.qzt360.utils.FuncUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by zhaogj on 05/11/2016.
 */
@Slf4j
@Component
public class JVMComponent {
    public void outputJVMInfo(){
        log.info(FuncUtil.getMemoryStatus());
    }
}
