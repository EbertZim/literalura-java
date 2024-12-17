package com.libreria.literatura_api.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "autores")

public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer cumpleanios;
    private Integer fechaFallecimiento;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Libro> libros;
    public Autor() {}

    public Autor(com.libreria.literatura_api.models.records.Autor autor) {
        this.nombre = autor.nombre();
        this.cumpleanios = autor.cumpleanios();
        this.fechaFallecimiento = autor.fechaFallecimiento();
    }

    @Override
    public String toString() {
        return
                "nombre='" + nombre +'\''+
                ", cumpleaños=" + cumpleanios +
                ", fecha fallecimiento=" + fechaFallecimiento;
}


}
