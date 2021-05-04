package net.superdark.minecraft.plugins.SuperDarkCore.services;

import com.google.gson.JsonObject;
import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class WebhookService
{

    public WebhookService(SuperDarkCorePlugin pl)
    {
        this.plugin_ = pl;
        this.superdarkConfig = this.plugin_.getSuperDarkCoreConfig();
    }

    //Ran Async. Remember, do not call bukkit functions.

    /**
     * Sends a post request with a jSON object.
     * @param url Webhook URL
     * @param object JSON object to be posted. Check whatever you are posting to for correct formatting.
     */
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

        if(this.superdarkConfig.getBoolean("DisableDiscordWebhook"))
        {
            return;
        }

        if (url == null)
        {
            this.plugin_.getLogger().severe("The URL was not found, or was malformed. Please fix the webhook URL in the config.");
            return;
        }

        JsonObject a = new JsonObject();
        a.addProperty("content", content);
        a.addProperty("type", 1);
        this.postJSON(url, a);
    }

    /**
     * Used when you want to post with a server tag, generally for bungeecord servers.
     * @param url Discord Webhook URL
     * @param content String message to post to a discord channel.
     * @param serverTag Name of the server that will be appended to the start of the message. Formatted: **[serverTag]** <-- The Asterisks add a bold to the server tag when posted to discord.
     */
    public void postDiscordMessage(URL url, String content, String serverTag)
    {


        content = "**[" + serverTag + "]** " + content;
        this.postDiscordMessage(url, content);
    }

    private SuperDarkCorePlugin plugin_;

    private FileConfiguration superdarkConfig;
}
