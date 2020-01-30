package io.redis.demos.graph;

import com.redislabs.redisgraph.Record;
import com.redislabs.redisgraph.ResultSet;
import com.redislabs.redisgraph.graph_entities.Edge;
import com.redislabs.redisgraph.graph_entities.Node;
import com.redislabs.redisgraph.impl.api.RedisGraph;

import java.nio.file.Path;

public class Demo001Queries {

    public static void main(String[] args) {

        RedisGraph graph = new RedisGraph();


        // find all movies with Tom Cruise
        ResultSet resultSet = graph.query(Demo000LoadMovies.IMDB_GRAPH_NAME, "MATCH (a:actor{name:'Tom Cruise'})-[r:acted_in]->(m:movie) RETURN m");
        while(resultSet.hasNext()) {
            Record record = resultSet.next();
            Node movie = record.getValue("m");

            System.out.println(movie.getProperty("title").getValue() + " ("+ movie.getProperty("year").getValue() +")");


        }

    }

}
