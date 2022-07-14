package game.sceneobjects.rays;

public class RayThread extends Thread
{
    private final RayHandler rayHandler;
    private final int start;
    private final int end;

    public RayThread(RayHandler rayHandler, int start, int end)
    {
        this.rayHandler = rayHandler;
        this.start = start;
        this.end = end;
    }

    public void run()
    {
        for(int i = start; i < end; i++)
        {
            Ray r = rayHandler.getObject(i);
            r.update();
        }
    }
}