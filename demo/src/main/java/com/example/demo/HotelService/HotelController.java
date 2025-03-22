package com.example.demo.HotelService;

import com.example.demo.Reserva.ReservaService;
import com.example.demo.model.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hoetl")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    // Proceso de search
    // Mostraremos todos los hoteles disponibles
    @GetMapping
    public List<Hotel> ListarTodosLosHotels(){
        return hotelService.ListarTodasOpcionesHotel();
    }

    // Proceso de quote aqui lo enviaremos con un boton
    @PostMapping("/{id}")
    public ResponseEntity<Hotel> GetProductById(@RequestParam Integer id){
        return ResponseEntity.ok(hotelService.GetProductById(id));
    }
}
