import java.awt.image.BufferedImage;

public class Entity
{
// superclass for all entities (i.e. player, enemies, bullets, etc.)
    protected int hp;
    protected int dmg;

    protected int x_pos, y_pos;
    protected String direction;

    protected int keyValue;
    
    protected BufferedImage img;

    /**Entity superclass constructor
     * 
     * @param x
     * @param y
     * @param hp
     * @param dmg
     * @param keyValue
     */
    public Entity (int x, int y, int hp, int dmg, int keyValue, BufferedImage img)
    {
        x_pos = x;
        y_pos = y;
        this.hp = hp;
        this.dmg = dmg;
        this.keyValue = keyValue;
        direction = "down";
        this.img = img;
    }

    public void takeDamage(int amount)
    { // reduce hp accordingly
        hp -= amount;
        if (hp <= 0) die();
    }

    public boolean die()
    { // if dead, return true (signal to remove)
    	return hp <= 0;
    }
    
    public int[] move(int x, int y)
    { // default movement code
        x_pos += x;
        y_pos += y;
        
        if (y > 0) direction = "down";
        if (y < 0) direction = "up";
        if (x > 0) direction = "right";
        if (x < 0) direction = "left";
        
        return new int[] {x_pos, y_pos}; // initializer list without creating a new variable
    }
}