import java.awt.image.BufferedImage;

public class Enemy extends Entity
{
	/**Enemy constructor
	 * 
	 * @param x
	 * @param y
	 * @param hp
	 * @param dmg
	 * @param keyValue
	 * @param img
	 */
	public Enemy(int x, int y, int hp, int dmg, int keyValue/*, BufferedImage img*/)
	{
		super(x, y, hp, dmg, keyValue/*, img*/);
	}
	
	public int[] move(int x, int y)
	{
		if (Math.random() > 0.5) // basically a coin flip to either move horizontally or vertically
		{
			return super.move((x > x_pos ? 1 : -1), 0);
		}
		return super.move(0, (y > y_pos ? 1 : -1));
	}
}
