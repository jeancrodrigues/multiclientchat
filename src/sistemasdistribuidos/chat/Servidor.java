/*
 * Autor: Jean Carlos Rodrigues
 */
package sistemasdistribuidos.chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
	
	public static void main(String args[]) {		
		ServerSocket echoServer = null;		
		Map<Integer, ServidorEcho> clientesConectados = new HashMap<>();
		
		try {			
			int conexoesRealizadas = 0;
			int porta = 7000;			
			
			System.out.println("Digite o numero da porta para se conectar:");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
						
			String portaDigitada = reader.readLine().trim();			
			porta = Integer.valueOf(portaDigitada);
			
			echoServer = new ServerSocket(porta); // socket - bind
			
			System.out.println("Servidor carregado, ip " + 
						InetAddress.getLocalHost().getHostAddress() +  " porta " + porta);
			
			while (!echoServer.isClosed()) {
				try {					
					System.out.println("Aguardando conexao");					
					Socket cliente = echoServer.accept(); // listen - accept
					conexoesRealizadas++;
					ServidorEcho conCliente = new ServidorEcho(cliente, clientesConectados, conexoesRealizadas);
					clientesConectados.put(conexoesRealizadas, conCliente);
					System.out.println("Cliente " + conCliente.getNome() + 
							" conectou. Clientes conectados " + clientesConectados.size());
					conCliente.start();	
				} catch (IOException e) {
					System.out.println(e);
					echoServer.close();
				}
			}			
		} catch (NumberFormatException e){
			System.err.println("Ops, parece que voce digitou um numero invalido para a porta.");
		} catch (BindException e){
			System.err.println("Ops, erro na porta, parece que voce nao tem permissao :(.");
		} catch (IOException e) {
			System.err.println("Ops, ocorreu um erro: " + e);
		}
	}
}