package launcher;

import game.main.Game;

public class Boot extends com.badlogic.gdx.Game
{
    public void create()
    {
        Game game = new Game();
        game.start();
        setScreen(game);
    }
}