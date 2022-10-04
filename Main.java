import java.io.*;
import java.util.*;
import bcp.*;
import comparadorProcesso.*;

public class Main {
	static Logfile log;

	public static void main(String[] args) {

		// Cria uma lista ligada de instâncias de BCP (note-se: temos um objeto BCP por
		// processo,
		// é como se um processo fosse um BCP)
		LinkedList<BCP> processos = new LinkedList<>();

		int i = 0;
		int quantum;

		// Loop que inicializa os objetos BCPs
		while (i < 10) {

			// Adiciona uma instância de BCP a processos
			processos.add(new BCP());

			// Atribui o texto programa ao processo i
			leProcessos(i, processos.get(i));

			// Inicializa os processos no estado "pronto"
			processos.get(i).setEstado(0);

			i++;

		}

		// Atribui as prioridades dos processos
		atribuiPrioridade(processos);

		// Ordena em ordem decrescente os processos de acordo com seus créditos
		Collections.sort(processos, new ComparadorProcesso());

		quantum = atribuiQuantum();

		log = new Logfile(quantum);

		executando(processos, quantum);

		// for(BCP tempBcp: processos) {
		// System.out.println(tempBcp);
		// }

	}

	public static void leProcessos(int i, BCP processo) {

		// i é um valor em [0, 9], logo são 10 valores mas iniciando em zero, por isso,
		// quando i for menor que 9, basta somá-lo a um, concatenar um zero e
		// convertê-lo para
		// string para obtermos o número do processo, todavia, se i for igual ou maior
		// que 9,
		// ao somar mais um o resultado será um número de dois algarismos, de modo que
		// só é
		// preciso converter esta soma para string
		String numProcesso = (i < 9) ? "0" + String.valueOf(i + 1) : String.valueOf(i + 1);

		int j = 0;

		try {

			// Objeto que representa o caminho dado como parâmetro
			File arq = new File("./programas/" + numProcesso + ".txt");

			// Abre o arquivo no caminho de arq
			Scanner leitor = new Scanner(arq);

			// Loop é executado enquanto houver linhas no arquivo aberto
			while (leitor.hasNextLine()) {

				// Atribui cada uma das linhas do arquivo aberto ao atributo textoPrograma (um
				// vetor)
				// do processo i
				processo.setLinhaTexto(j, leitor.nextLine());
				j++;

			}

			// Fecha o arquivo anteriormente aberto
			leitor.close();

		} catch (FileNotFoundException e) {

			System.out.println("Ocorreu um erro durante a leitura dos arquivos de programas.");

		}
	}

	public static void atribuiPrioridade(LinkedList<BCP> processos) {

		int i = 0;

		try {

			// Objeto que representa o caminho dado como parâmetro
			File arq = new File("./programas/prioridades.txt");

			// Abre o arquivo no caminho de arq
			Scanner leitor = new Scanner(arq);

			// Loop é executado enquanto houver linhas no arquivo aberto
			while (leitor.hasNextLine()) {

				// Atribui a prioridade do processo i
				processos.get(i).setPrioridade(Integer.valueOf(leitor.nextLine()));

				// Atribui o crédito do processo i (o qual, no primeiro momento, é igual à
				// prioridade)
				processos.get(i).setCreditos(processos.get(i).getPrioridade());

				i++;

			}

			// Fecha o arquivo anteriormente aberto
			leitor.close();

		} catch (FileNotFoundException e) {

			System.out.println("Ocorreu um erro durante a leitura do arquivo de prioridades.");

		}

	}

	public static int atribuiQuantum() {

		int ret;

		try {

			// Objeto que representa o caminho dado como parâmetro
			File arq = new File("./programas/quantum.txt");

			// Abre o arquivo no caminho de arq
			Scanner leitor = new Scanner(arq);

			// Atribui o quantum
			ret = Integer.valueOf(leitor.nextLine());

			// Fecha o arquivo anteriormente aberto
			leitor.close();

			return ret;

		} catch (FileNotFoundException e) {

			System.out.println("Ocorreu um erro durante a leitura do arquivo de quantum.");

		}

		return 0;

	}

	public static void carregando(LinkedList<BCP> processos) {
		int i = 0;
		while (i < 10)
			log.addLog("Carregando " + processos.get(i++).getLinhaTexto(0));

	}

	public static void executando(LinkedList<BCP> processos, int quantum) {
		// Imprime a informação de carregamento
		carregando(processos);
		for (int i = 0; i < 5; i++) {
			executaProcesso(processos, quantum);
		}
		
	}

	public static void executaProcesso(LinkedList<BCP> processos, int quantum) {
		int i = 0;
		String instrucao;
		int j;
		while (i < 10) {

			//j = processos.get(i).contadorPrograma;
			processos.get(i).setCreditos(processos.get(i).getCreditos() - 1);

			// Passa o i-ésimo processo para o estado "executando"
			processos.get(i).setEstado(1);

			log.addLog("Executando " + processos.get(i).getLinhaTexto(0));

			// Começa em um pois a linha zero é o nome do processo
			j = 1;

			System.out.println("\n");
			// Menor ou igual pois j começa em um
			while (j <= quantum) {
				
				// Lê a j-ésima instrução do i-ésimo processo
				instrucao = processos.get(i).getLinhaTexto(processos.get(i).contadorPrograma);

				if (instrucao == null) break;
				processos.get(i).setContador(processos.get(i).contadorPrograma+1);

				System.out.println(processos.get(i).contadorPrograma);
				
				// Verifica se a instrução é uma atribuição em X
				if (instrucao.contains("X=")) {

					// Atribui o valor ao registrador X (posição 2 pois é terceiro valor da string)
					String string = instrucao.substring(2);
					int value = Integer.parseInt(string);
					processos.get(i).setRegistradorX(value);

					// System.out.println(processos.get(i).getRegistradorX() + " " +
					// instrucao.charAt(2));
				}

				if (instrucao.contains("Y=")) {

					String string = instrucao.substring(2);
					int value = Integer.parseInt(string);
					processos.get(i).setRegistradorY(value);

					// processos.get(i).setRegistradorY(instrucao.charAt(2));
				}

				// Verifica se a instrução é uma atribuição em X
				if (instrucao.contains("E/S")) {
					log.addLog("E/S iniciada em " + processos.get(i).getLinhaTexto(0));
					if (j == 1)
						log.addLog("Interrompendo " + processos.get(i).getLinhaTexto(0) + " após 1 instrução");

					else
						log.addLog("Interrompendo " + processos.get(i).getLinhaTexto(0) + " após " + j + " instruções");

					break;

				}

				if (instrucao.contains("SAIDA")) {
					log.addLog("Interrompendo " + processos.get(i).getLinhaTexto(0) + " após " + j + " instruções");
				}

				j++;

			}

			// Verifica se o processo executou todos os processos no quantum sem
			// interrupções
			if (j > quantum && !processos.get(i).getLinhaTexto(j - 1).contains("E/S"))
				log.addLog("Interrompendo " + processos.get(i).getLinhaTexto(0) + " após " + quantum + " instruções");

			i++;

		}

	}

}