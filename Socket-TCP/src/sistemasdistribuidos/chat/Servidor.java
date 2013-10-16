package sistemasdistribuidos.chat;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	
	public static void main(String args[]) {
		
		ServerSocket echoServer = null;		
		Socket clientSocket = null; 

		try {			
			
			int porta = lePorta();			
			echoServer = new ServerSocket(porta); // socket - bind			
			System.out.println("Servidor carregado na porta 7000");
			
			while (!echoServer.isClosed()) {
				try {
					System.out.println("Aguardando conexao");
					clientSocket = echoServer.accept(); // listen - accept
					
					DataInputStream is = new DataInputStream(clientSocket.getInputStream());									
					PrintStream os = new PrintStream(clientSocket.getOutputStream());
					
					System.out.println("Cliente conectado: " + clientSocket.isConnected());
					os.println("Servidor responde: Conexao efetuada com o servidor");
					
					while(!echoServer.isClosed() && clientSocket.isBound()) {
						String line = is.readLine(); // recv
						System.out.println("Cliente enviou: " + line);
						os.println(line.toUpperCase());
						
						if("FIM".equals( line.trim().toUpperCase())){
							os.println("FIM");							
							System.out.println("Fechando conexão.");							
							echoServer.close();
						}						
					}
					
				} catch (IOException e) {
					System.out.println(e);
					echoServer.close();
				}
			}
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private static int lePorta() {
		// TODO Auto-generated method stub
		return 0;
	}
}