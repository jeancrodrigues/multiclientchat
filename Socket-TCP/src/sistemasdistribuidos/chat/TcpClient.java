package sistemasdistribuidos.chat;

public interface TcpClient {	
	public void envia();
	public void recebe();
	public void desconectar();	
	public void conectar(String host, int porta);
}
