package game.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class Minimap
{
    private final float width;
    private final float height;
    private FrameBuffer frameBuffer;

    public Minimap(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    public void setFrameBuffer(FrameBuffer frameBuffer)
    {
        this.frameBuffer = frameBuffer;
    }

    public void render(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, width, height);
    }
}