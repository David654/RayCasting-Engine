package game.sceneobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.GameConstants;
import game.main.Scene;

public class Player
{
    private final Scene scene;
    private Vector3 position;
    private float radius;
    private Vector3 velocity;
    private float rotationAngle;
    private float verticalShift = 0;
    private float height = 0;

    public Player(Scene scene, Vector3 position, float radius, float rotationAngle)
    {
        this.scene = scene;
        this.position = position;
        this.radius = radius;
        this.rotationAngle = rotationAngle;
        velocity = new Vector3(0, 0, 0);
    }

    public Vector3 getPosition()
    {
        return position;
    }

    public void setPosition(Vector3 position)
    {
        this.position = position;
    }

    public float getRadius()
    {
        return radius;
    }

    public void setRadius(float radius)
    {
        this.radius = radius;
    }

    public Vector3 getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector3 velocity)
    {
        this.velocity = velocity;
    }

    public float getRotationAngle()
    {
        return rotationAngle;
    }

    public void setRotationAngle(float rotationAngle)
    {
        this.rotationAngle = rotationAngle;
    }

    public float getVerticalShift()
    {
        return verticalShift;
    }

    public void setVerticalShift(float verticalShift)
    {
        this.verticalShift = verticalShift;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public String toString()
    {
        return "Position: " + position.toString() + "; Radius: " + radius + "; Velocity" + velocity.toString() + "; Rotation Angle" + rotationAngle;
    }

    public void move(Vector2 speed)
    {
        velocity = new Vector3();

        if(Gdx.input.isKeyPressed(Input.Keys.W))
        {
            velocity.x = MathUtils.cos(rotationAngle) * speed.x;
            velocity.y = MathUtils.sin(rotationAngle) * speed.y;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A))
        {
            velocity.x = MathUtils.sin(rotationAngle) * speed.x;
            velocity.y = -MathUtils.cos(rotationAngle) * speed.y;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S))
        {
            velocity.x = -MathUtils.cos(rotationAngle) * speed.x;
            velocity.y = -MathUtils.sin(rotationAngle) * speed.y;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D))
        {
            velocity.x = -MathUtils.sin(rotationAngle) * speed.x;
            velocity.y = MathUtils.cos(rotationAngle) * speed.y;
        }
    }

    public void update()
    {
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))
        {
            height = -150;
            move(new Vector2(GameConstants.playerSpeedCrouching, GameConstants.playerSpeedCrouching));
        }
        else
        {
            height = 0;
            move(new Vector2(GameConstants.playerSpeed, GameConstants.playerSpeed));
        }


        position.add(velocity);
    }

    public void render(ShapeRenderer renderer)
    {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.GREEN);
        renderer.circle(position.x, position.y, radius);
        renderer.end();
    }
}