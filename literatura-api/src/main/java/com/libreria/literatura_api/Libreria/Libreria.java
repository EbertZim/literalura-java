package com.libreria.literatura_api.Libreria;


import com.libreria.literatura_api.config.ConsumoApiGutendex;
import com.libreria.literatura_api.config.ConvertirDatos;
import com.libreria.literatura_api.models.Autor;
import com.libreria.literatura_api.models.Libro;
import com.libreria.literatura_api.models.LibrosRespuestaApi;
import com.libreria.literatura_api.models.records.DatosLibro;
import com.libreria.literatura_api.repository.AutorRepository;
import com.libreria.literatura_api.repository.LibroRepository;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class Libreria {
   private Scanner teclado = new Scanner(System.in);
   private ConsumoApiGutendex consumoApi = new ConsumoApiGutendex();
   private ConvertirDatos convertir = new ConvertirDatos();
   private static String API_BASE = "https://gutendex.com/books/?search=";
   private List<Libro> datosLibro = new ArrayList<>();
   private LibroRepository libroRepository;
   private AutorRepository autorRepository;
   public Libreria(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
   }
   public void consumo(){
       var opcion = -1;
       while(opcion != 0) {
           var menu = """
                   
                   |------------------------------------------------------|
                   |------         BIENVENDIO A LA LIBRERIA     ----------|
                   |------------------------------------------------------|
                   1 - Agregar Libro por Nombre
                   2 - Libros buscados
                   3 - Buscar libro por Nombre
                   4 - Buscar todos los Autores de libros buscados
                   5 - Buscar Autores por año
                   6 - Buscar Libros por Idioma
                   7 - Top 10 Libros mas Descargados
                   8 - Buscar Autor por Nombre



                   0 - Salir
                   
                   |------------------------------------------------------|
                   |------         INGRESE LA OPCIÓN            ----------|
                   |------------------------------------------------------|
                   """;
           try{
               System.out.println(menu);
               opcion = teclado.nextInt();
               teclado.nextLine();
           } catch (InputMismatchException e) {
               System.out.println("""
                   |------------------------------------------------------|
                   |------    Lo siento ;), Opcion no encontrada   -------|
                   |------------------------------------------------------|
                       """);
               teclado.nextLine();
               continue;
           }
           switch(opcion){
               case 1:
                   buscarLibroEnLaWeb();
                   break;
                   case 2:
                       librosBuscados();
                       break;
                       case 3:
                           buscarLibroPorNombre();
                           break;
                           case 4:
                               BuscarAutores();
                               break;
                               case 5:
                                   buscarAutoresPorAnio();
                                   break;
                                   case 6:
                                       buscarLibrosPorIdioma();
                                       break;
                                       case 7:
                                           top10LibrosMasDescargados();
                                           break;
                                           case 8:
                                               buscarAutorPorNombre();
                                               break;
               case 0:
                  opcion = 0;
                   System.out.println("""
                   |------------------------------------------------------|
                   |------            Hasta Pronto :)              -------|
                   |------------------------------------------------------|
                       """);
                   break;
                   default:
                       System.out.println("""
                   |------------------------------------------------------|
                   |------   Opción no valida, Vuelva a intentar   -------|
                   |------------------------------------------------------|
                       """);
                       consumo();
                       break;
           }
       }
   }
   private Libro getDatosLibro(){
       System.out.println("Ingrese el nombre del libro: ");
       var nombreLibro = teclado.nextLine().toLowerCase();
       var json = consumoApi.obtenerDatos(API_BASE + nombreLibro.replace(" ", "%20"));
       LibrosRespuestaApi datos = convertir.convertirDatosJsonAJava(json, LibrosRespuestaApi.class);

       if (datos != null && datos.getResultadoLibros() != null && !datos.getResultadoLibros().isEmpty()) {
           DatosLibro primerLibro = datos.getResultadoLibros().get(0); // Obtener el primer libro de la lista
           return new Libro(primerLibro);
       } else {
           System.out.println("No se encontraron resultados.");
           return null;
       }
   }
   private void buscarLibroEnLaWeb(){
       Libro libro = getDatosLibro();

       if (libro == null){
           System.out.println("Libro no encontrado. el valor es null");
           return;
       }

       try{
           boolean libroExists = libroRepository.existsByTitulo(libro.getTitulo());
           if (libroExists){
               System.out.println("El libro ya existe en la base de datos!");
           }else {
               libroRepository.save(libro);
               System.out.println(libro.toString());
           }
       }catch (InvalidDataAccessApiUsageException e){
           System.out.println("No se puede persisitir el libro buscado!");
       }
   }
    @Transactional(readOnly = true)
    protected void librosBuscados(){
        //datosLibro.forEach(System.out::println);
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros encontrados en la base de datos:");
            for (Libro libro : libros) {
                System.out.println(libro.toString());
            }
        }
    }
    private void buscarLibroPorNombre(){
        System.out.println("Ingrese Titulo libro que quiere buscar: ");
        var titulo = teclado.nextLine();
        Libro libroBuscado = libroRepository.findByTituloContainsIgnoreCase(titulo);
        if (libroBuscado != null) {
            System.out.println("El libro buscado fue: " + libroBuscado);
        } else {
            System.out.println("El libro con el titulo '" + titulo + "' no se encontró.");
        }
   }
    private void BuscarAutores(){
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos. \n");
        } else {
            System.out.println("Libros encontrados en la base de datos: \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autores) {
                // add() retorna true si el nombre no estaba presente y se añade correctamente
                if (autoresUnicos.add(autor.getNombre())){
                    System.out.println(autor.getNombre()+'\n');
                }
            }
        }
    }
    private void getBuscarLibrosPorIdioma(){
        System.out.println("Ingrese Idioma en el que quiere buscar: \n");
        System.out.println("|***********************************|");
        System.out.println("|  Opción - es : Libros en español. |");
        System.out.println("|  Opción - en : Libros en ingles.  |");
        System.out.println("|***********************************|\n");

        var idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros segun idioma encontrados en la base de datos:");
            for (Libro libro : librosPorIdioma) {
                System.out.println(libro.toString());
            }
        }
    }
    private void buscarAutoresPorAnio(){
        System.out.println("Indica el año para consultar que autores estan vivos: \n");
        var anioBuscado = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autoresVivos = autorRepository.findByCumpleaniosLessThanOrFechaFallecimientoGreaterThanEqual(anioBuscado, anioBuscado);

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores que estuvieran vivos en el año " + anioBuscado + ".");
        } else {
            System.out.println("Los autores que estaban vivos en el año " + anioBuscado + " son:");
            Set<String> autoresUnicos = new HashSet<>();

            for (Autor autor : autoresVivos) {
                if (autor.getCumpleanios() != null && autor.getFechaFallecimiento() != null) {
                    if (autor.getCumpleanios() <= anioBuscado && autor.getFechaFallecimiento() >= anioBuscado) {
                        if (autoresUnicos.add(autor.getNombre())) {
                            System.out.println("Autor: " + autor.getNombre());
                        }
                    }
                }
            }
        }
    }
    private void buscarLibrosPorIdioma(){
        System.out.println("Ingrese el Idioma en el que quiere buscar: \n");
        System.out.println("""
                |***********************************|
                |  Opción - es : Libros en español. |
                |  Opción - en : Libros en ingles.  |
                |***********************************|\n
                """);
        var idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros segun idioma encontrados en la base de datos:");
            for (Libro libro : librosPorIdioma) {
                System.out.println(libro.toString());
            }
        }
    }
    private void top10LibrosMasDescargados(){
        List<Libro> top10Libros = libroRepository.findTop10ByTituloByCantidadDescargas();
        if (!top10Libros.isEmpty()){
            int index = 1;
            for (Libro libro: top10Libros){
                System.out.printf("Libro %d: %s Autor: %s Descargas: %d\n",
                        index, libro.getTitulo(), libro.getAutor().getNombre(), libro.getCantidadDescargas());
                index++;
            }
        }
    }
    private void buscarAutorPorNombre(){
        System.out.println("Ingrese nombre del escritor que quiere buscar: ");
        var escritor = teclado.nextLine();
        Optional<Autor> escritorBuscado = autorRepository.findFirstByNombreContainsIgnoreCase(escritor);
        if (escritorBuscado != null) {
            System.out.println("\nEl escritor buscado fue: " + escritorBuscado.get().getNombre());

        } else {
            System.out.println("\nEl escritor con el titulo '" + escritor + "' no se encontró.");
        }
    }
}
