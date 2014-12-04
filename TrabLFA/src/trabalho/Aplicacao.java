package trabalho;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public class Aplicacao {

	public Aplicacao() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws IOException {
		
		Gramatica gramatica = new Gramatica();
		try{
			File arquivoEntrada = new File("gramatica.txt");
		//	File arquivoSaida = new File("FNC.txt");
			Reader entrada = new FileReader(arquivoEntrada);
			LineNumberReader reader = new LineNumberReader(entrada);
			
			while(reader.ready()){
				gramatica.adicionaRegra(reader.readLine());
			}
			reader.close();
			entrada.close();
		} catch (FileNotFoundException e) {
			System.out.println( "Arquivo não existe");
		}
		
		System.out.println("Gramatica antes de qualquer processamento:");
		gramatica.mostrarGramatica();
		gramatica.removeRecusaoInicial();
		System.out.println("Gramatica com simbolo inicial não recursivo: ");
		gramatica.mostrarGramatica();
		
		System.out.println("Gramatica essencialmente não contrátil:");
		gramatica.temLambda();
		gramatica.removeLambda();
		gramatica.mostrarGramatica();
		
		System.out.println("Gramatica tem regra da cadeia? " + gramatica.temRegraCadeia());
		
		gramatica.regraCadeia();
		System.out.println("Sem regra da cadeia:");
		gramatica.mostrarGramatica();
		
		gramatica.removeNaoTerminais();
		System.out.println("Gramatica sem não terminais: ");
		gramatica.mostrarGramatica();
		
		System.out.println("O indice é: " + gramatica.retornaIndice("S"));
		
		gramatica.removeInuteis();
		
		gramatica.chomsky();
		
		System.out.println("Gramatica sem simbolos inuteis");
		gramatica.mostrarGramatica();
		
		// teste
		System.out.println("Tem C: " + gramatica.existeVariavel("C"));
		
	}

}
