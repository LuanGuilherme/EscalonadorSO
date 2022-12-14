package bcp;

import registradores.*;

public class BCP {

	public int contadorPrograma = 1;

	// Estados: -1 - bloqueado; 0 - pronto; 1 - executando
	public int estado;

	public int prioridade;
	public int creditos;
	public Registradores registradores;
	public String [] textoPrograma = new String[22];

	public BCP() {

		// Inicializa os registradores com o valor inválido -1
		this.registradores = new Registradores(-1, -1);

	}

	public int getEstado() {
		return this.estado;
	}

	public int getPrioridade() {
		return this.prioridade;
	}

	public int getCreditos() {
		return this.creditos;
	}

	public int getRegistradorX() {
		return this.registradores.getX();
	}

	public int getRegistradorY() {
		return this.registradores.getY();
	}

	public String getLinhaTexto(int numLinha) {
		return this.textoPrograma[numLinha];
	}

	public int getContador() {
		return this.contadorPrograma;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}

	public void setRegistradorX(int x) {
		this.registradores.setX(x);
	}

	public void setRegistradorY(int y) {
		this.registradores.setY(y);
	}

	public void setContador(int i) {
		this.contadorPrograma = i;
	}


	public void setLinhaTexto(int numLinha, String linha) {
		this.textoPrograma[numLinha] = linha;
	}

	@Override
	public String toString() {
		String bcp = "Estado: " + getEstado() 
			+ " Creditos: " + getCreditos() 
			+ " Prioridade: " + getPrioridade() 
			+ " X: " + getRegistradorX() 
			+ " Y: " + getRegistradorY() 
			+ " Contador: " + contadorPrograma + "\n";
			//+ getLinhaTexto(numLinha);

		// for (String string : textoPrograma) {
		// 	bcp = bcp + string + "\n";
		// }
		return bcp;
	}

}