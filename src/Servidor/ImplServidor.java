package Servidor;

import java.io.IOException;
import java.util.List;

import Cliente.ConexaoCliente;
import Mensagem.Mensagem;

public class ImplServidor implements Runnable {

	List<ConexaoCliente> clientesInternos;
	List<ConexaoServidor> conexoesServidor;

	private static Mensagem ultimoBroadcast = new Mensagem(null, null, "", null),
			ultimoUnicast = new Mensagem(null, null, "", null);
	public String nome;
	public static int contConexoes = 0;
	private boolean conexao = true;

	public ImplServidor(List<ConexaoCliente> clientesInternos, List<ConexaoServidor> conexoesServidor, String nome) {
		this.clientesInternos = clientesInternos;
		this.conexoesServidor = conexoesServidor;
		this.nome = nome;
	}

	public void run() {
		int numThreads = 0;
		for (int i = 0; i < contConexoes; i++) {
			final int index = i;
			Thread threadServidor = new Thread(() -> servidorThread(index));
			threadServidor.start();
			numThreads = i;
		}
		while (true) {
			if (numThreads < contConexoes) {
				numThreads++;
				final int index = numThreads - 1;
				Thread threadServidor = new Thread(() -> servidorThread(index));
				threadServidor.start();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void servidorThread(int index) {
		Mensagem mensagemRecebida, mensagemAnterior = new Mensagem(null, null, "", null);
		try {
			while (conexao) {
				mensagemRecebida = (Mensagem) conexoesServidor.get(index).inputObject.readObject();
				if (mensagemAnterior != null
						&& !(mensagemAnterior.getConteudo().equals(mensagemRecebida.getConteudo()))) {
					if (mensagemRecebida.getTipo().equals("broadcast")) {
						receptarBroadcast(mensagemRecebida, index);
					} else if (mensagemRecebida.getTipo().equals("unicast")) {
						receptarUnicast(mensagemRecebida, index);
					}
				}
				mensagemAnterior = mensagemRecebida;
			}

			conexoesServidor.get(index).inputObject.close();
			conexoesServidor.get(index).outputObject.close();
			System.out
					.println("Fim do cliente " + conexoesServidor.get(index).cliente.getInetAddress().getHostAddress());
			conexoesServidor.get(index).cliente.close();

		} catch (IOException e) {
			e.getMessage();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void receptarUnicast(Mensagem mensagemRecebida, int index) {
		if (mensagemRecebida.getDestinatario() != null && mensagemRecebida.getDestinatario().equals(this.nome)) {
			if (mensagemRecebida.getConteudo().equalsIgnoreCase("fim")) {
				conexao = false;
			} else {
				if (!ultimoUnicast.getConteudo().equals(mensagemRecebida.getConteudo())) {
					System.out.println("Servidor " + this.nome + ": " + mensagemRecebida);
					encaminharMensagem(mensagemRecebida, index);
				}
				ultimoUnicast = mensagemRecebida;
			}
		} else if (!this.nome.equals(mensagemRecebida.getEmissor())) {
			if (!this.nome.equals(mensagemRecebida.getDestinatario())) {
				encaminharMensagem(mensagemRecebida, index);
			}
		} else {
			System.out.println("Servidor " + this.nome + ": destinatario não encontrado");
		}

	}

	private void receptarBroadcast(Mensagem mensagemRecebida, int index) {
		if (mensagemRecebida.getConteudo().equalsIgnoreCase("fim"))
			conexao = false;
		else if (!this.nome.equals(mensagemRecebida.getEmissor())) { // evita que o mesmo emissor repasse sua propia
																		// mensagem
			if (!ultimoBroadcast.getConteudo().equals(mensagemRecebida.getConteudo())) {
				System.out.println("Servidor " + this.nome + ": " + mensagemRecebida);
				encaminharMensagem(mensagemRecebida, index);
			}
			ultimoBroadcast = mensagemRecebida;
		}
	}

	private void encaminharMensagem(Mensagem mensagemRecebida, int index) {
		String receptor = mensagemRecebida.getUltimoReceptor();
		for (ConexaoCliente clienteInterno : clientesInternos) {
			try {
				if (!clienteInterno.getNomeServerConectado().equals(mensagemRecebida.getEmissor())) {
					if (!clienteInterno.getNomeServerConectado().equals(receptor)) {
						if (mensagemRecebida.getDestinatario() != null && !mensagemRecebida.getDestinatario().equals(this.nome)) {
							System.out.println(
									"Servidor " + this.nome + ": encaminhando mensagem de " + mensagemRecebida.getEmissor()
											+ " para " + clienteInterno.getNomeServerConectado() + " com destino em "
											+ mensagemRecebida.getDestinatario() + " e ultimo receptor " + receptor);
							mensagemRecebida.setUltimoReceptor(this.nome);
							clienteInterno.getOutputObject().writeObject(mensagemRecebida);
							
						}
	
					}
					
				}
				
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		
	}

}
