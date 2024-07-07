package Catalogo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class Book {
    private String title;
    private String[] authors;
    private String language;
    private String[] formats;

    // Constructor vacío (puede ser opcional dependiendo de tus necesidades)
    public Book() {
    }

    // Getters y Setters para los atributos de la clase
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("authors")
    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("formats")
    public String[] getFormats() {
        return formats;
    }

    public void setFormats(String[] formats) {
        this.formats = formats;
    }

    // Método toString() para imprimir los atributos de la clase (opcional)
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors=" + Arrays.toString(authors) +
                ", language='" + language + '\'' +
                ", formats=" + Arrays.toString(formats) +
                '}';
    }
}