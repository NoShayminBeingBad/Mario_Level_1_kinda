package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	
	public static final int  COIN_SCORE = 200;
	
	public static final int BLOCK_SIZE = 48;
	public static int frames;
	
	public static final int WIDTH = 960;
	public static final int HEIGHT = 600;
	private static final long serialVersionUID = 1L;
	
	public boolean[] key = new boolean[256];
	public boolean a = false;
	
	public Object[] stuff = new Object[2];
	
	private Graphics g;
	private Thread thread;
	private boolean running = false;
	private Map map;
	private Mario mario;
	
	private BufferedImage background;
	private BufferedImage coin;
	
	private int gameOver = 0;
	
	private BufferedImage title;
	private BufferedImage point;
	private BufferedImage control;
	private int select = 0;
	
	public static final int TITLE = 0;
	public static final int GAME = 1;
	public static final int GAMEOVER = 2;
	public static final int TIMEUP = 3;
	public static final int WIN = 4;
	private int screen = 0;
	
	public Game() {
		
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/PKMN.ttf")));
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		this.addKeyListener(new Keyboard(this));
		this.setBackground(Color.black);
		
		JFrame frame = new JFrame("Mario");
		frame.setVisible(true);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setResizable(false);
		frame.add(this);
		
		map = new Map(this);
		mario = new Mario(this);
		
		stuff[0] = map;
		stuff[1] = mario;

		//import
		background = Game.toBufferedImage(new ImageIcon("res/background.png").getImage());
		coin = Game.toBufferedImage(new ImageIcon("res/Coin.png").getImage()).getSubimage(0, 0, 16, 16);
		title = Game.toBufferedImage(new ImageIcon("res/title.png").getImage());
		point = Game.toBufferedImage(new ImageIcon("res/select.png").getImage());
		control = Game.toBufferedImage(new ImageIcon("res/Key.png").getImage());
		
		
		start();
	}
	
	public Map getMap() {
		return map;
	}
	
	public Mario getMario() {
		return mario;
	}

	public void setScreen(int x) {
		this.screen = x;
	}
	
	public boolean inGame() {
		return screen == GAME;
	}
	
	public boolean freshA() {
		if (key['J'] && !a) {
			a = true;
			return true;
		}
		return false;
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() {
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		int maxAmountOfFrames = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int framesPerTick = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
				framesPerTick = 0;
			}
			if (running && framesPerTick < maxAmountOfFrames/amountOfTicks) {
				render();
				framesPerTick++;
				frames++;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	public void tick() {
		if (screen == TITLE) {
			if (freshA()) {
				screen = GAME;
			}
		}else if (screen == GAME) {
			for (Object entity : stuff) {
				entity.tick();
			}
		}else if (screen == GAMEOVER || screen == TIMEUP) {
			gameOver++;
			if (key['J'] && gameOver > 180) {
				mario.reset();
				map = new Map(this);
				map.reset();
				stuff[0] = map;
				screen = TITLE;
				gameOver = 0;
			}
		}else if (screen == WIN) {
			gameOver++;
			if (key['J'] && gameOver > 180) {
				mario.reset();
				map = new Map(this);
				map.reset();
				stuff[0] = map;
				screen = TITLE;
				gameOver = 0;
			}
		}
		if (key['J']) {
			a = true;
		}else {
			a = false;
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		g = bs.getDrawGraphics();
		if (screen == Game.GAME || screen == Game.TITLE || screen == Game.WIN) {
			g.drawImage(background, 0 - map.getOffSetX(), 0, 10176, 624, null);
			for (Object entity : stuff) {
				entity.render(g);
			} 
		}else if (screen == Game.GAMEOVER) {
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			g.setFont(new Font("PKMN RBYGSC", Font.BOLD, 30));
			g.setColor(Color.white);
			g.drawString("GAME OVER", 350, 300);
		}else if (screen == Game.TIMEUP) {
			g.setColor(Color.black);
			g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			g.setFont(new Font("PKMN RBYGSC", Font.BOLD, 30));
			g.setColor(Color.white);
			g.drawString("TIMES UP", 350, 300);
		}
		if (screen == Game.TITLE) {
			g.drawImage(title, 200, 100, 528, 264, null);
			g.setFont(new Font("PKMN RBYGSC", Font.BOLD, 40));
			g.setColor(Color.white);
			g.drawString("PRESS J TO PLAY", 200, 420);
			g.drawImage(control, 700, 450, 192, 48, null);
		}else if (screen == Game.WIN) {
			g.setFont(new Font("PKMN RBYGSC", Font.BOLD, 30));
			g.setColor(Color.white);
			g.drawString("CONGRATULATIONS!", 200, 200);
			g.drawString(String.valueOf("YOUR SCORE: " + mario.getScore()), 200, 300);
		}
		
		g.setFont(new Font("PKMN RBYGSC", Font.BOLD, 30));
		g.setColor(Color.white);
		g.drawString("MARIO", 20, 35);
		g.drawString(String.valueOf(mario.getScore()), 20, 60);
		g.drawString("TIME", 800, 35);
		g.drawString(String.valueOf(mario.getTimer()), 805, 60);
		g.drawImage(coin, 200, 15, 35, 35, null);
		g.drawString(String.valueOf("x " + mario.getCoin()), 240, 45);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		new Game();
	}
	
	public static int sign(int x) {
		if (x > 0) {
			return 1;
		}else if (x < 0) {
			return -1;
		}else {
			return 0;
		}
	}
	
	public static int sign(double x) {
		if (x > 0) {
			return 1;
		}else if (x < 0) {
			return -1;
		}else {
			return 0;
		}
	}
	
	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		
		return bimage;
	}
	
	public static BufferedImage mirror(BufferedImage stand) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-stand.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage result = op.filter(stand, null);
		return result;
	}
}
