/* Ekansh Nama, Michael Unguryan
 * 6/6/25
 * Period 9
 */

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
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GameSpace extends JComponent implements Runnable, KeyListener
{
	private int[][] terrain; // grid-based movment system
	private int[][] characters;
	private BufferedImage[][] terrainImages; // Store images assigned for terrain tiles
	private ArrayList<Enemy> enemies = new ArrayList<>(); // handle enemies
	private ArrayList<Bullet> bullets = new ArrayList<>(); // handle bullets
	private Player player; // player

	private Sound bang, move, oof, oofSlow, pirateOof, dig, song, danger; // sounds and images
	private BufferedImage pirate, cowboy, treasure, openTreasure, hp, sand1, sand2, sand3, grass1, grass2, grass3;
	private BufferedImage sl, sr, shl, shr, d, psl, psr;

	private boolean died = false, escaped = false, started = true, endangered = false, healed = false; // end the game, start the song, check if danger sound has been played

	private boolean[] keys = new boolean[256]; // track key input

	private int moveCD = 0; // cooldowns for all actions to make a tick-based system
	private int shootCD = 0;
	private int digCD = 0;
	private int enemyMoveCD = 0;
	private int enemyDamageCD = 0;

	private int score = 0;
	private int lastHealScore = 0;

	public GameSpace()
	{
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		terrain = new int[][]{ // make the environment
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
			treasure = ImageIO.read(GameSpace.class.getResourceAsStream("marked.png"));
			openTreasure = ImageIO.read(GameSpace.class.getResourceAsStream("opentreasure.png"));
			hp = ImageIO.read(GameSpace.class.getResourceAsStream("hp.png"));
			sand1 = ImageIO.read(GameSpace.class.getResourceAsStream("sand1.png"));
			sand2 = ImageIO.read(GameSpace.class.getResourceAsStream("sand2.png"));
			sand3 = ImageIO.read(GameSpace.class.getResourceAsStream("sand3.png"));
			grass1 = ImageIO.read(GameSpace.class.getResourceAsStream("grass1.png"));
			grass2 = ImageIO.read(GameSpace.class.getResourceAsStream("grass2.png"));
			grass3 = ImageIO.read(GameSpace.class.getResourceAsStream("grass3.png"));

			sl = ImageIO.read(GameSpace.class.getResourceAsStream("cowboySL.png"));
			sr = ImageIO.read(GameSpace.class.getResourceAsStream("cowboySR.png"));
			shl = ImageIO.read(GameSpace.class.getResourceAsStream("cowboySHR.png"));
			shr = ImageIO.read(GameSpace.class.getResourceAsStream("cowboySHL.png"));
			d = ImageIO.read(GameSpace.class.getResourceAsStream("cowboyD.png"));
			psl = ImageIO.read(GameSpace.class.getResourceAsStream("pirateSL.png"));
			psr = ImageIO.read(GameSpace.class.getResourceAsStream("pirateSR.png"));

			bang = new Sound("C:\\Users\\27name01\\Downloads\\bang.wav");
			move = new Sound("C:\\Users\\27name01\\Downloads\\move.wav");
			oof = new Sound("C:\\Users\\27name01\\Downloads\\oof.wav");
			oofSlow = new Sound("C:\\Users\\27name01\\Downloads\\oofslow.wav");
			pirateOof = new Sound("C:\\Users\\27name01\\Downloads\\pirateoof.wav");
			dig = new Sound("C:\\Users\\27name01\\Downloads\\dig.wav");
			danger = new Sound("C:\\Users\\27name01\\Downloads\\danger.wav");
			song = new Sound("C:\\Users\\27name01\\Downloads\\song.wav");
		}
		catch(IOException | IllegalArgumentException e)
		{
			e.printStackTrace();
		}

		characters = new int[terrain.length][terrain[0].length];
		for (int i = 0; i < terrain.length; i++)
		{ // initialize the character array
			for (int j = 0; j < terrain[i].length; j++)
			{
				characters[i][j] = 0;
			}
		}

		player = new Player(11, 11, 3, 1, 1, sr); // add player

		assignTreasureSpots(); // set treasure spots and images
		initTerrainImgs();

		for (Entity entity : enemies)
		{ // add enemies on the characters array
			int x = entity.x_pos;
			int y = entity.y_pos;
			if (x >= 0 && x < terrain.length && y >= 0 && y < terrain[0].length)
			{
				characters[x][y] = entity.keyValue;
			}
		}
	}

	private void assignTreasureSpots()
	{ // generate 3 random spots for treasure
		int assigned = 0;
		while (assigned < 3)
		{
			int x = (int) (Math.random() * terrain.length);
			int y = (int) (Math.random() * terrain[0].length);
			if (terrain[x][y] == 1)
			{
				terrain[x][y] = 3; // treasure spot
				assigned++;
			}
		}
	}

	private void initTerrainImgs()
	{ // initalize terrain images
		terrainImages = new BufferedImage[terrain.length][terrain[0].length];
		for (int i = 0; i < terrain.length; i++)
		{
			for (int j = 0; j < terrain[i].length; j++)
			{
				if (terrain[i][j] == 1)
				{ // generate grass
					double x = Math.random();
					if (x > 0.66) terrainImages[i][j] = sand1;
					else if (x > 0.33) terrainImages[i][j] = sand2;
					else terrainImages[i][j] = sand3;
				}
				else if (terrain[i][j] == 2)
				{ // generate sand
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
	{ // update terrain array and terrainImages
		terrain[x][y] = newTerrainType;
		if (newTerrainType == 1)
		{ // generate grass
			double xRand = Math.random();
			if (xRand > 0.66) terrainImages[x][y] = sand1;
			else if (xRand > 0.33) terrainImages[x][y] = sand2;
			else terrainImages[x][y] = sand3;
		}
		else if (newTerrainType == 2)
		{ // generate sand
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

	private int[] getEnemySpawn()
	{ // get coords of a new enemy spawn
		Random rand = new Random();
		int x;
		int y;
		while(true)
		{
			x = rand.nextInt(terrain.length);
			y = rand.nextInt(terrain[0].length);
			if (terrain[x][y] != 0 && !withinRadius(x,y,1))
			{
				break;
			}
		}
		return new int[]{x, y};
	}

	private boolean withinRadius(int x, int y, int r)
	{ // check if a certain spot is within a 3x3 radius of the player
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
		for (int i = 0; i < terrain.length; i++)
		{ //draw the terrain
			for (int j = 0; j < terrain[i].length; j++)
			{
				BufferedImage draw = terrainImages[i][j];
				boolean drawbg = false;
				switch (terrain[i][j])
				{
				case 0: // ocean
					g.setColor(new Color(0xA7D2FF));
					break;
				case 3: // x marks the spot
					draw = treasure;
					break;
				case 4: // treasure
					drawbg = true;
					draw = openTreasure;
					break;
				}
				g.fillRect(j * 32, i * 32, 32, 32);
				if (drawbg) g.drawImage(sand1, j*32, i*32, 32, 32, null);
				if (draw != null) g.drawImage(draw, j * 32, i * 32, 32, 32, null);
			}
		}

		for (int i = 0; i < characters.length; i++)
		{
			for (int j = 0; j < characters[i].length; j++)
			{ // draw bullets
				g.setColor(Color.darkGray);
				if (characters[i][j] == -1) g.fillOval(j * 32 + 8, i * 32 + 8, 16, 16);
			}
		}
		g.drawImage(player.img, player.y_pos*32, player.x_pos*32, 32, 32, null); // draw player
		for (Enemy e: enemies)
		{ // draw enemies

			g.drawImage(e.img, e.y_pos*32, e.x_pos*32, 32, 32, null);
		}

		if (player.hp > 0)
		{ // draw player hp
			for (int i = 0; i < player.hp; i++)
			{
				g.drawImage(hp, i * 60 + 10, 10, 50, 50, null);
			}
		}

		if (!died)
		{ // display score and powerups
			g.setColor(Color.white);
			g.setFont(g.getFont().deriveFont(Font.BOLD, 32f)); // got this from Google
			String scoreText = "Score: " + score;
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			int xScore = (getWidth() - metrics.stringWidth(scoreText)) / 2;
			int yScore = metrics.getHeight() + 15;
			g.drawString(scoreText, xScore, yScore);

			g.setFont(g.getFont().deriveFont(Font.BOLD, 24f));
			String powerupText = "Damage: " + (1 + score/300) + ", Piercing: " + (score/500);
			FontMetrics metrics1 = g.getFontMetrics(g.getFont());
			int xPower = getWidth() - metrics.stringWidth(powerupText) - 15;
			int yPower = metrics.getHeight() + 15;
			g.drawString(powerupText, xPower, yPower);
		}

		if (died)
		{ // display death message
			g.setColor(new Color(0xDC2626));
			g.setFont(g.getFont().deriveFont(Font.BOLD, 64f));
			String gameOverText = "GAME OVER";
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			int x = (getWidth() - metrics.stringWidth(gameOverText)) / 2;
			int y = (getHeight() - metrics.getHeight()) / 2;
			g.drawString(gameOverText, x, y);

			g.setFont(g.getFont().deriveFont(Font.BOLD, 32f));
			String scoreText = "Score: " + score;
			FontMetrics scoreMetrics = g.getFontMetrics(g.getFont());
			int xScore = (getWidth() - scoreMetrics.stringWidth(scoreText)) / 2;
			int yScore = y + metrics.getHeight() + 30;
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
				if (started || song.isEnded()) song.makeSound(); // play song
				started = false;
				if (died) song.stop();

				int xAug = 0, yAug = 0;
				switch (player.direction) // decide bullet velocity and direction
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

				if (keys[KeyEvent.VK_SPACE]) // spawn bullets
				{
					if (shootCD == 0)
					{
						bullets.add(new Bullet(player.x_pos, player.y_pos, 1 + score/500, 1 + score/300, -1, (xAug == 0 ? yAug : xAug), xAug == 0, null));
						if (player.direction.equals("left") || player.direction.equals("down")) player.img = shl;
						if (player.direction.equals("right") || player.direction.equals("up")) player.img = shr;
						bang.makeSound();
					}
					shootCD++;
				}
				if (shootCD == 8) shootCD = 0; // shoot cooldown

				if (moveCD == 0) // move player
				{
					if(keys[KeyEvent.VK_W] && player.x_pos - 1 >= 0 && terrain[player.x_pos - 1][player.y_pos] != 0)
					{ // move up
						int[] a = player.move(-1, 0);
						player.img = sr;
						characters[a[0]][a[1]] = 0;
						move.makeSound();
					}
					if(keys[KeyEvent.VK_S] && terrain[player.x_pos + 1][player.y_pos] != 0)
					{ // move down
						int[] a = player.move(1, 0);
						player.img = sl;
						characters[a[0]][a[1]] = 0;
						move.makeSound();
					}
					if(keys[KeyEvent.VK_A] && terrain[player.x_pos][player.y_pos - 1] != 0)
					{ // move left
						int[] a = player.move(0, -1);
						player.img = sl;
						characters[a[0]][a[1]] = 0;
						move.makeSound();
					}
					if(keys[KeyEvent.VK_D] && terrain[player.x_pos][player.y_pos + 1] != 0)
					{ // move right
						int[] a = player.move(0, 1);
						player.img = sr;
						characters[a[0]][a[1]] = 0;
						move.makeSound();
					}
				}
				if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_S] || keys[KeyEvent.VK_A] || keys[KeyEvent.VK_D]) moveCD++;
				if (moveCD == 8) moveCD = 0; // move cooldown

				if (keys[KeyEvent.VK_UP]) player.direction = "left"; // set direction
				if (keys[KeyEvent.VK_DOWN]) player.direction = "right";
				if (keys[KeyEvent.VK_LEFT]) player.direction = "up";
				if (keys[KeyEvent.VK_RIGHT]) player.direction = "down";

				if (keys[KeyEvent.VK_Z]) // dig for treasure
				{
					if (digCD == 0)
					{
						dig.makeSound();
						player.img = d;
						for (int i = 0; i < terrain.length; i++)
						{
							for (int j = 0; j < terrain[i].length; j++)
							{
								if (terrain[i][j] == 4)
								{ // clear treasure boxes
									updateTerrain(i,j,1);
								}
							}
						}
						if (terrain[player.x_pos][player.y_pos] == 3)
						{ // dig treasure up
							score += 50;
							for (int i = 0; i < (score/100); i++)
							{
								int[] spawnPos = getEnemySpawn();
								enemies.add(new Enemy(spawnPos[0], spawnPos[1], score/100, 1, 2, psl));
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

				if (player.hp == 1 && !endangered)
				{ // play the danger sound when hp = 1
					danger.makeSound();
					endangered = true;
				}
				if (player.hp > 1)
				{ // reset if you heal
					endangered = false;
				}

				// healing
				if (score - lastHealScore >= 700)
				{
					player.hp++;
					lastHealScore += 700;
				}

				for (Bullet e: bullets)
				{ // add and move bullets
					characters[e.x_pos][e.y_pos] = 0;
					int[] a = e.move();
					if (a[0] >= 0 && a[0] < characters.length && a[1] >= 0 && a[1] < characters[0].length)
					{
						characters[a[0]][a[1]] = e.keyValue;
					}
				}

				Iterator<Enemy> enemyIterator = enemies.iterator();
				while (enemyIterator.hasNext())
				{ // move and damage enemies
					Enemy enemy = enemyIterator.next();
					int x = enemy.x_pos;
					int y = enemy.y_pos;

					for (Bullet b : bullets) // damage enemies
					{
						if (b.x_pos == x && b.y_pos == y)
						{
							enemy.takeDamage(b.dmg);
							b.takeDamage(1);
						}
					}
					if (enemy.die()) // kill enemies
					{
						pirateOof.makeSound();
						score += 10;
						enemyIterator.remove();
					}
				}

				if (enemyMoveCD == 0) // move enemies
				{
					for (Enemy e: enemies)
					{
						int[] a = e.move(player.x_pos, player.y_pos);
						if (e.direction.equals("left") || e.direction.equals("up")) e.img = psl;
						if (e.direction.equals("right") || e.direction.equals("down")) e.img = psr;
						characters[a[0]][a[1]] = 0;
					}
				}
				enemyMoveCD++;
				if (enemyMoveCD == 10) enemyMoveCD = 0;

				if (enemyDamageCD == 0)
				{ // damage player
					for (Enemy e : enemies)
					{
						if (e.x_pos == player.x_pos && e.y_pos == player.y_pos)
						{
							player.takeDamage(1); // take damage
							if (player.hp > 0) oof.makeSound();
							enemyDamageCD = 100; // invincible period
							break;
						}
					}
				}
				if (enemyDamageCD > 0) enemyDamageCD--;

				characters[player.x_pos][player.y_pos] = 1; // place player

				for (Enemy e : enemies) // place enemies
				{
					int x = e.x_pos;
					int y = e.y_pos;
					if (x > 0 && x < terrain.length - 1 && y > 0 && y < terrain[0].length - 1) characters[x][y] = e.keyValue;
				}

				Iterator<Bullet> bulletIterator = bullets.iterator(); // place and remove bullets
				while (bulletIterator.hasNext())
				{
					Bullet b = bulletIterator.next(); // got this from google
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

				if (player.die()) // kill player
				{
					oofSlow.makeSound();
					died = true;
				}

				repaint();
				Thread.sleep(Math.max(5, 20 - (score / 400)));
				if (died) break; // end if you died

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
	{ // handle keys
		if (e.getKeyCode() < 256) keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
		if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) moveCD = 0;
		if (e.getKeyCode() == KeyEvent.VK_SPACE) shootCD = 0; // reset cooldowns
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

