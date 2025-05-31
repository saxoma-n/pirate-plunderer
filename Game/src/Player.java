public class Player extends Entity
{
    public Player (int x, int y, int hp, int dmg, int keyValue)
    {
        super(x, y, hp, dmg, keyValue);
    }
    
    public boolean die()
    {
        if (hp == 0)
        {
            /* Die code */
            return true;
        }
        return false;
    }
}