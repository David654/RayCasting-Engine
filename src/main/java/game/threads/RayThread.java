package game.threads;

import game.sceneobjects.handlers.sceneobjects.RayHandler;
import game.sceneobjects.rays.Ray;

public class RayThread extends SceneObjectThread<Ray>
{
    public RayThread(RayHandler rayHandler, int start, int end)
    {
        super(rayHandler, start, end);
    }

    protected void update()
    {
        for(int i = start; i < end; i++)
        {
            Ray ray = sceneObjectHandler.getObject(i);
            ray.update();
        }
    }
}