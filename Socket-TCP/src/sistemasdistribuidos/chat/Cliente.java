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
		
		Socket cliente = null;
        BufferedReader entradaCliente = null;
    	BufferedWriter saidaCliente = null;

        try {
        	
            cliente = new Socket("127.0.0.1",7000);  // socket - bind
            
            entradaCliente = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        	saidaCliente = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));               	
        	System.out.println("Conectado na porta " + cliente.getLocalPort());        	
        	System.out.println(entradaCliente.readLine());
        	
            while (!cliente.isClosed()) {
            	
                System.out.print("Digite: ");
                String enviar = entrada.readLine(); 
                saidaCliente.write(enviar + "\n");                
                saidaCliente.flush();
                
                String resposta = entradaCliente.readLine();
                
                if (resposta.equals("fim")) {
                    if("FIM".equals(entradaCliente.readLine())){
                    	System.out.println("Servidor desconectou.");
                    	cliente.close();
                    }
                }else{
                	System.out.println("Servidor retornou: " + resposta); // listen
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
