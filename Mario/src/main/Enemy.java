package main;

import java.awt.Graphics;

public abstract class Enemy extends Entity {

	//enemy to fight
	public Enemy(Map map) {
		super(map.getGame());
		this.map = map;
	}

	public boolean touch() {
		return !map.getGame().getMario().isInvinsible() && map.getGame().getMario().getRec().intersects(this.getRec());
	}
	
	public abstract void tick();

	public abstract void render(Graphics g);

}
