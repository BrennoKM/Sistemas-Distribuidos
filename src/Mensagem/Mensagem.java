package Mensagem;

import java.io.Serializable;

public class Mensagem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String emissor, destinatario, conteudo, tipo, ultimoReceptor;
	
	public Mensagem(String emissor, String destinatario, String conteudo, String tipo) {
		this.emissor = emissor;
		this.destinatario = destinatario;
		this.conteudo = conteudo;
		this.tipo = tipo;
	}
	
	public String getEmissor() {
		return this.emissor;
	}
	
	public String getDestinatario() {
		return this.destinatario;
	}
	
	public String getConteudo() {
		return this.conteudo;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public String toString() {
		return this.emissor + " --> " + this.conteudo;
	}

	public String getUltimoReceptor() {
		return ultimoReceptor;
	}

	public void setUltimoReceptor(String ultimoReceptor) {
		this.ultimoReceptor = ultimoReceptor;
	}
}
