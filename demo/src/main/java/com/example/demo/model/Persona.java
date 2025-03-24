package com.example.demo.model;

import jakarta.persistence.*;

import java.util.List;

// Nombre de la tabla en la base de datos
@Entity
@Table(name = "personas")
public class Persona {

    // Tablas para la base de datos
    // Una persona pot tenir N reserves.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "tarjeta_bancaria")
    private Integer tarjetaBancaria;

    // Una persona puede tener muchas reservas
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas;

    // Constructores, Getters, Setters y toString
    public Persona() {
    }

    public Persona(String nombre, String apellido, String email, Integer edad, Integer tarjetaBancaria) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad = edad;
        this.tarjetaBancaria = tarjetaBancaria;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Integer getTarjetaBancaria() {
        return tarjetaBancaria;
    }

    public void setTarjetaBancaria(Integer tarjetaBancaria) {
        this.tarjetaBancaria = tarjetaBancaria;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}