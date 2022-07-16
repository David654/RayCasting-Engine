package game.main.raycasting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import game.sceneobjects.Player;
import game.sceneobjects.handlers.RayHandler;

public class Sky
{
    private Color color;
    private Texture texture;

    public Sky(Color color)
    {
        this.color = color;
    }

    public Sky(Texture texture)
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

    public void rayCast(RayHandler rayHandler, Player player, ShapeRenderer shapeRenderer)
    {
        for(int i = 0; i < rayHandler.getObjectsCount(); i++)
        {
            float step = (float) Gdx.graphics.getHeight() / rayHandler.getObjectsCount();
            float r = color.r / (1 + step * i * 0.001f);
            float g = color.g / (1 + step * i * 0.001f);
            float b = color.b / (1 + step * i * 0.001f);
            shapeRenderer.setColor(new Color(r, g, b, 1));
            shapeRenderer.rect(0, -Gdx.graphics.getHeight() / 2f + step * (rayHandler.getObjectsCount() - 1 - i) + player.getVerticalShift() + player.getHeight(), Gdx.graphics.getWidth(), step);
        }
    }

    public void rayCast(RayHandler rayHandler, Player player, SpriteBatch spriteBatch)
    {

    }
}