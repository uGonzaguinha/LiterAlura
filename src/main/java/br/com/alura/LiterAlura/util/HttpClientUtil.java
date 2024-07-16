package br.com.alura.LiterAlura.util;

import br.com.alura.LiterAlura.model.Autor;
import br.com.alura.LiterAlura.model.Livro;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class HttpClientUtil {

    private static final String BASE_URL = "http://gutendex.com/books/";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HttpClientUtil() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public List<Livro> buscarLivrosPorTitulo(String titulo) throws IOException, InterruptedException {
        String tituloEncoded = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + "?search=" + tituloEncoded))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        List<Livro> livros = new ArrayList<>();
        if (response.statusCode() == 200) {
            String responseBody = response.body();
            GutendexResponse gutendexResponse = objectMapper.readValue(responseBody, GutendexResponse.class);
            for (BookData bookData : gutendexResponse.getResults()) {
                List<String> autores = new ArrayList<>();
                for (Object autor : bookData.getAuthors()) {
                    if (autor instanceof String) {
                        autores.add((String) autor);
                    } else if (autor instanceof AuthorData) {
                        autores.add(((AuthorData) autor).getName());
                    }
                }

                StringBuilder formatos = new StringBuilder();
                for (Object formato : bookData.getFormats()) {
                    if (formato instanceof String) {
                        formatos.append((String) formato).append(", ");
                    } else {
                        formatos.append("Formato não reconhecido").append(", ");
                    }
                }
                String formatosString = formatos.length() > 0 ? formatos.substring(0, formatos.length() - 2) : "";

                Livro livro = new Livro(bookData.getTitle(), autores, formatosString);
                livros.add(livro);
            }
        } else {
            throw new IOException("Erro ao buscar livros. Código de status: " + response.statusCode());
        }

        return livros;
    }

    public Autor buscarPrimeiroAutorPorLivroId(String livroId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL + livroId))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            Livro livro = objectMapper.readValue(responseBody, Livro.class);
            Autor autor = new Autor();
            autor.setNome(livro.getAutores().get(0));
            return autor;
        } else {
            throw new IOException("Erro ao buscar autor. Código de status: " + response.statusCode());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GutendexResponse {
        private List<BookData> results;

        public List<BookData> getResults() {
            return results;
        }

        public void setResults(List<BookData> results) {
            this.results = results;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class BookData {
        private String title;
        private List<AuthorData> authors;
        private List<Object> formats;

        @JsonProperty("title")
        public String getTitle() {
            return title;
        }

        @JsonProperty("title")
        public void setTitle(String title) {
            this.title = title;
        }

        @JsonProperty("authors")
        public List<AuthorData> getAuthors() {
            return authors;
        }

        @JsonProperty("authors")
        public void setAuthors(List<AuthorData> authors) {
            this.authors = authors;
        }

        @JsonProperty("formats")
        public List<Object> getFormats() {
            return formats;
        }

        @JsonProperty("formats")
        public void setFormats(List<Object> formats) {
            this.formats = formats;
        }
    }


    private static class AuthorData {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
