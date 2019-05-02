package io.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class music extends Thread{
	
	private String ruta;
	
	public music() {
		
	}
	
	public void run() {
		
		Player apl = null;
		try {
			apl = new Player(new FileInputStream("./src/barbie.mp3"));
		} catch (FileNotFoundException | JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			apl.play();
		} catch (JavaLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	



}
