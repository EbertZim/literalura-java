package com.libreria.literatura_api.models.records;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
      @JsonAlias ("id") Long libroId,
      @JsonAlias ("title") String titulo,
      @JsonAlias ("authors") List<Autor> autor,
      @JsonAlias ("subjects") List<String> genero,
      @JsonAlias ("langueges") List<String> idioma,
      @JsonAlias("formats") Media imagen,
      @JsonAlias("download_count") Long cantidadDescargas
) {
}
