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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Relación con Hotel: Una cadena tiene muchos hoteles
    @OneToMany(mappedBy = "cadena", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hotel> hoteles;

    // Nombre de la cadena, puede ser null
    @Column(nullable = true)
    private String esCadena;

    // Métodos corregidos
    public long getID() {
        return id;
    }

    public void setID(long ID) {
        this.id = ID;
    }

    public List<Hotel> getHoteles() {
        return hoteles;
    }

    public void setHoteles(List<Hotel> hoteles) {
        this.hoteles = hoteles;
    }

    public String getEsCadena() {
        return esCadena;
    }

    public void setEsCadena(String esCadena) {
        this.esCadena = esCadena;
    }
}
