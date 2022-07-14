package game.sceneobjects.rays;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import game.sceneobjects.Handler;

public class RayHandler extends Handler<Ray>
{
    private final RayThread[] threads;

    public RayHandler()
    {
        threads = new RayThread[10];
    }

    public void initThreads()
    {
        int step = objects.size() / threads.length;

        for(int i = 0; i < threads.length; i++)
        {
            //RayThread t = new RayThread(this, i * step, (i + 1) * step);
            //t.start();
            //threads[i] = t;
        }
    }

    public void updateObjects()
    {
        /*for(int i = 0; i < threads.length; i++)
        {
            RayThread t = threads[i];
            t.run();
        }**/
        for(int i = 0; i < objects.size(); i++)
        {
            Ray r = objects.get(i);
            r.update();
        }
    }

    public void renderObjects(ShapeRenderer shapeRenderer)
    {
        for(int i = 0; i < objects.size(); i++)
        {
            Ray r = objects.get(i);
            r.render(shapeRenderer);
        }
    }
}