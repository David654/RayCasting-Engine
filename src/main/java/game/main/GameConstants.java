package game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class GameConstants
{
    public static final float mouseSensitivityX = 0.001f;
    public static final float mouseSensitivityY = 0.001f;
    public static final float playerSpeed = 25;
    public static final float playerSpeedCrouching = 5;
    public static final float fov = 60 * MathUtils.degreesToRadians;
    public static final int numRays = 512;
    public static final float rayLength = Gdx.graphics.getWidth() * 4;
    public static final float scale = (float) Gdx.graphics.getWidth() / numRays;
    public static final float distance = (float) (numRays / (2 * Math.tan(fov / 2)));
    public static final float projCoefficient = 600 * distance;
    public static final float minimapScale = 0.15f;
    public static final float textureSize = 100;
    public static final float textureScale = Gdx.graphics.getWidth() / 64f;
}