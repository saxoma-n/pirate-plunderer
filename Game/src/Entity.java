public abstract class Entity
{
// superclass for all entities (i.e. player, enemies, bullets, etc.)
    protected int hp;
    protected int dmg;

    protected int x_pos, y_pos;
    protected String direction;

    protected int keyValue;

    public Entity (int x, int y, int hp, int dmg, int keyValue)
    {
        x_pos = x;
        y_pos = y;
        this.hp = hp;
        this.dmg = dmg;
        this.keyValue = keyValue;
        direction = "down";
    }

    public int getHp()
    {
        return hp;
    }
    public int getXPos()
    {
    	return x_pos;
    }
    public int getYPos()
    {
    	return y_pos;
    }
    public void setHp(int hp)
    {
        this.hp = hp;
    }

    public int getDmg()
    {
        return dmg;
    }

    public void setDmg(int dmg)
    {
        this.dmg = dmg;
    }

    public void takeDamage(int amount)
    {
        hp -= amount;
        if (hp <= 0) die();
    }

    public abstract boolean die();

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