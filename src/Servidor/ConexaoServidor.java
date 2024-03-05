package Servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConexaoServidor {
	Socket cliente;
	ObjectInputStream inputObject;
	ObjectOutputStream outputObject;
	
	public ConexaoServidor(Socket cliente, ObjectInputStream inputObject, ObjectOutputStream outputObject) {
		this.cliente = cliente;
		this.inputObject = inputObject;
		this.outputObject = outputObject;
	}


}
