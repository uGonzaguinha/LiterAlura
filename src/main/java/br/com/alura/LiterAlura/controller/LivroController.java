package br.com.alura.LiterAlura.controller;

import br.com.alura.LiterAlura.model.Livro;
import br.com.alura.LiterAlura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public ResponseEntity<List<Livro>> buscarTodosLivros() {
        List<Livro> livros = livroService.buscarTodos();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarLivroPorId(@PathVariable Long id) {
        Livro livro = livroService.buscarPorId(id);
        if (livro != null) {
            return ResponseEntity.ok(livro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Livro> salvarLivro(@RequestBody Livro livro) {
        Livro livroSalvo = livroService.salvar(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}