import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

	private BufferedImage pirate, cowboy, treasure;

	private boolean[] keys = new boolean[256];

	private int moveCD = 0;
	private int shootCD = 0;
	private int digCD = 0;

	private int enemyMoveCD = 0;

	private int score = 0;

	public GameSpace()
	{
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		terrain = new int[][] {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};

		HashMap<Integer, String> terrainTypes = new HashMap<>();
		terrainTypes.put(0, "water");
		terrainTypes.put(1, "sand");
		terrainTypes.put(2, "grass");
		terrainTypes.put(3, "marked_treasure");
		terrainTypes.put(4, "treasure");

		try
		{ // Load images
			pirate = ImageIO.read(GameSpace.class.getResourceAsStream("pirate.jpg"));
			cowboy = ImageIO.read(GameSpace.class.getResourceAsStream("cowboy.jpg"));
			treasure = ImageIO.read(GameSpace.class.getResourceAsStream("treasure.jpg"));
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

		enemies.add(new Enemy(13, 14, 1, 0, 2));

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
				switch (terrain[i][j])
				{
				case 0:
					g.setColor(Color.cyan);
					break;
				case 1:
					g.setColor(Color.yellow);
					break;
				case 2:
					g.setColor(Color.green);
					break;
				case 3:
					g.setColor(Color.black);
					break;
				case 4:
					g.setColor(Color.pink);
				}
				g.fillRect(j * 32, i * 32, 32, 32);
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
		g.setColor(Color.magenta);
		g.fillRect(32, 32, score, 10);
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
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
						bullets.add(new Bullet(player.x_pos + xAug, player.y_pos + yAug, -1, (xAug == 0 ? yAug : xAug), xAug == 0));
					}
					shootCD++;
				}
				if (shootCD == 16) shootCD = 0;

				if (moveCD == 0)
				{
					if((keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) && player.x_pos - 1 >= 0 && terrain[player.x_pos - 1][player.y_pos] != 0)
					{
						int[] a = player.move(-1, 0);
						characters[a[0]][a[1]] = 0;
					}
					if(keys[KeyEvent.VK_S] && terrain[player.x_pos + 1][player.y_pos] != 0)
					{
						int[] a = player.move(1, 0);
						characters[a[0]][a[1]] = 0;
					}
					if(keys[KeyEvent.VK_A] && terrain[player.x_pos][player.y_pos - 1] != 0)
					{
						int[] a = player.move(0, -1);
						characters[a[0]][a[1]] = 0;
					}
					if(keys[KeyEvent.VK_D] && terrain[player.x_pos][player.y_pos + 1] != 0)
					{
						int[] a = player.move(0, 1);
						characters[a[0]][a[1]] = 0;
					}
				}
				if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_S] || keys[KeyEvent.VK_A] || keys[KeyEvent.VK_D]) moveCD++;
				if (moveCD == 8) moveCD = 0;

				if (keys[KeyEvent.VK_Z]) // dig for treasure
				{
					if (digCD == 0)
					{
						terrain[player.x_pos][player.y_pos] = player.dig(terrain[player.x_pos][player.y_pos]);
						if (player.dig(terrain[player.x_pos][player.y_pos]) == 4)
						{
							score += 100;
							for (int i = 0; i < (score/100); i++)
							{
								enemies.add(new Enemy((int)(Math.random()*14) + 3, (int)(Math.random() * 27) + 6, score/100, 1, 2));
							}
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
				for (Enemy e: enemies)
				{
					if (enemyMoveCD == 0)
					{
						int[] a = e.move(player.x_pos, player.y_pos);
						characters[a[0]][a[1]] = 0;
					}
					enemyMoveCD++;
				}
				if (enemyMoveCD == 8) enemyMoveCD = 0;

				characters[player.x_pos][player.y_pos] = 1;
				Iterator<Enemy> iterator = enemies.iterator(); // got this idea from Google
				while (iterator.hasNext())
				{ // basically a for loop, but this solves certain issues
					Enemy entity = iterator.next();
					int x = entity.x_pos;
					int y = entity.y_pos;
					if (x > 0 && x < terrain.length - 1 && y > 0 && y < terrain[0].length - 1) characters[x][y] = entity.keyValue;
					for (Bullet b : bullets)
					{
						if (b.x_pos == x && b.y_pos == y)
						{
							entity.takeDamage(b.dmg);
							b.takeDamage(1);
						}
						if (b.x_pos == player.x_pos && b.y_pos == player.y_pos) player.takeDamage(b.dmg);
					}
					if (entity.die()) iterator.remove(); // remove the enemy without throwing a concurrent modification exception
				}

				Iterator<Bullet> iterator1 = bullets.iterator();
				while (iterator1.hasNext())
				{
					Bullet b = iterator1.next();
					int x = b.x_pos;
					int y = b.y_pos;
					if (x > 0 && x < terrain.length - 1 && y > 0 && y < terrain[0].length - 1) characters[x][y] = b.keyValue;
					if (x <= 0 || x >= terrain.length - 1 || y <= 0 || y >= terrain[0].length - 1)
					{ // remove out of bounds bullets
						iterator1.remove();
						characters[x][y] = 0;
					}
					if (b.die())
					{
						characters[b.x_pos][b.y_pos] = 0;
						iterator1.remove();
					}
				}


				repaint();
				Thread.sleep(20);

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
		if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_DOWN) moveCD = 0;
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
