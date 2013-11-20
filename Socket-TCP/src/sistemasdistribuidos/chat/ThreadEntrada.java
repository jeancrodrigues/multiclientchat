package sistemasdistribuidos.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ThreadEntrada extends Thread{		
	private BufferedReader entrada;
	private boolean conectado = true;
		
	public ThreadEntrada(InputStream inputStream) {
		entrada = new BufferedReader(new InputStreamReader(inputStream));
	}

	@Override
	public void run() {
		while(conectado){				
			try {				
				String msg = entrada.readLine();
				if(msg != null)
					System.out.println(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}				
		}
		try {
			join(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void desconectar() throws IOException {
		synchronized (this) {
			conectado = false;
			entrada.close();			
		}			
	}
	
}
