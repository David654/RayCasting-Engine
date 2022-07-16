package game.sceneobjects.sprites.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import game.sceneobjects.Player;
import game.sceneobjects.rays.Ray;

import java.awt.geom.Line2D;

public abstract class Shape
{
    protected Color color = Color.GRAY;
    protected Texture texture;
    protected Vector2 velocity;
    protected float height;
    protected boolean doDrawTop = true;
    protected boolean doDrawBottom = true;

    public Shape(float height, Color color)
    {
        this.height = height;
        this.color = color;
        velocity = new Vector2(0, 0);
    }

    public Shape(float height, Texture texture)
    {
        this.height = height;
        this.texture = texture;
        velocity = new Vector2(0, 0);
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
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

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector2 velocity)
    {
        this.velocity = velocity;
    }

    public boolean isDoDrawTop()
    {
        return doDrawTop;
    }

    public void setDoDrawTop(boolean doDrawTop)
    {
        this.doDrawTop = doDrawTop;
    }

    public boolean isDoDrawBottom()
    {
        return doDrawBottom;
    }

    public void setDoDrawBottom(boolean doDrawBottom)
    {
        this.doDrawBottom = doDrawBottom;
    }

    public abstract Vector2 getCenter();

    public abstract Vector2 getNormal();

    public abstract String toString();

    private Pixmap flipPixmap(Pixmap src)
    {
        final int width = src.getWidth();
        final int height = src.getHeight();
        Pixmap flipped = new Pixmap(width, height, src.getFormat());

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                flipped.drawPixel(x, y, src.getPixel(x, height - y - 1));
            }
        }
        return flipped;
    }

    public abstract Vector2[] getPointsOfIntersectionWithLine(Ray ray, Player player);

    public abstract boolean intersectsWithLine(Ray ray);

    public Vector2 getPointOfIntersectionBetweenTwoLines(Line2D a, Line2D b)
    {
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(), x3 = b.getX1(), y3 = b.getY1(), x4 = b.getX2(), y4 = b.getY2();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if(d == 0) return new Vector2(Gdx.graphics.getWidth() * 2, Gdx.graphics.getHeight() * 2);
        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
        return new Vector2((float) xi, (float) yi);
    }

    public abstract void update();

    public abstract void render(ShapeRenderer shapeRenderer);
}