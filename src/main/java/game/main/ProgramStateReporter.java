package game.main;

import java.util.Date;
import java.text.SimpleDateFormat;

public class ProgramStateReporter
{
    public static void report(String message)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println("[ProgramStateReporter] " + message + " (" + formatter.format(date) + ")");
    }
}