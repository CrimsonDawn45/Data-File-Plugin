package me.crimsondawn45.datafileplugin.event;

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

        DataFile playerDataFile = DataFilePlugin.getPlayerData();
        FileConfiguration config = playerDataFile.getConfig();
        Player player = event.getPlayer();
        String playerUuid = player.getUniqueId().toString();

        config.set("player." + playerUuid + ".seen", System.currentTimeMillis());

        playerDataFile.save(config);
    }
}