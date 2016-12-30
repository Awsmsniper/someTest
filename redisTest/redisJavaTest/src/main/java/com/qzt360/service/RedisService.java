package com.qzt360.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaogj on 27/12/2016.
 */
@Service
@Slf4j
public class RedisService {
    // 非切片客户端链接对象
    private Jedis jedis;

    // 非切片链接池对象
    private JedisPool jedisPool;

    // 切片客户端链接对象
    private ShardedJedis shardedJedis;

    // 切片链接池
    private ShardedJedisPool shardedJedisPool;

    // IP以及端口
    private String strHost = "localhost";
    private int nPort = 6379;

    public void someTest() {
        Jedis jedis = new Jedis("localhost");
        jedis.set("foo", "bars");
        log.info("foo:{}", jedis.get("foo"));
    }

    private void initialPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(20);
        config.setMinIdle(5);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
        jedisPool = new JedisPool(config, strHost, nPort);
    }

    // 初始化切片池
    private void initialShardedPool() {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo(strHost, nPort, "master"));
        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }

}
