package com.libreria.literatura_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libreria.literatura_api.config.iConfig.IConvertirDatos;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ConvertirDatos implements IConvertirDatos {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T convertirDatosJsonAJava(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON a objeto Java", e); // Manejo explícito de la excepción
        }
    }
}