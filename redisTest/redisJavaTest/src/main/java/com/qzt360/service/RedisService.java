package com.qzt360.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by zhaogj on 27/12/2016.
 */
@Service
@Slf4j
public class RedisService {
    public void someTest() {
        Jedis jedis = new Jedis("localhost");
        jedis.set("foo", "bars");
        log.info("foo:{}", jedis.get("foo"));
    }
}
