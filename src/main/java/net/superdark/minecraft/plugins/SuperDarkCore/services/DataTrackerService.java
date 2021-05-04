package net.superdark.minecraft.plugins.SuperDarkCore.services;

import net.superdark.minecraft.plugins.SuperDarkCore.SuperDarkCorePlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The current plan for the DataTrackApi is to have each player's data be in a YAML file where the name of the file is the player's Minecraft UUID.
 *
 */
public class DataTrackerService {

    private SuperDarkCorePlugin superDarkCorePlugin_;

    private HashMap<UUID, PlayerDataObject> toFlush = new LinkedHashMap<>();

    public DataTrackerService(SuperDarkCorePlugin instance)
    {
        this.superDarkCorePlugin_ = instance;
        this.flushTask();
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
        //if the object is already about to be flushed, get it and remove it.
        if (toFlush.containsKey(uuid))
        {
            PlayerDataObject obj = toFlush.get(uuid);
            toFlush.remove(uuid);
            return obj;
        }

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
        File pathToFile = getPlayerFile(uuid);
        FileConfiguration config = YamlConfiguration.loadConfiguration(pathToFile); // Remember, a blank config is returned if the yml file is malformed.
        config.createSection("data", obj.serialize()); //Any current data wll be overwritten.
        try {
            config.save(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush()
    {
        for (Map.Entry<UUID, PlayerDataObject> obj : this.toFlush.entrySet())
        {
            this.save(obj.getValue(), obj.getKey());
        }
    }

    private void flushTask()
    {
        new BukkitRunnable() {
            @Override
            public void run() {
                flush();
                superDarkCorePlugin_.getLogger().info("Flushing PlayerDataObjects.");
                this.runTaskLater(superDarkCorePlugin_, 6000);
            }
        }.runTaskLater(superDarkCorePlugin_, 6000); //6000 ticks = 20(ticks per second) * 60 (seconds per minute) * 5 (minutes) =  5 minutes of delay
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

        public void saveToFlush(UUID playerUUID)
        {
            toFlush.put(playerUUID, this);
        }

        //===== Getting =====//

        public int getPlayerKills(UUID uuid) {
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

        //===== Setting =====//

        public void setPlayerKills(UUID playerUUID,int amount) {
            this.playerKills = amount;
            this.saveToFlush(playerUUID);
        }

        public void setPlayerDeaths(UUID playerUUID,int amount) {
            this.playerDeaths = amount;
            this.saveToFlush(playerUUID);
        }

        public void setBlocksBroken(UUID playerUUID,int amount) {
            this.blocksBroken = amount;
            this.saveToFlush(playerUUID);
        }

        public void setSpawnersMined(UUID playerUUID,int amount) {
            this.spawnersMined = amount;
            this.saveToFlush(playerUUID);
        }

        //===== Adding =====//

        public void addPlayerKills(UUID playerUUID, int amount) {
            this.playerKills += amount;
            this.saveToFlush(playerUUID);
        }

        public void addPlayerDeaths(UUID playerUUID, int amount) {
            this.playerDeaths += amount;
            this.saveToFlush(playerUUID);
        }

        public void addBlocksBroken(UUID playerUUID, int amount) {
            this.blocksBroken += amount;
            this.saveToFlush(playerUUID);
        }

        public void addSpawnersMined(UUID playerUUID, int amount) {
            this.spawnersMined += amount;
            this.saveToFlush(playerUUID);
        }
    }

}
