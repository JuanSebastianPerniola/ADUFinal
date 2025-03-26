    package com.example.demo.Reserva;

    import com.example.demo.model.Reserva;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;
    import java.util.List;

    @Repository
    public interface IReservaJPA extends JpaRepository<Reserva, Long> {

        @Query("SELECT r.idReserva, r.checkIn, r.checkOut, p.nombre, p.apellido, p.email" +
                " FROM Reserva r JOIN r.persona p")
        List<Object[]> findAllWithPersonas();
    }