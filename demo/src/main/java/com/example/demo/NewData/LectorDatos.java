package com.example.demo.NewData;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.logging.XMLFormatter;


public class LectorDatos extends ProcesarArchivos {
    private static final Logger logger = LoggerFactory.getLogger(LectorDatos.class);

    public static void main(String[] args) {
        File carpeta = new File("Archivos");

        if (!carpeta.exists() || !carpeta.isDirectory()) {
            logger.error("La carpeta '{}' no existe o no es un directorio.", carpeta.getAbsolutePath());
            return;
        }

        File[] archivos = carpeta.listFiles();
        if (archivos == null || archivos.length == 0) {
            logger.info("No hay archivos en la carpeta.");
            return;
        }

        for (File archivo : archivos) {
            try {
                JsonNode jsonData = null;
                if (archivo.getName().endsWith(".xml")) {
                    jsonData = ProcesarArchivos.readJsonFile(archivo);
                } else if (archivo.getName().endsWith(".json")) {

                    jsonData = ProcesarArchivos.readJsonFile(archivo);
                }

                if (jsonData != null) {
                    logger.info("Procesando archivo: {}", archivo.getName());
                    procesarDatos(jsonData);
                    if (archivo.delete()) {
                        logger.info("Archivo '{}' eliminado después de procesarlo.", archivo.getName());
                    } else {
                        logger.warn("No se pudo eliminar el archivo '{}'.", archivo.getName());
                    }
                }
            } catch (Exception e) {
                logger.error("Error procesando {}: {}", archivo.getName(), e.getMessage());
            }
        }
    }

    public static void procesarDatos(JsonNode jsonData) {
        String url = "jdbc:mysql://localhost:3306/hotel_db";
        String usuario = "root";
        String contraseña = "password";

        String sqlPersona = "INSERT INTO persona (nombre, apellido, email, edad, tarjeta_bancaria) " +
                "VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                "nombre=VALUES(nombre), apellido=VALUES(apellido), edad=VALUES(edad), tarjeta_bancaria=VALUES(tarjeta_bancaria)";

        String sqlReserva = "INSERT INTO reservas (id_persona, check_in, check_out) VALUES (?, ?, ?)";

        try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña)) {
            JsonNode reservas = jsonData.get("reservas");
            if (reservas == null || !reservas.has("persona")) {
                logger.warn("El JSON no contiene datos válidos para procesar.");
                return;
            }

            JsonNode personaNode = reservas.get("persona");

            String nombre = personaNode.get("nombre").asText();
            String apellido = personaNode.get("apellido").asText();
            String email = personaNode.get("email").asText();
            int edad = personaNode.get("edad").asInt();
            long tarjeta = personaNode.get("tarjeta").asLong();
            String checkIn = personaNode.get("checkIn").asText();
            String checkOut = personaNode.get("checkOut").asText();

            int idPersona;
            try (PreparedStatement stmtPersona = conexion.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                stmtPersona.setString(1, nombre);
                stmtPersona.setString(2, apellido);
                stmtPersona.setString(3, email);
                stmtPersona.setInt(4, edad);
                stmtPersona.setLong(5, tarjeta);
                stmtPersona.executeUpdate();

                try (ResultSet rs = stmtPersona.getGeneratedKeys()) {
                    if (rs.next()) {
                        idPersona = rs.getInt(1);
                    } else {
                        // Si no se generó clave, buscar el ID manualmente
                        try (PreparedStatement stmtGetId = conexion.prepareStatement("SELECT id FROM persona WHERE email = ?")) {
                            stmtGetId.setString(1, email);
                            try (ResultSet rsGetId = stmtGetId.executeQuery()) {
                                if (rsGetId.next()) {
                                    idPersona = rsGetId.getInt(1);
                                } else {
                                    logger.error("No se encontró el ID de la persona con email '{}'.", email);
                                    return;
                                }
                            }
                        }
                    }
                }
            }

            // Insertar reserva asociada a la persona
            try (PreparedStatement stmtReserva = conexion.prepareStatement(sqlReserva)) {
                stmtReserva.setInt(1, idPersona);
                stmtReserva.setString(2, checkIn);
                stmtReserva.setString(3, checkOut);
                stmtReserva.executeUpdate();
                logger.info("Reserva insertada para {} {} (Email: {}).", nombre, apellido, email);
            }

        } catch (SQLException e) {
            logger.error("Error en la base de datos: {}", e.getMessage());
        }
    }
}
