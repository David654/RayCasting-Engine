package game.sceneobjects.handlers.sceneobjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import game.sceneobjects.entities.obstacles.Obstacle;

public class ObstacleHandler extends SceneObjectHandler<Obstacle>
{
    public void updateObjects()
    {
        for(int i = 0; i < objects.size(); i++)
        {
            Obstacle o = objects.get(i);
            o.update();
        }
    }

    public void renderObjects(ShapeRenderer shapeRenderer)
    {
        for(int i = 0; i < objects.size(); i++)
        {
            Obstacle o = objects.get(i);
            o.render(shapeRenderer);
        }
    }
}