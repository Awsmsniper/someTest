package com.qzt360.service;

import com.qzt360.repository.ESRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhaogj on 04/11/2016.
 */
@Slf4j
@Service
public class TestService {
    @Autowired
    private ESRepository es;

    public void doTest() {
        log.info("doTest read count:{}");//, es.nCount);
    }
}
