package me.lythox.sco.regionmusic;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import me.lythox.sco.regionmusic.commands.Play;
import me.lythox.sco.regionmusic.commands.Remove;
import me.lythox.sco.regionmusic.events.RegionEvents;

public class Main extends JavaPlugin {
    public static Main plugin;
    private static String author;
    private static Logger log;
    private static String version;

    public Main() {
        plugin = this;
        author = "lythox";
        log = this.getLogger();
        version = "1.1.0";
    }

    @Override
    public void onEnable() {
        log.info("Welcome to RegionMusic for SCO");

        // Registring commands and events
        new Play(this);
        new Remove(this);
        new RegionEvents(this);

        // Getting the config.yml
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        log.info("Disabled Region Music");
    }

    public static String getAuthor() {
        return author;
    }

    public static Logger getLog() {
        return log;
    }

    public static String getVersion() {
        return version;
    }
}