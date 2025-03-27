package com.example.demo.HotelService;

import java.util.List;

import com.example.demo.model.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class HotelService {
    @Autowired
    private IHotelJPA iHotelJPA;

    /*
     * Listamos todas las opciones disponibles
     * Las hacemos desde los hoteles, por que los hoteles son las que tienen las opciones
     */
    // opcion a => mostrar todos los hoteles, y hacer un fetch del checkin y check out y que eso sea la opcion
    // opcion b => mostrar opciones preparadas con una estrucuta mostrado un join de varias opciones
    public List<Hotel> ListarTodasOpcionesHotel(){
        return iHotelJPA.findAll();
    }

    /*
     * Metodo de la clase ProductoService que llama al servicio CRUD (Read) pero por id
     */
    public Hotel GetProductById(Integer id) {
        return iHotelJPA.findById(id).get();
    }
}
