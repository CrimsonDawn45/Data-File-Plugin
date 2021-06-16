package me.crimsondawn45.datafileplugin.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.crimsondawn45.datafileplugin.DataFile;
import me.crimsondawn45.datafileplugin.DataFilePlugin;

public class DataFileTab implements TabCompleter {

    List<String> arguments = new ArrayList<String>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(arguments.isEmpty()) {
            arguments.add("list");
            arguments.add("get");
            arguments.add("set");
        }
        List<String> result = new ArrayList<>();

        //Handle First Argument
        if(args.length == 1) {
            for(String entry : arguments) {
                if(entry.startsWith(args[0].toLowerCase())) {
                    result.add(entry);
                }
            }
            return result;
        }

        //Handle Second Argument
        if(args.length == 2) {

            //Make sure command needs second argument
            if(args[0].toLowerCase() != "list") {

                //Return list of datafiles
                for(DataFile dataFile : DataFilePlugin.getDataIndex()) {

                    if(dataFile.getName().startsWith(args[1].toLowerCase())) {
                        result.add(dataFile.getName());
                    }
                }
                return result;
            }
        }

        return null;
    }  
}
