package com.aluracursos.literalura.main;

import com.aluracursos.literalura.models.*;
import com.aluracursos.literalura.repository.AuthorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.services.ConvierteDatos;
import com.aluracursos.literalura.services.RequestAPI;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private RequestAPI requestAPI = new RequestAPI();
    private Scanner scanner = new Scanner(System.in);
    private String urlBase ="https://gutendex.com/books/?search=";
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AuthorRepository authorRepository;
    private List<Libro> libros;
    private List<Author> autores;

    public Main(LibroRepository libroRepository, AuthorRepository authorRepository) {
        this.libroRepository = libroRepository;
        this.authorRepository = authorRepository;
    }


    public void showMenu()
    {
        var opcion = -1;
        while (opcion != 0){
            var menu ="""
                    **************************************************
                        LiterAlura - Búsqueda de Libros y Autores
                    **************************************************
                    
                    Selecciona una opción acontinuacion: 
                    
                    1 - Buscar un libro
                    2 - Consultar libros buscados
                    3 - Consultar autores
                    4 - Consultar autores de un año específico
                    5 - Consultar libros por lenguaje
                     
                    0 - Salir
                    
                    ***************************************************               
                    """;

            try {
                System.out.println(menu);
                opcion = scanner.nextInt();
                scanner.nextLine();
            }catch (Exception e){

                System.out.println("Ingrese una opción válida");
            }

            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    consultarLibros();
                    break;
                case 3:
                    consultarAutores();
                    break;
                case 4:
                    consultarAutoresPorAno();
                    break;
                case 5:
                    consultarLibrosLenguaje();
                    break;
                case 0:
                    System.out.println("Hasta luego");
                    break;
                default:
                    System.out.println("Ingrese una opción valida (0-5)");
            }
        }
    }


    private DatosLibro getDatosLibro() {
        System.out.println("Ingrese el nombre del libro");
        var busqueda = scanner.nextLine().toLowerCase().replace(" ","%20");
        var json = requestAPI.getData(urlBase + busqueda);

        DatosLibro datosLibro = convierteDatos.obtenerDatos(json, DatosLibro.class);
        return datosLibro;
    }


    private void buscarLibro()
    {
        DatosLibro datosLibro = getDatosLibro();

        try {
            DatosResultados datosResultados = datosLibro.resultados().get(0);
            Libro libro = new Libro(datosResultados);


            for (DatosAutor datosAutor : datosResultados.autorList()) {
                Author author = new Author(datosAutor);


                Optional<Author> existingAuthor = authorRepository.findByName(author.getAutor());

                if (existingAuthor.isPresent()) {
                    author = existingAuthor.get();
                } else {
                    author = authorRepository.save(author);
                }

                libro.addAutor(author);
            }

            System.out.println("""
                    libro[
                        titulo: %s
                        autor: %s
                        lenguaje: %s
                        descargas: %s
                    ]
                    """.formatted(libro.getTitulo(),
                    libro.getAutores().stream().map(Author::getAutor).collect(Collectors.joining(", ")),
                    libro.getLenguaje(),
                    libro.getDescargas().toString()));


            libroRepository.save(libro);


        }catch (Exception e){
            System.out.println("No se encuentra ese libro");
            e.printStackTrace();
        }

    }


    private void consultarLibros() {
        libros = libroRepository.findAll();
        libros.stream().forEach(l -> {
            System.out.printf("""
                                Titulo: %s
                                Autor: %s
                                Lenguaje: %s
                                Descargas: %s
                            %n""", l.getTitulo(),
            l.getAutores(),
            l.getLenguaje(),
            l.getDescargas().toString());
        });
    }


    private void consultarAutores() {
        autores = authorRepository.findAll();
        autores.stream().forEach(a -> {
            System.out.println("""
                        Autor: %s
                        Año de nacimiento: %s
                        Año de defuncion: %s
                    """.formatted(a.getAutor(),
                    a.getNacimiento().toString(),
                    a.getDefuncion().toString()));
        });
    }


    public void consultarAutoresPorAno()
    {
        System.out.println("Ingrese el año a partir del cual buscar:");
        var anoBusqueda = scanner.nextInt();
        scanner.nextLine();

        List<Author> authors = authorRepository.autorPorFecha(anoBusqueda);
        authors.forEach( a -> {
            System.out.println("""
                    Nombre: %s
                    Fecha de nacimiento: %s
                    Fecha de defuncion: %s
                    """.formatted(a.getAutor(),a.getNacimiento().toString(),a.getDefuncion().toString()));
        });
    }


    private void consultarLibrosLenguaje()
    {
        System.out.println("""
                ****************************************************************    
                    Selccione el lenguaje de los libros que deseas consultar
                ****************************************************************
                1 - En (Inglés)
                2 - Es (Español)
                
                *****************************************************************
                """);

        try {

            var opcion2 = scanner.nextInt();
            scanner.nextLine();

            switch (opcion2) {
                case 1:
                    libros = libroRepository.findByLenguaje("en");
                    break;
                case 2:
                    libros = libroRepository.findByLenguaje("es");
                    break;

                default:
                    System.out.println("Ingrese una opción valida");
            }

            libros.stream().forEach(l -> {
                System.out.println("""    
                        Título: %s
                        Autor: %s
                        Lenguaje: %s
                        Descargas: %s
                    """.formatted(l.getTitulo(),
                        l.getAutores(),
                        l.getLenguaje(),
                        l.getDescargas().toString()));
            });

        } catch (Exception e){
            System.out.println("Ingrese una opción válida");
        }
    }
}
