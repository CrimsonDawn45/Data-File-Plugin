package me.crimsondawn45.datafileplugin.event;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.geysermc.floodgate.api.FloodgateApi;

import me.crimsondawn45.datafileplugin.DataFile;
import me.crimsondawn45.datafileplugin.DataFilePlugin;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {

        //Grab data
        DataFile dataFileSettingsFile = DataFilePlugin.getDataFileSettings();
        FileConfiguration dataFileSettingsData = dataFileSettingsFile.getConfig();
        FileConfiguration playerData = DataFilePlugin.getPlayerData().getConfig();

        //Grab player
        Player player = event.getPlayer();
        String playerUuid = player.getUniqueId().toString();

        //Grab string
        String joinMsg;
        String joinMsgFormat;

        if(!playerData.contains("player." + playerUuid + ".name")) {    //Ensure name
            playerData.set("player." + playerUuid + ".name", player.getDisplayName());
            DataFilePlugin.getPlayerData().save(playerData);
        }

        if(!playerData.contains("player." + playerUuid + "joined")) {   //Ensure join date
            playerData.set("player." + playerUuid + ".joined", player.getFirstPlayed());
            DataFilePlugin.getPlayerData().save(playerData);
        }

        if(!playerData.contains("player." + playerUuid + "is-floodgate")) { //Ensure floodgate
            playerData.set("player." + playerUuid + ".is-floodgate", FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId()));
            DataFilePlugin.getPlayerData().save(playerData);
        }

        String playerName = DataFilePlugin.getPlayerName(player);

        if(!playerName.equals(player.getDisplayName())) {   //Make name match file
            player.setDisplayName(playerName);
        }

        //Log if player is a floodgate player


        //Ensure message is from file
        if(dataFileSettingsData.contains("join-message-format")) {
            joinMsgFormat = dataFileSettingsData.getString("join-message-format");
        } else {
            dataFileSettingsData.set("join-message-format", "&e%player% joined the game.&r");
            dataFileSettingsFile.save(dataFileSettingsData);
            joinMsgFormat = dataFileSettingsData.getString("join-message-format");
        }

        //Generate join message
        joinMsg = ChatColor.translateAlternateColorCodes('&', joinMsgFormat).replace("%player%", playerName);

        //Set join message
        event.setJoinMessage(joinMsg);
    }
}