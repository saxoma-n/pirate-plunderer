import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;

import javax.swing.JComponent;

public class BallComponent extends JComponent implements Runnable, KeyListener
{
	private int x_pos;
	private int x_speed, y_speed;
	private int y_pos;

	private int x_pos2, y_pos2;
	
	private int score;

	private boolean[] keys = new boolean[256];

	public BallComponent()
	{ // All initializations here
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		x_pos = 20;
		y_pos = 200;
		x_speed = 8;
		y_speed = 2;

		x_pos2 = 30;
		y_pos2 = 350;		
	}

	public BallComponent(int x, int y, int xs, int ys)
	{
		x_pos = x;
		y_pos = y;
		x_speed = xs;
		y_speed = ys;
	}

	public void startAnimation()
	{
		Thread t = new Thread(this);
		t.start();
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(Color.blue);
		g.fillOval(x_pos, y_pos, 50, 50);
		g.setColor(Color.black);
		//	g.draw3DRect(200, 200, 30, 50, true);

		g.setColor(Color.black);
		g.fillOval(x_pos2, y_pos2, 50, 50);
		g.setColor(Color.black);
		
		
	}

	public void run()
	{
		int count = 0;
		while (true)
		{
			try
			{
				if (x_pos >= 910 || x_pos <= 0) x_speed *= -1;
				if (y_pos >= 510 || y_pos <= 0) y_speed *= -1;
				
				if (x_pos2 >= 790) x_pos2 = 790;
				if (x_pos2 <= 120) x_pos2 = 120;
				if (y_pos2 >= 390) y_pos2 = 390;
				if (y_pos2 <= 120) y_pos2 = 120;


				if (Math.abs(x_pos - x_pos2) <= 50 && Math.abs(y_pos - y_pos2) <= 50)
				{
					x_speed *= -1;
					y_speed *= -1; // maybe add "realistic" collisions?

					count++;
					if (count == 3)
					{
						count = 0;
						score += 10;
						System.out.println(score);
						x_speed = (int)(((Math.random()*21) - 10) * ((score > 100) ? (score / 100.0) : 1));
						y_speed = (int)(((Math.random()*21) - 10) * ((score > 100) ? (score / 100.0) : 1));
						x_pos = (int)(Math.random()*701) + 150;
						y_pos = (int)(Math.random()*301) + 150;
					}
				}

				x_pos += x_speed;
				y_pos += y_speed;

				if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {
					y_pos2 -= (int)(4 * ((score > 100) ? (score / 100.0) : 1));
				}
				if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
					x_pos2 -= (int)(4 * ((score > 100) ? (score / 100.0) : 1));
				}
				if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) {
					y_pos2 += (int)(4 * ((score > 100) ? (score / 100.0) : 1));
				}
				if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
					x_pos2 += (int)(4 * ((score > 100) ? (score / 100.0) : 1));
				}

				
				
				repaint();
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{

			}
		}
	}

	public void keyTyped(KeyEvent e)
	{

	}


	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
	}


	public void keyReleased(KeyEvent e)
	{
		keys [e.getKeyCode()] = false;
	}

	public int getScore()
	{
		return score;
	}
}