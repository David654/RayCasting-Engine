package game.sceneobjects;

import java.util.ArrayList;

public abstract class Handler<T>
{
    public final ArrayList<T> objects;

    public Handler()
    {
        objects = new ArrayList<>();
    }

    public void addObject(T object)
    {
        objects.add(object);
    }

    public void removeObject(T sprite)
    {
        objects.remove(sprite);
    }

    public void removeObject(int index)
    {
        objects.remove(index);
    }

    public T getObject(int index)
    {
        return objects.get(index);
    }

    public int getObjectsCount()
    {
        return objects.size();
    }

    public abstract void updateObjects();
}