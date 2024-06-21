package com.aluracursos.literalura.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "autores")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String autor;
    private Integer nacimiento;
    private Integer defuncion;

    @ManyToMany(mappedBy = "autores")
    private Set<Libro> libros = new HashSet<>();

    public Author(){}

    public Author(DatosAutor datosAutors) {
        this.autor = datosAutors.nombreAutor();
        this.nacimiento = datosAutors.nacimiento();
        this.defuncion = datosAutors.defuncion();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Integer nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Integer getDefuncion() {
        return defuncion;
    }

    public void setDefuncion(Integer defuncion) {
        this.defuncion = defuncion;
    }

    public Set<Libro> getLibros() {
        return libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "Author{" +
                "autor='" + autor + '\'' +
                '}';
    }
}

