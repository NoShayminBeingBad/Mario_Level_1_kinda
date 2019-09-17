package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Mushroom extends Item {
	
	//gives a buff
	private BufferedImage image;
	
	public Mushroom(int x, int y, Map map) {
		super(map);
		this.x = x * 48;
		this.y = y * 48;
		movementX = 4;
		this.image = Game.toBufferedImage(new ImageIcon("res/mushroom.png").getImage());
	}

	public void tick() {
		gravity();
		
		x += movementX;
		collision();
		if (touch()) {
			effect(game.getMario());
			map.removeEntity(this);
			game.getMario().addScore(1000);
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.cyan);
		g.drawImage(image, x - map.getOffSetX(), y - map.getOffSetY(), Game.BLOCK_SIZE, Game.BLOCK_SIZE, null);
	}

	public void effect(Mario m) {
		if (game.getMario().getState() == 0) {
			game.getMario().setState(1);
		}
	}

}
