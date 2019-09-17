package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Point extends Entity {
	
	//shows points added
	private int up = 10;
	private int point;
	
	public Point(int x, int y, int point, Game game) {
		super(game);
		this.point = point;
		this.x = x;
		this.y = y;
	}

	public void tick() {
		y -= up;
		up -= 1;
		if (up < 0.01) {
			map.removeEntity(this);
		}
	}

	public void render(Graphics g) {
		g.setFont(new Font("PKMN RBYGSC", Font.BOLD, 15));
		g.setColor(Color.white);
		g.drawString(String.valueOf(point), x - map.getOffSetX(), y);
	}

}
