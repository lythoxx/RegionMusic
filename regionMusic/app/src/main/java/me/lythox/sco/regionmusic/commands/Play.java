package me.lythox.sco.regionmusic.commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.lythox.sco.regionmusic.Main;
import me.lythox.sco.regionmusic.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Play implements CommandExecutor {
    private List<String> songs;
    private Main plugin;

    public Play(Main plugin) {
        this.songs = plugin.getConfig().getStringList("songs");
        this.plugin = plugin;

        plugin.getCommand("/play").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {

            sender.sendMessage(Utils.color("&cYou cannot use that command"));
            return false;

        } else if (cmd.getName().equalsIgnoreCase("/play") && sender.hasPermission("op")) {

            Player player = (Player) sender;

            if (args.length == 0) { // send a help message if the player didn't provide arguments

                Utils.msgPlayer(player, "&6==================== &rHelp &6====================",
                        "&6Usage:",
                        "&6//play <&rName of WorldGuard region&6> <&rName of song&6>");
                return false;

            } else if (args.length > 2) { // send a message if the player provided too many arguments

                Utils.msgPlayer(player, "&cYou used too many arguments");
                Utils.msgPlayer(player, "&6==================== &rHelp &6====================",
                        "&6Usage:",
                        "&6//play <&rName of WorldGuard region&6> <&rName of song&6>");
                return false;

            } else if (args.length == 1) { // send a message if the player provided too few arguments

                Utils.msgPlayer(player, "&cYou used too few arguments");
                Utils.msgPlayer(player, "&6==================== &rHelp &6====================",
                        "&6Usage:",
                        "&6//play <&rName of WorldGuard region&6> <&rName of song&6>");
                return false;

            } else {
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                World world = BukkitAdapter.adapt(player.getWorld());
                RegionManager regions = container.get(world); // get all regions in the world
                if (regions.hasRegion(args[0])) {
                    ProtectedRegion region = regions.getRegion(args[0]);
                    String regionName = region.getId();
                    String sound;
                    if (songs.contains(args[1])) {
                        for (int i = 1; i <= songs.size(); i++) {
                            if (args[1].toLowerCase().equalsIgnoreCase(songs.get(i - 1))) {
                                if (i < 10) { // Because of the numbering in the resource pack the "0" must be added
                                              // manually
                                    sound = "sao:music.0" + i + args[1].toLowerCase();
                                } else {
                                    sound = "sao:music." + i + args[1].toLowerCase();
                                }
                                plugin.getConfig().set(regionName, sound);
                                plugin.saveConfig();
                                Utils.msgPlayer(player,
                                        "&a\"" + args[1].toLowerCase() + "\" has been added to the region",
                                        "&aIt will now play when a player enters the region");
                            }
                        }
                    } else {
                        Utils.msgPlayer(player, "&cPlease select a song that is in the resource pack!",
                                "&7Available songs are:");
                        for (String song : songs) {
                            TextComponent message = new TextComponent(
                                    ">> " + song);
                            message.setColor(ChatColor.GOLD);
                            message.setClickEvent(
                                    new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                            "//play " + args[0] + " " + song));
                            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new Text(ChatColor.GRAY + "Click to select the song")));
                            player.spigot().sendMessage(message);
                        }
                    }
                    return true;
                } else {
                    Utils.msgPlayer(player, "&cThe region does not exist");
                    return false;
                }

            }

        }
        return false;
    }
}
