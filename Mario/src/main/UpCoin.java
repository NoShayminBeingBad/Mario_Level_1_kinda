package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class UpCoin extends Entity {

	//this class if for when a coin block is punched
	private double up = 50;
	private BufferedImage coin;
	
	public UpCoin(int x, int y, Map map) {
		super(map.getGame());
		coin = Game.toBufferedImage(new ImageIcon("res/Coin.png").getImage()).getSubimage(0, 0, 16, 16);
		this.x = x * 48;
		this.y = y * 48;
	}
	
	public void tick() {
		y -= up;
		up /= 2;
		if (up < 0.01) {
			map.removeEntity(this);
		}
	}

	public void render(Graphics g) {
		g.drawImage(coin,x - map.getOffSetX(), (int)(y), 48, 48, null);
	}

}
