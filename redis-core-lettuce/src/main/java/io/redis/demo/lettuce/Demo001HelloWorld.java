package io.redis.demo.lettuce;


import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.protocol.RedisCommand;

public class Demo001HelloWorld {


    public static void main(String[] args) throws Throwable {

        RedisClient client = RedisClient.create(RedisURI.create("localhost", 6379));
        RedisCommands<String, String> commands = client.connect().sync();

        commands.set("lettuce:foo", "bar");

        System.out.println(
                commands.get("lettuce:foo")
        );

        client.shutdown();


    }

}
