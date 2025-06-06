/* Ekansh Nama, Michael Unguryan
 * 6/6/25
 * Period 9
 */

import java.awt.image.BufferedImage;

public class Player extends Entity
{// player class
	
	/** Player constructor
	 * 
	 * @param x
	 * @param y
	 * @param hp
	 * @param dmg
	 * @param keyValue
	 */
    public Player (int x, int y, int hp, int dmg, int keyValue, BufferedImage img)
    {
        super(x, y, hp, dmg, keyValue, img);
    }
}