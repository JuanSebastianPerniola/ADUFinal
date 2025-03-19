package com.example.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReservationList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;

    @Join
}
