package com.libreria.literatura_api;

import com.libreria.literatura_api.Libreria.Libreria;
import com.libreria.literatura_api.repository.AutorRepository;
import com.libreria.literatura_api.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApiApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApiApplication.class, args);
	}
	@Override
	public void run (String... args) throws Exception {
		Libreria libreria = new Libreria(libroRepository, autorRepository);
		libreria.consumo();
	}

}
