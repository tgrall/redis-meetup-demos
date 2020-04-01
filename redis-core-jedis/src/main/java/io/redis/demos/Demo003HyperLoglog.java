package io.redis.demos;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

public class Demo003HyperLoglog {

    public static void main(String[] args) {


        JedisPool jedispool = new JedisPool();

        Jedis jedis = jedispool.getResource();


        // Hyperloglog
        for (int i = 0 ; i < 1000000 ; i++) {
            jedis.pfadd("visitors:page:home", "user-key0"+ i + UUID.randomUUID().toString());
            jedis.pfadd("visitors:page:home", "user-key1-"+ i + UUID.randomUUID().toString());
            jedis.pfadd("visitors:page:home", "user-key2-"+ i + UUID.randomUUID().toString());

            if (  (i % 10000) == 0 ) {
                System.out.println(jedis.pfcount("visitors:page:home"));
            }

        }



        jedis.close();
        jedispool.close();
    }


}
