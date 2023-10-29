package com.example.zoolambda;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SpringBootApplication
@RequiredArgsConstructor
public class ZooLambdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZooLambdaApplication.class, args);
    }

    @Bean
    public Function<String, List<Document>> getAnimals() {
        return s -> {
            MongoClient mongoClient = MongoClients.create(System.getenv("MONGODB_URI"));
            MongoDatabase database = mongoClient.getDatabase("main_dev_db");
            MongoCollection<Document> collection = database.getCollection("animals");

            Bson filter = new BsonDocument();

            if (!s.isEmpty()) {
                filter = Filters.eq("identity", s);
            }

            List<Document> results = new ArrayList<>();

            collection.find(filter).limit(10).into(results);

            return results;
        };
    }

}
