package game.sceneobjects.entities.geometry;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;
import game.sceneobjects.entities.Player;
import game.sceneobjects.rays.Ray;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Polygon extends Shape
{
    private float[] vertices;
    private float y;

    public Polygon(float[] vertices, float height, float y, Color color)
    {
        super(height, color);
        this.vertices = vertices;
        this.y = y;
    }

    public Polygon(float[] vertices, float height, float y, Texture texture)
    {
        super(height, texture);
        this.vertices = vertices;
        this.y = y;
    }

    public float[] getVertices()
    {
        return vertices;
    }

    public void setVertices(float[] vertices)
    {
        this.vertices = vertices;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public Vector2 getCenter()
    {
        return GeometryUtils.polygonCentroid(vertices, 0, vertices.length, new Vector2(0, 0));
    }

    public Vector2 getNormal() {
        return null;
    }

    public String toString()
    {
        return "Vertices: " + Arrays.toString(vertices) + "; Velocity: " + velocity.toString();
    }

    public Vector2[] getPointsOfIntersectionWithLine(Ray ray, Player player)
    {
        Line2D l0 = new Line2D.Float(ray.getStart().x, ray.getStart().y, ray.getEnd().x, ray.getEnd().y);
        ArrayList<Line2D> lines = new ArrayList<>();
        ArrayList<Vector2> intersections = new ArrayList<>();
        ArrayList<Float> distances = new ArrayList<>();

        for(int i = 0; i < vertices.length - 3; i += 2)
        {
            Line2D l = new Line2D.Float(vertices[i], vertices[i + 1], vertices[i + 2], vertices[i + 3]);
            lines.add(l);
        }
        lines.add(new Line2D.Float(vertices[vertices.length - 2], vertices[vertices.length - 1], vertices[0], vertices[1]));

        for(int i = 0; i < lines.size(); i++)
        {
            if(l0.intersectsLine(lines.get(i)))
            {
                Vector2 intersection = getPointOfIntersectionBetweenTwoLines(l0, lines.get(i));
                intersections.add(intersection);
                distances.add(Vector2.dst(player.getPosition().x, player.getPosition().y, intersection.x, intersection.y));
            }
        }

        if(distances.size() > 1)
        {
            float dist1 = distances.get(0);
            float dist2 = distances.get(1);

            Vector2 i1 = intersections.get(distances.indexOf(dist1));
            Vector2 i2 = intersections.get(distances.indexOf(dist2));

            if(dist1 < dist2)
            {
                return new Vector2[] {i1, i2};
            }
            else
            {
                return new Vector2[] {i2, i1};
            }
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
        return Intersector.intersectSegmentPolygon(ray.getStart(), ray.getEnd(), new com.badlogic.gdx.math.Polygon(vertices));
    }

    public void update()
    {
        for(int i = 0; i < vertices.length - 1; i++)
        {
            vertices[i] += velocity.x;
            vertices[i + 1] += velocity.y;
        }
    }

    public void render(ShapeRenderer shapeRenderer)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);

        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray array = triangulator.computeTriangles(vertices);

        for (int i = 0; i < array.size - 1; i += 2)
        {
            float x1 = vertices[array.get(i) * 2];
            float y1 = vertices[(array.get(i) * 2) + 1];

            float x2 = vertices[(array.get(i + 1)) * 2];
            float y2 = vertices[(array.get(i + 1) * 2) + 1];

            float x3 = vertices[array.get(i + 2) * 2];
            float y3 = vertices[(array.get(i + 2) * 2) + 1];

            shapeRenderer.triangle(x1, y1, x2, y2, x3, y3);
        }
        shapeRenderer.end();
    }
}