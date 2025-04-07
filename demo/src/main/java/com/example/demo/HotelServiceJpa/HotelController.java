package com.example.demo.HotelServiceJpa;

import com.example.demo.model.Hotel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoteles")
public class HotelController {

    private final IHotelJPA hotelRepository;

    public HotelController(IHotelJPA hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> listarHoteles() {
        return ResponseEntity.ok(hotelRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Hotel> crearHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelRepository.save(hotel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> obtenerHotel(@PathVariable Long id) {
        return ResponseEntity.ok(hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado")));
    }
}