package io.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class music extends Thread{
	
	private String nombre;
	private String ruta;
	final int PUERTO_SERVIDOR = 5098;
	private  byte[] buffer;
	private Player apl;
	private boolean continuar;
		 
	public music() {
		
		ruta = "a";
		buffer = new byte[1024];
		Player apl = null;
		continuar = true;
		
	}
	
	public void run() {		
	
		while(continuar) {
		
			try {
				
				
				System.out.println("Inició el hilo");
				
				InetAddress direccionServidor = InetAddress.getByName("localhost");

	            DatagramSocket socketUDP = new DatagramSocket();
				
	            	
	            	buffer = nombre.getBytes();
					 
		            DatagramPacket servPaquete;
		            DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, PUERTO_SERVIDOR);
		 
		          
		            //System.out.println("Envio el datagrama");
		            //System.out.println(mensaje);
		            socketUDP.send(pregunta);
		            
		            //recibir datos
		            byte[] RecogerServidor_bytes = new byte[1024];
		           
		     
		            servPaquete = new DatagramPacket(RecogerServidor_bytes,1024);
		            socketUDP.receive(servPaquete);
		          
		            //System.out.println("Recibo la peticion");
		 
		           String cadenaMensaje = new String(RecogerServidor_bytes).trim();
		           System.out.println(cadenaMensaje);
		    	
		           ruta = cadenaMensaje;
		           
		           if(apl != null) {
		        	   
		        	   apl.close();
		        	   
		           }
		           
		           apl = new Player(new FileInputStream(ruta));
		           apl.play(); 
		           
		           continuar = false;
		        	   
		           
			} catch (FileNotFoundException | JavaLayerException | UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	public void setMensaje(String ruta) {
		
		this.nombre = ruta;
		
	}

}
