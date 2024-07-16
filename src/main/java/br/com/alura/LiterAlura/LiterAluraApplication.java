package br.com.alura.LiterAlura;

import br.com.alura.LiterAlura.model.Autor;
import br.com.alura.LiterAlura.model.Livro;
import br.com.alura.LiterAlura.service.LivroService;
import br.com.alura.LiterAlura.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	@Autowired
	private HttpClientUtil httpClientUtil;

	@Autowired
	private LivroService livroService;

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Bem-vindo ao LiterAlura - Catálogo de Livros!");
		System.out.println("Selecione uma opção:");
		System.out.println("1 - Buscar livros por título");
		System.out.println("2 - Buscar autor por ID do livro");
		System.out.print("Opção: ");

		int opcao = scanner.nextInt();
		scanner.nextLine(); // Consumir o newline após nextInt()

		switch (opcao) {
			case 1:
				System.out.print("Digite o título do livro: ");
				String titulo = scanner.nextLine();

				try {
					List<Livro> livros = httpClientUtil.buscarLivrosPorTitulo(titulo);
					System.out.println("Livros encontrados:");
					for (Livro livro : livros) {
						System.out.println(livro);
					}
				} catch (IOException e) {
					System.err.println("Erro ao buscar livros: " + e.getMessage());
				}
				break;

			case 2:
				System.out.print("Digite o ID do livro: ");
				String livroId = scanner.nextLine();

				try {
					Autor autor = httpClientUtil.buscarPrimeiroAutorPorLivroId(livroId);
					System.out.println("Autor encontrado:");
					System.out.println(autor);
				} catch (IOException e) {
					System.err.println("Erro ao buscar autor: " + e.getMessage());
				}
				break;

			default:
				System.out.println("Opção inválida.");
				break;
		}

		scanner.close();
	}
}
