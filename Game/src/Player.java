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
    public Player (int x, int y, int hp, int dmg, int keyValue)
    {
        super(x, y, hp, dmg, keyValue);
    }
    
    public int dig (int x)
    {
    	if (x == 3) return 4;
    	return x;
    }
}