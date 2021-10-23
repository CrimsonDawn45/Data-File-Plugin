package me.crimsondawn45.datafileplugin.event;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.crimsondawn45.datafileplugin.DataFile;
import me.crimsondawn45.datafileplugin.DataFilePlugin;

public class PlayerQuit implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {

        //Grab data
        DataFile dataFileSettingsFile = DataFilePlugin.getDataFileSettings();
        FileConfiguration dataFileSettingsData = dataFileSettingsFile.getConfig();
        FileConfiguration playerData = DataFilePlugin.getPlayerData().getConfig();

        //Grab player
        Player player = event.getPlayer();
        String playerName = DataFilePlugin.getPlayerName(player);

        //Grab Strings
        String leaveMsg;
        String leaveMsgFormat;

        //Save leave time
        playerData.set("player." + player.getUniqueId().toString() + ".seen", System.currentTimeMillis());
        DataFilePlugin.getPlayerData().save(playerData);

        //Ensure message is from file
        if(dataFileSettingsData.contains("leave-message-format")) {
            leaveMsgFormat = dataFileSettingsData.getString("leave-message-format");
        } else {
            dataFileSettingsData.set("leave-message-format", "&e%player% left the game.&r");
            dataFileSettingsFile.save(dataFileSettingsData);
            leaveMsgFormat = dataFileSettingsData.getString("leave-message-format");
        }

        //Generate leave message
        leaveMsg = ChatColor.translateAlternateColorCodes('&', leaveMsgFormat).replace("%player%", playerName);

        //Set leave message
        event.setQuitMessage(leaveMsg);
    }
}