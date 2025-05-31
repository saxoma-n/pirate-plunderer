import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GameSpace extends JComponent implements Runnable, KeyListener {
	private int[][] terrain;
	private int[][] characters;
	private ArrayList<Entity> entities = new ArrayList<>();

	private BufferedImage pirate, cowboy, treasure;

	private boolean[] keys = new boolean[256];

	private int moveTimer = 0;

	public GameSpace() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		terrain = new int[][]{
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0},
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
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		};

		HashMap<Integer, String> terrainTypes = new HashMap<>();
		terrainTypes.put(0, "water");
		terrainTypes.put(1, "sand");
		terrainTypes.put(2, "grass");
		terrainTypes.put(3, "marked_treasure");
		terrainTypes.put(4, "treasure");

		try
		{// Load images
			pirate = ImageIO.read(GameSpace.class.getResourceAsStream("pirate.jpg"));
			cowboy = ImageIO.read(GameSpace.class.getResourceAsStream("cowboy.jpg"));
			treasure = ImageIO.read(GameSpace.class.getResourceAsStream("treasure.jpg"));
		}
		catch(IOException | IllegalArgumentException e) {
			e.printStackTrace();
		}

		characters = new int[terrain.length][terrain[0].length];
		for (int i = 0; i < terrain.length; i++) {
			for (int j = 0; j < terrain[i].length; j++) {
				characters[i][j] = 0;
			}
		}

		entities.add(new Player(11, 11, 3, 1, 1));

		for (Entity entity : entities) {
			int x = entity.x_pos;
			int y = entity.y_pos;
			if (x >= 0 && x < terrain.length && y >= 0 && y < terrain[0].length) {
				characters[x][y] = entity.keyValue;
			}
		}
	}

	public void startAnimation() {
		Thread t = new Thread(this);
		t.start();
	}

	public void paintComponent(Graphics g) {
		for (int i = 0; i < terrain.length; i++) {
			for (int j = 0; j < terrain[i].length; j++) {
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
				}
				g.fillRect(j * 32, i * 32, 32, 32);
			}
		}

		for (int i = 0; i < characters.length; i++) {
			for (int j = 0; j < characters[i].length; j++) {
				boolean paint = false;
				switch (characters[i][j])
				{
				case 1:
					g.setColor(Color.blue);
					paint = true;
					break;
				case 2:
					g.setColor(Color.red);
					paint = true;
					break;
				case 3:
					g.setColor(Color.green);
					paint = true;
					break;
				case 4:
					g.setColor(Color.black);
					paint = true;
					break;
				}
				if (paint) g.fillOval(j * 32, i * 32, 32, 32);
			}
		}
	}

	@Override
	public void run() {
		for (int[] x: characters) {
			for (int e: x)
				System.out.print(e);
			System.out.println();
		}

		while (true) {
			try
			{
				if (moveTimer == 0) {
					if(keys[KeyEvent.VK_UP] && terrain[entities.get(0).x_pos][entities.get(0).y_pos - 1] != 0)
					{
						characters[entities.get(0).x_pos][entities.get(0).y_pos] = 0;
						entities.get(0).move(-1, 0);
					}
					if(keys[KeyEvent.VK_DOWN] && terrain[entities.get(0).x_pos][entities.get(0).y_pos + 1] != 0)
					{
						characters[entities.get(0).x_pos][entities.get(0).y_pos] = 0;
						entities.get(0).move(1, 0);
					}
					if(keys[KeyEvent.VK_LEFT] && terrain[entities.get(0).x_pos - 1][entities.get(0).y_pos] != 0)
					{
						characters[entities.get(0).x_pos][entities.get(0).y_pos] = 0;
						entities.get(0).move(0, -1);
					}
					if(keys[KeyEvent.VK_RIGHT] && terrain[entities.get(0).x_pos + 1][entities.get(0).y_pos] != 0)
					{
						characters[entities.get(0).x_pos][entities.get(0).y_pos] = 0;
						entities.get(0).move(0, 1);
					}
				}
				if (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_RIGHT]) moveTimer++;
				if (moveTimer == 8) moveTimer = 0;

				for (Entity entity : entities) {
					int x = entity.x_pos;
					int y = entity.y_pos;
					if (x >= 0 && x < terrain.length && y >= 0 && y < terrain[0].length) {
						characters[x][y] = entity.keyValue;
					}
				}

				repaint();
				Thread.sleep(20);

			}
			catch (InterruptedException e) {
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
		keys [e.getKeyCode()] = false;
		moveTimer = 0;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		final int FRAME_WIDTH = 1000;
		final int FRAME_HEIGHT = 600;
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Game");
		final GameSpace b = new GameSpace();
		frame.add(b, BorderLayout.CENTER);
		frame.setVisible(true);
		b.startAnimation();
	}
}
