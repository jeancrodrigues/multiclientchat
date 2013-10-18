package sistemasdistribuidos.chat;
import java.io.IOException;
import java.net.Socket;

public class Cliente {
    
	public static void main(String[] args) throws IOException {   	
		Socket socket = new Socket("localhost", 7000);
		System.out.println("Cliente conectado na porta " + socket.getLocalPort());
		
		ChatCliente chat = new ChatCliente(socket);
		chat.recebe();		
		chat.iniciaThreads();

    }
}
