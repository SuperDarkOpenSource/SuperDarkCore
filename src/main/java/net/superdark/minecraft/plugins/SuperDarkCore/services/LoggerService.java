package net.superdark.minecraft.plugins.SuperDarkCore.services;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;

public class LoggerService {

    private SuperDarkCorePlugin superDarkCorePlugin_;

    private int LOG_FLUSH_INTERVAL = 6000; //interval in TICKS (20 ticks = 1 second) so 6000 = 5 minutes. TODO: When a configuration is added, add LOG_FLUSH_INTERVAL to it.

    private ArrayList<String> logList = new ArrayList<>();

    public LoggerService(SuperDarkCorePlugin plugin)
    {
        this.superDarkCorePlugin_ = plugin;
        this.flushTask();
    }

    /**
     * Gets a file location for logs. If the the directory(ies) do not exist, they are created.
     * @return File path to .txt file to log to.
     */
    private File getLogFile()
    {
        Calendar calendar = Calendar.getInstance();
        //TXT file name is: YEAR_MONTH_DAY
        File fileLocation = new File(this.superDarkCorePlugin_.getDataFolder() + "/logs/", "" + calendar.get(Calendar.YEAR) + "_" + calendar.get(Calendar.MONTH) + "_" + calendar.get(Calendar.DAY_OF_MONTH) + ".txt");
        if(!fileLocation.exists())
        {
            fileLocation.getParentFile().mkdirs();
        }

        return fileLocation;
    }

    /**
     * Logs string to a file found in this plugin's data folder. Convention: [HOUR:MINUTE:SECONDS] (String)
     * @param string String to log.
     */
    public void log(String string)
    {
        Calendar calendar = Calendar.getInstance();
        //Convention is: [HOUR:MINUTE:SECONDS]
        String CONVENTION = "[" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "] ";
        String toArray = CONVENTION + string + "\n"; //make sure to go to the next line.
        this.logList.add(toArray);
    }

    /**
     * User friendly way to log chat messages.
     * @param username String username associated with a minecraft account.
     * @param stringToLog String message sent by a player.
     */
    public void log(String username, String stringToLog)
    {
        this.log(username + ": " + stringToLog);
    }

    public void flush()
    {
        try {

            File file = getLogFile();
            Writer fileWriter = new FileWriter(file, true);  // true appends to file instead of overwriting.

            for (String string : logList)
            {
                fileWriter.write(string);
            }

            fileWriter.close();
            logList.clear();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


    }

    public void flushTask()
    {
         new BukkitRunnable() {

            @Override
            public void run() {
                flush();
                this.runTaskLater(superDarkCorePlugin_, LOG_FLUSH_INTERVAL);
            }
        }.runTaskLater(this.superDarkCorePlugin_, LOG_FLUSH_INTERVAL);
    }
}
