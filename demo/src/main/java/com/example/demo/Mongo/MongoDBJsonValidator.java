package com.example.demo.Mongo;

import com.mongodb.MongoClientException;
import com.mongodb.MongoCursorNotFoundException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MongoDBJsonValidator {
    public static void main(String[] args) {
        String uri = "mongodb://localhost:27017/JsonSpring"; // URL de conexión a la base de datos
        MongoClient mongoClient = null;
        String outputDirectory = "C:/Users/JuanPrograma/OneDrive/Desktop/ADUFinal/ADUFinal/demo/src/main/java/com/example/demo/Datos/pending"; // Directorio de salida

        try {
            mongoClient = MongoClients.create(uri);

            // Seleccionar la base de datos y la colección
            MongoDatabase database = mongoClient.getDatabase("prct_java");
            MongoCollection<org.bson.Document> collection = database.getCollection("JsonToXML");

            MongoCursor<org.bson.Document> cursor = collection.find().iterator();

            // Iterar sobre los documentos de la colección
            int counter = 1; // Para numerar los archivos
            while (cursor.hasNext()) {
                org.bson.Document doc = cursor.next();
                // Convertir el documento BSON a JSON
                JSONObject jsonObject = new JSONObject(doc.toJson());
                String jsonString = jsonObject.toString(4); // Formato bonito (con 4 espacios de indentación)

                // Crear un archivo JSON en la ubicación especificada
                String filename = outputDirectory + "/document_" + counter + ".json"; // Nombre del archivo
                File outputFile = new File(filename);
                try (FileWriter fileWriter = new FileWriter(outputFile)) {
                    fileWriter.write(jsonString);
                    System.out.println("✅ Documento guardado como: " + filename);
                    counter++;
                } catch (IOException e) {
                    System.out.println("❌ Error al escribir el archivo " + filename + ": " + e.getMessage());
                }
            }

        } catch (MongoClientException e) {
            System.out.println("Error al conectar a MongoDB: " + e);
        } catch (MongoCursorNotFoundException exception) {
            System.out.println("Error en el cursor: " + exception);
        } finally {
            if (mongoClient != null) {
                mongoClient.close(); // Cerrar la conexión a MongoDB
            }
        }
    }
}
