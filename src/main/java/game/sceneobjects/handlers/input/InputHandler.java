package game.sceneobjects.handlers.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.main.GameConstants;
import game.main.Game;

public class InputHandler implements InputProcessor
{
    private final Game game;
    private int keycode;
    private boolean isCtrlPressed = false;

    public InputHandler(Game game)
    {
        this.game = game;
    }

    public boolean keyDown(int keycode)
    {
        this.keycode = keycode;
        return false;
    }

    public boolean keyUp(int keycode)
    {
        this.keycode = -1;
        if(keycode == Input.Keys.ESCAPE) game.exit();
        return false;
    }

    public boolean keyTyped(char character)
    {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        float differenceX = screenX - Gdx.graphics.getWidth() / 2f;
        float differenceY = screenY - Gdx.graphics.getHeight() / 2f;
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        game.player.setRotationAngle(game.player.getRotationAngle() + GameConstants.mouseSensitivityX * differenceX);

        float verticalShift = game.player.getVerticalShift() - 2000 * GameConstants.mouseSensitivityY * differenceY;
        verticalShift = MathUtils.clamp(verticalShift, -700, 700);
        game.player.setVerticalShift(verticalShift);

        if(keycode != -1) game.player.move(new Vector2(GameConstants.playerSpeed, GameConstants.playerSpeed));
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY)
    {
        float differenceX = screenX - Gdx.graphics.getWidth() / 2f;
        float differenceY = screenY - Gdx.graphics.getHeight() / 2f;
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        game.player.setRotationAngle(game.player.getRotationAngle() + GameConstants.mouseSensitivityX * differenceX);

        float verticalShift = game.player.getVerticalShift() - 2000 * GameConstants.mouseSensitivityY * differenceY;
        verticalShift = MathUtils.clamp(verticalShift, -700, 700);
        game.player.setVerticalShift(verticalShift);

        if(keycode != -1) game.player.move(new Vector2(GameConstants.playerSpeed, GameConstants.playerSpeed));
        return false;
    }

    public boolean scrolled(float amountX, float amountY)
    {
        return false;
    }
}