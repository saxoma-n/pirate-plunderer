public abstract class Entity {
    protected int hp;
    protected int dmg;

    protected int x_pos, y_pos;

    protected int keyValue;

    public Entity (int x, int y, int hp, int dmg, int keyValue)
    {
        x_pos = x;
        y_pos = y;
        this.hp = hp;
        this.dmg = dmg;
        this.keyValue = keyValue;
    }

    public int getHp()
    {
        return hp;
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

    public void move(int x, int y)
    {
        x_pos += x;
        y_pos += y;
    }
}