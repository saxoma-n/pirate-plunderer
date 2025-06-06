import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GameSpace extends JComponent implements Runnable, KeyListener
{
private int[][] terrain;
private int[][] characters;
private ArrayList<Enemy> enemies = new ArrayList<>();
private ArrayList<Bullet> bullets = new ArrayList<>();
private Player player;

private Sound bang, move, oof, oofSlow, pirateOof, dig, banger;
private BufferedImage pirate, cowboy, treasure, hp, sand1, sand2, sand3;

private boolean gameOver = false, started = true;

private boolean[] keys = new boolean[256];

private int moveCD = 0;
private int shootCD = 0;
private int digCD = 0;

private int enemyMoveCD = 0;

private int enemyDamageCD = 0; // Added cooldown for enemy damage to player

private int score = 0;

public GameSpace()
{
	addKeyListener(this);
	setFocusable(true);
	setFocusTraversalKeysEnabled(false);

	terrain = new int[][] { // make the environment
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};

	try
	{ // Load images + sounds
		pirate = ImageIO.read(GameSpace.class.getResourceAsStream("pirate.jpg"));
		cowboy = ImageIO.read(GameSpace.class.getResourceAsStream("cowboy.jpg"));
		treasure = ImageIO.read(GameSpace.class.getResourceAsStream("treasure.jpg"));
		hp = ImageIO.read(GameSpace.class.getResourceAsStream("hp.png"));
		sand1 = ImageIO.read(GameSpace.class.getResourceAsStream("sand1.png"));
		sand2 = ImageIO.read(GameSpace.class.getResourceAsStream("sand2.png"));
		sand3 = ImageIO.read(GameSpace.class.getResourceAsStream("sand3.png"));

		bang = new Sound("C:\\Users\\27name01\\Downloads\\bang.wav");
		move = new Sound("C:\\Users\\27name01\\Downloads\\move.wav");
		oof = new Sound("C:\\Users\\27name01\\Downloads\\oof.wav");
		oofSlow = new Sound("C:\\Users\\27name01\\Downloads\\oofslow.wav");
		pirateOof = new Sound("C:\\Users\\27name01\\Downloads\\pirateoof.wav");
		dig = new Sound("C:\\Users\\27name01\\Downloads\\dig.wav");
		banger = new Sound("C:\\Users\\27name01\\Downloads\\song.wav");
	}
	catch(IOException | IllegalArgumentException e)
	{
		e.printStackTrace();
	}

	characters = new int[terrain.length][terrain[0].length];
	for (int i = 0; i < terrain.length; i++)
	{
		for (int j = 0; j < terrain[i].length; j++)
		{
			characters[i][j] = 0;
		}
	}

	player = new Player(11, 11, 3, 1, 1);

	for (Entity entity : enemies)
	{
		int x = entity.x_pos;
		int y = entity.y_pos;
		if (x >= 0 && x < terrain.length && y >= 0 && y < terrain[0].length)
		{
			characters[x][y] = entity.keyValue;
		}
	}
}

public void startAnimation()
{
	Thread t = new Thread(this);
	t.start();
}

public void paintComponent(Graphics g)
{
	for (int i = 0; i < terrain.length; i++)
	{
		for (int j = 0; j < terrain[i].length; j++)
		{
			BufferedImage draw = null;
			double x = Math.random();
			switch (terrain[i][j])
			{
			case 0:
				g.setColor(Color.cyan);
				break;
			case 1:
				if (x > 0.66) draw = sand1;
				else if (x > 0.33) draw = sand2;
				else draw = sand3;
				break;
			case 2:
				g.setColor(Color.green);
				if (x > 0.66) draw = null;
				else if (x > 0.33) draw = null;
				else draw = null;
				break;
			case 3:
				g.setColor(Color.black);
				break;
			case 4:
				g.setColor(Color.pink);
			}
			g.fillRect(j * 32, i * 32, 32, 32);
			g.drawImage(draw, j * 32, i * 32, 32, 32, null);
		}
	}

	for (int i = 0; i < characters.length; i++) // print characters
	{
		for (int j = 0; j < characters[i].length; j++)
		{
			if (characters[i][j] == 0) continue; // skip trying to print if the value is 0
			boolean small = false;
			switch (characters[i][j]) // 
			{
			case 1:
				g.setColor(Color.blue);
				g.drawImage(cowboy, player.y_pos*32, player.x_pos*32, 32, 32, null);
				break;
			case 2:
				g.setColor(Color.red);
				g.drawImage(pirate, j*32, i*32, 32, 32, null);
				break;
			case 3:
				g.setColor(Color.green);
				break;
			case 4:
				g.setColor(Color.black);
				break;
			case -1:
				g.setColor(Color.darkGray);
				small = true;
				break;
			}
			if (characters[i][j] != 1 && characters[i][j] != 2) g.fillOval(j * 32 + (small ? 8 : 0), i * 32 + (small ? 8 : 0), (small ? 16 : 32), (small ? 16 : 32));
		}
	}
	if (player.hp > 0)
	{
		for (int i = 0; i < player.hp; i++)
		{
			g.drawImage(hp, i*60 + 10, 10, 50, 50, null);
		}
	}

	if (!gameOver)
	{
		g.setColor(Color.blue);
		g.setFont(g.getFont().deriveFont(Font.BOLD, 32f));
		String scoreText = "Score: " + score;
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int xScore = (getWidth() - metrics.stringWidth(scoreText)) / 2;
		int yScore = metrics.getHeight() + 15;
		g.drawString(scoreText, xScore, yScore);
	}

	if (gameOver)
	{
		g.setColor(Color.red);
		g.setFont(g.getFont().deriveFont(64f)); // set the font size
		String gameOverText = "GAME OVER";
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int x = (getWidth() - metrics.stringWidth(gameOverText)) / 2; // center the text
		int y = (getHeight() - metrics.getHeight()) / 2;
		g.drawString(gameOverText, x, y); // draw the text
	}
}

@Override
public void run()
{
	while (true)
	{
		try
		{
			if (started || banger.isEnded()) banger.makeSound();
			started = false;
			if (gameOver) banger.stop();
			
			int xAug = 0, yAug = 0;
			switch (player.direction)
			{
			case "left":
				xAug = -1;
				break;
			case "right":
				xAug = 1;
				break;
			case "up":
				yAug = -1;
				break;
			case "down":
				yAug = 1;
				break;
			}

			if (keys[KeyEvent.VK_SPACE])
			{
				if (shootCD == 0)
				{
					bullets.add(new Bullet(player.x_pos, player.y_pos, -1, (xAug == 0 ? yAug : xAug), xAug == 0));
					bang.makeSound();
				}
				shootCD++;
			}
			if (shootCD == 8) shootCD = 0;

			if (moveCD == 0)
			{
				if(keys[KeyEvent.VK_W] && player.x_pos - 1 >= 0 && terrain[player.x_pos - 1][player.y_pos] != 0)
				{
					int[] a = player.move(-1, 0);
					characters[a[0]][a[1]] = 0;
					move.makeSound();
				}
				if(keys[KeyEvent.VK_S] && terrain[player.x_pos + 1][player.y_pos] != 0)
				{
					int[] a = player.move(1, 0);
					characters[a[0]][a[1]] = 0;
					move.makeSound();
				}
				if(keys[KeyEvent.VK_A] && terrain[player.x_pos][player.y_pos - 1] != 0)
				{
					int[] a = player.move(0, -1);
					characters[a[0]][a[1]] = 0;
					move.makeSound();
				}
				if(keys[KeyEvent.VK_D] && terrain[player.x_pos][player.y_pos + 1] != 0)
				{
					int[] a = player.move(0, 1);
					characters[a[0]][a[1]] = 0;
					move.makeSound();
				}
			}
			if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_S] || keys[KeyEvent.VK_A] || keys[KeyEvent.VK_D]) moveCD++;
			if (moveCD == 8) moveCD = 0;

			if (keys[KeyEvent.VK_UP]) player.direction = "left";
			if (keys[KeyEvent.VK_DOWN]) player.direction = "right";
			if (keys[KeyEvent.VK_LEFT]) player.direction = "up";
			if (keys[KeyEvent.VK_RIGHT]) player.direction = "down";

			if (keys[KeyEvent.VK_Z]) // dig for treasure
			{
				dig.makeSound();
				if (digCD == 0)
				{
					for (int i = 0; i < terrain.length; i++) // clear previously dug up treasures
					{
						for (int j = 0; j < terrain[i].length; j++)
						{
							if (terrain[i][j] == 4) terrain[i][j] = 1;
						}
					}
					if (terrain[player.x_pos][player.y_pos] == 3) // if the spot has an x, then reveal a treasure
					{
						score += 50;
						for (int i = 0; i < (score/100); i++)
						{
							enemies.add(new Enemy((int)(Math.random() * 14) + 3, (int)(Math.random() * 27) + 6, score/100, 1, 2));
						}
						terrain[player.x_pos][player.y_pos] = 4;

						int x = (int)(Math.random() * 14) + 3;
						int y = (int)(Math.random() * 27) + 6;

						terrain[x][y] = 3;

					}
				}
				digCD++;
			}
			if (digCD == 100) digCD = 0;

			// Move bullets and clear previous positions
			for (Bullet e: bullets)
			{
				int[] a = e.move();
				characters[a[0]][a[1]] = 0;
			}

			// Check bullet collisions on enemies and player
			Iterator<Enemy> enemyIterator = enemies.iterator();
			while (enemyIterator.hasNext())
			{
				Enemy enemy = enemyIterator.next();
				int x = enemy.x_pos;
				int y = enemy.y_pos;

				for (Bullet b : bullets)
				{
					if (b.x_pos == x && b.y_pos == y)
					{
						enemy.takeDamage(b.dmg);
						b.takeDamage(1);
					}
				}
				if (enemy.die())
				{
					pirateOof.makeSound();
					enemyIterator.remove();
				}
			}

			// move enemies
			if (enemyMoveCD == 0)
			{
				for (Enemy e: enemies)
				{
					int[] a = e.move(player.x_pos, player.y_pos);
					characters[a[0]][a[1]] = 0;
				}
			}

			// enemy damages player on bump
			if (enemyDamageCD == 0)
			{
				for (Enemy e : enemies)
				{
					if (e.x_pos == player.x_pos && e.y_pos == player.y_pos)
					{
						player.takeDamage(1);
						if (player.hp > 0) oof.makeSound();
						enemyDamageCD = 100;
						break;
					}
				}
			}

			if (enemyDamageCD > 0) enemyDamageCD--;


			enemyMoveCD++;
			if (enemyMoveCD == 10) enemyMoveCD = 0;

			characters[player.x_pos][player.y_pos] = 1;

			// update enemy positions
			for (Enemy e : enemies)
			{
				int x = e.x_pos;
				int y = e.y_pos;
				if (x > 0 && x < terrain.length - 1 && y > 0 && y < terrain[0].length - 1) characters[x][y] = e.keyValue;
			}

			// remove bullets
			Iterator<Bullet> bulletIterator = bullets.iterator();
			while (bulletIterator.hasNext())
			{
				Bullet b = bulletIterator.next();
				int x = b.x_pos;
				int y = b.y_pos;
				if (x > 0 && x < terrain.length - 1 && y > 0 && y < terrain[0].length - 1)
				{
					characters[x][y] = b.keyValue;
				}
				if (x <= 0 || x >= terrain.length - 1 || y <= 0 || y >= terrain[0].length - 1)
				{
					bulletIterator.remove();
					characters[x][y] = 0;
				}
				if (b.die()) // stops the bullets from infinitely piercing
				{
					characters[b.x_pos][b.y_pos] = 0;
					bulletIterator.remove();
				}
			}

			if (player.die())
			{
				oofSlow.makeSound();
				gameOver = true;
			}

			repaint();
			Thread.sleep(20 - (score / 400));
			if (gameOver) break;

		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}

@Override
public void keyTyped(KeyEvent e) {}

@Override
public void keyPressed(KeyEvent e)
{
	keys[e.getKeyCode()] = true;
}

@Override
public void keyReleased(KeyEvent e)
{
	keys[e.getKeyCode()] = false;
	if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) moveCD = 0;
	if (e.getKeyCode() == KeyEvent.VK_SPACE) shootCD = 0;
	if (e.getKeyCode() == KeyEvent.VK_Z) digCD = 0;
}

public static void main(String[] args)
{
	JFrame frame = new JFrame();
	frame.setExtendedState(Frame.MAXIMIZED_BOTH); // fullscreens window, got this from Google
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setTitle("Game");
	final GameSpace b = new GameSpace();
	frame.add(b, BorderLayout.CENTER);
	frame.setVisible(true);
	b.startAnimation();
}
}