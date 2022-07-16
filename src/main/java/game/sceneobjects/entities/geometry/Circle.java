package game.sceneobjects.entities.geometry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.sceneobjects.Player;
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
        float k = (float) Math.tan(ray.getFullAngle());
        float c = -k * ray.getStart().x + ray.getStart().y;

        float a = getCenter().x;
        float b = getCenter().y;

        float z = c - b;

        float D = (float) (4 * Math.pow(a - k * z, 2) - 4 * (1 + k * k) * (a * a + z * z - radius * radius));

        float x1 = (float) ((2 * (a - k * z) + Math.sqrt(D)) / (2 * (1 + k * k)));
        float x2 = (float) ((2 * (a - k * z) - Math.sqrt(D)) / (2 * (1 + k * k)));
        float y1 = k * x1 + c;
        float y2 = k * x2 + c;

        float dist1 = Vector2.dst(x1, y1, player.getPosition().x, player.getPosition().y);
        float dist2 = Vector2.dst(x2, y2, player.getPosition().x, player.getPosition().y);

        if(dist1 < dist2)
        {
            return new Vector2[]{new Vector2(x1, y1), new Vector2(x2, y2)};
        }
        else
        {
            return new Vector2[] {new Vector2(x2, y2), new Vector2(x1, y1)};
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