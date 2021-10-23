package me.crimsondawn45.datafileplugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.crimsondawn45.datafileplugin.command.DataFileCommand;
import me.crimsondawn45.datafileplugin.command.DataFileTab;
import me.crimsondawn45.datafileplugin.event.PlayerJoin;
import me.crimsondawn45.datafileplugin.event.PlayerQuit;

public class DataFilePlugin extends JavaPlugin {

    private static List<DataFile> datafile_index;
    private static DataFile player_data;
    private static DataFile datafile_settings;

    @Override
    public void onEnable() {

        //Initialize DataFile Index
        datafile_index = new ArrayList<DataFile>();

        //Initialize Playerdata DataFile
        datafile_settings = new DataFile("datafile_settings", this);
        player_data = new DataFile("player_data", this);

        //Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);

        //Register Commands
        this.getCommand("datafile").setExecutor(new DataFileCommand());
        //this.getCommand("seen").setExecutor(new SeenCommand(this));

        //Register Tab Completion
        this.getCommand("datafile").setTabCompleter(new DataFileTab());
    }

    @Override
    public void onDisable() {

    }

    /**
     * @param name - name of the DataFile
     * 
     * @return whether or not file is contained in the data index.
     */
    public static boolean containsDataFile(String name) {

        String pName = name.toLowerCase().trim().replace(" ", "_");

        for(DataFile dataFile : datafile_index) {
            if(dataFile.getName().equals(pName)) {
                return true;
            }
        }
        return false;
    }

    public static DataFile getDataFile(String name) {
        String pName = name.toLowerCase().trim().replace(" ", "_");

        for(DataFile dataFile : datafile_index) {
            if(dataFile.getName().equals(pName)) {
                return dataFile;
            }
        }
        return null;
    }

    /**
     * @return an array of all existing DataFiles
     */
    public static DataFile[] getDataIndex() {
        DataFile[] result = new DataFile[datafile_index.size()];
        datafile_index.toArray(result);
        return result;
    }

    /**
     * @return a datafile for storing player information.
     */
    public static DataFile getPlayerData() {
        return player_data;
    }

    /**
     * Grab dataFile's settings
     * @return
     */
    public static DataFile getDataFileSettings() {
        return datafile_settings;
    }

    /**
     * Grabs a player's name using player instance
     * @param player player
     * @return name
     */
    public static String getPlayerName(Player player) {
        //Grab Data
        FileConfiguration playerData = DataFilePlugin.getPlayerData().getConfig();

        if(playerData.contains("player." + player.getUniqueId().toString() + ".name")) {
            return playerData.getString("player." + player.getUniqueId().toString() + ".name");
        } else {
            playerData.set("player." + player.getUniqueId().toString() + ".name", player.getName());
            DataFilePlugin.getPlayerData().save(playerData);
            return playerData.getString("player." + player.getUniqueId().toString() + ".name");
        }
    }

    /**
     * Grabs a player's name using a offlinePlayer instance
     * @param player player
     * @return name
     */
    public static String getPlayerName(OfflinePlayer player) {
        //Grab Data
        FileConfiguration playerData = DataFilePlugin.getPlayerData().getConfig();
        String uuidString = player.getUniqueId().toString();

        if(playerData.contains("player." + uuidString + ".name")) {
            return playerData.getString("player." + uuidString + ".name");
        } else {
            playerData.set("player." + uuidString + ".name", player.getName());
            DataFilePlugin.getPlayerData().save(playerData);
            return playerData.getString("player." + uuidString + ".name");
        }
    }

    /**
     * Grabs a player's name using UUID and a plugin instance
     * @param plugin JavaPlugin
     * @param uuid  UUID
     * @return name
     */
    public static String getPlayerName(JavaPlugin plugin, UUID uuid) {

        //Grab players
        Set<Player> onlinePlayers = Set.copyOf(plugin.getServer().getOnlinePlayers());
        OfflinePlayer[] offlinePlayers = plugin.getServer().getOfflinePlayers();

        for(Player onlinePlayer : onlinePlayers) {
            if(onlinePlayer.getUniqueId().equals(uuid)) {
                return getPlayerName(onlinePlayer);
            }
        }

        for(OfflinePlayer offlinePlayer : offlinePlayers) {
            if(offlinePlayer.getUniqueId().equals(uuid)) {
                return getPlayerName(offlinePlayer);
            }
        }

        return "null";
    }

    /**
     * Tells you if a player is a floodgaate player or not
     * 
     * @param player player
     * @return if they are a floodgate player
     */
    public static boolean isFloodgatePlayer(Player player) {
        if(player_data.contains("player." + player.getUniqueId() + ".is-floodgate")) {
            return player_data.getConfig().getBoolean("player." + player.getUniqueId() + ".is-floodgate");
        }
        return false;
    }

    /**
     * Tells you if a player is a floodgaate player or not
     * 
     * @param player player
     * @return if they are a floodgate player
     */
    public static boolean isFloodgatePlayer(UUID uuid) {
        if(player_data.contains("player." + uuid + ".is-floodgate")) {
            return player_data.getConfig().getBoolean("player." + uuid + ".is-floodgate");
        }
        return false;
    }

    /**
     * Tells you if a player is a floodgaate player or not
     * 
     * @param player player
     * @return if they are a floodgate player
     */
    public static boolean isFloodgatePlayer(OfflinePlayer player) {
        if(player_data.contains("player." + player.getUniqueId() + ".is-floodgate")) {
            return player_data.getConfig().getBoolean("player." + player.getUniqueId() + ".is-floodgate");
        }
        return false;
    }

    protected static void registerDataFile(DataFile dataFile) {
        datafile_index.add(dataFile);
    }

    protected static void deRegisterDataFile(DataFile dataFile) {
        datafile_index.remove(dataFile);
    }
}