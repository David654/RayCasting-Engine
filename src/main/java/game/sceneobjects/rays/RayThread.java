package game.sceneobjects.rays;

import game.sceneobjects.handlers.RayHandler;
import game.threads.SceneObjectThread;

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