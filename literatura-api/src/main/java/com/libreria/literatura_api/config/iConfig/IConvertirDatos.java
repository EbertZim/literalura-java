package com.libreria.literatura_api.config.iConfig;

public interface IConvertirDatos {
    <T> T convertirDatosJsonAJava(String json , Class<T> clase);
}
