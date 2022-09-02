package game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.hud.HUD;
import game.sceneobjects.entities.geometry.Polygon;
import game.sceneobjects.handlers.input.InputHandler;
import game.raycasting.Floor;
import game.raycasting.RayCaster;
import game.raycasting.Sky;
import game.sceneobjects.entities.Player;
import game.sceneobjects.rays.Ray;
import game.sceneobjects.handlers.sceneobjects.RayHandler;
import game.sceneobjects.handlers.sceneobjects.ObstacleHandler;
import game.sceneobjects.entities.obstacles.Wall;
import game.sceneobjects.entities.geometry.Circle;
import game.sceneobjects.entities.geometry.Rectangle;
import launcher.LaunchConstants;
import org.lwjgl.opengl.GL20;

public class Game extends ScreenAdapter implements Runnable
{
    private Thread thread;
    private boolean running = false;
    private final OrthographicCamera camera;
    public final OrthographicCamera hudCamera;
    private final ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private SpriteBatch shaderBatch;
    private final BitmapFont font;
    public final FrameBuffer frameBuffer;
    public final FrameBuffer miniMapFrameBuffer;
    public ShaderProgram shader;
    public final Player player;
    public final RayHandler rayHandler;
    public final ObstacleHandler obstacleHandler;
    public final HUD hud;
    public final RayCaster rayCaster;

    public float time;
    public final Texture binocularMask;
    public final Texture noise;

    public Game()
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
        shaderBatch = new SpriteBatch();
        font = new BitmapFont();
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        miniMapFrameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getHeight(), Gdx.graphics.getHeight(), false);

        initShader();

        player = new Player(this, new Vector3(500, 400, 0), 16, 0);
        rayHandler = new RayHandler();
        obstacleHandler = new ObstacleHandler();
        hud = new HUD(this);
        rayCaster = new RayCaster(rayHandler, new Sky(new Color(0, 0.59f, 1, 1)), new Floor(Color.FOREST));

        initRays(GameConstants.fov, GameConstants.numRays, GameConstants.rayLength);
        initSprites();

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.None);
        Gdx.input.setInputProcessor(new InputHandler(this));

        binocularMask = new Texture("src\\main\\resources\\binocular mask.png");
        noise = new Texture("src\\main\\resources\\noise.png");
    }

    private void initShader()
    {
        ShaderProgram.pedantic = false;
        String vertexShader = shaderBatch.getShader().getVertexShaderSource();
        String fragmentShader = Gdx.files.local("src\\main\\java\\game\\shaders\\shader.glsl").readString();
        shader = new ShaderProgram(vertexShader, fragmentShader);
        if(!shader.isCompiled()) ProgramStateReporter.report(shader.getLog());
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
        obstacleHandler.addObject(new Wall(new Polygon(new float[] {700, 700, 900, 900, 800, 1200}, 0.3f, 500, Color.SCARLET)));
        obstacleHandler.addObject(new Wall(new Polygon(new float[] {200, 1000, 600, 1200, 400, 1500}, 1, 500, new Texture("src\\main\\resources\\img 4.jpg"))));
        obstacleHandler.addObject(new Wall(new Circle(new Vector3(100, 400, 500), 32, 0.6f, new Color(0.85f, 0.97f, 0.65f, 1))));
        obstacleHandler.addObject(new Wall(new Circle(new Vector3(100, 192, 0), 64, 1, new Texture("src\\main\\resources\\img 5.jpg"))));
        obstacleHandler.addObject(new Wall(new Circle(new Vector3(100, 64, 0), 48, 0.4f, new Color(1, 0.76f, 0, 1))));

        Rectangle rectangle = new Rectangle(new Vector3(1000, 200, 0), new Vector2(500, 1000), 1, new Texture("src\\main\\resources\\img 6.png"));
        rectangle.setDoDrawTop(false);
        rectangle.setDoDrawBottom(false);
        obstacleHandler.addObject(new Wall(rectangle));

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

    private void updateShader()
    {
        /*shader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float[][] distances = rayCaster.getDistances();
        for(int row = 0; row < Gdx.graphics.getHeight(); row++)
        {
            for(int col = 0; col < Gdx.graphics.getWidth(); col++)
            {
                int index = row * Gdx.graphics.getHeight() + col;
                shader.setUniform3fv("u_distances[" + index + "]", distances[row], 0, Gdx.graphics.getHeight());
            }
        }**/
        shader.bind();
        time += Gdx.graphics.getDeltaTime();
        shader.setUniformf("uResolution", new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        shader.setUniformf("uTime", time);

        Texture buffer = frameBuffer.getColorBufferTexture();
        buffer.bind(0);
        shader.setUniformi("sFrameBuffer", 0);
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

        // MINIMAP
        miniMapFrameBuffer.begin();

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        player.render(shapeRenderer);
        obstacleHandler.renderObjects(shapeRenderer);

        miniMapFrameBuffer.end();
        hud.getMinimap().setFrameBuffer(miniMapFrameBuffer);

        // GAME
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        //frameBuffer.begin();

        rayHandler.updateObjects();
        rayCaster.render(player, shapeRenderer, batch);
        //frameBuffer.end();

        /*updateShader();
        shaderBatch.setShader(shader);
        shaderBatch.begin();
        shaderBatch.draw(frameBuffer.getColorBufferTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shaderBatch.end();**/

        hudBatch.begin();
        hud.render(hudBatch, font);
        hudBatch.end();
    }
}