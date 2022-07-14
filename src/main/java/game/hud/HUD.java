package game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.GameConstants;
import game.main.Scene;
import launcher.LaunchConstants;

public class HUD
{
    private final Scene scene;
    private final Minimap minimap;

    public HUD(Scene scene)
    {
        this.scene = scene;
        minimap = new Minimap(Gdx.graphics.getWidth() * GameConstants.minimapScale, Gdx.graphics.getWidth() * GameConstants.minimapScale);
    }

    public Minimap getMinimap()
    {
        return minimap;
    }

    public void update()
    {

    }

    public void render(SpriteBatch batch, BitmapFont font)
    {
        font.setColor(Color.YELLOW);
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "Tick Rate: " + LaunchConstants.tickRate, 10, Gdx.graphics.getHeight() - 30);
        font.draw(batch, "Rays: " + scene.rayHandler.getObjectsCount(), 10, Gdx.graphics.getHeight() - 50);
        font.draw(batch, "Sprites: " + scene.obstacleHandler.getObjectsCount(), 10, Gdx.graphics.getHeight() - 70);

        minimap.render(batch);
    }
}