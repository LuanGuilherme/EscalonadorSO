import java.io.*;
import java.util.*;
import bcp.*;

public class Main {

	public static void main(String[] args) {

		// Cria uma lista ligada de instâncias de BCP (note-se: temos um objeto BCP por processo,
		// é como se um processo fosse um BCP)
		LinkedList<BCP> processos = new LinkedList<>();

		int i = 0;

		// Loop que inicializa os objetos BCPs
		while (i < 10) {
			
			// Adiciona uma instância de BCP a processos
			processos.add(new BCP());

			// Atribui o texto programa ao processo i
			leProcessos(i, processos.get(i));

			i++;

		}

	}

	public static void leProcessos (int i, BCP processo) {

		// i é um valor em [0, 9], logo são 10 valores mas iniciando em zero, por isso,
		// quando i for menor que 9, basta somá-lo a um, concatenar um zero e convertê-lo para 
		// string para obtermos o número do processo, todavia, se i for igual ou maior que 9,
		// ao somar mais um o resultado será um número de dois algarismos, de modo que só é
		// preciso converter esta soma para string
		String numProcesso = (i < 9) ? "0" + String.valueOf(i+1) : String.valueOf(i+1);

		int j = 0;

		try {

			// Objeto que representa o caminho dado como parâmetro
      		File arq = new File("./programas/" + numProcesso + ".txt");

      		// Abre o arquivo no caminho de arq
      		Scanner leitor = new Scanner(arq);

      		// Loop é executado enquanto houver linhas no arquivo aberto
      		while (leitor.hasNextLine()) {

      			// Atribui cada uma das linhas do arquivo aberto ao atributo textoPrograma (um vetor)
      			// do processo i
        		processo.setLinhaTexto(j, leitor.nextLine());
        		System.out.println(processo.getLinhaTexto(j)); //REMOVER
        		j++;

      		}
      		System.out.println(); // REMOVER
      		// Fecha o arquivo anteriormente aberto
      		leitor.close();

    	} catch (FileNotFoundException e) {
      		System.out.println("Ocorreu um erro durante a leitura dos arquivos.");
    	}
	}
	
}