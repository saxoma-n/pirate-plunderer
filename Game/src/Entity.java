import java.awt.image.BufferedImage;

public class Entity
{
// superclass for all entities (i.e. player, enemies, bullets, etc.)
    protected int hp;
    protected int dmg;

    protected int x_pos, y_pos;
    protected String direction;

    protected int keyValue;

    /**Entity superclass constructor
     * 
     * @param x
     * @param y
     * @param hp
     * @param dmg
     * @param keyValue
     */
    public Entity (int x, int y, int hp, int dmg, int keyValue)
    {
        x_pos = x;
        y_pos = y;
        this.hp = hp;
        this.dmg = dmg;
        this.keyValue = keyValue;
        direction = "down";
    }

    public void takeDamage(int amount)
    {
        hp -= amount;
        if (hp <= 0) die();
    }

    public boolean die()
    {
    	return hp <= 0;
    }

    public int[] move(int x, int y)
    {
    	int[] a = {x_pos, y_pos};
        x_pos += x;
        y_pos += y;
        
        if (x > 0) direction = "right";
        if (x < 0) direction = "left";
        if (y > 0) direction = "down";
        if (y < 0) direction = "up";
        
        return a;
    }
}