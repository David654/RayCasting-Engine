package game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.GameConstants;
import game.sceneobjects.rays.Ray;
import game.sceneobjects.rays.RayHandler;
import game.sceneobjects.sprites.obstacles.Obstacle;
import game.sceneobjects.sprites.shapes.Shape;
import game.sceneobjects.sprites.shapes.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

public class RayCaster
{
    private final RayHandler rayHandler;
    private final HashMap<Shape, ArrayList<Rectangle>> sprites;

    public RayCaster(RayHandler rayHandler)
    {
        this.rayHandler = rayHandler;
        sprites = new HashMap<>();
    }

    public void update()
    {

    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch)
    {
        // SKY
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(int i = 0; i < rayHandler.getObjectsCount(); i++)
        {
            Ray ray = rayHandler.getObject(i);
            float step = (float) Gdx.graphics.getHeight() / rayHandler.getObjectsCount();
            Color color = new Color(0, 0.59f, 1, 1);
            float r = color.r / (1 + step * i * 0.001f);
            float g = color.g / (1 + step * i * 0.001f);
            float b = color.b / (1 + step * i * 0.001f);
            shapeRenderer.setColor(new Color(r, g, b, 1));
            shapeRenderer.rect(0, -Gdx.graphics.getHeight() / 2f + step * (rayHandler.getObjectsCount() - 1 - i) + ray.getVerticalShift() + ray.getPlayerHeight(), Gdx.graphics.getWidth(), step);
        }

        // FLOOR
        for(int i = 0; i < rayHandler.getObjectsCount(); i++)
        {
            Ray ray = rayHandler.getObject(i);
            float step = (float) Gdx.graphics.getHeight() * 1.2f / rayHandler.getObjectsCount();
            Color color = Color.FOREST;
            float r = color.r / (1 + step * i * 0.001f);
            float g = color.g / (1 + step * i * 0.001f);
            float b = color.b / (1 + step * i * 0.001f);
            shapeRenderer.setColor(new Color(r, g, b, 1));
            shapeRenderer.rect(0, Gdx.graphics.getHeight() / 2f + step * (rayHandler.getObjectsCount() - 1 - i) + ray.getVerticalShift() + ray.getPlayerHeight(), Gdx.graphics.getWidth(), step);
        }
        shapeRenderer.end();

        // OBSTACLES
        for(int i = 0; i < rayHandler.getObjectsCount(); i++)
        {
            Ray ray = rayHandler.getObject(i);
            ArrayList<Vector2> pointsOfIntersection = ray.getPointsOfIntersection();
            ArrayList<Vector2> otherPointsOfIntersection = ray.getOtherPointsOfIntersection();
            ArrayList<Obstacle> obstacles = ray.getIntersectedObstacles();
            if(!pointsOfIntersection.isEmpty() && pointsOfIntersection.get(0).x < GameConstants.rayLength)
            {
                for(int j = obstacles.size() - 1; j >= 0; j--)
                {
                    float distance1 = ray.distanceTo(ray.getStart(), pointsOfIntersection.get(j));
                    distance1 *= MathUtils.cos(ray.getAngle());
                    distance1 = (float) Math.max(distance1, 0.00001);
                    float projectionHeight1 = Math.min(GameConstants.projCoefficient / distance1, Gdx.graphics.getHeight() * 5);

                    Shape shape = obstacles.get(j).getShape();
                    float y1 = Gdx.graphics.getHeight() / 2f - projectionHeight1 / 2 + projectionHeight1 * (1 - shape.getHeight()) + ray.getVerticalShift() + ray.getPlayerHeight();

                    float distance2 = ray.distanceTo(ray.getStart(), otherPointsOfIntersection.get(j));
                    distance2 *= MathUtils.cos(ray.getAngle());
                    distance2 = (float) Math.max(distance2, 0.00001);
                    float projectionHeight2 = Math.min(GameConstants.projCoefficient / (distance2), Gdx.graphics.getHeight() * 5);

                    float y2 = Gdx.graphics.getHeight() / 2f - projectionHeight2 / 2 + projectionHeight2 * (1 - shape.getHeight()) + ray.getVerticalShift() + ray.getPlayerHeight();

                    Color spriteColor = shape.getColor();
                    Texture texture = shape.getTexture();

                    // TOP
                    if(texture == null)
                    {
                        float r = spriteColor.r / (1 + distance1 * 0.0005f);
                        float g = spriteColor.g / (1 + distance1 * 0.0005f);
                        float b = spriteColor.b / (1 + distance1 * 0.0005f);

                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(new Color(r, g, b, 1));
                        shapeRenderer.rect(i * GameConstants.scale, y2, GameConstants.scale, y1 - y2);
                        shapeRenderer.end();
                    }
                    else
                    {
                        Vector2 diff = otherPointsOfIntersection.get(j).sub(shape.getCenter());
                        float offset = diff.x < diff.y ? otherPointsOfIntersection.get(j).x : otherPointsOfIntersection.get(j).y;
                        offset %= GameConstants.textureWidth;
                        offset = Math.max(offset, 0);

                        spriteBatch.begin();
                        TextureRegion textureRegion = new TextureRegion(texture, (int) (offset * GameConstants.scale), 0, (int) GameConstants.scale, (int) GameConstants.textureWidth);
                        spriteBatch.draw(textureRegion, i * GameConstants.scale, y2, GameConstants.scale, y1 - y2);
                        spriteBatch.end();
                    }

                    // BODY
                    if(texture == null)
                    {
                        float r = spriteColor.r / (1 + distance1 * 0.0005f);
                        float g = spriteColor.g / (1 + distance1 * 0.0005f);
                        float b = spriteColor.b / (1 + distance1 * 0.0005f);

                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(new Color(r, g, b, 1));
                        shapeRenderer.rect(i * GameConstants.scale, y1, GameConstants.scale, projectionHeight1 * shape.getHeight());
                        shapeRenderer.end();
                    }
                    else
                    {
                        Vector2 diff = pointsOfIntersection.get(j).sub(shape.getCenter());
                        diff.x = Math.abs(diff.x);
                        diff.y = Math.abs(diff.y);
                        float offset = diff.x < diff.y ? pointsOfIntersection.get(j).x : pointsOfIntersection.get(j).y;
                        offset %= 64;
                        //offset = Math.max(offset, 0);

                        spriteBatch.begin();
                        TextureRegion textureRegion = new TextureRegion(texture, (int) (GameConstants.scale * offset), 0, (int) GameConstants.scale, (int) GameConstants.textureWidth);
                        spriteBatch.draw(textureRegion, i * GameConstants.scale, y1, GameConstants.textureScale, projectionHeight1 * shape.getHeight());
                        spriteBatch.end();
                    }
                }
            }
        }
    }
}