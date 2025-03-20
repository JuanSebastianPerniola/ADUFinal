package com.example.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ID;

    @ManyToMany
    @JoinColumn(name = "reserva")
    private Reserva reserva;
    @ManyToMany
    @JoinColumn(name = "habitaciones")
    private Habitaciones habitaciones;
    @ManyToOne
    @JoinColumn(name = "fk_esCadena")
    private Cadena esCadena;

    private String nombreDeHotel;


    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Habitaciones getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(Habitaciones habitaciones) {
        this.habitaciones = habitaciones;
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
}
