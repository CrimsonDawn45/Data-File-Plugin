package me.crimsondawn45.datafileplugin.event;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.crimsondawn45.datafileplugin.DataFile;
import me.crimsondawn45.datafileplugin.DataFilePlugin;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {

        DataFile playerDataFile = DataFilePlugin.getPlayerData();
        FileConfiguration config = playerDataFile.getConfig();
        Player player = event.getPlayer();
        String playerUuid = player.getUniqueId().toString();

        if(!config.contains("player." + playerUuid)) {
            config.set("player." + playerUuid + ".name", player.getDisplayName());
            config.set("player." + playerUuid + ".joined", System.currentTimeMillis());

            playerDataFile.save(config);
        }
    }
}