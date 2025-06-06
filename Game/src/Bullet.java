/* Ekansh Nama, Michael Unguryan
 * 6/6/25
 * Period 9
 */

import java.awt.image.BufferedImage;

public class Bullet extends Entity
{ // Bullet class. Created in order to automate bullet movement.
	protected int velocity;
	protected boolean direction;
	
	/** Bullet constructor
	 * 
	 * @param x
	 * @param y
	 * @param keyValue
	 * @param velocity positive if right/down, negative if left/up
	 * @param direction true if up/down, false if left/right
	 */
	public Bullet(int x, int y, int hp, int dmg, int keyValue, int velocity, boolean direction, BufferedImage img)
	{
		super(x, y, hp, dmg, keyValue, img);
		this.direction = direction;
		this.velocity = velocity;
	}
	
	public int[] move()
	{ // automated bullet movement; keeps it moving in a straight line automatically
		return super.move((direction ? 0 : velocity), (direction ? velocity : 0));
	}
}
