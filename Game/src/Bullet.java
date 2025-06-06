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
	 * @param img
	 */
	public Bullet(int x, int y, int keyValue, int velocity, boolean direction/*, BufferedImage img*/)
	{
		super(x, y, 1, 1, keyValue/*, img*/);
		this.direction = direction;
		this.velocity = velocity;
	}
	
	public int[] move()
	{
		return super.move((direction ? 0 : velocity), (direction ? velocity : 0));
	}

	@Override
	public boolean die()
	{
		return hp <= 0;
	}

}
