package sistemasdistribuidos.chat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    
	public static void main(String[] args) throws IOException {
   	
		BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));		
		Socket socket = null;
    	BufferedWriter saidaCliente = null;
    	ThreadEntrada entradaCliente;
    	
        try {
        	
            socket = new Socket("127.0.0.1",7000);  // socket - bind            
            entradaCliente = new ThreadEntrada(socket.getInputStream());
            entradaCliente.start();
            
        	saidaCliente = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));               	
        	System.out.println("Conectado na porta " + socket.getLocalPort());        	
        	
            while (!socket.isClosed()) {            	
                System.out.print("Digite: ");
                String enviar = entrada.readLine(); 
                saidaCliente.write(enviar + "\n");                
                saidaCliente.flush();                
                if ("FIM".equals( enviar.toUpperCase())) {
                	entradaCliente.desconectar();                	
                    socket.close();
                    System.out.println("Saindo do chat.");
                }               
            }
            
        } catch (ConnectException ex){    
        	ex.printStackTrace();
        } catch (UnknownHostException ex) {
        	ex.printStackTrace();
        } catch (IOException ex) {
        	ex.printStackTrace();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
    }
}
