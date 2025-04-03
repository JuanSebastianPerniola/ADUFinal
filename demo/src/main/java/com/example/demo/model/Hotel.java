package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cadena;

    @Column(name = "nombre_de_hotel")
    private String nombreDeHotel;

    @Column(name = "numero_de_hoteles")
    private Integer numeroDeHoteles;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habitaciones> habitaciones;
}