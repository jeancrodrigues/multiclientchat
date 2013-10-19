package sistemasdistribuidos.chat;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
	
	public static void main(String args[]) {
		
		ServerSocket echoServer = null;		
		List<ServidorEcho> clientes = new ArrayList<>();

		try {			
			int conexoesRealizadas = 0;
			int porta = 7000;			
			echoServer = new ServerSocket(porta); // socket - bind			
			
			System.out.println("Servidor carregado na porta " + porta);
			
			while (!echoServer.isClosed()) {
				try {
					
					System.out.println("Aguardando conexao");
					
					Socket cliente = echoServer.accept(); // listen - accept
					
					System.out.println("Cliente " + conexoesRealizadas + " conectou.");
					
					ServidorEcho conCliente = new ServidorEcho(cliente, conexoesRealizadas);
					conCliente.start();					
					clientes.add(conCliente);					
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