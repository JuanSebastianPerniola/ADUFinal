package com.example.demo.PersonaServicio;

import com.example.demo.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findById(Long id);
}

