package br.com.alura.LiterAlura.service;


import br.com.alura.LiterAlura.model.Livro;
import java.util.List;

public interface LivroService {
    List<Livro> buscarTodos();
    Livro buscarPorId(Long id);
    Livro salvar(Livro livro);
    void deletar(Long id);
}
