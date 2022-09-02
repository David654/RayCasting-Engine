package game.sceneobjects.entities.geometry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.sceneobjects.entities.Player;
import game.sceneobjects.rays.Ray;

public class Circle extends Shape
{
    private Vector3 position;
    private float radius;

    public Circle(Vector3 position, float radius, float height, Color color)
    {
        super(height, color);
        this.position = position;
        this.radius = radius;
    }

    public Circle(Vector3 position, float radius, float height, Texture texture)
    {
        super(height, texture);
        this.position = position;
        this.radius = radius;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        this.position = position;
    }

    public Vector2 getCenter()
    {
        return new Vector2(position.x + radius, position.y + radius);
    }

    public Vector2 getNormal()
    {
        return null;
    }

    public float getRadius()
    {
        return radius;
    }

    public void setRadius(float radius)
    {
        this.radius = radius;
    }

    public String toString()
    {
        return "Position: " + position.toString() + "; Radius: " + radius + "; Velocity: " + velocity.toString();
    }

    public Vector2[] getPointsOfIntersectionWithLine(Ray ray, Player player)
    {
        double k = Math.tan(ray.getFullAngle());
        double c = -k * ray.getStart().x + ray.getStart().y;

        double a = getCenter().x;
        double b = getCenter().y;

        double z = c - b;

        double D = 4 * Math.pow(a - k * z, 2) - 4 * (1 + k * k) * (a * a + z * z - radius * radius);

        double x1 = (2 * (a - k * z) + Math.sqrt(D)) / (2 * (1 + k * k));
        double x2 = (2 * (a - k * z) - Math.sqrt(D)) / (2 * (1 + k * k));
        double y1 = k * x1 + c;
        double y2 = k * x2 + c;

        double dist1 = Vector2.dst((float) x1, (float) y1, player.getPosition().x, player.getPosition().y);
        double dist2 = Vector2.dst((float) x2, (float) y2, player.getPosition().x, player.getPosition().y);

        if(dist1 < dist2)
        {
            return new Vector2[]{new Vector2((float) x1, (float) y1), new Vector2((float) x2, (float) y2)};
        }
        else
        {
            return new Vector2[] {new Vector2((float) x2, (float) y2), new Vector2((float) x1, (float) y1)};
        }
    }

    public boolean intersectsWithLine(Ray ray)
    {
        return Intersector.intersectSegmentCircle(ray.getStart(), ray.getEnd(), getCenter(), radius * radius);
    }

    public void update()
    {
        position.add(new Vector3(velocity, 0));
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x + radius, position.y + radius, radius);
        shapeRenderer.end();
    }
}