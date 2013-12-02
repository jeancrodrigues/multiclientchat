/*
 * Autor: Jean Carlos Rodrigues
 * 
 * Classe responsavel por gerenciar um cliente no servidor
 * Para cada cliente conectado uma instancia dessa classe será criada
 * 
 */
package sistemasdistribuidos.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

public class ServidorEcho extends Thread {

	private BufferedReader entradaCliente;
	private BufferedWriter saidaCliente;
	private Socket cliente;
	private int num;
	private String nome;

	private Map<Integer, ServidorEcho> listaclientes; 
	
	public ServidorEcho(Socket cliente, Map<Integer, ServidorEcho> listaclientes, int num) {
		try {
			this.listaclientes = listaclientes;
			this.cliente = cliente;
			this.num = num;
			
			entradaCliente = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			saidaCliente = new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream()));
			String infoMessage = entradaCliente.readLine(); 
			if(infoMessage.regionMatches(0, "nome:", 0, 5)){
				nome = infoMessage.substring(5);
			}
			saidaCliente.write("Bem vindo, voce e o cliente " + num + "\n");
			saidaCliente.flush();		
			
		} catch (IOException e) {
			System.out.println("Erro de conexao com o cliente");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while ( cliente.isConnected() && cliente.isBound()) {				
				String msg = entradaCliente.readLine();
				if(msg!=null){															
					System.out.println(nome + " disse: " + msg);					
					if(verificaFimConexao(msg)){
						msg = "Cliente " + nome + " saiu do chat\n";
						cliente.close();
					}					
					enviarMensagem(msg);					
				}
			}			
			this.join();			
		} catch (IOException | InterruptedException e) {
			System.out.println("Excecao no cliente " + num	+ " " + nome + ".");
			e.printStackTrace();
		}

	}

	private void enviarMensagem(String msg) {
		for(ServidorEcho client : listaclientes.values()){
			if (client != this) {
				client.enviarMensagemSaida(nome + " disse: " + msg);				
			}
		}
	}

	private void enviarMensagemSaida(String msg) {
		try {
			saidaCliente.write("\n" + msg + "\n");
			saidaCliente.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean verificaFimConexao(String msg) {
		if ("FIM".equals(msg.trim().toUpperCase())) {		
			try {
				System.out.println("Cliente " + nome + " saiu.");
				listaclientes.remove(num);
				cliente.shutdownInput();				
				cliente.shutdownOutput();
				cliente.close();				
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
		return false;
	}

	public String getNome() {
		return nome;
	}

}
