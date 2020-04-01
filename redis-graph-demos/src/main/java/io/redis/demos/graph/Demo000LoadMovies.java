package io.redis.demos.graph;

import com.redislabs.redisgraph.impl.api.RedisGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.redislabs.redisgraph.impl.api.RedisGraph;


public class Demo000LoadMovies {

    public final static String IMDB_GRAPH_NAME = "imdb";

    public static void main(String[] args) {

        RedisGraph graph = new RedisGraph("localhost",6379);

        graph.query( IMDB_GRAPH_NAME, "CALL db.idx.fulltext.createNodeIndex('movie', 'title')");


        String line = "";
        String cvsSplitBy = ",";

        File csvMovieFile = new File(Demo000LoadMovies.class.getClassLoader().getResource("movies.csv").getFile());
        try (BufferedReader br = new BufferedReader(new FileReader(csvMovieFile))) {
            while ((line = br.readLine()) != null) {
                Map<String,Object> movie = new HashMap<>();
                String[] movieLine = line.split(cvsSplitBy);
                movie.put("title", movieLine[0]);
                movie.put("genre", movieLine[1]);
                movie.put("votes", Integer.parseInt(movieLine[2]));
                movie.put("rating", Float.parseFloat(movieLine[3]));
                movie.put("year", Integer.parseInt(movieLine[4]));
                graph.query(IMDB_GRAPH_NAME, "MERGE (:movie{title:$title,genre:$genre,year:$year,votes:$votes,rating:$rating})", movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        File csvActorFile = new File(Demo000LoadMovies.class.getClassLoader().getResource("actors.csv").getFile());
        try (BufferedReader br = new BufferedReader(new FileReader(csvActorFile))) {
            while ((line = br.readLine()) != null) {
                Map<String,Object> actor = new HashMap<>();
                String[] actorLine = line.split(cvsSplitBy);
                actor.put("name", actorLine[0]);
                actor.put("yearOfBirth", Integer.parseInt(actorLine[1]));
                actor.put("movie", actorLine[2]);
                // create the actor, and add the link with the movie
                graph.query(IMDB_GRAPH_NAME, " MATCH (m:movie{ title: $movie}) MERGE (a:actor{ name:$name, yearOfBirth:$yearOfBirth }) MERGE (a)-[r:acted_in]->(m) ", actor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
