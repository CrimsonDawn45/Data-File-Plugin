package me.crimsondawn45.datafileplugin.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.crimsondawn45.datafileplugin.DataFile;
import me.crimsondawn45.datafileplugin.DataFilePlugin;

public class DataFileCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {

            Player player = (Player) sender;

            if(player.isOp() || player.hasPermission("datafile.edit")) {
                handleFirstOption(sender, command, label, args);
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "You must be an operator to use this command!");
                return true;
            }
        } else {
            handleFirstOption(sender, command, label, args);
            return true;
        }
    }

    private void handleFirstOption(CommandSender sender, Command command, String label, String[] args) {

        if(args.length > 0) {
            String option = args[0].toLowerCase();

            switch(option) {

                case("list"):
                    list(sender);
                    break;

                case("get"):
                    get(sender, args);
                    break;

                case("set"):
                    set(sender, args);
                    break;
                
                default:
                    sender.sendMessage(ChatColor.RED + "Invalid option please use \"list\", \"get\", or \"set\"");
                    break;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid option please use \"list\", \"get\", or \"set\"");
        }
    }

    private void list(CommandSender sender) {

        String result = "";
        DataFile[] index = DataFilePlugin.getDataIndex();

        for(int i = 0; i < index.length; i++) {

            if(i == 0) {
                result = index[i].getName();
                continue;
            }

            result = result + "\n" + index[i].getName();
        }

        sender.sendMessage(ChatColor.GREEN + result);
    }

    private void  get(CommandSender sender, String[] args) {

        if(args.length > 1) {
            String dataFileName = args[1];

            if(DataFilePlugin.containsDataFile(dataFileName)) {
                DataFile dataFile = DataFilePlugin.getDataFile(dataFileName);
                FileConfiguration config = dataFile.getConfig();

                if(args.length > 2) {
                    String valuePath = args[2].toLowerCase();

                    if(config.contains(valuePath)) {
                        sender.sendMessage(dataFileName + ":" + valuePath + " = " + config.get(valuePath).toString());
                    } else {
                        sender.sendMessage(ChatColor.RED + "No such value \"" + valuePath + "\"!");
                    }

                } else {
                    sender.sendMessage(ChatColor.RED + "Missing value path! Usage: /datafile [list | get | set] <datafile> <path> <value>" );
                }
                
            } else {
                sender.sendMessage(ChatColor.RED + "No such datafile \"" + dataFileName + "\"!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Missing datafile name! Usage: /datafile [list | get | set] <datafile> <path> <value>" );
        }
    }

    private void  set(CommandSender sender, String[] args) {

        if(args.length > 1) {
            String dataFileName = args[1];

            if(DataFilePlugin.containsDataFile(dataFileName)) {
                DataFile dataFile = DataFilePlugin.getDataFile(dataFileName);
                FileConfiguration config = dataFile.getConfig();

                if(args.length > 2) {
                    String valuePath = args[2].toLowerCase();

                    if(args.length > 3) {
                        String value = "";

                        for(int i = 3; i < args.length; i++) {
                            value = value + args[i];
                        }

                        //Set Value
                        config.set(valuePath, value);

                        //Save to file
                        dataFile.save(config);

                        sender.sendMessage(ChatColor.GREEN + "Set value \"" + dataFileName + ":" + valuePath + "\" to " + value);

                    } else {
                        sender.sendMessage(ChatColor.RED + "Missing value!  Usage: /datafile [list | get | set] <datafile> <path> <value>");
                    }

                } else {
                    sender.sendMessage(ChatColor.RED + "Missing value path! Usage: /datafile [list | get | set] <datafile> <path> <value>" );
                }
                
            } else {
                sender.sendMessage(ChatColor.RED + "No such datafile \"" + dataFileName + "\"!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Missing datafile name! Usage: /datafile [list | get | set] <datafile> <path> <value>" );
        }
    }
}