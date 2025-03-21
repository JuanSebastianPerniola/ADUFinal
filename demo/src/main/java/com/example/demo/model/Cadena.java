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
    private long id; // Corrección: 'ID' → 'id' (convención Java)

    // Una cadena tiene muchos hoteles
    @OneToMany(mappedBy = "esCadena", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hotel> hoteles; // Corrección: 'hotel' → 'hoteles' (mejor claridad)

    private boolean esCadena;

    // Constructor
    public void cadena(long id, List<Hotel> hoteles, boolean esCadena) {
        this.id = id;
        this.esCadena = esCadena;
        this.hoteles = hoteles;
    }

    // Setter y getters
    public long getID() {
        return id;
    }

    public void setID(long ID) {
        this.id = id;
    }

    public List<Hotel> getHotel() {
        return hoteles;
    }

    public void setHotel(List<Hotel> hotel) {
        this.hoteles = hoteles;
    }

    public boolean isEsCadena() {
        return esCadena;
    }

    public void setEsCadena(boolean esCadena) {
        this.esCadena = esCadena;
    }
}
