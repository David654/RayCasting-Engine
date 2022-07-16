package game.sceneobjects.entities.obstacles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import game.sceneobjects.entities.geometry.Shape;

public abstract class Obstacle
{
    protected Shape shape;

    public Obstacle(Shape shape)
    {
        this.shape = shape;
    }

    public Shape getShape()
    {
        return shape;
    }

    public void setShape(Shape shape)
    {
        this.shape = shape;
    }

    public void update()
    {
        shape.update();
    }

    public void render(ShapeRenderer renderer)
    {
        shape.render(renderer);
    }
}