package Cliente;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Mensagem.Mensagem;

public class ImplCliente implements Runnable {
	List<ConexaoCliente> clientes;
	private boolean conexao = true;
	private String nome;

	public ImplCliente(List<ConexaoCliente> clientes, String nome) {
		this.clientes = clientes;
		this.nome = nome;
	}

	public void run() {
		try {

			// Prepara para leitura do teclado
			Scanner in = new Scanner(System.in);
			Mensagem mensagem = null;
			while (conexao) {

				System.out.println(
						"Cliente " + this.nome + ": escolha uma opção de mensagem \n1 - Unicast\n2 - Broadcast");
				int opcaoInt = 0;
				do {
					
					try {
						String opcaoString = in.nextLine();
						opcaoInt = Integer.valueOf(opcaoString);
					} catch (NumberFormatException | NoSuchElementException e) {
						System.err.println("Formato ou valor invalido");
					}
				} while (opcaoInt < 1 || opcaoInt > 2);
				switch (opcaoInt) {
				case 1:
					mensagem = construirMensagem("unicast", in);
					break;
				case 2:
					mensagem = construirMensagem("broadcast", in);
					break;
				}

				for (ConexaoCliente cliente : this.clientes) {
					cliente.setMensagemAnterior(mensagem);
					if (mensagem.getConteudo().equalsIgnoreCase("fim")) {
						conexao = false;
					} else {
						System.out.println("Cliente " + this.nome + ": enviando mensagem para "
								+ cliente.getNomeServerConectado() + " com destino em " + mensagem.getDestinatario());
						cliente.getOutputObject().writeObject(mensagem);
					}
				}
			}
			for (ConexaoCliente cliente : this.clientes) {
				cliente.inputObject.close();
				cliente.getOutputObject().close();
				cliente.getSocket().close();
				System.out.println(
						"Cliente " + this.nome + ": finaliza conexão com " + cliente.getNomeServerConectado() + ".");
			}
			in.close();
			System.out.println("Cliente " + this.nome + ": finaliza conexão.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Mensagem construirMensagem(String tipo, Scanner in) {
		String emissor = this.nome;
		String destinatario;
		if (tipo.equals("unicast")) {
			System.out.println("Digite o destinatario da mensagem: ");
			destinatario = in.nextLine();
		} else {
			destinatario = "todos(Broadcast)";
		}
		System.out.println("Digite o conteudo da mensagem: ");
		String conteudo = in.nextLine();
		return new Mensagem(emissor, destinatario, conteudo, tipo);
	}
}
