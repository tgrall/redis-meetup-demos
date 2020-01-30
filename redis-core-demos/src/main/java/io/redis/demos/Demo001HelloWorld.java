package io.redis.demos;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

public class Demo001HelloWorld {

    public static void main(String[] args) {

        JedisPool jedispool = new JedisPool();

        Jedis jedis = jedispool.getResource();


        // String key GET/SET
        jedis.set("hello", "world");
        System.out.println( jedis.get("hello") );


        // Incr counter
        System.out.println("\n === Increment value ==");
        jedis.del("counter");
        System.out.println("\t get : "+ jedis.get("counter") );
        System.out.println("\t increment : "+ jedis.incr("counter"));
        System.out.println("\t get : "+ jedis.get("counter"));
        System.out.println("\t increment : "+ jedis.incr("counter"));
        System.out.println("\t get : "+ jedis.get("counter"));
        System.out.println("\t many increments");
        jedis.incr("counter");
        jedis.incr("counter");
        jedis.incr("counter");
        System.out.println("\t get : "+ jedis.get("counter"));
        System.out.println("\t decrement by 3 : "+ jedis.decrBy("counter", 3));


        // Hashes
        System.out.println("\n === === == Hash === === ===");
        Map userSession = new HashMap();
        userSession.put("userid", "3526" );
        userSession.put("name", "John Doe" );
        userSession.put("ip", "10.20.104.31");
        userSession.put("hits", "1");
        jedis.hset("usersession:3526", userSession);

        System.out.println(
          jedis.hgetAll("usersession:3526")
        );

        // Increment hits
        jedis.hincrBy("usersession:3526", "hits", 1);

        System.out.println("Hits "+
                jedis.hget("usersession:3526","hits")
        );


        // LIST & SETS
        System.out.println("\n === === == Lists & Sets === === ===");

        jedis.lpush("list-001", "one", "two", "three", "four", "one");
        System.out.println(
                jedis.lrange("list-001", 0, -1)
        );

        jedis.sadd("set-001", "one", "two", "three", "four", "one");
        System.out.println(
                jedis.smembers("set-001")
        );

        jedis.del("list-001", "set-001" );


        System.out.println("\n === === == Intersection === === ===");

        // SETS 2
        jedis.sadd("brands:enterprises", "oracle", "redis", "sap", "apple", "google", "amazon");
        jedis.sadd("brands:consumers", "apple", "google", "amazon", "nike", "duotone");

        System.out.println("Enterprises : "+ jedis.smembers("brands:enterprises"));
        System.out.println("Consumers   : "+jedis.smembers("brands:consumers"));
        System.out.println("  Both      : "+
            jedis.sinter("brands:enterprises", "brands:consumers")
        );



        jedis.close();
        jedispool.close();

    }

}
