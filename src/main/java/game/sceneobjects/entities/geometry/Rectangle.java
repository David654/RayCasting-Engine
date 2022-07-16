package game.sceneobjects.entities.geometry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.sceneobjects.Player;
import game.sceneobjects.rays.Ray;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Rectangle extends Shape
{
    private Vector3 position;
    private Vector2 size;

    public Rectangle(Vector3 position, Vector2 size, float height, Color color)
    {
        super(height, color);
        this.position = position;
        this.size = size;
    }

    public Rectangle(Vector3 position, Vector2 size, float height, Texture texture)
    {
        super(height, texture);
        this.position = position;
        this.size = size;
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        this.position = position;
    }

    public Vector2 getSize()
    {
        return size;
    }

    public void setSize(Vector2 size)
    {
        this.size = size;
    }

    public Vector2 getCenter()
    {
        return new Vector2(position.x + size.x / 2, position.y + size.y / 2);
    }

    public Vector2 getNormal()
    {
        return null;
    }

    public String toString()
    {
        return "Position: " + position.toString() + "; Size: " + size.toString() + "; Velocity: " + velocity.toString();
    }

    public Vector2[] getPointsOfIntersectionWithLine(Ray ray, Player player)
    {
        float x = position.x;
        float y = position.y;
        float w = size.x;
        float h = size.y;

        Line2D l0 = new Line2D.Float(ray.getStart().x, ray.getStart().y, ray.getEnd().x, ray.getEnd().y);
        Line2D l1 = new Line2D.Float(x, y, x + w, y);
        Line2D l2 = new Line2D.Float(x + w, y, x + w, y + h);
        Line2D l3 = new Line2D.Float(x, y + h, x + w, y + h);
        Line2D l4 = new Line2D.Float(x, y, x, y + h);

        Vector2 intersection;

        ArrayList<Vector2> intersections = new ArrayList<>();
        ArrayList<Float> distances = new ArrayList<>();

        if(l0.intersectsLine(l1))
        {
            intersection = getPointOfIntersectionBetweenTwoLines(l0, l1);
            intersections.add(intersection);
            distances.add(Vector2.dst(player.getPosition().x, player.getPosition().y, intersection.x, intersection.y));
        }
        if(l0.intersectsLine(l2))
        {
            intersection = getPointOfIntersectionBetweenTwoLines(l0, l2);
            intersections.add(intersection);
            distances.add(Vector2.dst(player.getPosition().x, player.getPosition().y, intersection.x, intersection.y));
        }

        if(l0.intersectsLine(l3))
        {
            intersection = getPointOfIntersectionBetweenTwoLines(l0, l3);
            intersections.add(intersection);
            distances.add(Vector2.dst(player.getPosition().x, player.getPosition().y, intersection.x, intersection.y));
        }
        if(l0.intersectsLine(l4))
        {
            intersection = getPointOfIntersectionBetweenTwoLines(l0, l4);
            intersections.add(intersection);
            distances.add(Vector2.dst(player.getPosition().x, player.getPosition().y, intersection.x, intersection.y));
        }

        if(distances.size() > 1)
        {
            float dist1 = distances.get(0);
            float dist2 = distances.get(1);

            Vector2 i1 = intersections.get(distances.indexOf(dist1));
            Vector2 i2 = intersections.get(distances.indexOf(dist2));

            if(dist1 < dist2) return new Vector2[] {i1, i2};
            else return new Vector2[] {i2, i1};
        }
        else if(distances.size() == 1)
        {
            float dist = distances.get(0);
            return new Vector2[] {intersections.get(distances.indexOf(dist)), new Vector2(Float.MAX_VALUE, Float.MAX_VALUE)};
        }
        else
        {
            return new Vector2[] {new Vector2(Float.MAX_VALUE, Float.MAX_VALUE), new Vector2(Float.MAX_VALUE, Float.MAX_VALUE)};
        }
    }

    public boolean intersectsWithLine(Ray ray)
    {
        return Intersector.intersectSegmentRectangle(ray.getStart(), ray.getEnd(), new com.badlogic.gdx.math.Rectangle(position.x, position.y, size.x, size.y));
    }

    public void update()
    {
        position.add(new Vector3(velocity, 0));
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(position.x, position.y, size.x, size.y);
        shapeRenderer.end();
    }
}