/*
 * Autor: Jean Carlos Rodrigues
 */
package sistemasdistribuidos.chat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
	
	private String nomeUsuario; 
	private BufferedReader entrada;
	private boolean pronto;
	private Socket socket;
	private BufferedWriter saidaCliente;
	private ThreadEntrada entradaCliente;
    
	public Cliente(String endereco, int porta, String nomeUsuario) throws IllegalArgumentException{		
		if(nomeUsuario == null || "".equals(nomeUsuario.trim())){
			throw new IllegalArgumentException("o nome do usuario nao pode ser vazio.");
		}		
		this.nomeUsuario = nomeUsuario;
		try {
			conectar(endereco, porta);
			iniciarStream();
			pronto = true;
		} catch (Exception e) {
			pronto = false;
			System.out.println();
			System.err.println(e.getMessage());
		}
	}
	
	private void conectar(String endereco, int porta) throws Exception{
		try {
			socket = new Socket(endereco,porta);
			System.out.println("o/ Conectado em " + endereco + " \\o/");
		} catch (UnknownHostException e) {
			throw new Exception("Erro: O endereço de "+ endereco +" não pode ser determinado");
		} catch (IOException e) {
			throw new Exception("Ops, alguma coisa deu errada ao conectar.");
		}	
	}
	
	private void iniciarStream() throws Exception {
		try {
			entradaCliente = new ThreadEntrada(socket.getInputStream());
			entrada = new BufferedReader(new InputStreamReader(System.in));
			saidaCliente = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			saidaCliente.write("nome:" + nomeUsuario + "\n");
			saidaCliente.flush();			
		} catch (IOException e) {
			throw new Exception("Erro ao iniciar comunicação.");
		}
	}
	
	public void chat(){		
		if(pronto){
			entradaCliente.start();
			while (!socket.isClosed()) {            	
	            System.out.print("Digite: ");
	            String enviar;
				try {
					enviar = entrada.readLine();
					saidaCliente.write(enviar + "\n");                
		            saidaCliente.flush();                
		            if ("FIM".equals( enviar.toUpperCase())) {
		            	entradaCliente.desconectar();                	
		                socket.close();
		                System.out.println("Saindo do chat.");
		            }
				} catch (IOException e) {
					e.printStackTrace();
				}            
			}
		}else{
			System.out.println("Cliente não está pronto.");			
		}
	}
	
	public boolean isPronto() {
		return pronto;
	}
	
	public static void main(String[] args) {
		String servidor;
		int porta = 7000;
		String nome;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {					
			System.out.println("Bem vindo ao chat, digite o servidor em que deseja conectar: ");
			servidor = reader.readLine().trim();
			
			System.out.println("Digite o numero da porta para se conectar:");
			String portaDigitada = reader.readLine().trim();			
			porta = Integer.valueOf(portaDigitada);		
			
			System.out.println("Digite o seu nome de usuário: ");		
			nome = reader.readLine().trim();		
			
			Cliente cliente = new Cliente(servidor, porta, nome);			
			cliente.chat();
			
			System.out.println("Você saiu do chat.");
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e){			
			System.out.println("Numero da porta digitada incorretamente.\nSaindo.");
		}
	}
}
