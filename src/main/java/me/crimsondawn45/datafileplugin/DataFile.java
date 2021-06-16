package me.crimsondawn45.datafileplugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class DataFile {

    private String name;
    private JavaPlugin plugin;
    private FileConfiguration configData = null;
    private File configFile = null;

    /**
     * NOTE: PLEASE CHECK IF A DATAFILE EXISTS BEFORE TRYING TO MAKE ONE WITH THE SAME NAME
     * 
     * @param name - name of DataFile
     * @param plugin - instance of your plugin
     */
    public DataFile(String name, JavaPlugin plugin) {
        this.name = name.toLowerCase().replace(" ", "_");
        this.plugin = plugin;
        DataFilePlugin.registerDataFile(this);
        reload();
    }
    
    public void reload() {
        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), this.name + ".yml");
        }
        this.configData = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public FileConfiguration getConfig() {
        //Reload if there are changes
        if(this.configData != YamlConfiguration.loadConfiguration(this.configFile)) {
            reload();
        }
        return this.configData;
    }
    /**
     * @param config - Configuration data to be saved.
     */
    public void save(FileConfiguration config) {
        try {
            config.save(this.configFile);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "\"" + this.name +".yml\" failed to save!", e);
        }
    }

    /**
     * @return whether or not file was successfully deleted.
     */
    public boolean delete() {
        try {
            DataFilePlugin.deRegisterDataFile(this);
            return configFile.delete();
        } catch (SecurityException e) {
            this.plugin.getLogger().log(Level.SEVERE, "\"" + this.name +".yml\" failed to delete!", e);
            return false;
        }
    }

    public String getColoredString(String path) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(path).trim());
    }

    public void set(String path, Object object) {
        FileConfiguration config = getConfig();
        config.set(path, object);
        save(config);
    }

    public boolean contains(String path) {
        return getConfig().contains(path);
    }

    /**
     * @return name of DataFile
     */
    public String getName() {
        return this.name;
    }
}