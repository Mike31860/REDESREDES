package io.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class music {
	
	public static void main(String[] args) throws FileNotFoundException,
    JavaLayerException {

	Player apl = new Player(new FileInputStream("./src/barbie.mp3"));

	apl.play();

}

}
