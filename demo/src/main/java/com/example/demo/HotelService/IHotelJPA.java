package com.example.demo.HotelService;

import com.example.demo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Vale no utilizar JPA de hotel, no hace falta
@Repository
public interface IHotelJPA extends JpaRepository<Hotel,Integer> {
}
