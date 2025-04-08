package com.example.demo.Reserva;

import com.example.demo.model.Reserva;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;

    import java.util.List;
    import java.util.Optional;

@Repository
public interface IReservaJPA extends JpaRepository<Reserva, Long> {
    @Query("SELECT r FROM Reserva r " +
            "LEFT JOIN FETCH r.hotel " +
            "LEFT JOIN FETCH r.habitacion " +
            "LEFT JOIN FETCH r.persona ")
    List<Reserva> findAllWithRelations();
    // Option 2: Using JPQL query
    @Query("SELECT r FROM Reserva r LEFT JOIN FETCH r.persona LEFT JOIN FETCH r.hotel LEFT JOIN FETCH r.habitacion WHERE r.id = :id")
    Optional<Reserva> findByIdWithRelations(@Param("id") Long id);
}