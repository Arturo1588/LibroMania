package Catalogo;

import java.util.Scanner;

public class Menu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void mostrarMenu() {
        System.out.println("===== Bienvenido al Catálogo de Libros =====");
        System.out.println("Seleccione una opción:");
        System.out.println("1. Buscar libro por título");
        System.out.println("2. Buscar libros por autor");
        System.out.println("3. Buscar libros por género");
        System.out.println("4. Buscar libro por idioma");
        System.out.println("5. Lista de libros registrados");
        System.out.println("6. Lista de autores registrados");
        System.out.println("7. Lista de géneros registrados");
        System.out.println("8. Lista Completa de libros");
        System.out.println("9. Hacer nueva búsqueda");
        System.out.println("10. Salir");
        System.out.print("Ingrese el número de la opción deseada: ");
    }

    public static int leerOpcion() {
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Consume el salto de línea después de leer el número
        return opcion;
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine();
    }

    public static void cerrarScanner() {
        scanner.close();
    }
}