package me.lythox.sco.regionmusic.commands;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.lythox.sco.regionmusic.Main;
import me.lythox.sco.regionmusic.utils.Utils;

public class Remove implements CommandExecutor {

    private Main plugin;

    public Remove(Main plugin) {
        this.plugin = plugin;

        plugin.getCommand("/remove").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { // disallow someone to use the command in the console
            sender.sendMessage(Utils.color("&cYou cannot use that command"));
            return false;
        } else if (cmd.getName().equalsIgnoreCase("/remove") && sender.hasPermission("op")) { // only allow if the
                                                                                              // player has op
                                                                                              // permissions
            Player player = (Player) sender;

            if (args.length == 0) { // send a help message if the player didn't provide any arguments
                Utils.msgPlayer(player, "&6==================== &rHelp &6====================",
                        "&6Usage:",
                        "&6//remove <&rName of WorldGuard region&6>");
                return false;
            } else if (args.length > 1) { // send the message if the player provided too many arguments
                Utils.msgPlayer(player, "&cYou used too many arguments");
                return false;
            } else if (plugin.getConfig().contains(args[0])) {
                try { // try remove the region from the config file
                    plugin.getConfig().set(args[0], null);
                    plugin.saveConfig();
                    Utils.msgPlayer(player, "&a" + args[0] + " has been removed");
                    return true;
                } catch (Exception e) {
                    Utils.msgPlayer(player, "&cCould not remove " + args[0]);
                    return false;
                }
            } else {
                Utils.msgPlayer(player, "&c" + args[0] + " does not have any music!");
            }
        }
        return false;
    }
}