package br.com.alura.LiterAlura.service;


import br.com.alura.LiterAlura.model.Livro;
import br.com.alura.LiterAlura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LivroServiceImpl implements LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Override
    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    @Override
    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id).orElse(null);
    }

    @Override
    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }

    @Override
    public void deletar(Long id) {
        livroRepository.deleteById(id);
    }
}