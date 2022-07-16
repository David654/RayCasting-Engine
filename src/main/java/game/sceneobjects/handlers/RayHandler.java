package game.sceneobjects.handlers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import game.sceneobjects.rays.Ray;
import game.sceneobjects.rays.RayThread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class RayHandler extends SceneObjectHandler<Ray>
{
    private final RayThread[] threads;
    private final ThreadPoolExecutor executor;

    public RayHandler()
    {
        threads = new RayThread[10];
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    }

    public void initThreads()
    {
        int step = objects.size() / threads.length;

        for(int i = 0; i < threads.length; i++)
        {
            RayThread t = new RayThread(this, i * step, (i + 1) * step);
            threads[i] = t;
        }
    }

    public void updateObjects()
    {
        for(int i = 0; i < threads.length; i++)
        {
            RayThread t = threads[i];
            t.run();
            //executor.submit(t);
        }
        /*for(int i = 0; i < objects.size(); i++)
        {
            Ray r = objects.get(i);
            r.update();
        }**/
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