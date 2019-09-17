package main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Mario extends Entity {

	private boolean[] key;
	private int screenX;
	private int screenY;
	private BufferedImage[][] frames;
	
	private final int JUMPCOUNT = 20;
	private final int WALKSPEED = 4;
	private final int RUNSPEED = 6;
	
	private int secondTimer = 0;
	private int timer = 300;
	
	private int lives = 0;
	private int score = 0;
	private int coin = 0;
	
	private boolean dead = false;
	private int jumpCount = JUMPCOUNT;
	private boolean jumping = false;
	private int direction = 0;
	private int walkCycle = 0;
	private int walkCount = 0;
	private int state = 0;
	private int height = 1;
	private int invinsibleFrames = 0;
	private boolean finished = false;
	
	public Mario(Game game) {
		super(game);
		this.key = game.key;
		
		BufferedImage image = Game.toBufferedImage(new ImageIcon("res/small_mario.png").getImage());
		
		frames = new BufferedImage[2][(image.getWidth() / 16) * 2];
		
		//(image.getWidth() / 16);
		
		for (int i = 0; i < image.getWidth() / 16; i++) {
			frames[0][i] = image.getSubimage(16 * i, 0, 16, 16);
			frames[0][i + image.getWidth() / 16] = Game.mirror(frames[0][i]);
			frames[1][i] = image.getSubimage(16 * i, 16, 16, 32);
			frames[1][i + image.getWidth() / 16] = Game.mirror(frames[1][i]);
		}
		
		frames[1][6] = image.getSubimage(16 * 6, 16, 16, 32);
		frames[1][6 + image.getWidth() / 16] = Game.mirror(frames[1][6]);
		
		x = 5 * 48;
		y = 10 * 48;
		screenX = x - game.getMap().getOffSetX();
		screenY = y - game.getMap().getOffSetY();
		grounded = true;
	}
	
	public void load() {
		this.map = game.getMap();
	}
	
	public Rectangle getRec() {
		return new Rectangle(x, y, 48, 48 * height);
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	public int getHeight() {
		return height;
	}
	
	public double getMovementY() {
		return movementY;
	}
	
	public void setMovmentY(int i) {
		this.movementY = i;
	}
	
	public void hit() {
		if (state == 1) {
			height = 1;
			y += 48;
		}
		state--;
		if (state < 0) {
			dead = true;
			movementY = -30;
		}else {
			invinsibleFrames++;
		}
	}
	
	public void timeUp() {
		state = 0;
		height = 1;
		dead = true;
		movementY = -30;
	}
	
	public boolean isInvinsible() {
		return invinsibleFrames > 0;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public void crouch() {
		if (state > 0) {
			if (height == 2) {
				y += Game.BLOCK_SIZE;
			}
			height = 1;
		}
	}
	
	public void stand() {
		if (state > 0) {
			Rectangle tempup = map.getRec(gridX(), gridY() - 1);
			Rectangle tempup2 = map.getRec(gridX(), gridY());
			if (tempup == null && tempup2 == null) {
				if (height == 1) {
					y -= Game.BLOCK_SIZE;
				}
				height = 2;
			}
		}
	}
	
	public int getDirec() {
		if (direction == 0) {
			return 1;
		}else {
			return -1;
		}
	}
	
	public int getTimer() {
		return timer;
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore(int x) {
		this.score += x;
		map.addEntity(new Point(this.x ,this.y, x, game));
	}
	
	public int getCoin() {
		return coin;
	}
	
	public void addCoin() {
		coin++;
	}
	
	public boolean isFinsihed() {
		return finished;
	}
	
	public void setFinished(boolean x) {
		this.finished = x;
	}
	
	public void reset() {
		x = 5 * 48;
		y = 10 * 48;
		screenX = x;
		screenY = y;
		grounded = true;
		dead = false;
		score = 0;
		timer = 300;
		coin = 0;
		state = 0;
		height = 1;
		finished = false;
	}

	public void tick() {
		map = game.getMap();
		if (!finished) {
			this.secondTimer++;
			if (secondTimer % 60 == 0 && timer > 0) {
				timer--;
			}
			gravity();
			if (key['V']) {
				hit();
			}
			if (!dead) {
				if (isInvinsible()) {
					invinsibleFrames++;
				}
				int speed = WALKSPEED;
				int cycle = 4;
				if (key['J']) {
					speed = RUNSPEED;
					cycle = 2;
				}
				
				if ((key['W'] || key['K']) && grounded) {
					jumping = true;
				}
				
				if (jumping) {
					if ((key['W'] || key['K']) ) {
						if (jumpCount > 0) {
							movementY = -20;
							jumpCount --;
						}
					}else {
						jumping = false;
					}
				}
				
				if (key['S'] && movementY > 0) {
					jumpCount = 0;
					movementY = 30;
				}
				
				if (movementY > 25) {
					movementY = 25;
				}
				if (key['S']) {
					crouch();
				}else {
					stand();
				}
				
				if (key['D']) {
					if (grounded) {
						direction = 0;
					}
					if ((state > 0 && !key['S']) || state == 0) {
						if (movementX < speed) {
							movementX += speed/12.0;
						}
						if (walkCount > cycle) {
							walkCycle++;
							walkCount = 0;
						}
						walkCount++;
					}
				}else if (key['A']) {
					if (grounded) {
						direction = 1;
					}
					if ((state > 0 && !key['S']) || state == 0) {
						if (movementX > -speed) {
							movementX -= speed/12.0;
						}
						if (walkCount > cycle) {
							walkCycle++;
							walkCount = 0;
						}
						walkCount++;
					}
				}
				
				if (!key['A'] && !key['D'] || key['S']){
					movementX -= Game.sign(movementX) * (WALKSPEED / 10.0);
				}
				if(Math.abs(movementX) < 0.1) {
					movementX = 0;
				}
				
				if (walkCycle == 3) {
					walkCycle = 0;
				}
				x += (int)movementX;
				
				boolean tempGround = false;
				
				for (int i = -1; i < 2; i++) {
					Rectangle tempup = map.getRec(gridX() + i, gridY());
					if (tempup != null && getRec().intersects(tempup)) {
						if (movementY < 0) {
							map.setPunchedBlock(gridX(getDirec() * Game.BLOCK_SIZE/ 4), gridY());
						}
						Rectangle rect = (Rectangle) getRec().createIntersection(tempup);
						if (rect.width > 15) {
							//System.out.println("UP");
							jumpCount = 0;
							movementY = Math.abs(movementY/3);
							y += movementY + 1;
						}
					}
					Rectangle tempdown = map.getRec(gridX() + i, gridY() + height);
					if (tempdown != null && getRec().intersects(tempdown)) {
						Rectangle rect = (Rectangle) getRec().createIntersection(tempdown);
						if (rect.height < 25 && rect.width > 15 && movementY >= 0) {
							//System.out.println("DOWN");
							y = gridY() * 48 + 1;
							jumpCount = JUMPCOUNT;
							movementY = 0;
							tempGround = true;
						}
					}
				}
				
				grounded = tempGround;
				
				for (int i = 0; i < 3; i++) {
					Rectangle templeft = map.getRec(gridX() - 1, gridY() + i);
					if (templeft != null && getRec().intersects(templeft)) {
						Rectangle rect = (Rectangle) getRec().createIntersection(templeft);
						if (rect.height > 3 && rect.width < 25) {
							//System.out.println("LEFT");
							x = (gridX()) * 48 - 1;
							if (!key['D'])
								movementX = 0;
						}
					}
					Rectangle tempright = map.getRec(gridX() + 1, gridY() + i);
					if (tempright != null && getRec().intersects(tempright)) {
						Rectangle rect = (Rectangle) getRec().createIntersection(tempright);
						if (rect.height > 3 && rect.width < 25) {
							//System.out.println("RIGHT");
							x = gridX() * 48 + 1;
							if (!key['A'])
								movementX = 0;
						}
					}
				}
				
				if (screenX < 0 && movementX < 0) {
					x -= (int)movementX;
					screenX = x - game.getMap().getOffSetX();
				}
				if (movementX > 0 && screenX >= 500) {
					x -= screenX - 500;
					game.getMap().scrollScreen((int)movementX);
				}
				if (invinsibleFrames > 180) {
					invinsibleFrames = 0;
				}
				if (timer == 0) {
					timeUp();
				}
			}
			
			if (y > 700) {
				if (timer == 0) {
					game.setScreen(Game.TIMEUP);
				}else if (lives < 0) {
					game.setScreen(Game.GAMEOVER);
					dead = false;
				}else {
					lives--;
				}
			}
			if ((map.getCollision(gridX(), gridY()) == 15|| map.getCollision(gridX(), gridY()) == 16) && x > 9450) {
				finished = true;
				grounded = false;
				x = 197 * 48;
				this.addScore(2000 - gridY() * 200);
			}
		}else if (x < 9800){
			Rectangle tempdown = map.getRec(gridX(), gridY() + height);
			if (tempdown != null && getRec().intersects(tempdown)) {
				Rectangle rect = (Rectangle) getRec().createIntersection(tempdown);
				if (rect.height < 25 && rect.width > 15) {
					y = gridY() * 48 + 1;
					jumpCount = JUMPCOUNT;
					movementY = 0;
					grounded = true;
				}
			}else {
				grounded = false;
			}
			if (grounded) {
				if (game.getMap().flagIsDown()) {
					movementY = 0;
					x += 4;
					if (walkCount > 4) {
						walkCycle++;
						walkCount = 0;
					}
					walkCount++;
					if (walkCycle == 3) {
						walkCycle = 0;
					}
				}
			}else {
				y += 6;
			}
		}else {
			if (timer > 0) {
				timer--;
				score++;
			}else {
				game.setScreen(Game.WIN);
			}
		}
		if (state < 1) {
			state = 0;
		}
		screenX = x - game.getMap().getOffSetX();
		screenY = y - game.getMap().getOffSetY();
	}
	
	public void render(Graphics g) {
		if (x < 9800) {
			if (game.inGame()) {
				if (!finished) {
					if (dead) {
						g.drawImage(frames[state][14], screenX, screenY, 48, 48, null);
					}else if (invinsibleFrames == 0 || invinsibleFrames % 10.0 < 3){
						int h = Game.BLOCK_SIZE;
						if (state > 0) {
							h = Game.BLOCK_SIZE * 2;
						}
						if (state > 0 && key['S']) {
							g.drawImage(frames[1][6 + direction * 8], screenX, screenY - Game.BLOCK_SIZE, Game.BLOCK_SIZE, h, null);
						}else if (grounded) {
							if (!key['D'] && !key['A']) {
								g.drawImage(frames[state][0 + (direction * 8)], screenX, screenY, Game.BLOCK_SIZE, h, null);
							}else {
								if (movementX < 0 && key['D'] && !key['A']) {
									g.drawImage(frames[state][4], screenX, screenY, Game.BLOCK_SIZE, h, null);
								}else if (movementX > 0 && key['A'] && !key['D']) {
									g.drawImage(frames[state][12], screenX, screenY, Game.BLOCK_SIZE, h, null);
								}else {
									g.drawImage(frames[state][1 + walkCycle + (direction * 8)], screenX, screenY, Game.BLOCK_SIZE, h, null);
								}
							}
						}else {
							g.drawImage(frames[state][5 + (direction * 8)], screenX, screenY, Game.BLOCK_SIZE, h, null);
						}
					}
				}else{
					int h = Game.BLOCK_SIZE;
					if (state > 0) {
						h = Game.BLOCK_SIZE * 2;
					}
					if ((map.getCollision(gridX(), gridY()) == 15|| map.getCollision(gridX(), gridY()) == 16)) {
						if (grounded) {
							g.drawImage(frames[state][7 + 8], screenX, screenY, 48, h, null);
						}else {
							g.drawImage(frames[state][7], screenX, screenY, 48, h, null);
						}
					}else {
						if (grounded) {
							if (movementX > 0) {
								g.drawImage(frames[state][1 + walkCycle + (direction * 8)], screenX, screenY, Game.BLOCK_SIZE, h, null);
							}else {
								g.drawImage(frames[state][0 + (direction * 8)], screenX, screenY, Game.BLOCK_SIZE, h, null);
							}
						}else {
							g.drawImage(frames[state][0 + (direction * 8)], screenX, screenY, Game.BLOCK_SIZE, h, null);
						}
					}
				}
			}else {
				g.drawImage(frames[state][0], screenX, screenY, Game.BLOCK_SIZE, 48, null);
			}
		}
	}

}
