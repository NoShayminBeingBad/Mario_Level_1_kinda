package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard extends KeyAdapter {
	
	private Game game;
	
	public Keyboard (Game game) {
		this.game = game;
	}
	
	public void keyPressed (KeyEvent e) {
		game.key[e.getKeyCode()] = true;
	}
	
	public void keyReleased (KeyEvent e) {
		game.key[e.getKeyCode()] = false;
		if (e.getKeyCode() == 'J') {
			game.a = false;
		}
	}

}
