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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GameSpace extends JComponent implements Runnable, KeyListener
{

	private int[][] terrain;
	private int[][] characters;
	private BufferedImage[][] terrainImages; // Store images assigned for terrain tiles
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private ArrayList<Bullet> bullets = new ArrayList<>(); 
	private Player player;

	private Sound bang, move, oof, oofSlow, pirateOof, dig, song;
	private BufferedImage pirate, cowboy, treasure, hp, sand1, sand2, sand3, grass1, grass2, grass3;

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

		terrain = new int[][]
		{ // make the environment
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
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
			//treasure = ImageIO.read(GameSpace.class.getResourceAsStream("treasure.jpg"));
			hp = ImageIO.read(GameSpace.class.getResourceAsStream("hp.png"));
			sand1 = ImageIO.read(GameSpace.class.getResourceAsStream("sand1.png"));
			sand2 = ImageIO.read(GameSpace.class.getResourceAsStream("sand2.png"));
			sand3 = ImageIO.read(GameSpace.class.getResourceAsStream("sand3.png"));
			grass1 = ImageIO.read(GameSpace.class.getResourceAsStream("grass1.png"));
			grass2 = ImageIO.read(GameSpace.class.getResourceAsStream("grass2.png"));
			grass3 = ImageIO.read(GameSpace.class.getResourceAsStream("grass3.png"));

			bang = new Sound("C:\\Users\\27name01\\Downloads\\bang.wav");
			move = new Sound("C:\\Users\\27name01\\Downloads\\move.wav");
			oof = new Sound("C:\\Users\\27name01\\Downloads\\oof.wav");
			oofSlow = new Sound("C:\\Users\\27name01\\Downloads\\oofslow.wav");
			pirateOof = new Sound("C:\\Users\\27name01\\Downloads\\pirateoof.wav");
			dig = new Sound("C:\\Users\\27name01\\Downloads\\dig.wav");
			song = new Sound("C:\\Users\\27name01\\Downloads\\song.wav");
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

		assignTreasureSpots();
		initializeTerrainImages();

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

	private void assignTreasureSpots()
	{
		Random rand = new Random();
		Set<String> usedPositions = new HashSet<>();
		int assigned = 0;
		while (assigned < 3)
		{
			int x = rand.nextInt(terrain.length);
			int y = rand.nextInt(terrain[0].length);
			if (terrain[x][y] == 1)
			{
				String key = x + "," + y;
				if (!usedPositions.contains(key))
				{
					terrain[x][y] = 3; // treasure spot
					usedPositions.add(key);
					assigned++;
				}
			}
		}
	}

	private void initializeTerrainImages()
	{
		terrainImages = new BufferedImage[terrain.length][terrain[0].length];
		for (int i = 0; i < terrain.length; i++)
		{
			for (int j = 0; j < terrain[i].length; j++)
			{
				if (terrain[i][j] == 1)
				{
					double x = Math.random();
					if (x > 0.66) terrainImages[i][j] = sand1;
					else if (x > 0.33) terrainImages[i][j] = sand2;
					else terrainImages[i][j] = sand3;
				}
				else if (terrain[i][j] == 2)
				{
					double x = Math.random();
					if (x > 0.66) terrainImages[i][j] = grass1;
					else if (x > 0.33) terrainImages[i][j] = grass2;
					else terrainImages[i][j] = grass3;
				}
				else
				{
					terrainImages[i][j] = null;
				}
			}
		}
	}

	private void updateTerrain(int x, int y, int newTerrainType)
	{
		terrain[x][y] = newTerrainType;
		if (newTerrainType == 1)
		{
			double xRand = Math.random();
			if (xRand > 0.66) terrainImages[x][y] = sand1;
			else if (xRand > 0.33) terrainImages[x][y] = sand2;
			else terrainImages[x][y] = sand3;
		}
		else if (newTerrainType == 2)
		{
			double xRand = Math.random();
			if (xRand > 0.66) terrainImages[x][y] = grass1;
			else if (xRand > 0.33) terrainImages[x][y] = grass2;
			else terrainImages[x][y] = grass3;
		}
		else
		{
			terrainImages[x][y] = null;
		}
	}

	private int[] getValidEnemySpawnPosition()
	{
		Random rand = new Random();
		int x;
		int y;
		while(true)
		{
			x = rand.nextInt(terrain.length);
			y = rand.nextInt(terrain[0].length);
			if (terrain[x][y] != 0 && !isWithinRadiusOfPlayer(x,y,1))
			{
				break;
			}
		}
		return new int[]{x, y};
	}

	private boolean isWithinRadiusOfPlayer(int x, int y, int r)
	{
		return (x >= player.x_pos - r) && (x <= player.x_pos + r) && (y >= player.y_pos - r) && (y <= player.y_pos + r);
	}

	public void startAnimation()
	{
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(0xF9FAFB));
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0; i < terrain.length; i++)
		{
			for (int j = 0; j < terrain[i].length; j++)
			{
				BufferedImage draw = terrainImages[i][j];
				switch (terrain[i][j])
				{
				case 0:
					g.setColor(new Color(0xA7D2FF));
					break;
				case 1:
					break;
				case 2:
					g.setColor(new Color(0x97C682));
					break;
				case 3:
					g.setColor(Color.yellow.darker());
					break;
				case 4:
					g.setColor(new Color(0xFBC8D4));
					break;
				}
				g.fillRect(j * 32, i * 32, 32, 32);
				if (draw != null) g.drawImage(draw, j * 32, i * 32, 32, 32, null);
			}
		}

		for (int i = 0; i < characters.length; i++)
		{
			for (int j = 0; j < characters[i].length; j++)
			{
				if (characters[i][j] == 0) continue;
				boolean small = false;
				switch (characters[i][j])
				{
				case 1:
					g.setColor(Color.blue);
					g.drawImage(cowboy, player.y_pos * 32, player.x_pos * 32, 32, 32, null);
					break;
				case 2:
					g.setColor(Color.red);
					g.drawImage(pirate, j * 32, i * 32, 32, 32, null);
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
				g.drawImage(hp, i * 60 + 10, 10, 50, 50, null);
			}
		}

		if (!gameOver)
		{
			g.setColor(new Color(0x2563EB));
			g.setFont(g.getFont().deriveFont(Font.BOLD, 32f));
			String scoreText = "Score: " + score;
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			int xScore = (getWidth() - metrics.stringWidth(scoreText)) / 2;
			int yScore = metrics.getHeight() + 15;
			g.drawString(scoreText, xScore, yScore);
		}

		if (gameOver)
		{
			g.setColor(new Color(0xDC2626));
			g.setFont(g.getFont().deriveFont(Font.BOLD, 64f));
			String gameOverText = "GAME OVER";
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			int x = (getWidth() - metrics.stringWidth(gameOverText)) / 2;
			int y = (getHeight() - metrics.getHeight()) / 2;
			g.drawString(gameOverText, x, y);
			
			g.setFont(g.getFont().deriveFont(Font.BOLD, 32f)); // Set a smaller font for the score
		    String scoreText = "Score: " + score;
		    FontMetrics scoreMetrics = g.getFontMetrics(g.getFont());
		    int xScore = (getWidth() - scoreMetrics.stringWidth(scoreText)) / 2;
		    int yScore = y + metrics.getHeight() + 30; // Position it below the game over text
		    g.drawString(scoreText, xScore, yScore);
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				if (started || song.isEnded()) song.makeSound();
				started = false;
				if (gameOver) song.stop();

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
						for (int i = 0; i < terrain.length; i++)
						{
							for (int j = 0; j < terrain[i].length; j++)
							{
								if (terrain[i][j] == 4)
								{
									updateTerrain(i,j,1);
								}
							}
						}
						if (terrain[player.x_pos][player.y_pos] == 3)
						{
							score += 50;
							for (int i = 0; i < (score/100); i++)
							{
								int[] spawnPos = getValidEnemySpawnPosition();
								enemies.add(new Enemy(spawnPos[0], spawnPos[1], score/100, 1, 2));
							}
							updateTerrain(player.x_pos, player.y_pos, 4);

							int x = (int)(Math.random() * 14) + 3;
							int y = (int)(Math.random() * 27) + 6;

							updateTerrain(x, y, 3);
						}
					}
					digCD++;
				}
				if (digCD == 100) digCD = 0;

				for (Bullet e: bullets)
				{
					int[] a = e.move();
					characters[a[0]][a[1]] = 0;
				}

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

				if (enemyMoveCD == 0)
				{
					for (Enemy e: enemies)
					{
						int[] a = e.move(player.x_pos, player.y_pos);
						characters[a[0]][a[1]] = 0;
					}
				}

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

				for (Enemy e : enemies)
				{
					int x = e.x_pos;
					int y = e.y_pos;
					if (x > 0 && x < terrain.length - 1 && y > 0 && y < terrain[0].length - 1) characters[x][y] = e.keyValue;
				}

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
					if (b.die())
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
				Thread.sleep(Math.max(5, 20 - (score / 400)));
				if (gameOver) break;

			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{}

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
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Game");
		final GameSpace b = new GameSpace();
		frame.add(b, BorderLayout.CENTER);
		frame.setVisible(true);
		b.startAnimation();
	}
}