package me.crimsondawn45.datafileplugin.command;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.crimsondawn45.datafileplugin.DataFilePlugin;

public class SeenCommand implements CommandExecutor {

    private DataFilePlugin plugin;

    public SeenCommand(DataFilePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0) {
            String name = args[0];
            Player player = this.plugin.getServer().getPlayerExact(name);

            if(player == null) {

                OfflinePlayer[] offlinePlayers = this.plugin.getServer().getOfflinePlayers();

                for(OfflinePlayer offlinePlayer : offlinePlayers) {
                    if(offlinePlayer.getName().equals(name)) {
                        Long seenTime = DataFilePlugin.getPlayerData().getConfig().getLong("player." + offlinePlayer.getUniqueId().toString() + ".seen");
                        SimpleDateFormat sdf = new SimpleDateFormat(offlinePlayer.getName() + " was last seen MMM dd, yyyy at HH:mm.");
                        Date seenDate = new Date(seenTime);
                        sender.sendMessage(sdf.format(seenDate).toString());
                        return true;
                    }
                }

                sender.sendMessage(ChatColor.RED + "No such player \"" + name + "\"!");
                return true;

            } else {
                sender.sendMessage("Player \"" + player.getDisplayName() + "\" is currently online!");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Missing player name! Usage: /seen <player>");
            return true;
        }
    }
}