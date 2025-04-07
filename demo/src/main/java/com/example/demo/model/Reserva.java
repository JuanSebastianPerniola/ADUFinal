    package com.example.demo.model;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonIncludeProperties;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDate;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Reserva {
        @Setter
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long idReserva;

        @Column(nullable = false)
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate checkIn;

        @Setter
        @Column(nullable = false)
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate checkOut;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "hotel_id", nullable = false)
        @JsonIncludeProperties({"idHotel", "nombre", "direccion"})
        private Hotel hotel;

        @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "habitacion_id", nullable = true)
        @JsonIncludeProperties({"idHabitacion", "tipo", "capacidad"})
        private Habitaciones habitacion;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "persona_id", nullable = false)
        @JsonIncludeProperties({"id", "nombre", "apellidos", "email", "telefono"})
        private Persona persona;

        public void setTipoHabitacion(Habitaciones habitacion) {
        }
    }
