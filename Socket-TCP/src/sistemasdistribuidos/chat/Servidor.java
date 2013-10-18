package sistemasdistribuidos.chat;
import java.io.IOException;
import java.net.ServerSocket;

public class Servidor {
	
	public static void main(String args[]) {
		
		ServerSocket server = null;
				
		try {						
			int porta = lePorta();			
		
			server = new ServerSocket(porta); // socket - bind			
			
			System.out.println("Servidor carregado na porta " + server.getLocalPort());
			
			while(true){
			
				ChatCliente cliente = new ChatCliente(server.accept());
				
			
			}
			
			
			
			cliente.envia("Servidor no endereco " + server.getLocalSocketAddress() + " porta " + server.getLocalPort() + "\n");
			cliente.iniciaThreads();		
			
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private static int lePorta() {
		return 7000;
	}
}