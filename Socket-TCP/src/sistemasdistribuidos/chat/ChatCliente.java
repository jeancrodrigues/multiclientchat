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
		inicializaCliente(socket);
	}
	
	public void iniciaThreads(){
		ThreadEnvia envia = new ThreadEnvia(saida);
		envia.start();
		ThreadRecebe recebe = new ThreadRecebe(reader);
		recebe.start();
	}
		
	@Override
	public void envia(){
		try {
			System.out.println("Digite: ");
			String linha = reader.readLine();
			if (socket.isConnected()){
				saida.write(linha);
				saida.newLine();
				saida.flush();
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
			entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			saida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			saida.write("Cliente conectado " + socket.getLocalAddress() + ":" + socket.getLocalPort() );			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao inicializar cliente.");
			desconectar();
		}
		
	}

	public boolean isConectado() {
		return socket.isConnected();
	}

	public void envia(String msg) {
		try {
			if (socket.isConnected()){			
				saida.write(msg);
				saida.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class ThreadEnvia extends Thread{
		
		private BufferedReader entrada;
		private BufferedWriter writer;
		
		public ThreadEnvia(BufferedWriter writer) {
			this.writer = writer;
			entrada = new BufferedReader(new InputStreamReader(System.in));
		}

		@Override
		public void run() {
			while(true){
				try {
					String linha = entrada.readLine();					
					writer.write(linha);					
					writer.newLine();
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	class ThreadRecebe extends Thread{
		
		private BufferedReader reader;		
		public ThreadRecebe(BufferedReader reader) {
			this.reader = reader;
		}

		@Override
		public void run() {
			while(true){
				try {
					String linha = reader.readLine();					
					System.out.println(linha);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

}
