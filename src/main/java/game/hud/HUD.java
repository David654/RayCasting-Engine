package game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.GameConstants;
import game.main.Game;
import launcher.LaunchConstants;

public class HUD
{
    private final Game game;
    private final Minimap minimap;

    public HUD(Game game)
    {
        this.game = game;
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
        font.draw(batch, "Rays: " + game.rayHandler.getObjectsCount(), 10, Gdx.graphics.getHeight() - 50);
        font.draw(batch, "Sprites: " + game.obstacleHandler.getObjectsCount(), 10, Gdx.graphics.getHeight() - 70);

        minimap.render(batch);
    }
}