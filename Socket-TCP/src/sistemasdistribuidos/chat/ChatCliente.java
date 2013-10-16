package sistemasdistribuidos.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ChatCliente implements TcpClient{

	private BufferedReader reader;
	private BufferedReader entrada;
	private BufferedWriter saida;
	private Socket socket;
	
	public ChatCliente() {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public ChatCliente(Socket socket){
		reader = new BufferedReader(new InputStreamReader(System.in));
		this.socket = socket;		
	}
		
	@Override
	public void envia(){
		try {
			String linha = reader.readLine();
			if (socket.isConnected()){
				saida.write(linha);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void recebe(){
		String linha;
		try {
			linha = entrada.readLine();
			System.out.println(linha);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void desconectar() {		
		try {		
			saida.write("fim_conexao\n");
			if("fim_conexao".equals(entrada.readLine())){
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao desconectar");
		}			
	}

	@Override
	public void conectar(String host, int porta) {
		try {
			socket = new Socket(host, porta);
			inicializaCliente(socket);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao conectar.");
		}		
	}
	
	private void inicializaCliente(Socket socket){
		this.socket = socket;
		try {			
			this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.saida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao inicializar cliente.");
			desconectar();
		}
		
	}

}
