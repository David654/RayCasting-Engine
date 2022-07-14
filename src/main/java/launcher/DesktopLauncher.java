package launcher;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import game.main.ProgramStateReporter;

public class DesktopLauncher
{
    public static void main(String[] args)
    {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setIdleFPS(LaunchConstants.tickRate);
        config.useVsync(LaunchConstants.vsync);
        config.setWindowedMode(LaunchConstants.windowWidth, LaunchConstants.windowHeight);
        config.setMaximized(true);
        config.setDecorated(false);
        config.setTitle(LaunchConstants.title);

        ProgramStateReporter.report("Program Launched");

        new Lwjgl3Application(new Boot(), config);
    }
}