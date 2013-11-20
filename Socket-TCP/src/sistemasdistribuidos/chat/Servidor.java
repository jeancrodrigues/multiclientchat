package sistemasdistribuidos.chat;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servidor {
	
	public static void main(String args[]) {
		
		ServerSocket echoServer = null;		
		Map<Integer, ServidorEcho> clientesConectados = new HashMap<>();

		try {			
			int conexoesRealizadas = 0;
			int porta = 7000;			
			echoServer = new ServerSocket(porta); // socket - bind
			System.out.println("Servidor carregado na porta " + porta);
			
			while (!echoServer.isClosed()) {
				try {
					
					System.out.println("Aguardando conexao");					
					Socket cliente = echoServer.accept(); // listen - accept					
					System.out.println("Cliente " + conexoesRealizadas + " conectou. Clientes conectados " + clientesConectados.size());					
					ServidorEcho conCliente = new ServidorEcho(cliente, clientesConectados, conexoesRealizadas);
					
					clientesConectados.put(conexoesRealizadas, conCliente);
					conCliente.start();
					
					conexoesRealizadas++;
					
				} catch (IOException e) {
					System.out.println(e);
					echoServer.close();
				}
			}			
		} catch (IOException e) {
			System.out.println(e);
		}
	}

}