package net.superdark.minecraft.plugins.SuperDarkCore.api;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The current plan for the DataTrackApi is to have each player's data be in a YAML file where the name of the file is the player's Minecraft UUID.
 *
 */
public class DataTrackerAPI {

    private SuperDarkCorePlugin superDarkCorePlugin_;

    public DataTrackerAPI(SuperDarkCorePlugin instance)
    {
        this.superDarkCorePlugin_ = instance;
    }

    /**
     * Attempts to create a new file PlayerData.yml if it does not exist in this plugin's plugin directory.
     * @return The file pathname for PlayerData.yml
     */
    private File getPlayerFile(@NotNull UUID uuid) {
        File file = new File(superDarkCorePlugin_.getDataFolder() + "/data/", uuid.toString() + ".yml" );

        try {
            if(!file.exists())
            {
                file.getParentFile().mkdirs();
            }
            if (superDarkCorePlugin_.getResource(uuid.toString() + ".yml") == null)
            {
                file.createNewFile();
            }

        } catch (IOException err) {
            err.printStackTrace();
        }
        return file.getAbsoluteFile();
    }

    /**
     * Gets and builds a PlayerDataObject from from a user's unique yml file.
     * @param uuid UUID associated with a minecraft account.
     * @return PlayerDataObject from data from user's unique yml folder if it exists, else a new blank PlayerDataObject.
     */
    public PlayerDataObject getPlayerData(UUID uuid)
    {
        FileConfiguration config = YamlConfiguration.loadConfiguration(getPlayerFile(uuid));
        ConfigurationSection data =  config.getConfigurationSection("data");
        if (data != null) {
            return new PlayerDataObject(data.getInt("PlayerKills"), data.getInt("PlayerDeaths"), data.getInt("BlocksBroken"), data.getInt("SpawnersMined"));
        } else return new PlayerDataObject();
    }

    /**
     * Saves a PlayerDataObject to a file.
     * @param obj Use DataTrackerAPI.getPlayerData() to get a Player's PlayerDataObject
     * @param uuid A minecraft account's UUID.
     */
    public void save(PlayerDataObject obj, UUID uuid)
    {
        Map<String, Integer> toFile = obj.serialize();
        File pathToFile = getPlayerFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(pathToFile); // Remember, a blank config is returned if the yml file is malformed.
        config.createSection("data", obj.serialize()); //Any current data wll be overwritten.
        try {
            config.save(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






//================================================================//
//                                                                //
//================================================================//


    public class PlayerDataObject
    {
        private int playerKills;
        private int playerDeaths;
        private int blocksBroken;
        private int spawnersMined;

        public PlayerDataObject()
        {
            this(0,0,0,0); // constructor chaining
        }

        public PlayerDataObject(int pk, int pd, int bb, int sm)
        {
            this.playerKills = pk;
            this.playerDeaths = pd;
            this.blocksBroken = bb;
            this.spawnersMined = sm;
        }

        public @NotNull Map<String, Integer> serialize()
        {
            LinkedHashMap<String, Integer> data = new LinkedHashMap<>();
            data.put("PlayerKills", playerKills);
            data.put("PlayerDeaths", playerDeaths);
            data.put("BlocksBroken", blocksBroken);
            data.put("SpawnersMined", spawnersMined);
            return data;
        }

        public int getPlayerKills() {
            return playerKills;
        }

        public int getPlayerDeaths() {
            return playerDeaths;
        }

        public int getBlocksBroken() {
            return blocksBroken;
        }

        public int getSpawnersMined() {
            return spawnersMined;
        }

        public void setPlayerKills(int amount) {
            this.playerKills = amount;
        }

        public void setPlayerDeaths(int amount) {
            this.playerDeaths = amount;
        }

        public void setBlocksBroken(int amount) {
            this.blocksBroken = amount;
        }

        public void setSpawnersMined(int amount) {
            this.spawnersMined = amount;
        }

        public void addPlayerKills(int amount) {
            this.playerKills += amount;
        }

        public void addPlayerDeaths(int amount) {
            this.playerDeaths += amount;
        }

        public void addBlocksBroken(int amount) {
            this.blocksBroken += amount;
        }

        public void addSpawnersMined(int amount) {
            this.spawnersMined += amount;
        }
    }

}
