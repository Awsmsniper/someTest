package com.qzt360.esTest;

import com.qzt360.utils.ESUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Hello world!
 */
@Slf4j
public class App {
    public static void main(String[] args) {
        log.info("start App");
        try {
            ESUtil.buildAll();
        } catch (Exception e) {
            log.error("", e);
        }
        ESTest esTest = new ESTest();
        esTest.doTest();
    }
}
