package net.superdark.minecraft.plugins.SuperDarkCore.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

public class Json
{
    public Json()
    {

    }

    /**
     * Saves a Gson JsonElement to disk. I would suggest keeping a final string variable with the filename saved in your class.
     * @param pluginMainClass Main Class used to get directory of plugin jar.
     * @param jsonElement Json to save.
     * @param filename Path under /plugins/{PLUGIN_NAME}/{FILE_NAME}.json to save data to.
     */
    public void saveJsonToDisk(Class<?> pluginMainClass, JsonElement jsonElement, String filename)
    {
        Bukkit.getScheduler().runTaskAsynchronously(SuperDarkCorePlugin.getInstance(), () ->
        {
            try {
                FileWriter fileWriter = new FileWriter(getJsonDataFile(pluginMainClass, filename));
                Gson gson = new Gson();
                gson.toJson(jsonElement, fileWriter);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Uses the name of the plugin jar to get/create a new folder and file named after the plugin itself.
     * @param c Main class of your plugin. For example: SuperDarkCorePlugin.class
     * @param filename JSON file name without the .json included.
     * @return File of where JSON will be saved. ../plugins/{PLUGIN_NAME}/{FILE_NAME}.json
     */
    public static File getJsonDataFile(Class<?> c, String filename)
    {
        File dataFile = null;
        try {
            File parentFile = new File(c.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
            File parentDir = new File(parentFile + "/" + c.getSimpleName());
            dataFile = new File(parentDir + "/" + filename + ".json");

            if(!dataFile.exists())
            {
                parentDir.mkdirs();
                dataFile.createNewFile();
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return dataFile;
    }
}
