package net.appz.waterwaves;

/**
 * Created by App-z.net on 23.04.15.
 */
public class Rock {
    public Vector2 Position, Velocity;
    static Vector2 Gravity = new Vector2(0, 0.3f);

    public Rock(float x, float y, float vx, float vy){
        Position = new Vector2(x, y);
        Velocity = new Vector2(vx, vy);
    }

    public void Update(Water water)
    {
        if (Position.y > water.GetHeight(Position.x)) {
            Velocity.x *= 0.84f;
            Velocity.y *= 0.84f;
        }

        Position.x += Velocity.x;
        Position.y += Velocity.y;

        Velocity.x += Gravity.x;
        Velocity.y += Gravity.y;

    }
}
