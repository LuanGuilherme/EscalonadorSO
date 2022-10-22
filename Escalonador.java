import java.io.*;
import java.util.*;
import bcp.*;
import comparadorProcesso.*;

public class Escalonador {

	static Logfile log;

    static float troca = 0; // Numero de trocas de processos
    static float nroProcs = 0; // Numero de processos
	static float instPorInter = 0; // Numero de instruções por interrupção
	static float quantaUsados = 0; // Numero de Quanta usados

	public static void main(String[] args) {

		// Cria uma lista ligada de instâncias de BCP (note-se: temos um objeto BCP por
		// processo, é como se um processo fosse um BCP)
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

        nroProcs = i;

		// Atribui os créditos dos processos
		atribuiCreditos(processos);

		// Ordena em ordem decrescente os processos de acordo com seus créditos
		Collections.sort(processos, new ComparadorProcesso());

		quantum = atribuiQuantum();

		log = new Logfile(quantum);

		// Cria uma lista de processos prontos (inicialmente contendo todos os processos)
		LinkedList<BCP> processosProntos = new LinkedList<>(processos);

		// Cria uma lista de processos bloqueados (inicialmente vazia)
		LinkedList<BCP> processosBloqueados = new LinkedList<>();

		executando(processosProntos, processosBloqueados, quantum);

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

	public static void atribuiCreditos(LinkedList<BCP> processos) {

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

				// Atribui o crédito do processo i (o qual, no primeiro momento, é igual à prioridade)
				processos.get(i).setCreditos(processos.get(i).getPrioridade());

				i++;

			}

			// Fecha o arquivo anteriormente aberto
			leitor.close();

		} catch (FileNotFoundException e) {

			System.out.println("Ocorreu um erro durante a leitura do arquivo de prioridades.");

		}

	}

	public static void reatribuiCreditos(LinkedList<BCP> processos) {

		int i = 0;

		while (i < processos.size()) {

			processos.get(i).setCreditos(processos.get(i).getPrioridade());

			i++;

		}

	}

	public static void verificaCreditos(LinkedList<BCP> processos) {

		int somaCreditos = 0;
		int i = 0;

		while (i < processos.size()) {

			somaCreditos += processos.get(i).getCreditos();

			i++;

		}

		if (somaCreditos == 0)
			reatribuiCreditos(processos);

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

	public static void executando(LinkedList<BCP> processosProntos, LinkedList<BCP> processosBloqueados, int quantum) {

		// Imprime a informação de carregamento
		carregando(processosProntos);

		executaProcesso(processosProntos, processosBloqueados, quantum);
		
	}

	public static void verificaBloqueados (LinkedList<BCP> processosBloqueados, LinkedList<BCP> processosProntos) {

		for (int i = 0; i < processosBloqueados.size(); i++) {

			// Remove 100 do program counting
			processosBloqueados.get(i).setContador(processosBloqueados.get(i).getContador() - 100);

			// Se o program counting for menor que 100, o processo é desbloqueado (passa para 'pronto')
			if (processosBloqueados.get(i).getContador() < 100) {

				processosBloqueados.get(i).setEstado(0);

				// Adiciona o processo pronto à fila de prontos e o remove da de bloqueados
				processosProntos.add(processosBloqueados.get(i));
				processosBloqueados.remove(i);

			}

		}

	}

	public static void executaProcesso(LinkedList<BCP> processosProntos, LinkedList<BCP> processosBloqueados, int quantum) {

		int j, flag;

		String instrucao;

		while (true) {

			flag = 1;

			if (processosProntos.size() == 0) {
				verificaBloqueados(processosBloqueados, processosProntos);
				break;
			}

			if (processosProntos.get(0).getCreditos() > 0) {
				// Ao iniciar a execução, o processo executando perde um crédito de prioridade
				processosProntos.get(0).setCreditos(processosProntos.get(0).getCreditos() - 1);
			} else {
				verificaCreditos(processosProntos);
			}

			log.addLog("Executando " + processosProntos.get(0).getLinhaTexto(0));

			// Passa o processo de maior prioridade para o estado "executando"
			processosProntos.get(0).setEstado(1);

			// Começa em um pois a linha zero é o nome do processo
			j = 1;

			// Menor ou igual pois j começa em um
			while (j <= quantum && processosProntos.get(0).getCreditos() >= 0) {
				
				instPorInter = instPorInter + j;

				// Lê a j-ésima instrução do processo em execução
				instrucao = processosProntos.get(0).getLinhaTexto(processosProntos.get(0).contadorPrograma);

				if (instrucao == null) break;

				// Incrementa program counting
				processosProntos.get(0).setContador(processosProntos.get(0).contadorPrograma+1);
				
				// Verifica se a instrução é uma atribuição em X
				if (instrucao.contains("X=")) {

					// Atribui o valor ao registrador X (posição 2 pois é terceiro valor da string)
					String string = instrucao.substring(2);
					int value = Integer.parseInt(string);
					processosProntos.get(0).setRegistradorX(value);

				}

				// Verifica se a instrução é uma atribuição em Y
				if (instrucao.contains("Y=")) {

					String string = instrucao.substring(2);
					int value = Integer.parseInt(string);
					processosProntos.get(0).setRegistradorY(value);

				}

				// Verifica se a instrução é uma E/S
				if (instrucao.contains("E/S")) {

					log.addLog("E/S iniciada em " + processosProntos.get(0).getLinhaTexto(0));

					if (j == 1)
						log.addLog("Interrompendo " + processosProntos.get(0).getLinhaTexto(0) + " após 1 instrução");

					else
						log.addLog("Interrompendo " + processosProntos.get(0).getLinhaTexto(0) + " após " + j + " instruções");

					troca++;
					quantaUsados += j;
					

					// Quando há uma E/S, o processo passa para o estado bloqueado
					processosProntos.get(0).setEstado(-1);

					// O processo bloqueado recebe mais 300 de program counting
					processosProntos.get(0).setContador(processosProntos.get(0).getContador() + 300);

					// Adiciona o processo bloqueado à fila de processos bloqueados e o remove da fila de prontos
					processosBloqueados.add(processosProntos.get(0));
					processosProntos.remove(0);

					// Decrementa i
					flag = 0;
					break;

				}

				if (instrucao.contains("SAIDA")) {
					log.addLog(processosProntos.get(0).getLinhaTexto(0) + " terminado. X=" + processosProntos.get(0).getRegistradorX() + ". Y=" + processosProntos.get(0).getRegistradorY());
					processosProntos.remove(0);
					flag = 0;
					troca++;
					break;
				}

				j++;

			}

			if (flag == 1) {

				// Muda o estado do processo que foi executado para pronto
				processosProntos.get(0).setEstado(0);

				// Verifica se o processo executou todos os processos no quantum sem interrupções
				if (j > quantum)
					log.addLog("Interrompendo " + processosProntos.get(0).getLinhaTexto(0) + " após " + quantum + " instruções");
					troca++;
					quantaUsados += 3;
			}

			// Ao terminar a execução dos quanta de um processo, reordena 
			Collections.sort(processosProntos, new ComparadorProcesso());

			// Atualiza os program countings dos processos bloqueados
			verificaBloqueados(processosBloqueados, processosProntos);
            
		}

        log.addLog("MEDIA DE TROCAS: " + (troca/nroProcs) + " \n" + "MEDIA DE INSTRUCOES: " + (instPorInter/quantaUsados) + " \n" + "QUANTUM: " + quantum);
            
	}

}