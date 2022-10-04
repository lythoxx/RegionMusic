package me.lythox.sco.regionmusic.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.lythox.sco.regionmusic.Main;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import net.raidstone.wgevents.events.RegionLeftEvent;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class RegionEvents implements Listener {

    public int musicTask;

    private Main plugin;

    public RegionEvents(Main plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRegionEnter(RegionEnteredEvent e) {

        Player player = e.getPlayer();
        String region = e.getRegionName();
        String sound = plugin.getConfig().getString(region);

        if (plugin.getConfig().contains(region)) {

            // start a repeating task to play the set song in the region the player is in
            musicTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
                public void run() {
                    // player.playSound(player.getLocation(), sound, 0.0875F, 1.0F);
                    player.playSound(player.getLocation(), sound, SoundCategory.MASTER, 0.25F, 1.0F);
                }
            }, 20L, 6_000L);

        }

    }

    @EventHandler
    public void onRegionLeave(RegionLeftEvent e) {

        // cancel the task and stop the playing music when the player leaves the region
        Bukkit.getServer().getScheduler().cancelTask(musicTask);
        Player player = e.getPlayer();
        String region = e.getRegionName();
        String sound = plugin.getConfig().getString(region);
        if (plugin.getConfig().contains(region)) {
            player.stopSound(sound, SoundCategory.MASTER);
        }
    }

}