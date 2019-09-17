package main;

import java.awt.Graphics;

public abstract class Item extends Entity {
	
	public Item(Map map) {
		super(map.getGame());
		this.map = map;
	}

	public abstract void tick();
	
	public abstract void render(Graphics g);

	public abstract void effect(Mario m);
	
}
