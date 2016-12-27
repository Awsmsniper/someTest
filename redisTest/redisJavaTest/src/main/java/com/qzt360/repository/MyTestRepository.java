package com.qzt360.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Created by zhaogj on 15/12/2016.
 */
@Repository
@Slf4j
public class MyTestRepository {
    public void doSomething() {
        log.info("系统启动时执行一次");
    }
}
