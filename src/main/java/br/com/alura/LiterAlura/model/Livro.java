package br.com.alura.LiterAlura.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @ElementCollection
    private List<String> autores;

    private String formatoTexto;

    public Livro() {
    }

    public Livro(String titulo, List<String> autores, String formatoTexto) {
        this.titulo = titulo;
        this.autores = autores;
        this.formatoTexto = formatoTexto;
    }
}
