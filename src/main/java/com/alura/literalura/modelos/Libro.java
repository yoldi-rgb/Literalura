package com.alura.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @ManyToOne()
    private Autor autor;

    private String idiomas;
    private Double numeroDeDescargas;

    public Libro(){

    }



    public Libro(DatosLibros libros,Autor autor) {
        this.titulo = libros.titulo();
        this.idiomas = libros.idiomas().stream().findFirst().orElse(null);
        this.numeroDeDescargas = libros.numeroDeDescargas();
        this.autor = autor;

    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return
                " Libro = [" + titulo + "]\n" +
                " Autor = [" + autor.getAutor() +"]\n"+
                " Idioma = [" + idiomas + "]\n" +
                " Numero de descargas = [" + numeroDeDescargas+"]"
                ;
    }
}
