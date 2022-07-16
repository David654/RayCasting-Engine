package game.threads;

import game.sceneobjects.handlers.sceneobjects.SceneObjectHandler;

public abstract class SceneObjectThread<T> extends Thread
{
    protected final SceneObjectHandler<T> sceneObjectHandler;
    protected final int start;
    protected final int end;

    public SceneObjectThread(SceneObjectHandler<T> sceneObjectHandler, int start, int end)
    {
        this.sceneObjectHandler = sceneObjectHandler;
        this.start = start;
        this.end = end;
    }

    protected abstract void update();

    public void run()
    {
        update();
    }
}