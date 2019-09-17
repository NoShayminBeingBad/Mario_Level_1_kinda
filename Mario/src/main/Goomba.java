package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Goomba extends Enemy {

	private int deadFrame = 0;
	private int walkFrame = 0;
	private int walkCycle = 0;
	private BufferedImage[] frames;
	
	public Goomba(int x, int y, Map map) {
		super(map);
		this.x = x * 48;
		this.y = y * 48;
		movementX = -4;
		BufferedImage image = Game.toBufferedImage(new ImageIcon("res/Goomba.png").getImage());
		frames =  new BufferedImage[image.getWidth() / 16];
		for (int i = 0; i < image.getWidth() / 16; i++) {
			frames[i] = image.getSubimage(16 * i, 0, 16, 16);
		}
	}

	public void tick() {
		if (deadFrame == 0) {
			gravity();
			walkFrame++;
			if (walkFrame % 20 == 0) {
				walkCycle++;
			}
			x += movementX;
			collision();
			if (touch()) {
				if (game.getMario().getMovementY() > 0) {
					game.getMario().setMovmentY(-20);
					game.getMario().addScore(500);
					deadFrame++;
				}else {
					game.getMario().hit();
				}
			}
		}else {
			deadFrame ++;
			if (deadFrame == 15) {
				map.removeEntity(this);
			}
		}
	}

	public void render(Graphics g) {
		if (deadFrame > 0) {
			g.drawImage(frames[2], x - map.getOffSetX(), y, 48, 48, null);
		}else {
			g.drawImage(frames[walkCycle % 2], x - map.getOffSetX(), y, 48, 48, null);
		}
	}

}
