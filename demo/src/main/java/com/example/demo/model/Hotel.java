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

public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // Un hotel puede tener muchas reservas
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reserva> reservas;

    // Un hotel tiene muchas habitaciones
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habitaciones> habitaciones;

    // Un hotel pertenece a una cadena (relacion muchos a uno)
    @ManyToOne
    @JoinColumn(name = "fk_esCadena") // FK en la tabla 'hotel'
    private Cadena esCadena;

    private String nombreDeHotel;

    private Integer numeroDeHoteles;

    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }

    public Cadena getEsCadena(Cadena esCadena) {
        return esCadena;
    }

    public void setEsCadena(Cadena esCadena) {
        this.esCadena = esCadena;
    }

    public String getNombreDeHotel() {
        return nombreDeHotel;
    }

    public void setNombreDeHotel(String nombreDeHotel) {
        this.nombreDeHotel = nombreDeHotel;
    }

    public Integer getNumeroDeHoteles() {
        return numeroDeHoteles;
    }

    public void setNumeroDeHoteles(Integer numeroDeHoteles) {
        this.numeroDeHoteles = numeroDeHoteles;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Habitaciones> getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(List<Habitaciones> habitaciones) {
        this.habitaciones = habitaciones;
    }
}
