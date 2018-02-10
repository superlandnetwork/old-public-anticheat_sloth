package com.hydrapvp.sloth.config.manager;

import com.hydrapvp.sloth.utils.Logger;
import com.hydrapvp.sloth.text.Text;
import com.hydrapvp.sloth.config.values.core.FlySetBack;
import com.hydrapvp.sloth.config.values.core.PhaseSetBack;
import com.hydrapvp.sloth.config.values.autoban.AutoBanVL;
import com.hydrapvp.sloth.config.values.core.CoolDownDelay;
import com.hydrapvp.sloth.config.values.message.HoverText;
import com.hydrapvp.sloth.config.values.message.ClickCommand;
import com.hydrapvp.sloth.config.values.message.BroadcastMessage;
import com.hydrapvp.sloth.config.values.core.TPSDisable;
import com.hydrapvp.sloth.config.values.core.NotifyDelayMS;
import com.hydrapvp.sloth.config.values.autoban.AutoBanNotifyPlayers;
import com.hydrapvp.sloth.config.values.autoban.AutoBanMessage;
import com.hydrapvp.sloth.config.values.autoban.AutoBanEnabled;
import com.hydrapvp.sloth.config.values.autoban.AutoBanDelay;
import com.hydrapvp.sloth.config.values.autoban.AutoBanCommand;
import com.hydrapvp.sloth.config.values.autoban.AutoBanBroadcastMessage;
import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.SlothPlugin;
import java.util.ArrayList;
import com.hydrapvp.sloth.api.API;
import com.hydrapvp.sloth.config.Config;
import java.util.List;

public class Configuration
{
    private List<Config> config;
    private API api;
    
    public Configuration() {
        this.config = new ArrayList<Config>();
        this.api = SlothPlugin.getAPI();
    }
    
    public void init() {
        this.api = SlothPlugin.getAPI();
        for (final Check check : this.api.getCheckManager().getChecks()) {
            for (final Config config : check.getConfig()) {
                this.addConfig(config);
            }
        }
        this.addConfig(new AutoBanBroadcastMessage());
        this.addConfig(new AutoBanCommand());
        this.addConfig(new AutoBanDelay());
        this.addConfig(new AutoBanEnabled());
        this.addConfig(new AutoBanMessage());
        this.addConfig(new AutoBanNotifyPlayers());
        this.addConfig(new NotifyDelayMS());
        this.addConfig(new TPSDisable());
        this.addConfig(new BroadcastMessage());
        this.addConfig(new ClickCommand());
        this.addConfig(new HoverText());
        this.addConfig(new CoolDownDelay());
        this.addConfig(new AutoBanVL());
        this.addConfig(new PhaseSetBack());
        this.addConfig(new FlySetBack());
        for (final Config c : this.config) {
            this.api.getPlugin().getConfig().addDefault(c.getRoot() + "." + c.getName(), c.getValue());
        }
        for (final Text t : this.api.getTextManager().getText()) {
            this.api.getPlugin().getConfig().addDefault("text." + t.getRoot() + "." + t.getName(), (Object)t.getText());
        }
        this.api.getPlugin().getConfig().options().copyDefaults(true);
        this.api.getPlugin().saveConfig();
        for (final Config c : this.config) {
            if (c.getRoot().startsWith("checks.")) {
                for (final Check check2 : this.api.getCheckManager().getChecks()) {
                    for (final Config config2 : check2.getConfig()) {
                        if ((config2.getRoot() + "." + config2.getName()).equalsIgnoreCase(c.getRoot() + "." + c.getName())) {
                            config2.updateValue(this.readConfig(c.getRoot() + "." + c.getName()));
                        }
                    }
                }
            }
            else {
                c.updateValue(this.readConfig(c.getRoot() + "." + c.getName()));
            }
        }
        for (final Text text : this.api.getTextManager().getText()) {
            text.setText(this.readConfig("text." + text.getRoot() + "." + text.getName()).toString());
        }
    }
    
    public void reload() {
        for (final Config c : this.config) {
            if (c.getRoot().startsWith("checks.")) {
                for (final Check check : this.api.getCheckManager().getChecks()) {
                    for (final Config config : check.getConfig()) {
                        if ((config.getRoot() + "." + config.getName()).equalsIgnoreCase(c.getRoot() + "." + c.getName())) {
                            config.updateValue(this.readConfig(c.getRoot() + "." + c.getName()));
                        }
                    }
                }
            }
            else if (c.getRoot().startsWith("text.")) {
                for (final Text t : this.api.getTextManager().getText()) {
                    if ((t.getRoot() + "." + t.getName()).equalsIgnoreCase(c.getRoot().replace("text.", "") + c.getName())) {
                        t.setText(this.readConfig(c.getRoot() + "." + c.getName()));
                    }
                }
            }
            else {
                c.updateValue(this.readConfig(c.getRoot() + "." + c.getName()));
            }
        }
    }
    
    private String readConfig(final String path) {
        return this.api.getPlugin().getConfig().getString(path);
    }
    
    public void destroy() {
    }
    
    private List<Config> getConfiguration() {
        return this.config;
    }
    
    private void addConfig(final Config e) {
        this.config.add(e);
    }
    
    public void writeValue(final String path, final Object value) {
        for (final Config c : this.getConfiguration()) {
            if (path.equalsIgnoreCase(c.getRoot() + "." + c.getName())) {
                c.updateValue(value);
            }
        }
    }
    
    private Object readValue(final String path) {
        for (final Config c : this.getConfiguration()) {
            if (path.equalsIgnoreCase(c.getRoot() + "." + c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
    
    public String readString(final String path) {
        if (this.readValue(path) != null) {
            return this.readValue(path).toString();
        }
        Logger.log("[ERROR] Severe problem with config. Disabling...");
        this.api.setPluginEnabled(false);
        return null;
    }
    
    public int readInteger(final String path) {
        return Integer.parseInt(this.readString(path));
    }
    
    public float readFloat(final String path) {
        return Float.parseFloat(this.readString(path));
    }
    
    public double readDouble(final String path) {
        return Double.parseDouble(this.readString(path));
    }
    
    public boolean readBoolean(final String path) {
        return Boolean.parseBoolean(this.readString(path));
    }
}