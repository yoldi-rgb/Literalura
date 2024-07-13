package com.alura.literalura.repositorio;

import com.alura.literalura.modelos.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro,Long> {

    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) = LOWER(:titulo)")
    Libro encontrarTitulo(String titulo);

    @Query("SELECT l FROM Libro l WHERE l.idiomas = :idiomaElegido")
    List<Libro> obtenerLibrosPorIdioma(String idiomaElegido);

    List<Libro> findTop5ByOrderByNumeroDeDescargasDesc();

}
