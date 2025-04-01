package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.UpdateResult;
import com.example.demo.model.Reserva;
import com.example.demo.model.Habitaciones;
import com.example.demo.Reserva.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class NearRealTimeIngestionModule {

    private static final Logger logger = LoggerFactory.getLogger(NearRealTimeIngestionModule.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IReservaJPA reservationRepository;

    @Value("${ingestion.mongo.collection.name}")
    private String collectionName;

    @Scheduled(fixedRateString = "${ingestion.nrt.schedule.rate:30000}")
    public void processMongoReservations() {
        logger.info("Starting near-real-time ingestion process");

        try {
            // Find all non-processed reservations
            Query query = new Query(
                    new Criteria().orOperator(
                            Criteria.where("processed").exists(false),
                            Criteria.where("processed").is(false)
                    )
            );

            List<Document> pendingReservations = mongoTemplate.find(query, Document.class, collectionName);
            logger.info("Found {} pending reservations to process", pendingReservations.size());

            for (Document doc : pendingReservations) {
                try {
                    Reserva reservation = parseMongoReservation(doc);
                    reservationRepository.save(reservation);

                    // Mark as successfully processed
                    Query updateQuery = new Query(Criteria.where("_id").is(doc.get("_id")));
                    Update update = new Update()
                            .set("processed", true)
                            .set("status", "ingested");

                    UpdateResult result = mongoTemplate.updateFirst(updateQuery, update, collectionName);
                    logger.info("Marked reservation as ingested: {}, matched count: {}",
                            doc.get("_id"), result.getMatchedCount());

                } catch (Exception e) {
                    logger.error("Error processing MongoDB reservation: {} - {}",
                            doc.get("_id"), e.getMessage(), e);

                    // Mark as error
                    Query updateQuery = new Query(Criteria.where("_id").is(doc.get("_id")));
                    Update update = new Update()
                            .set("processed", true)
                            .set("status", "incorrect")
                            .set("errorMessage", e.getMessage());

                    UpdateResult result = mongoTemplate.updateFirst(updateQuery, update, collectionName);
                    logger.info("Marked reservation as incorrect: {}, matched count: {}",
                            doc.get("_id"), result.getMatchedCount());
                }
            }

            logger.info("Near-real-time ingestion process completed");

        } catch (Exception e) {
            logger.error("Error in near-real-time processing: {}", e.getMessage(), e);
        }
    }

    private Reserva parseMongoReservation(Document doc) throws Exception {
        String reservationId = doc.getString("id");
        String clientName = doc.getString("clientName");
        String email = doc.getString("email");
        String checkInDate = doc.getString("checkInDate");
        String checkOutDate = doc.getString("checkOutDate");
        String guests = doc.getString("guests");
        Double totalPrice = doc.getDouble("totalPrice");

        if (reservationId == null || clientName == null || email == null ||
                checkInDate == null || checkOutDate == null || guests == null || totalPrice == null) {
            throw new IllegalArgumentException("Missing required reservation fields");
        }

        // Extract room details
        List<Document> roomDocs = doc.getList("rooms", Document.class);
        List<Habitaciones> rooms = new ArrayList<>();

        if (roomDocs != null) {
            for (Document roomDoc : roomDocs) {
                String roomId = roomDoc.getString("roomId");
                String roomType = roomDoc.getString("roomType");
                Double roomPrice = roomDoc.getDouble("price");

                if (roomId == null || roomType == null || roomPrice == null) {
                    throw new IllegalArgumentException("Missing required room fields");
                }

                Habitaciones room = new Habitaciones(roomId, roomType, roomPrice);
                rooms.add(room);
            }
        }
    return null;
//        return new Reserva(reservationId, clientName, email, checkInDate, checkOutDate, guests, totalPrice, rooms);
    }
}