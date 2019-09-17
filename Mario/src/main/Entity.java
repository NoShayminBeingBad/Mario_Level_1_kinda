package main;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity extends Object{
	
	//anything on the map
	protected int x;
	protected int y;
	protected double movementX;
	protected double movementY;
	protected boolean grounded;
	protected int gravity = 1;
	
	protected Game game;
	protected Map map;
	
	public Entity(Game game) {
		this.game = game;
		this.map = game.getMap();
	}
	
	public Rectangle getRec() {
		return new Rectangle(x, y, Game.BLOCK_SIZE, Game.BLOCK_SIZE);
	}
	
	public boolean isGrounded() {
		return grounded;
	}
	
	public void gravity() {
		y += (movementY/3);
		movementY += gravity;
		if (movementY > 40) {
			movementY = 40;
		}
	}
	
	public void collision() {
		boolean tempGround = false;
		
		for (int i = -1; i < 2; i++) {
			Rectangle tempup = map.getRec(gridX() + i, gridY());
			if (tempup != null && getRec().intersects(tempup)) {
				Rectangle rect = (Rectangle) getRec().createIntersection(tempup);
				if (rect.width > 15) {
					movementY = Math.abs(movementY/3);
					y += movementY;
				}
			}
			Rectangle tempdown = map.getRec(gridX() + i, gridY() + 1);
			if (tempdown != null && getRec().intersects(tempdown)) {
				Rectangle rect = (Rectangle) getRec().createIntersection(tempdown);
				if (rect.height < 15 && rect.width > 20 && movementY >= 0) {
					y = gridY() * 48 + 1;
					movementY = 0;
					tempGround = true;
				}
			}
		}
		
		grounded = tempGround;
		
		for (int i = 0; i < 2; i++) {
			Rectangle templeft = map.getRec(gridX() - 1, gridY() + i);
			if (templeft != null && getRec().intersects(templeft)) {
				Rectangle rect = (Rectangle) getRec().createIntersection(templeft);
				if (rect.height > 3 && rect.width < 20) {
					x = (gridX()) * 48;
					movementX *= -1;
				}
			}
			Rectangle tempright = map.getRec(gridX() + 1, gridY() + i);
			if (tempright != null && getRec().intersects(tempright)) {
				Rectangle rect = (Rectangle) getRec().createIntersection(tempright);
				if (rect.height > 3 && rect.width < 20) {
					x = gridX() * 48;
					movementX *= -1;
				}
			}
		}
	}
	
	public boolean touch() {
		if (map.getGame().getMario().getRec().intersects(this.getRec())) {
			return true;
		}
		return false;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public int getX() {
		return x;
	}
	
	public int gridX() {
		return (x + 24) / 48;
	}
	
	public int gridX(int offSet) {
		return (x + 24 + offSet) / 48;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public int gridY() {
		return y / 48;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
}
