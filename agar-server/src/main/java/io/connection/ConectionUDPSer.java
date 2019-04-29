package io.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class ConectionUDPSer extends Thread {
	
	 private Server servidor; 
     private byte[] buffer;
    private  DatagramSocket socketUDP;
    private DatagramPacket peticion;
    private String  mensaje;
	
	
     public ConectionUDPSer(Server server, DatagramSocket cone , byte[] buffer) throws Exception {

 		this.servidor = server;
 		this.socketUDP = cone;
 		this.buffer=buffer;
 		mensaje="a";
 

 	}
     
     
	
     @Override
		public synchronized void run() {

    	 try {
             System.out.println("Iniciado el servidor UDP");
       
        
//
//             while (true) {
                  
             
               peticion = new DatagramPacket(buffer, buffer.length);
                  
             
                 socketUDP.receive(peticion);
                 System.out.println("Recibo la informacion del cliente");
                  
         
                String mensaje1 = new String(peticion.getData());
                System.out.println(mensaje1);
  
            
                 int puertoCliente = peticion.getPort();
                 InetAddress direccion = peticion.getAddress();
  
                String otro= servidor.processUDP(mensaje1, this);
                String elmejor="hola@esta";
          
                	 buffer = elmejor.getBytes();
                     System.out.println(otro);
                	  
               
                     DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length, direccion, puertoCliente);
      
                
        
                     socketUDP.send(respuesta);
                	
                	
				
                
               //  socketUDP.close();
                  
//             }
  
         } catch (SocketException ex) {
           ex.getLocalizedMessage();
         } catch (IOException ex) {
        	 ex.getLocalizedMessage();
         }

		}



	public String getMensaje() {
		return mensaje;
	}



	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
	

}
