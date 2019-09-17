package main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Flag extends Entity {

	//Flag at the end of the level
	private BufferedImage flag;
	
	public Flag(int x, int y, Map map) {
		super(map.getGame());
		this.x = x * 48 - 24;
		this.y = y * 48;
		this.flag = Game.toBufferedImage(new ImageIcon("res/TileSet.png").getImage()).getSubimage(14 * 16, 0, 16, 16);
	}
	
	public void tick() {
		//if mario touches flag, start moving
		map = game.getMap();
		if (!grounded) {
			if (game.getMario().isFinsihed()) {
				if (y >= 430) {
					grounded = true;
				}else {
					grounded = false;
					y += 6;
				}
			}
		}
	}

	public void render(Graphics g) {
		map = game.getMap();
		g.drawImage(flag, x - map.getOffSetX(), y, 48, 48, null);
	}

}
