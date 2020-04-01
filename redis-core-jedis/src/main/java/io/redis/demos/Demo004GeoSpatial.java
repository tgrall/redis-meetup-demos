package io.redis.demos;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Demo004GeoSpatial {

    public static void main(String[] args) {
        JedisPool jedispool = new JedisPool();
        Jedis jedis = jedispool.getResource();

        jedis.geoadd("geo:jugs", -1.55336,  47.21725, "Nantes");
        jedis.geoadd("geo:jugs", -0.45877, 46.32313, "Niort");
        jedis.geoadd("geo:jugs", 2.3488, 48.85341, "Paris");


        System.out.println(
               jedis.geodist("geo:jugs", "Nantes" , "Niort" ) / 1000
        );

        System.out.println(
                jedis.geodist("geo:jugs", "Nantes" , "Paris" ) / 1000
        );

        jedis.close();
        jedispool.close();
    }
}
