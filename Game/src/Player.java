public class Player
{
    private int health;
    public Player()
    {
        health = 3;
    }
    public void takeDamage()
    {
        health --;
        if(health==0)
        {
            die();
        }
    }
    public void die()
    {
        System.out.println("you have died");
    }



}