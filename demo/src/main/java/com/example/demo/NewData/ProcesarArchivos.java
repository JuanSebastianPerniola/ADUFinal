package com.example.demo.NewData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ProcesarArchivos {
    private static final Logger logger = LoggerFactory.getLogger(ProcesarArchivos.class);
    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static JsonNode convertXmlFileToJson(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            return xmlMapper.readTree(inputStream);
        } catch (IOException e) {
            logger.error("Error al convertir XML a JSON", e);
            return null;
        }
    }

    public static JsonNode convertXmlStringToJson(String xmlContent) {
        try {
            return xmlMapper.readTree(xmlContent);
        } catch (IOException e) {
            logger.error("Error al convertir XML a JSON desde String", e);
            return null;
        }
    }
    public static JsonNode readJsonFile(File file) {
        try {
            return jsonMapper.readTree(file); // Lee el JSON y lo convierte en JsonNode
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON: " + e.getMessage(), e);
        }
    }
    public static void saveJsonToFile(JsonNode jsonNode, File outputFile) {
        try {
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, jsonNode);
        } catch (IOException e) {
            logger.error("Error al guardar JSON", e);
        }
    }
}
