package game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.GameConstants;
import game.hud.HUD;
import game.input.InputHandler;
import game.sceneobjects.Player;
import game.sceneobjects.rays.Ray;
import game.sceneobjects.rays.RayHandler;
import game.sceneobjects.sprites.obstacles.Mirror;
import game.sceneobjects.sprites.obstacles.ObstacleHandler;
import game.sceneobjects.sprites.obstacles.Wall;
import game.sceneobjects.sprites.shapes.Circle;
import game.sceneobjects.sprites.shapes.Polygon;
import game.sceneobjects.sprites.shapes.Rectangle;
import launcher.LaunchConstants;
import org.lwjgl.opengl.GL20;

public class Scene extends ScreenAdapter implements Runnable
{
    private Thread thread;
    private boolean running = false;
    private final OrthographicCamera camera;
    public final OrthographicCamera hudCamera;
    private final ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private final BitmapFont font;
    public final FrameBuffer frameBuffer;
    public final Player player;
    public final RayHandler rayHandler;
    public final ObstacleHandler obstacleHandler;
    public final HUD hud;
    public final RayCaster rayCaster;

    public Scene()
    {
        camera = new OrthographicCamera(Gdx.graphics.getWidth() * 4, Gdx.graphics.getHeight() * 4);
        camera.setToOrtho(true);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        hudCamera = new OrthographicCamera(Gdx.graphics.getWidth() * 4, Gdx.graphics.getHeight() * 4);
        hudCamera.setToOrtho(false);
        hudCamera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        font = new BitmapFont();
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getHeight(), Gdx.graphics.getHeight(), false);

        player = new Player(this, new Vector3(500, 400, 0), 16, 0);
        rayHandler = new RayHandler();
        obstacleHandler = new ObstacleHandler();
        hud = new HUD(this);
        rayCaster = new RayCaster(rayHandler);

        initRays(GameConstants.fov, GameConstants.numRays, GameConstants.rayLength);
        initSprites();

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
        Gdx.input.setInputProcessor(new InputHandler(this));
    }

    private void initRays(float fov, float numRays, float rayLength)
    {
        for(float i = -fov / 2; i < fov / 2; i += (fov / numRays))
        {
            Vector2 start = new Vector2(player.getPosition().x, player.getPosition().y);
            rayHandler.addObject(new Ray(this, start, i, rayLength));
        }
        rayHandler.initThreads();
        ProgramStateReporter.report(rayHandler.getObjectsCount() + " Rays Loaded");
    }

    private void initSprites()
    {
        String[] map = new String[]
                {
                        "..RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR..",
                        ".P................................P.",
                        "R..................................R",
                        "R..................................R",
                        "R.......SC.................SC......R",
                        "R..................................R",
                        "R..................................R",
                        "R..................................R",
                        "R.......S..................S.......R",
                        "R.......C..........................R",
                        "R..................................R",
                        "R..........................C.......R",
                        "R.......S.................CS.......R",
                        "R..................................R",
                        "R..................................R",
                        "R..................................R",
                        ".P.............CCC................P.",
                        "..RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR.."
                }; 
        
        obstacleHandler.addObject(new Wall(new Polygon(new float[] {700, 700, 900, 900, 800, 1200}, 0.3f, Color.SCARLET)));
        obstacleHandler.addObject(new Wall(new Polygon(new float[] {200, 1000, 600, 1200, 400, 1500}, 1.2f, "src\\main\\resources\\img 4.jpg")));
        obstacleHandler.addObject(new Wall(new Circle(new Vector2(100, 320), 64, 0.6f, new Color(0.85f, 0.97f, 0.65f, 1))));
        obstacleHandler.addObject(new Wall(new Circle(new Vector2(100, 192), 64, 0.8f, "src\\main\\resources\\img 5.jpg")));
        obstacleHandler.addObject(new Wall(new Circle(new Vector2(100, 64), 64, 0.4f, new Color(1, 0.76f, 0, 1))));
        obstacleHandler.addObject(new Wall(new Rectangle(new Vector2(1000, 200), new Vector2(500, 1000), 2, "src\\main\\resources\\img 2.png")));

        /*for(int i = 0; i < map.length; i++)
        {
            for(int j = 0; j < map[i].length(); j++)
            {
                char ch = map[i].charAt(j);
                switch(ch)
                {
                    case 'R' -> obstacleHandler.addObject(new Wall(new Rectangle(new Vector2(i * 100, j * 100), new Vector2(100, 100), 2, Color.GRAY)));
                    case 'S' -> obstacleHandler.addObject(new Wall(new Rectangle(new Vector2(i * 100, j * 100), new Vector2(25, 25), 2, Color.GRAY)));
                    case 'C' -> obstacleHandler.addObject(new Wall(new Circle(new Vector2(i * 100, j * 100), 25, 0.3f, Color.SCARLET)));
                    case 'P' -> obstacleHandler.addObject(new Wall(new Polygon(new float[] {i * 100, j * 100, i * 100 - 200, j * 100, i * 100, j * 100 - 200}, 2, Color.SCARLET)));
                    //case "P" -> obstacleHandler.addObject(new Wall(new Polygon(new float[] {700, 700, 900, 900, 800, 1200}, 0.3f, Color.SCARLET)));
                }
            }
        }**/

        ProgramStateReporter.report(obstacleHandler.getObjectsCount() + " Sprites Loaded");
    }

    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop()
    {
        try
        {
            thread.join();
            running = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void exit()
    {
        Gdx.app.exit();
        ProgramStateReporter.report("Program Stopped");
        System.exit(0);
    }

    private void update()
    {
        player.update();
        obstacleHandler.updateObjects();
        rayCaster.update();
        hud.update();

        camera.update();
        hudCamera.position.set(player.getPosition().x, player.getPosition().y, 0);
        hudCamera.up.set(0, -1, 0);
        hudCamera.rotate(90 - player.getRotationAngle() * MathUtils.radiansToDegrees);
        hudCamera.update();
    }

    public void run()
    {
        long lastTime = System.nanoTime();
        double amountOfTicks = LaunchConstants.tickRate;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();

        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1)
            {
                update();
                delta--;
            }

            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
            }
        }
        stop();
    }

    public void render(float delta)
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // minimap
        frameBuffer.begin();

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        player.render(shapeRenderer);
        obstacleHandler.renderObjects(shapeRenderer);

        frameBuffer.end();
        hud.getMinimap().setFrameBuffer(frameBuffer);

        // game
        shapeRenderer.setProjectionMatrix(camera.combined);
        rayHandler.updateObjects();
        //obstacleHandler.renderObjects(shapeRenderer);
        //player.render(shapeRenderer);
        batch.setProjectionMatrix(camera.combined);
        rayCaster.render(shapeRenderer, batch);

        hudBatch.begin();
        hud.render(hudBatch, font);
        hudBatch.end();
    }
}