package game.raycasting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.main.GameConstants;
import game.sceneobjects.entities.Player;
import game.sceneobjects.rays.Ray;
import game.sceneobjects.handlers.sceneobjects.RayHandler;
import game.sceneobjects.entities.obstacles.Obstacle;
import game.sceneobjects.entities.geometry.Circle;
import game.sceneobjects.entities.geometry.Polygon;
import game.sceneobjects.entities.geometry.Shape;
import game.sceneobjects.entities.geometry.Rectangle;

import java.util.ArrayList;

public class RayCaster
{
    private final RayHandler rayHandler;
    private final Sky sky;
    private final Floor floor;

    public RayCaster(RayHandler rayHandler, Sky sky, Floor floor)
    {
        this.rayHandler = rayHandler;
        this.sky = sky;
        this.floor = floor;
    }

    public void update()
    {

    }

    public void render(Player player, ShapeRenderer shapeRenderer, SpriteBatch spriteBatch)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // SKY
        sky.rayCast(rayHandler, player, shapeRenderer);

        // FLOOR
        floor.rayCast(rayHandler, player, shapeRenderer);
        shapeRenderer.end();

        // OBSTACLES
        for(int i = 0; i < rayHandler.getObjectsCount(); i++)
        {
            Ray ray = rayHandler.getObject(i);
            ArrayList<Vector2> pointsOfIntersection = ray.getPointsOfIntersection();
            ArrayList<Vector2> otherPointsOfIntersection = ray.getOtherPointsOfIntersection();
            ArrayList<Obstacle> obstacles = ray.getIntersectedObstacles();
            if(!pointsOfIntersection.isEmpty() && pointsOfIntersection.get(0).x <= GameConstants.rayLength)
            {
                for(int j = obstacles.size() - 1; j >= 0; j--)
                {
                    Shape shape = obstacles.get(j).getShape();
                    
                    float height = shape.getHeight();
                    float y = shape.getY();

                    float distance1 = ray.distanceTo(ray.getStart(), pointsOfIntersection.get(j));
                    distance1 *= MathUtils.cos(ray.getAngle());
                    distance1 = (float) Math.max(distance1, 0.00001);
                    float projectionHeight1 = Math.min(GameConstants.projCoefficient / distance1, Gdx.graphics.getHeight() * 5);

                    float y1 = Gdx.graphics.getHeight() / 2f - projectionHeight1 / 2 + projectionHeight1 * (1 - height) + player.getVerticalShift() + player.getHeight() - y / distance1 * 1000f;

                    float distance2 = ray.distanceTo(ray.getStart(), otherPointsOfIntersection.get(j));
                    distance2 *= MathUtils.cos(ray.getAngle());
                    distance2 = (float) Math.max(distance2, 0.00001);
                    float projectionHeight2 = Math.min(GameConstants.projCoefficient / (distance2), Gdx.graphics.getHeight() * 5);

                    float y2 = Gdx.graphics.getHeight() / 2f - projectionHeight2 / 2 + projectionHeight2 * (1 - height) + player.getVerticalShift() + player.getHeight() - y / distance2 * 1000f;

                    Color spriteColor = shape.getColor();
                    Texture texture = shape.getTexture();

                    // TOP & BOTTOM
                    if(texture == null)
                    {
                        float r = spriteColor.r / (1 + distance1 * 0.0005f);
                        float g = spriteColor.g / (1 + distance1 * 0.0005f);
                        float b = spriteColor.b / (1 + distance1 * 0.0005f);

                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(new Color(r, g, b, 1));
                        if(shape.isDoDrawTop())
                            shapeRenderer.rect(i * GameConstants.scale, y2, GameConstants.scale, y1 - y2);
                        if(y > 0 && shape.isDoDrawBottom())
                            shapeRenderer.rect(i * GameConstants.scale, y2 + projectionHeight2 * height, GameConstants.scale, y1 - y2);
                        shapeRenderer.end();
                    }
                    else
                    {
                        float offset = (otherPointsOfIntersection.get(j).x + otherPointsOfIntersection.get(j).y) / GameConstants.textureSize % 1;
                        offset = MathUtils.floor(offset * texture.getWidth());

                        spriteBatch.begin();
                        TextureRegion textureRegion = new TextureRegion(texture, (int) (offset), 0, 1, texture.getHeight());
                        textureRegion.flip(false, true);
                        if(shape.isDoDrawTop())
                            spriteBatch.draw(textureRegion, i * GameConstants.scale, y2, GameConstants.scale, y1 - y2);
                        if(y > 0 && shape.isDoDrawBottom())
                            spriteBatch.draw(textureRegion, i * GameConstants.scale, y2 + projectionHeight2 * height, GameConstants.scale, y1 - y2);
                        spriteBatch.end();

                        float a = 1 / (1 + distance2 * distance2 * 0.000001f);
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(new Color(0, 0, 0, 1 - a));
                        if(shape.isDoDrawTop())
                        {
                            //shapeRenderer.rect(i * GameConstants.scale, y2, GameConstants.scale, y1 - y2);
                        }
                        if(shape.isDoDrawBottom())
                        {
                            //shapeRenderer.rect(i * GameConstants.scale, y2 + projectionHeight2 * height, GameConstants.scale, y1 - y2);
                        }
                        shapeRenderer.end();

                    }

                    // BODY
                    if(texture == null)
                    {
                        float r = spriteColor.r / (1 + distance1 * 0.0005f);
                        float g = spriteColor.g / (1 + distance1 * 0.0005f);
                        float b = spriteColor.b / (1 + distance1 * 0.0005f);

                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(new Color(r, g, b, 1));
                        shapeRenderer.rect(i * GameConstants.scale, y1, GameConstants.scale, projectionHeight1 * height);
                        shapeRenderer.end();
                    }
                    else
                    {
                        float offset = (pointsOfIntersection.get(j).x + pointsOfIntersection.get(j).y) / GameConstants.textureSize % 1;
                        offset = MathUtils.floor(offset * texture.getWidth());

                        spriteBatch.begin();
                        TextureRegion textureRegion = new TextureRegion(texture, (int) offset, 0, 1, texture.getHeight());
                        textureRegion.flip(false, true);
                        spriteBatch.draw(textureRegion, i * GameConstants.scale, y1, GameConstants.scale, projectionHeight1 * height);
                        spriteBatch.end();

                        float a = 1 / (1 + distance1 * distance1 * 0.000001f);
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(new Color(0, 0, 0, 1 - a));
                        //shapeRenderer.rect(i * GameConstants.scale, y1, GameConstants.scale, projectionHeight1 * height);
                        shapeRenderer.end();
                    }
                }
            }
        }
    }
}