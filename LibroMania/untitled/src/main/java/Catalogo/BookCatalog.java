package Catalogo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BookCatalog {

    private static final String API_URL = "https://gutendex.com/books/";

    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private List<JsonNode> resultadosBusqueda;
    private String nextUrl;
    private String urlActual;

    public BookCatalog() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        this.mapper = new ObjectMapper();
        this.resultadosBusqueda = new ArrayList<>();
        this.urlActual = null;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            Menu.mostrarMenu();
            int opcion = Menu.leerOpcion();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo(Menu.leerTexto("Ingrese el título del libro"));
                    break;
                case 2:
                    buscarLibrosPorAutor(Menu.leerTexto("Ingrese el nombre del autor"));
                    break;
                case 3:
                    buscarLibrosPorGenero(Menu.leerTexto("Ingrese el género del libro"));
                    break;
                case 4:
                    buscarLibrosPorIdioma(Menu.leerTexto("Ingrese el idioma del libro"));
                    break;
                case 5:
                    mostrarResultadosBusqueda("libros");
                    break;
                case 6:
                    mostrarResultadosBusqueda("autores");
                    break;
                case 7:
                    mostrarLibrosPorGenero();
                    break;
                case 8:
                    mostrarResultadosCompletos();
                    break;
                case 9:
                    continuarNuevaBusqueda();
                    break;
                case 10:
                    System.out.println("Saliendo del programa...");
                    running = false;
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione una opción válida del 1 al 10.");
            }
        }
        scanner.close();
    }

    private void buscarLibroPorTitulo(String titulo) {
        try {
            String encodedTitle = URLEncoder.encode(titulo, StandardCharsets.UTF_8.toString());
            String url = API_URL + "?search=" + encodedTitle;
            realizarConsulta(url, false, "", "", true, titulo);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error encoding URL: " + e.getMessage());
        }
    }

    private void buscarLibrosPorAutor(String autor) {
        try {
            String encodedAuthor = URLEncoder.encode(autor, StandardCharsets.UTF_8.toString());
            String url = API_URL + "?search=" + encodedAuthor;
            realizarConsulta(url, false, autor, "", false, "");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error encoding URL: " + e.getMessage());
        }
    }

    private void buscarLibrosPorGenero(String genero) {
        try {
            String encodedGenre = URLEncoder.encode(genero, StandardCharsets.UTF_8.toString());
            String url = API_URL + "?topic=" + encodedGenre;
            realizarConsulta(url, false, "", genero, false, "");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error encoding URL: " + e.getMessage());
        }
    }

    private void buscarLibrosPorIdioma(String idioma) {
        try {
            String encodedLanguage = URLEncoder.encode(idioma, StandardCharsets.UTF_8.toString());
            String url = API_URL + "?languages=" + encodedLanguage;
            realizarConsulta(url, false, "", "", false, "");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error encoding URL: " + e.getMessage());
        }
    }

    private void realizarConsulta(String url, boolean cargarMas, String filtroAutor, String filtroGenero, boolean esBusquedaPorTitulo, String tituloBuscado) {
        if (cargarMas && nextUrl != null) {
            url = nextUrl;
        } else if (!cargarMas) {
            this.urlActual = url;
        }

        if (this.urlActual == null) {
            this.urlActual = "";
        }

        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonNode jsonNode = mapper.readTree(responseBody);
                if (!cargarMas) {
                    resultadosBusqueda.clear();
                }
                nextUrl = jsonNode.path("next").asText(null);

                if (jsonNode.has("results") && jsonNode.get("results").isArray()) {
                    int count = 0;
                    for (JsonNode result : jsonNode.get("results")) {
                        if (count >= 5) break;  // Limitar a los primeros 5 resultados

                        boolean matchesFilter = true;
                        String tituloLibro = result.path("title").asText("").toLowerCase();

                        if (esBusquedaPorTitulo && !tituloLibro.contains(tituloBuscado.toLowerCase())) {
                            continue;
                        }

                        if (!filtroAutor.isEmpty()) {
                            matchesFilter = result.path("authors").findValuesAsText("name").stream()
                                    .anyMatch(name -> name.equalsIgnoreCase(filtroAutor));
                        }
                        if (!filtroGenero.isEmpty() && matchesFilter) {
                            matchesFilter = result.path("subjects").toString().toLowerCase().contains(filtroGenero.toLowerCase()) ||
                                    result.path("bookshelves").toString().toLowerCase().contains(filtroGenero.toLowerCase());
                        }

                        if (matchesFilter) {
                            resultadosBusqueda.add(result);
                            count++;
                        }
                    }
                    mostrarResultadosBusqueda("busqueda");
                }
            } else {
                System.out.println("Error al realizar la solicitud: " + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            System.out.println("Error en la conexión o al parsear la respuesta: " + e.getMessage());
        }
    }

    private void mostrarResultadosBusqueda(String tipo) {
        if (resultadosBusqueda.isEmpty()) {
            System.out.println("No se encontraron resultados para la búsqueda.");
            return;
        }

        System.out.println("Resultados de la búsqueda de " + tipo + ":");
        for (int i = 0; i < resultadosBusqueda.size(); i++) {
            JsonNode result = resultadosBusqueda.get(i);
            String titulo = result.path("title").asText("Título no disponible");
            StringBuilder autores = new StringBuilder();
            result.path("authors").forEach(author -> {
                if (autores.length() > 0) autores.append(", ");
                autores.append(author.path("name").asText("Nombre no disponible"));
            });

            StringBuilder generos = new StringBuilder();
            result.path("subjects").forEach(subject -> {
                if (generos.length() > 0) generos.append(", ");
                generos.append(subject.asText("Género no disponible"));
            });

            StringBuilder idiomas = new StringBuilder();
            result.path("languages").forEach(language -> {
                if (idiomas.length() > 0) idiomas.append(", ");
                idiomas.append(language.asText("Idioma no disponible"));
            });

            String descargas = result.path("download_count").asText("Descargas no disponibles");

            System.out.println((i + 1) + ". Título: " + titulo);
            System.out.println("   Autores: " + autores);
            System.out.println("   Géneros: " + generos);
            System.out.println("   Idiomas: " + idiomas);
            System.out.println("   Descargas: " + descargas);
        }

        if (nextUrl != null) {
            preguntarYcargarMasResultados();
        } else {
            System.out.println("No hay más resultados para mostrar.");
        }
    }

    private void preguntarYcargarMasResultados() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("¿Desea cargar más resultados? (si/no): ");
        String respuesta = scanner.nextLine();
        if ("si".equalsIgnoreCase(respuesta)) {
            realizarConsulta(nextUrl, true, "", "", false, "");
        } else {
            System.out.println("Regresando al menú principal...");
        }
    }

    private void mostrarResultadosCompletos() {
        if (resultadosBusqueda.isEmpty()) {
            System.out.println("No se encontraron resultados en la búsqueda completa.");
            return;
        }

        System.out.println("Lista completa de libros buscados:");
        for (JsonNode result : resultadosBusqueda) {
            System.out.println("- " + result.path("title").asText("Título no disponible"));
        }
    }

    private void continuarNuevaBusqueda() {
        resultadosBusqueda.clear();
        nextUrl = null;
        System.out.println("Listas de búsqueda limpiadas y listo para nueva búsqueda.");
    }

    private void mostrarLibrosPorGenero() {
        if (resultadosBusqueda.isEmpty()) {
            System.out.println("No se encontraron resultados para la búsqueda por género.");
            return;
        }

        Map<String, List<String>> librosPorGenero = new HashMap<>();
        for (JsonNode result : resultadosBusqueda) {
            result.path("subjects").forEach(subject -> {
                String genero = subject.asText("Género no disponible");
                librosPorGenero.computeIfAbsent(genero, k -> new ArrayList<>()).add(result.path("title").asText("Título no disponible"));
            });
        }

        librosPorGenero.forEach((genero, libros) -> {
            System.out.println("Género: " + genero);
            libros.forEach(libro -> System.out.println("  - " + libro));
        });
    }

    public static void main(String[] args) {
        BookCatalog catalogoLibros = new BookCatalog();
        catalogoLibros.run();
    }
}