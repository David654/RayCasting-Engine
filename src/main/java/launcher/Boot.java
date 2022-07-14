package launcher;

import com.badlogic.gdx.Game;
import game.main.Scene;

public class Boot extends Game
{
    public void create()
    {
        Scene scene = new Scene();
        scene.start();
        setScreen(scene);
    }
}