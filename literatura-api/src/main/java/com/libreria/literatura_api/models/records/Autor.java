package com.libreria.literatura_api.models.records;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Autor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer cumpleanios,
        @JsonAlias("death_year") Integer fechaFallecimiento
) {
}
