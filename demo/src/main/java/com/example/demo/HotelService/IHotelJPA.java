package com.example.demo.HotelService;

import com.example.demo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clase para utilizar el metodo crud de JPA
@Repository
public interface IHotelJPA extends JpaRepository<Hotel,Integer> { }
