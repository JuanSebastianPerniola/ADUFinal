package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"reservas"}) // Evita la recursión infinita
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreDeHotel;

    @Column(nullable = true)
    private String cadena;

    @Column(nullable = false)
    private Integer numeroDeHoteles;

    // Un hotel tiene muchas reservas
    @JsonIgnore
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
