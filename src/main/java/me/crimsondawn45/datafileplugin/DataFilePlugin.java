package me.crimsondawn45.datafileplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import me.crimsondawn45.datafileplugin.command.DataFileCommand;
import me.crimsondawn45.datafileplugin.event.PlayerJoin;
import me.crimsondawn45.datafileplugin.event.PlayerQuit;

public class DataFilePlugin extends JavaPlugin {

    private static List<DataFile> datafile_index;
    private static DataFile player_data;

    @Override
    public void onEnable() {

        //Initialize DataFile Index
        datafile_index = new ArrayList<DataFile>();

        //Initialize Playerdata DataFile
        player_data = new DataFile("player_data", this);

        //Register Events
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);

        //Register Commands
        this.getCommand("datafile").setExecutor(new DataFileCommand());
        //this.getCommand("seen").setExecutor(new SeenCommand(this));

        //Register Tab Completion
        //this.getCommand("datafile").setTabCompleter(new DataFileTab());
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

    protected static void registerDataFile(DataFile dataFile) {
        datafile_index.add(dataFile);
    }

    protected static void deRegisterDataFile(DataFile dataFile) {
        datafile_index.remove(dataFile);
    }
}