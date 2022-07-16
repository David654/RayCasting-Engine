package game.sceneobjects.rays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.main.Scene;
import game.sceneobjects.sprites.obstacles.Mirror;
import game.sceneobjects.sprites.obstacles.Obstacle;
import game.sceneobjects.sprites.obstacles.Wall;
import game.sceneobjects.sprites.shapes.Shape;
import game.sceneobjects.sprites.shapes.Circle;
import game.sceneobjects.sprites.shapes.Polygon;
import game.sceneobjects.sprites.shapes.Rectangle;;import java.util.ArrayList;
public class Ray
{
    private final Scene scene;
    private Vector2 start;
    private Vector2 end;
    private float angle, length;
    private final ArrayList<Vector2> pointsOfIntersection;
    private final ArrayList<Vector2> otherPointsOfIntersection;
    private final ArrayList<Obstacle> intersectedObstacles;

    public Ray(Scene scene, Vector2 start, float angle, float length)
    {
        this.scene = scene;
        this.start = start;
        this.angle = angle;
        this.length = length;
        end = new Vector2(start.x + length * MathUtils.cos(scene.player.getRotationAngle() + angle), start.y + length * MathUtils.sin(scene.player.getRotationAngle() + angle));
        pointsOfIntersection = new ArrayList<>();
        otherPointsOfIntersection = new ArrayList<>();
        intersectedObstacles = new ArrayList<>();
    }

    public Vector2 getStart()
    {
        return start;
    }

    public void setStart(Vector2 start)
    {
        this.start = start;
    }

    public Vector2 getEnd()
    {
        return end;
    }

    public void setEnd(Vector2 end)
    {
        this.end = end;
    }

    public float getAngle()
    {
        return angle;
    }

    public void setAngle(float angle)
    {
        this.angle = angle;
    }

    public float getFullAngle()
    {
        return scene.player.getRotationAngle() + angle;
    }

    public float getLength()
    {
        return length;
    }

    public void setLength(float length)
    {
        this.length = length;
    }

    public float getDistance()
    {
        return Vector2.dst(start.x, start.y, end.x, end.y);
    }

    public ArrayList<Vector2> getPointsOfIntersection()
    {
        return pointsOfIntersection;
    }

    public ArrayList<Vector2> getOtherPointsOfIntersection()
    {
        return otherPointsOfIntersection;
    }

    public ArrayList<Obstacle> getIntersectedObstacles()
    {

        return intersectedObstacles;
    }

    public float distanceTo(Vector2 p1, Vector2 p2)
    {
        return Vector2.dst(p1.x, p1.y, p2.x, p2.y);
    }

    public String toString()
    {
        return "Start: " + start.toString() + "; End: " + end.toString() + "; Angle: " + angle + "; Length: " + length;
    }

    private boolean intersects(Obstacle obstacle)
    {
        Shape shape = obstacle.getShape();
        if(shape instanceof Circle circle)
        {
            return circle.intersectsWithLine(this);
        }
        else if(shape instanceof Rectangle rectangle)
        {
            return rectangle.intersectsWithLine(this);
        }
        else if(shape instanceof Polygon polygon)
        {
            return polygon.intersectsWithLine(this);
        }
        return false;
    }

    public Vector2[] getPointOfIntersection(Obstacle obstacle)
    {
        Shape shape = obstacle.getShape();
        if(shape instanceof Circle circle)
        {
            return circle.getPointsOfIntersectionWithLine(this, scene.player);
        }
        else if(shape instanceof Rectangle rectangle)
        {
            return rectangle.getPointsOfIntersectionWithLine(this, scene.player);
        }
        else if(shape instanceof Polygon polygon)
        {
            return polygon.getPointsOfIntersectionWithLine(this, scene.player);
        }
        return new Vector2[] {end, end};
    }

    public void update()
    {
        start = new Vector2(scene.player.getPosition().x, scene.player.getPosition().y);
        end = new Vector2(start.x + length * MathUtils.cos(scene.player.getRotationAngle() + angle), start.y + length * MathUtils.sin(scene.player.getRotationAngle() + angle));
        pointsOfIntersection.clear();
        otherPointsOfIntersection.clear();
        intersectedObstacles.clear();

        for(int i = 0; i < scene.obstacleHandler.getObjectsCount(); i++)
        {
            Obstacle s = scene.obstacleHandler.getObject(i);
            if(intersects(s))
            {
                if(s instanceof Wall)
                {
                    Vector2[] pointsOfIntersection = getPointOfIntersection(s);
                    this.pointsOfIntersection.add(pointsOfIntersection[0]);
                    this.otherPointsOfIntersection.add(pointsOfIntersection[1]);
                    intersectedObstacles.add(s);
                }
                /*else if(s instanceof Mirror)
                {
                    for(int j = 0; j < scene.obstacleHandler.getObjectsCount(); j++)
                    {
                        Obstacle o = scene.obstacleHandler.getObject(j);
                        if(intersects(s))
                        {
                            if(o instanceof Wall)
                            {
                                Vector2[] pointsOfIntersection = getPointOfIntersection(s);
                                distances.add(distanceTo(start, pointsOfIntersection[0]));
                                otherDistances.add(distanceTo(start, pointsOfIntersection[1]));
                                intersectedObstacles.add(o);
                            }
                            else if(o instanceof Mirror)
                            {
                                Vector2[] pointsOfIntersection = getPointOfIntersection(s);
                                distances.add(distanceTo(start, pointsOfIntersection[0]));
                                otherDistances.add(distanceTo(start, pointsOfIntersection[1]));
                                intersectedObstacles.add(o);
                            }
                        }
                    }
                }**/
            }
        }

        pointsOfIntersection.sort((v1, v2) -> Float.compare(distanceTo(start, v1), distanceTo(start, v2)));
        otherPointsOfIntersection.sort((v1, v2) -> Float.compare(distanceTo(start, v1), distanceTo(start, v2)));
        intersectedObstacles.sort((o1, o2) ->
        {
            Vector2 i1 = getPointOfIntersection(o1)[0];
            Vector2 i2 = getPointOfIntersection(o2)[0];
            float dist1 = distanceTo(start, i1);
            float dist2 = distanceTo(start, i2);
            return Float.compare(dist1, dist2);
        });
    }

    public void render(ShapeRenderer renderer)
    {
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.line(start, end);
        renderer.end();
    }
}