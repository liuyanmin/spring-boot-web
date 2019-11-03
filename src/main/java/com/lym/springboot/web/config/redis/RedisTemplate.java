package com.lym.springboot.web.config.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @ClassName RedisTemplate
 * @Description Redis 模板工具类，几乎封装Redis能用到的所有方法
 * @Author LYM
 * @Date 2018/12/11 15:01
 * @Version 1.3.2
 */
public class RedisTemplate {
    private static final Logger LOG = LoggerFactory.getLogger(RedisTemplate.class);
    
    private ShardedJedisPool shardedJedisPool;

    public RedisTemplate() {
    }

    public RedisTemplate(String host, String port, String password, String db) {
        try {
            newInstance(host, port, password, db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ******************************************connect start*******************************************
    public synchronized ShardedJedis getRedisClient() {
        try {
            ShardedJedis shardJedis = shardedJedisPool.getResource();
            return shardJedis;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("getRedisClent error", e);
        }
        return null;
    }

    public void returnResource(ShardedJedis shardedJedis) {
        shardedJedisPool.returnResource(shardedJedis);
    }

    public void returnResource(ShardedJedis shardedJedis, boolean broken) {
        if (broken) {
            shardedJedisPool.returnBrokenResource(shardedJedis);
        } else {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }

    public RedisTemplate newInstance(String host, String port, String password, String db) throws Exception {
        if (StringUtils.isEmpty(host)) {
            throw new Exception("host is null");
        }
        if (!NumberUtils.isNumber(port)) {
            throw new Exception("host is not number");
        }
        if (!NumberUtils.isNumber(db)) {
            throw new Exception("db is not number");
        }

        return newInstance(host, Integer.valueOf(port), password, Integer.valueOf(db));
    }

    public RedisTemplate newInstance(String host, int port, String password, int db) throws Exception {
        if (StringUtils.isEmpty(host)) {
            throw new Exception("host is null");
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(8);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(getJedisShardInfo(host, port, password, db));
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, shards);

        this.setShardedJedisPool(shardedJedisPool);
        return this;
    }

    public JedisShardInfo getJedisShardInfo(String host, int port, String password, int db) {
        JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port);
        if(StringUtils.isNotEmpty(password)) {
            jedisShardInfo.setPassword(password);
        }
        Class<? extends JedisShardInfo> clz = jedisShardInfo.getClass();
        try {
            Field declaredField = clz.getDeclaredField("db");
            declaredField.setAccessible(true);
            declaredField.set(jedisShardInfo, db);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jedisShardInfo;
    }


    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    public void disconnect() {
        ShardedJedis shardedJedis = this.getRedisClient();
        shardedJedis.disconnect();
    }

    public List<Object> pipelined(ShardedJedisPipeline shardedJedisPipeline) {
        ShardedJedis shardedJedis = this.getRedisClient();
        List<Object> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pipelined(shardedJedisPipeline);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Jedis getShard(byte[] key) {
        ShardedJedis shardedJedis = this.getRedisClient();
        Jedis result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShard(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Jedis getShard(String key) {
        ShardedJedis shardedJedis = this.getRedisClient();
        Jedis result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShard(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public JedisShardInfo getShardInfo(byte[] key) {
        ShardedJedis shardedJedis = this.getRedisClient();
        JedisShardInfo result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShardInfo(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public JedisShardInfo getShardInfo(String key) {
        ShardedJedis shardedJedis = this.getRedisClient();
        JedisShardInfo result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getShardInfo(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String getKeyTag(String key) {
        ShardedJedis shardedJedis = this.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getKeyTag(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Collection<JedisShardInfo> getAllShardInfo() {
        ShardedJedis shardedJedis = this.getRedisClient();
        Collection<JedisShardInfo> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getAllShardInfo();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Collection<Jedis> getAllShards() {
        ShardedJedis shardedJedis = this.getRedisClient();
        Collection<Jedis> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getAllShards();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    // ******************************************connect end*******************************************


    // ******************************************key start*******************************************
    public Long del(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.del(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long del(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.del(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Boolean exists(String key) {
        Boolean result = false;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.exists(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Boolean exists(byte[] key) {
        Boolean result = false;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.exists(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long expire(String key, int seconds) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.expire(key, seconds);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long expire(byte[] key, int seconds) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.expire(key, seconds);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long expireAt(String key, long unixTime) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.expireAt(key, unixTime);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long expireAt(byte[] key, long unixTime) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.expireAt(key, unixTime);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long pexpireAt(String key, long milliseconds) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pexpireAt(key, milliseconds);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long pexpireAt(byte[] key, long milliseconds) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pexpireAt(key, milliseconds);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long pexpire(String key, long milliseconds) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pexpire(key, milliseconds);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long pexpire(byte[] key, long milliseconds) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pexpire(key, milliseconds);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long move(String key, int dbIndex) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.move(key, dbIndex);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long move(byte[] key, int dbIndex) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.move(key, dbIndex);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long persist(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.persist(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long persist(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.persist(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long ttl(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ttl(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long ttl(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ttl(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long pttl(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pttl(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String type(String key) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.type(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String type(byte[] key) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.type(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }
    // ******************************************key end*******************************************






    // ******************************************String start*******************************************
    public String set(String key, String value) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String set(byte[] key, byte[] value) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String get(String key) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.get(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] get(byte[] key) {
        byte[] result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.get(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String getrange(String key, long startOffset, long endOffset) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getrange(key, startOffset, endOffset);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        byte[] result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getrange(key, startOffset, endOffset);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String getSet(String key, String value) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getSet(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] getSet(byte[] key, byte[] value) {
        byte[] result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getSet(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public boolean getbit(String key, long offset) {
        boolean result = false;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getbit(key, offset);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public boolean getbit(byte[] key, long offset) {
        boolean result = false;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.getbit(key, offset);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public boolean setbit(String key, long offset, boolean value) {
        boolean result = false;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setbit(key, offset, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public boolean setbit(byte[] key, long offset, boolean value) {
        boolean result = false;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setbit(key, offset, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String setex(String key, int seconds, String value) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setex(key, seconds, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String setex(byte[] key, int seconds, byte[] value) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setex(key, seconds, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long setnx(String key, String value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setnx(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long setnx(byte[] key, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setnx(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public long setrange(String key, long offset, String value) {
        long result = 0;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setrange(key, offset, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public long setrange(byte[] key, long offset, byte[] value) {
        long result = 0;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.setrange(key, offset, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long strlen(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.strlen(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long strlen(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.strlen(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String psetex(String key, long milliseconds, String value) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.psetex(key, milliseconds, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long incr(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incr(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long incr(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incr(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long incrBy(String key, long integer) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incrBy(key, integer);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long incrBy(byte[] key, long integer) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incrBy(key, integer);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double incrByFloat(String key, double integer) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incrByFloat(key, integer);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double incrByFloat(byte[] key, double integer) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.incrByFloat(key, integer);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long decr(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.decr(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long decr(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.decr(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long decrBy(String key, long integer) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.decrBy(key, integer);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long decrBy(byte[] key, long integer) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.decrBy(key, integer);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long append(String key, String value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.append(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long append(byte[] key, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.append(key, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    // ******************************************String end*******************************************






    // ******************************************Hash start*******************************************
    public Long hdel(String key, String... fields) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hdel(key, fields);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hdel(String key, List<String> fieldList) {
        Long result = null;
        String[] fields = (String[]) fieldList.toArray();
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hdel(key, fields);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hdel(byte[] key, byte[]... fields) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hdel(key, fields);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Boolean hexists(String key, String field) {
        Boolean result = false;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hexists(key, field);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Boolean hexists(byte[] key, byte[] field) {
        Boolean result = false;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hexists(key, field);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String hget(String key, String field) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hget(key, field);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] hget(byte[] key, byte[] field) {
        byte[] result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hget(key, field);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hgetAll(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Map<byte[], byte[]> hgetAll(byte[] key) {
        Map<byte[], byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hgetAll(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hincrBy(String key, String field, long value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hincrBy(key, field, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hincrBy(byte[] key, byte[] field, long value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hincrBy(key, field, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double hincrByFloat(String key, String field, double value) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hincrByFloat(key, field, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double hincrByFloat(byte[] key, byte[] field, long value) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hincrByFloat(key, field, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> hkeys(String key) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hkeys(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> hkeys(byte[] key) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hkeys(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hlen(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hlen(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hlen(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hlen(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> hmget(String key, String... fields) {
        List<String> result = null;
        if (fields == null || fields.length == 0) {
            return result;
        }
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmget(key, fields);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> hmget(String key, List<String> fieldList) {
        List<String> result = null;
        if (fieldList == null || fieldList.size() == 0) {
            return result;
        }
        String[] arr = new String[fieldList.size()];
        String[] fields = fieldList.toArray(arr);
        result = hmget(key, fields);
        return result;
    }

    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        List<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmget(key, fields);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String hmset(String key, Map<String, String> hash) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmset(key, hash);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hmset(key, hash);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hset(String key, String field, String value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hset(key, field, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hset(byte[] key, byte[] field, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hset(key, field, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hsetnx(String key, String field, String value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hsetnx(key, field, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hsetnx(key, field, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> hvals(String key) {
        List<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hvals(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Collection<byte[]> hvals(byte[] key) {
        Collection<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hvals(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        ScanResult<Map.Entry<String, String>> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hscan(key, cursor);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams scanParams) {
        ScanResult<Map.Entry<String, String>> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hscan(key, cursor, scanParams);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        ScanResult<Map.Entry<byte[], byte[]>> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hscan(key, cursor);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams scanParams) {
        ScanResult<Map.Entry<byte[], byte[]>> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.hscan(key, cursor, scanParams);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    // ******************************************Hash end*******************************************





    // ******************************************List start*******************************************
    public List<String> blpop(String key) {
        List<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.blpop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> blpop(int timeout, String key) {
        List<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.blpop(timeout, key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<byte[]> blpop(byte[] key) {
        List<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.blpop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> brpop(String key) {
        List<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.brpop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> brpop(int timeout, String key) {
        List<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.brpop(timeout, key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<byte[]> brpop(byte[] key) {
        List<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.brpop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String lindex(String key, long index) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lindex(key, index);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] lindex(byte[] key, int index) {
        byte[] result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lindex(key, index);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.linsert(key, where, pivot, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.linsert(key, where, pivot, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long llen(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.llen(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long llen(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.llen(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String lpop(String key) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] lpop(byte[] key) {
        byte[] result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long lpush(String key, String... strings) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpush(key, strings);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long lpush(String key, List<String> stringList) {
        if (stringList == null || stringList.size() == 0) {
            return null;
        }
        String[] strings = (String[]) stringList.toArray();
        return lpush(key, strings);
    }

    public Long lpush(byte[] key, byte[] string) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpush(key, string);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long lpushx(String key, String... strings) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpushx(key, strings);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long lpushx(byte[] key, byte[]... strings) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lpushx(key, strings);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> lrange(String key, long start, long end) {
        List<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrange(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<byte[]> lrange(byte[] key, int start, int end) {
        List<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrange(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long lrem(String key, long count, String value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrem(key, count, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long lrem(byte[] key, int count, byte[] value) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lrem(key, count, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String lset(String key, long index, String value) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lset(key, index, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String lset(byte[] key, int index, byte[] value) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.lset(key, index, value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String ltrim(String key, long start, long end) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ltrim(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String ltrim(byte[] key, int start, int end) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.ltrim(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String rpop(String key) {
        String result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] rpop(byte[] key) {
        byte[] result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long rpush(String key, String... strings) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpush(key, strings);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long rpush(String key, List<String> stringList) {
        if (stringList == null || stringList.size() == 0) {
            return null;
        }
        String[] strings = (String[]) stringList.toArray();
        return rpush(key, strings);
    }

    public Long rpush(byte[] key, byte[] string) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpush(key, string);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long rpushx(String key, String... strings) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpushx(key, strings);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long rpushx(byte[] key, byte[]... strings) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.rpushx(key, strings);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    // ******************************************List end*******************************************




    // ******************************************Set start*******************************************
    public Long sadd(String key, String... members) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sadd(key, members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long sadd(String key, List<String> stringList) {
        if (stringList == null || stringList.size() == 0) {
            return null;
        }
        String[] strings = (String[]) stringList.toArray();
        return sadd(key, strings);
    }

    public Long sadd(byte[] key, byte[]... member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sadd(key, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long scard(String key) {
        ShardedJedis shardedJedis = this.getRedisClient();
        Long result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.scard(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long scard(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.scard(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Boolean sismember(String key, String member) {
        ShardedJedis shardedJedis = this.getRedisClient();
        Boolean result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sismember(key, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> smembers(String key) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.smembers(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> smembers(byte[] key) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.smembers(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String spop(String key) {
        ShardedJedis shardedJedis = this.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.spop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] spop(byte[] key) {
        byte[] result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.spop(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> srandmember(String key, int count) {
        ShardedJedis shardedJedis = this.getRedisClient();
        List<String> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srandmember(key, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public String srandmember(String key) {
        ShardedJedis shardedJedis = this.getRedisClient();
        String result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srandmember(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<byte[]> srandmember(byte[] key, int count) {
        ShardedJedis shardedJedis = this.getRedisClient();
        List<byte[]> result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srandmember(key, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public byte[] srandmember(byte[] key) {
        ShardedJedis shardedJedis = this.getRedisClient();
        byte[] result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srandmember(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long srem(String key, String... members) {
        ShardedJedis shardedJedis = this.getRedisClient();
        Long result = null;
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srem(key, members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long srem(String key, List<String> stringList) {
        if (stringList == null || stringList.size() == 0) {
            return null;
        }
        String[] strings = (String[]) stringList.toArray();
        return srem(key, strings);
    }

    public Long srem(byte[] key, byte[] member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.srem(key, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<String> sscan(String key, String cursor) {
        ScanResult<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sscan(key, cursor);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<String> sscan(String key, String cursor, ScanParams scanParams) {
        ScanResult<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sscan(key, cursor, scanParams);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        ScanResult<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sscan(key, cursor);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams scanParams) {
        ScanResult<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.sscan(key, cursor, scanParams);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    // ******************************************Set end*******************************************





    // ******************************************sorted set start*******************************************
    public Long zadd(String key, double score, String member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zadd(key, score, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zadd(byte[] key, double score, byte[] member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zadd(key, score, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zcard(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zcard(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zcard(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zcard(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zcount(String key, double min, double max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zcount(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zcount(byte[] key, double min, double max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zcount(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double zincrby(String key, double score, String member) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zincrby(key, score, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double zincrby(byte[] key, double score, byte[] member) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zincrby(key, score, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zlexcount(String key, String min, String max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zlexcount(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zlexcount(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrange(String key, int start, int end) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrange(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrange(byte[] key, int start, int end) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrange(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeWithScores(String key, int start, int end) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeWithScores(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeWithScores(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrangeByLex(String key, String min, String max) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByLex(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByLex(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByLex(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByLex(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrangeByScore(String key, double min, double max) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrangeByScore(String key, String min, String max) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        Set<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zrank(String key, String member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrank(key, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zrank(byte[] key, byte[] member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrank(key, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zrem(String key, String... members) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrem(key, members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zrem(String key, List<String> memberList) {
        Long result = null;
        if (memberList == null || memberList.size() == 0) {
            return result;
        }
        String[] members = (String[]) memberList.toArray();
        result = zrem(key, members);
        return result;
    }

    public Long zrem(byte[] key, byte[]... members) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrem(key, members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zremrangeByLex(String key, String min, String max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zremrangeByLex(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zremrangeByLex(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zremrangeByRank(String key, long start, long end) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zremrangeByRank(byte[] key, long start, long end) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zremrangeByScore(String key, String min, String max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zremrangeByScore(String key, double min, double max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zremrangeByScore(byte[] key, double min, double max) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zremrangeByScore(key, min, max);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrange(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrevrange(byte[] key, int start, int end) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrange(key, start, end);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrevrangeByScore(String key, String max, String min) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        Set<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        Set<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zrevrank(String key, String member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrank(key, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long zrevrank(byte[] key, byte[] member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zrevrank(key, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double zscore(String key, String member) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zscore(key, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double zscore(byte[] key, byte[] member) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zscore(key, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<Tuple> zscan(String key, String cursor) {
        ScanResult<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zscan(key, cursor);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams scanParams) {
        ScanResult<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zscan(key, cursor, scanParams);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        ScanResult<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zscan(key, cursor);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams scanParams) {
        ScanResult<Tuple> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.zscan(key, cursor, scanParams);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }


    // ******************************************sorted set end*******************************************





    // ******************************************HyperLogLog start*******************************************
    public Long pfadd(String key, String... elements) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pfadd(key, elements);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long pfadd(String key, List<String> elementList) {
        Long result = null;
        if (elementList == null || elementList.size() == 0) {
            return result;
        }
        String[] elements = (String[]) elementList.toArray();
        result = pfadd(key, elements);
        return result;
    }

    public Long pfadd(byte[] key, byte[]... elements) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pfadd(key, elements);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long pfcount(String key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pfcount(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long pfcount(byte[] key) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.pfcount(key);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }
    
    // ******************************************HyperLogLog end*******************************************





    // ******************************************geo start*******************************************
    public Long geoadd(String key, double longitude, double latitude, String member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geoadd(key, longitude, latitude, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geoadd(key, longitude, latitude, member);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geoadd(key, memberCoordinateMap);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        Long result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geoadd(key, memberCoordinateMap);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoCoordinate> geopos(String key, String... members) {
        List<GeoCoordinate> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geopos(key, members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        List<GeoCoordinate> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geopos(key, members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoCoordinate> geopos(String key, List<String> memberList) {
        List<GeoCoordinate> result = null;
        if (memberList != null && memberList.size() == 0) {
            String[] members = (String[]) memberList.toArray();
            result = geopos(key, members);
        }
        return result;
    }

    public Double geodist(String key, String member1, String member2) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geodist(key, member1, member2);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geodist(key, member1, member2);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geodist(key, member1, member2, unit);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        Double result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geodist(key, member1, member2, unit);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        List<GeoRadiusResponse> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.georadius(key, longitude, latitude, radius, unit);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
        List<GeoRadiusResponse> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.georadius(key, longitude, latitude, radius, unit);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        List<GeoRadiusResponse> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.georadius(key, longitude, latitude, radius, unit, param);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        List<GeoRadiusResponse> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.georadius(key, longitude, latitude, radius, unit, param);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        List<GeoRadiusResponse> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.georadiusByMember(key, member, radius, unit);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
        List<GeoRadiusResponse> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.georadiusByMember(key, member, radius, unit);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        List<GeoRadiusResponse> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.georadiusByMember(key, member, radius, unit, param);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
        List<GeoRadiusResponse> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.georadiusByMember(key, member, radius, unit, param);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> geohash(String key, String... members) {
        List<String> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geohash(key, members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<byte[]> geohash(byte[] key, byte[]... members) {
        List<byte[]> result = null;
        ShardedJedis shardedJedis = this.getRedisClient();
        if (shardedJedis == null) {
            return result;
        }
        boolean broken = false;
        try {
            result = shardedJedis.geohash(key, members);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            broken = true;
        } finally {
            this.returnResource(shardedJedis, broken);
        }
        return result;
    }

    public List<String> geohash(String key, List<String> memberList) {
        List<String> result = null;
        if (memberList != null && memberList.size() == 0) {
            String[] members = (String[]) memberList.toArray();
            result = geohash(key, members);
        }
        return result;
    }

    // ******************************************geo end*******************************************


}
