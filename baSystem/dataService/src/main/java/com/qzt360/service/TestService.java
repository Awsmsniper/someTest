package com.qzt360.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by zhaogj on 10/11/2016.
 */
@Service
@Slf4j
public class TestService {
    public void doPrint() {
        log.info("I'm in TestService");
    }
}
