package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Coin extends Item {

	public BufferedImage[] frames = new BufferedImage[4];
	
	public Coin(int x, int y, Map map) {
		super(map);
		BufferedImage image = Game.toBufferedImage(new ImageIcon("res/Coin.png").getImage());
		frames =  new BufferedImage[image.getWidth() / 16];
		for (int i = 0; i < image.getWidth() / 16; i++) {
			frames[i] = image.getSubimage(16 * i, 0, 16, 16);
		}
		this.x = x * 48;
		this.y = y * 48;
	}

	public void tick() {
		if (touch()) {
			map.removeEntity(this);
			game.getMario().addScore(Game.COIN_SCORE);
			game.getMario().addCoin();
		}
	}
	
	public void render(Graphics g) {
		int shine = map.getShine();
		g.drawImage(frames[shine], x - map.getOffSetX(), y + map.getOffSetY(), Game.BLOCK_SIZE, Game.BLOCK_SIZE, null);
	}
	
	public void effect(Mario m) {
		
	}

}
