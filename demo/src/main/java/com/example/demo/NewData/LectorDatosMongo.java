package com.example.demo.NewData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LectorDatosMongo {
    private static final Logger logger = LoggerFactory.getLogger(LectorDatosMongo.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static List<JsonNode> obtenerDatosDesdeMongo() {
        List<JsonNode> datosMongo = new ArrayList<>();
        String uri = "mongodb://localhost:27017";
        String databaseName = "practica_java";
        String collectionName = "JsonToXML";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            FindIterable<Document> documents = collection.find();
            try (MongoCursor<Document> cursor = documents.iterator()) {
                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    JsonNode jsonNode = jsonMapper.readTree(doc.toJson());
                    datosMongo.add(jsonNode);
                }
            }

            logger.info("Se obtuvieron {} documentos de MongoDB", datosMongo.size());

        } catch (Exception e) {
            logger.error("Error al obtener datos de MongoDB. URI: {}, DB: {}, Collection: {}",
                    uri, databaseName, collectionName, e);
            throw new RuntimeException("Error al leer datos de MongoDB", e);
        }

        return datosMongo;
    }
}