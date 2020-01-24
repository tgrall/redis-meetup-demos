package io.redis.demo;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static java.lang.Thread.*;

public class Demo002WSCache {


    private JedisPool pool = null;
    private final static int TTL = 3;

    public Demo002WSCache() {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");

        // just init the pool
        Jedis j = pool.getResource();
        j.close();

    }

    public static void main(String[] args) throws InterruptedException {


           String url = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/135976?apikey=JabHZWSTOuPEa4M1hKpz4zSgLzWBlUJo&language=fr-fr&details=true&metric=true";

           Demo002WSCache app = new Demo002WSCache();

           System.out.println(" \u001b[32m=== === === CALL #1 === === ===\u001b[37m");
           System.out.println(
            app.callRestAPI(url)
           );


           System.out.println(" \n\u001b[32m=== === === CALL #2 === === ===\u001b[37m");
           System.out.println(
                   app.callRestAPI(url)
           );

           System.out.println(" \n\u001b[32m=== === === CALL #3 === === ===\u001b[37m");
           System.out.println(
                   app.callRestAPI(url)
           );

           System.out.println("\n\u001b[31m..sleep for 3 seconds (expiration) ....");
           sleep(3400);

           System.out.println(" \u001b[32m=== === === CALL #4 === === ===\u001b[37m");
           System.out.println(
                   app.callRestAPI(url)
           );


    }


    private String callRestAPI(String url) {
        String returnValue = null;
        long start = System.currentTimeMillis();

        String restCallKey = UUID.nameUUIDFromBytes(url.getBytes()).toString();

        try (Jedis jedis = pool.getResource() ){

            returnValue = jedis.get(restCallKey);

            if (returnValue == null) {
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpGet getRequest = new HttpGet(url);
                getRequest.addHeader("accept", "application/json");
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                returnValue = httpClient.execute(getRequest,responseHandler);
                jedis.set(restCallKey, returnValue, SetParams.setParams().ex(TTL) );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Call REST : "+ (end-start) +"ms" );


        return returnValue;

    }



}
