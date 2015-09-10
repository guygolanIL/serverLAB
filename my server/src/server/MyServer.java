package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyServer {
	
	protected int port;
	protected ServerSocket server;
	volatile boolean stop;
	protected Thread theMainServerThread;
	protected ClientHandler ch;
	protected int maxClients;
	protected ExecutorService threadPool;
	        

	
	public MyServer(int port , ClientHandler ch , int maxClients) {
		this.port = port;
		this.stop = false;
		this.ch =ch;
		this.maxClients = maxClients;
		
		
	}
	
	public void start() throws Exception{
		server = new ServerSocket(port);
		server.setSoTimeout(10*1000);
		this.threadPool = Executors.newFixedThreadPool(this.maxClients);
		
		this.theMainServerThread = new Thread(new Runnable() {
			
			@Override
			public void run(){
				
				while(!stop)
				{
					try {
						
						Socket someClient = server.accept();
						if(someClient!=null)
						{
							threadPool.execute(new Runnable() {
								
								@Override
								public void run() {
									
									try {
										ch.handleClient(someClient.getInputStream(), someClient.getOutputStream());
										someClient.close();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
							});
							
						}
						
						
						
					} 
					catch (SocketException s) {
						// TODO: handle exception
					}
					catch (IOException e) {
						// TODO: handle exception
					}
					
				}
				
			}
		});
		
		theMainServerThread.start();
		
	}
	
	public void close()throws Exception{
		stop = true;
		threadPool.shutdown();
		
		theMainServerThread.join();
		server.close();
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("SERVER SIDE");
		System.out.println("type : 'close the server' to close");
		MyServer server = new MyServer(5400, new MyClientHandler() , 10);
		server.start();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String line; 
		
		while(!(line = in.readLine()).equals("close the server"))
		{
			
		}
		
		server.close();
		
	}
}
