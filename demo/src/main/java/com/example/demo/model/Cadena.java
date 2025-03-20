package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cadena {
    // por ahora lo dejamos sin usar
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;

    // Guardaremos un listado de todos los tipos de hoteles
    // 2 tipos de hoteles, cadena, no cadena,
    // esto se añadira con true or false con boolean
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cadena_id")
    private List<Hotel> hotel;

    private boolean esCadena;

    // Constructor
    public void cadena(long id, List<Hotel> hotel, boolean esCadena) {
        this.ID = id;
        this.esCadena = esCadena;
        this.hotel = hotel;
    }

    // Setter y getters
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public List<Hotel> getHotel() {
        return hotel;
    }

    public void setHotel(List<Hotel> hotel) {
        this.hotel = hotel;
    }

    public boolean isEsCadena() {
        return esCadena;
    }

    public void setEsCadena(boolean esCadena) {
        this.esCadena = esCadena;
    }
}
