package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habitaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String roomName;
    private String roomPrice;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public Habitaciones(String roomId, String roomType, double roomPrice) {
        this.roomName = roomType;  // O usa roomId si prefieres
        this.roomPrice = String.valueOf(roomPrice);
    }
}
