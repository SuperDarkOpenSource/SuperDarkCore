package net.superdark.minecraft.plugins.SuperDarkCore.api;

import com.google.gson.JsonObject;
import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class WebhookAPI
{

    public WebhookAPI(SuperDarkCorePlugin pl)
    {
        this.plugin_ = pl;
    }

    //Ran Async. Remember, do not call bukkit functions.
    private void postJSON(URL url, JsonObject object)
    {

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin_, () ->
        {

            try
            {
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.addRequestProperty("Content-Type", "application/json");
                con.addRequestProperty("User-Agent", "SuperDarkCorePlugin");
                con.setDoOutput(true);
                con.setRequestMethod("POST");

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(object.toString().getBytes());

                con.getInputStream().close(); //close input

                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    /**
     * Posts a string message to a discord channel.
     * @param url Discord Webhook URL
     * @param content String message to post to a discord channel.
     */
    public void postDiscordMessage(URL url, String content) {
        JsonObject a = new JsonObject();
        a.addProperty("content", content);
        a.addProperty("type", 1);
        this.postJSON(url, a);
    }


    private SuperDarkCorePlugin plugin_;


}
