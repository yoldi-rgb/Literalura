package com.alura.literalura.principal;

import com.alura.literalura.modelos.*;
import com.alura.literalura.repositorio.AutorRepository;
import com.alura.literalura.repositorio.LibroRepository;
import com.alura.literalura.servicios.ConsumirAPI;
import com.alura.literalura.servicios.ConvierteDatos;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumirAPI api =  new ConsumirAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final LibroRepository repositorylibro;
    private final AutorRepository repositoryAutor;
    private Optional<DatosLibros> libroEncontrado;


    public Principal(LibroRepository repository, AutorRepository repository2){
        this.repositorylibro = repository;
        this.repositoryAutor = repository2;
    }

    public void obtenerMenu() {

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    --------------------------------------------------------
                    ##############        LITERALURA            ###########
                    1 - Buscar libros por titulo
                    2 - Buscar libros registrados
                    3 - Buscar autores registrados
                    4 - Buscar autores  vivos en un determinado año
                    5 - Buscar libros por idiomas
                    6 - Top 5 libros
                    0 - Salir
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalida entrada, Por favor coloca un numero ");
                teclado.nextLine();
            }


            switch (opcion) {
                case 1:
                    buscarLibros();
                    break;
                case 2:
                    obtenerlibrosRegistrados();
                    break;
                case 3:
                    obtenerAutoresRegistrados();
                    break;
                case 4:
                    obtenerAutoresPorFecha();
                    break;
                case 5:
                    obtenerPorIdioma();
                    break;
                case 6:
                    obtenerTop5();
                    break;
                case 0:
                    System.out.println("Cerrando Aplicacion ");
                    break;
                default:
                    System.out.println("Elegi una opcion del menu");


            }
        }
    }

        public void obtenerLibrosAPI () {
            System.out.println("Ingrese el nombre del libro que desea buscar");
            String nombreDeLibro = teclado.nextLine();
            var json = api.obtenerDatos(URL_BASE + "?search=" + nombreDeLibro.replace(" ", "+"));
            var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

            libroEncontrado = datosBusqueda.resultados().stream()
                    .filter(l -> l.titulo().toUpperCase().contains(nombreDeLibro.toUpperCase()))
                    .findFirst();

            if (libroEncontrado.isPresent()) {
                System.out.println("Libro Encontrado");

            } else {
                System.out.println("------------------------------");
            }
        }


        public void buscarLibros () {
            obtenerLibrosAPI();
            if (libroEncontrado.isPresent()) {
                DatosLibros datosLibros = libroEncontrado.get();
                DatosAutor datosAutor = datosLibros.autor().getFirst();


                var registroLibro = repositorylibro.encontrarTitulo(datosLibros.titulo());
                if (registroLibro == null) {
                    var registroAutor = repositoryAutor.encontrarAutor(datosAutor.autor());
                    Libro nuevoLibro;
                    if (registroAutor == null) {
                        Autor autorNuevo = new Autor(datosAutor);
                        repositoryAutor.save(autorNuevo);
                        nuevoLibro = new Libro(datosLibros, autorNuevo);
                    } else {
                        nuevoLibro = new Libro(datosLibros, registroAutor);
                    }
                    repositorylibro.save(nuevoLibro);
                    System.out.println("--------------LIBRO GUARDADO----------------");
                    System.out.println(nuevoLibro);
                } else {
                    System.out.printf("EL libro: '%s' del autor: '%s' se encuentra registrado\n",datosLibros.titulo(),datosAutor.autor());
                }
            } else {
                System.out.println("NO SE ENCONTRO EL LIBRO SOLICITADO :c ");
            }


        }

        public void obtenerlibrosRegistrados() {
            System.out.println("------------------LIBROS REGISTRADOS----------------------");
            List<Libro> libros = repositorylibro.findAll();

            libros.forEach(l-> System.out.println(l+"\n-----------------------------------"));

        }

        public void obtenerAutoresRegistrados(){
            System.out.println("----------------AUTORES REGISTRADOS -------------------");
            List<Autor> autores = repositoryAutor.findAll();
            autores.forEach(s-> System.out.println(s+"\n------------------------------"));

        }

        public void obtenerAutoresPorFecha() {
            System.out.println("------------------AUTORES POR FECHA-----------------");
            System.out.println("Elige una fecha (YYYY)");
            int fecha = 0;
            try {
                fecha = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada Invalida, Coloca un año (YYYY)");
                teclado.nextLine();


            }

            List<Autor> autoresPorFecha = repositoryAutor.obtenerAutorPorFecha(fecha);

            if (!autoresPorFecha.isEmpty()){
                System.out.println("       AUTORES A PARTIR DEL AÑO: "+ fecha);
                autoresPorFecha.forEach(System.out::println);
            }else {
                System.out.printf("No se encontro ningun autor registrado a partir: %s\n",fecha);
            }


        }

        public void obtenerPorIdioma(){
        String idiomamenu = """
                       Elegi entre los siguiente idiomas
                       es - español
                       en - ingles
                       fr - frances
                       """;
        System.out.println(idiomamenu);

        var idioma = teclado.nextLine();


        List<Libro> librosPorIdioma = repositorylibro.obtenerLibrosPorIdioma(idioma);

        if (!librosPorIdioma.isEmpty()){
            librosPorIdioma.forEach(System.out::println);
        }else {
            System.out.println("Libros en ese idioma no disponibles");

        }

    }

        public  void obtenerTop5(){

        List<Libro> librosTop5 = repositorylibro.findTop5ByOrderByNumeroDeDescargasDesc();

            System.out.println("------------TOP 5 LIBROS--------------");
        librosTop5.forEach(System.out::println);


        }


}
