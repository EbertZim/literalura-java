package com.libreria.literatura_api.repository;

import com.libreria.literatura_api.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    List<Autor> findAll();
    List<Autor> findByCumpleaniosLessThanOrFechaFallecimientoGreaterThanEqual(int anioBuscado, int anioBuscado1);
    Optional<Autor> findFirstByNombreContainsIgnoreCase(String escritor);
}
