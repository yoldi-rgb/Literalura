package com.alura.literalura.repositorio;

import com.alura.literalura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor,Long> {
    @Query("SELECT a FROM Autor a WHERE LOWER(a.autor) = LOWER(:nombreAutor)")
    Autor encontrarAutor(String nombreAutor);
    @Query("SELECT a FROM Autor a WHERE "+
            "(:fecha >=  a.fechaDeNacimiento OR a.fechaDeNacimiento IS NULL) AND"+
            "(:fecha <= a.fechaDeFallecimiento OR a.fechaDeFallecimiento IS NULL)")
    List<Autor> obtenerAutorPorFecha(Integer fecha);


}
