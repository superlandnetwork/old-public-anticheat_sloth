package com.hydrapvp.sloth.api;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.hydrapvp.sloth.checks.manager.CheckManager;
import com.hydrapvp.sloth.commands.manager.CommandManager;
import com.hydrapvp.sloth.config.manager.Configuration;
import com.hydrapvp.sloth.data.manager.DataManager;
import com.hydrapvp.sloth.events.manager.EventManager;
import com.hydrapvp.sloth.listener.BukkitListener;
import com.hydrapvp.sloth.text.manager.TextManager;
import com.hydrapvp.sloth.utils.Logger;

public class API {
    private boolean pluginEnabled;
    private JavaPlugin plugin;
    private DataManager dataManager;
    private Configuration config;
    private CheckManager checkManager;
    private EventManager eventManager;
    private CommandManager commandManager;
    private TextManager textManager;
    private double serverVersion;
    public boolean isDisabledDueToTPS;
    public double tpsDisableValue;
    public long lastTPSBroadcastTime;
    
    public API(final JavaPlugin plugin) {
        this.pluginEnabled = false;
        this.plugin = plugin;
    }
    
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    public void init() {
        Logger.log("Initializing Event Manager...");
        (this.eventManager = new EventManager()).init();
        Logger.log("Initializing Data Manager...");
        this.dataManager = new DataManager();
        Logger.log("Initializing Check Manager...");
        (this.checkManager = new CheckManager()).init();
        Logger.log("Initializing Command Manager...");
        (this.commandManager = new CommandManager()).init();
        Logger.log("Initializing Text Manager...");
        (this.textManager = new TextManager()).init();
        Logger.log("Initializing Configuration...");
        (this.config = new Configuration()).init();
        Logger.log("Detecting server version...");
        this.serverVersion = 1.8;
        Logger.log("Detected version: " + this.serverVersion);
        Logger.log("Initializing player listeners...");
        this.getPlugin().getServer().getPluginManager().registerEvents((Listener)new BukkitListener(), (Plugin)this.getPlugin());
        Logger.log("Enabling Sloth...");
        this.setPluginEnabled(true);
        Logger.log("Sloth was enabled.");
    }
    
    public void destroy() {
    }
    
    public void setPluginEnabled(final boolean pluginEnabled) {
        if (pluginEnabled) {
            this.getDataManager().getPlayers().clear();
        }
        this.pluginEnabled = pluginEnabled;
    }
    
    public boolean isPluginEnabled() {
        return this.pluginEnabled;
    }
    
    public DataManager getDataManager() {
        return this.dataManager;
    }
    
    public CheckManager getCheckManager() {
        return this.checkManager;
    }
    
    public Configuration getConfiguration() {
        return this.config;
    }
    
    public EventManager getEventManager() {
        return this.eventManager;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }
    
    public double getServerVersion() {
        return this.serverVersion;
    }
    
    public TextManager getTextManager() {
        return this.textManager;
    }
}
