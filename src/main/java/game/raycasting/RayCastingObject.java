package game.raycasting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import game.sceneobjects.entities.Player;
import game.sceneobjects.handlers.sceneobjects.RayHandler;

public abstract class RayCastingObject
{
    protected Color color;
    protected Texture texture;

    public RayCastingObject(Color color)
    {
        this.color = color;
    }

    public RayCastingObject(Texture texture)
    {
        this.texture = texture;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Texture getTexture()
    {
        return texture;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public abstract void rayCast(RayHandler rayHandler, Player player, ShapeRenderer shapeRenderer);

    public abstract void rayCast(RayHandler rayHandler, Player player, SpriteBatch spriteBatch);
}