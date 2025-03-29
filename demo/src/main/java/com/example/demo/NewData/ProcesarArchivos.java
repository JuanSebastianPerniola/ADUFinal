package com.example.demo.NewData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ProcesarArchivos {
    private static final Logger logger = LoggerFactory.getLogger(ProcesarArchivos.class);
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();

    /**
     * Convierte un archivo XML en JSON.
     * @param file Archivo XML a convertir
     * @return JsonNode con el contenido convertido o null si hay error
     */
    public static JsonNode convertXmlFileToJson(File file) {
        Objects.requireNonNull(file, "El archivo no puede ser null");
        return convertXmlToJson(file.toPath());
    }

    /**
     * Convierte una cadena XML en JSON.
     * @param xmlContent Contenido XML como String
     * @return JsonNode con el contenido convertido o null si hay error
     */
    public static JsonNode convertXmlStringToJson(String xmlContent) {
        Objects.requireNonNull(xmlContent, "El contenido XML no puede ser null");
        return convertXmlToJson(xmlContent);
    }

    /**
     * Método interno para manejar la conversión de XML a JSON.
     */
    private static JsonNode convertXmlToJson(Object xmlSource) {
        try (InputStream inputStream = xmlSource instanceof Path
                ? Files.newInputStream((Path) xmlSource)
                : new ByteArrayInputStream(((String) xmlSource).getBytes())) {
            return xmlMapper.readTree(inputStream);
        } catch (IOException e) {
            logger.error("Error al convertir XML a JSON", e);
            return null;
        }
    }

    /**
     * Lee un archivo JSON y lo convierte en un JsonNode.
     * @param file Archivo JSON a leer
     * @return JsonNode con el contenido o null si hay error
     * @throws IllegalArgumentException si el archivo es null
     */
    public static JsonNode readJsonFile(File file) {
        Objects.requireNonNull(file, "El archivo no puede ser null");

        try {
            return jsonMapper.readTree(file);
        } catch (IOException e) {
            logger.error("Error al leer el archivo JSON '{}'", file.getAbsolutePath(), e);
            return null;
        }
    }

    /**
     * Guarda un JsonNode en un archivo JSON.
     * @param jsonNode Datos JSON a guardar
     * @param outputFile Archivo de destino
     * @throws IllegalArgumentException si alguno de los parámetros es null
     */
    public static void saveJsonToFile(JsonNode jsonNode, File outputFile) {
        Objects.requireNonNull(jsonNode, "El nodo JSON no puede ser null");
        Objects.requireNonNull(outputFile, "El archivo de salida no puede ser null");

        try {
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, jsonNode);
            logger.info("JSON guardado en '{}'", outputFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error al guardar JSON en '{}'", outputFile.getAbsolutePath(), e);
            throw new RuntimeException("Error al guardar el archivo JSON", e);
        }
    }
}