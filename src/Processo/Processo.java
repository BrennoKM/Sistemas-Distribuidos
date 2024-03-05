package Processo;

import Cliente.Cliente;
import Servidor.Servidor;

public class Processo {
	Cliente cliente;
	Servidor servidor;
	
	public Processo(String nome, int host, int[] porta) {
		this.cliente = new Cliente(nome, "localhost", porta, host);
		Thread client = new Thread(this.cliente);
		client.start();
		
		this.servidor = new Servidor(nome, host, this.cliente);
		Thread server = new Thread(this.servidor);
		server.start();
		
	}
	
	public static void main(String[]args) {
		// exemplo anel bidirecional
		//		new Processo("p1", 5001, new int[]{5002, 5004});
		//		new Processo("p2", 5002, new int[]{5003, 5001});
		//		new Processo("p3", 5003, new int[]{5004, 5002});
		//		new Processo("p4", 5004, new int[]{5001, 5003});
		
		
		// exemplo anel unidirecional
		//		new Processo("p1", 5001, new int[]{5002});
		//		new Processo("p2", 5002, new int[]{5003});
		//		new Processo("p3", 5003, new int[]{5004});
				new Processo("p4", 5004, new int[]{5001});
		
		
		
		// exemplo estrela extendido 
		//		new Processo("p1", 5001, new int[]{5002, 5003, 5004});
		//		new Processo("p2", 5002, new int[]{5001});
		//		new Processo("p3", 5003, new int[]{5001});
		//		new Processo("p4", 5004, new int[]{5001});
		
		//		new Processo("p5", 5005, new int[]{5006});
		//		new Processo("p6", 5006, new int[]{5007});
		//		new Processo("p7", 5007, new int[]{5006, 5008});
		//		new Processo("p8", 5008, new int[]{5003});
	}
}

