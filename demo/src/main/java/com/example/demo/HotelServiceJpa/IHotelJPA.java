package com.example.demo.HotelServiceJpa;

import com.example.demo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHotelJPA extends JpaRepository<Hotel, Long> {
}