package Catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadFormats {
    private String pdf;
    private String plaintext;
    private String epub;

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }

    public String getEpub() {
        return epub;
    }

    public void setEpub(String epub) {
        this.epub = epub;
    }

    @Override
    public String toString() {
        return "DownloadFormats{" +
                "pdf='" + pdf + '\'' +
                ", plaintext='" + plaintext + '\'' +
                ", epub='" + epub + '\'' +
                '}';
    }
}