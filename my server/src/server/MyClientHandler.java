package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MyClientHandler implements ClientHandler {

	
	
	@Override
	public void handleClient(InputStream in, OutputStream out) {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(in));
			PrintWriter outToClient = new PrintWriter(out);
			
			String line;
			
			while(!(line =inFromClient.readLine()).equals("exit"))
			{
				StringBuffer send = new StringBuffer(line);
				outToClient.println(send.reverse().toString());
				outToClient.flush();
			}
			
			outToClient.println("bye");
			outToClient.flush();
					
			
			outToClient.close();
			inFromClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				

	}

}
