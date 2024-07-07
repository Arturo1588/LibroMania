# Book Catalog

Este proyecto es una aplicación de consola en Java que permite buscar y catalogar libros utilizando la API de Gutendex.

## Descripción
La aplicación permite realizar búsquedas de libros por título, autor, género e idioma, y muestra los primeros 5 resultados obtenidos. También permite navegar por los resultados obtenidos, mostrando detalles como el título, autores, géneros, idiomas y número de descargas.

## Características
- Búsqueda de libros por título
- Búsqueda de libros por autor
- Búsqueda de libros por género
- Búsqueda de libros por idioma
- Navegación por resultados obtenidos
- Visualización de detalles de los libros

## Requisitos
- Java 8 o superior
- Biblioteca OkHttp
- Biblioteca Jackson

## Instalación
1. Clona este repositorio:
    ```bash
    git clone https://github.com/tu_usuario/book-catalog.git
    ```
2. Importa el proyecto en tu IDE de preferencia (IntelliJ IDEA, Eclipse, etc.).
3. Asegúrate de tener las dependencias necesarias:
    - OkHttp
    - Jackson

## Uso
1. Compila y ejecuta la clase principal `BookCatalog`:
    ```bash
    javac Catalogo/BookCatalog.java
    java Catalogo.BookCatalog
    ```
2. Sigue las instrucciones en la consola para buscar libros por título, autor, género o idioma.

## Ejemplo de Uso
```java
public static void main(String[] args) {
    BookCatalog catalogoLibros = new BookCatalog();
    catalogoLibros.run();
}
