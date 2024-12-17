package com.libreria.literatura_api.models;

import com.libreria.literatura_api.dto.Genero;
import com.libreria.literatura_api.models.records.DatosLibro;
import com.libreria.literatura_api.models.records.Media;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Data
@Table (name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long libroId;
    @Column(unique = true)
    private String titulo;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    @Enumerated(EnumType.STRING)
    private Genero genero;
    private String idioma;
    private String imagen;
    private Long cantidadDescargas;

    public Libro() {
    }
    public Libro(DatosLibro datosLibro) {
        this.libroId = datosLibro.libroId();
        this.titulo = datosLibro.titulo();
        // Si autor es una lista de autores (como parece en tu registro DatosLibro)
        if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
            this.autor = new Autor(datosLibro.autor().get(0)); // Toma el primer autor de la lista
        } else {
            this.autor = null; // o maneja el caso de que no haya autor
        }
        this.genero =  generoModificado(datosLibro.genero());
        this.idioma = idiomaModificado(datosLibro.idioma());
        this.imagen = imagenModificada(datosLibro.imagen());
        this.cantidadDescargas = datosLibro.cantidadDescargas();
    }



    public Libro(Libro libro) {
    }
    private Genero generoModificado (List<String> generos){
        if (generos == null || generos.isEmpty() ){
            return Genero.DESCONOCIDO;
        }
        Optional<String> firstGenero = generos.stream()
                .map(g -> {
                    int index = g.indexOf("--");
                    return index != -1 ? g.substring(index + 2).trim() : null;
                })
                .filter(Objects::nonNull)
                .findFirst();
        return firstGenero.map(Genero::fromString).orElse(Genero.DESCONOCIDO);
    }
    private String idiomaModificado(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "Desconocido";
        }
        return idiomas.get(0);
    }
    private String imagenModificada(Media media) {
        if (media == null || media.imagen().isEmpty()) {
            return "Sin imagen";
        }
        return media.imagen();
    }
    @Override
    public String toString() {
        return
                "  \nid=" + id +
                        "  \nLibro id=" + libroId +
                        ", \ntitulo='" + titulo + '\'' +
                        ", \nauthors=" + (autor != null ? autor.getNombre() : "N/A")+
                        ", \ngenero=" + genero +
                        ", \nidioma=" + idioma +
                        ", \nimagen=" + imagen +
                        ", \ncantidadDescargas=" + cantidadDescargas;
    }
}
