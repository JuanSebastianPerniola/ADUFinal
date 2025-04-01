package com.example.demo.Reserva;

    import com.example.demo.model.Reserva;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface IReservaJPA extends JpaRepository<Reserva, Long> {

        @Query("SELECT r FROM Reserva r JOIN FETCH r.hotel JOIN FETCH r.persona")
        List<Reserva> findAllWithHotelAndPersona();
    }