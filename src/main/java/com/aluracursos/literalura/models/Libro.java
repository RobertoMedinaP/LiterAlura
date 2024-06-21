package com.aluracursos.literalura.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "libro_autores",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autores_id")
    )
    private Set<Author> autores = new HashSet<>();
    private String titulo;
    private String lenguaje;
    private Integer descargas;

    public Libro(){}

    public Libro(DatosResultados datosLibro) {

        this.titulo = datosLibro.titulo();
        this.lenguaje = datosLibro.language().get(0);
        this.descargas = datosLibro.descargas();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }


    public void addAutor(Author author) {
        this.autores.add(author);
        author.getLibros().add(this);
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public Set<Author> getAutores() {
        return autores;
    }

    public void setAutores(Set<Author> autores) {
        this.autores = autores;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }
}
