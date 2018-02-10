package com.hydrapvp.sloth.data.manager;

import java.util.HashMap;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.hydrapvp.sloth.SlothPlugin;
import com.hydrapvp.sloth.api.API;
import com.hydrapvp.sloth.data.PlayerData;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class DataManager
{
    private HashMap<Player, PlayerData> playerDatas;
    
    public DataManager() {
        this.playerDatas = new HashMap<Player, PlayerData>();
    }
    
    public PlayerData getPlayerDataByPlayer(final Player player) {
        if (!this.playerDatas.containsKey(player)) {
            this.addPlayer(player);
        }
        return this.playerDatas.get(player);
    }
    
    private void addPlayer(final Player player) {
        this.playerDatas.put(player, new PlayerData(player));
    }
    
    public HashMap<Player, PlayerData> getPlayers() {
        return this.playerDatas;
    }
    
    private String color(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public void legacyNotifyCheck(final PlayerData player) {
        if (player == null) {
            return;
        }
        if (player.getDetectedHack() == null) {
            return;
        }
        if (player.getDetectedHackData() == null) {
            return;
        }
        final API api = SlothPlugin.getAPI();
        final ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getPlayer().getName());
        final HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.color("&cCPS: &8" + player.getCPS() + "\n&cHits: &8" + player.getHits() + "\n&cMisses: &8" + player.getMisses() + "\n&aLag: &8" + this.getPing(player))).create());
        final ChatColor lbracket = ChatColor.DARK_GRAY;
        final ChatColor sloth = ChatColor.RED;
        final ChatColor rbracket = ChatColor.DARK_GRAY;
        final ChatColor playername = ChatColor.RED;
        final ChatColor mightbeusing = ChatColor.GRAY;
        final ChatColor hackname = ChatColor.RED;
        final ChatColor lparenthesis = ChatColor.RED;
        final ChatColor hackdata = ChatColor.RED;
        final ChatColor rparenthesis = ChatColor.RED;
        for (final Player p : api.getPlugin().getServer().getOnlinePlayers()) {
            try {
                if (!p.hasPermission("sloth.notify") && !p.isOp()) {
                    continue;
                }
                p.spigot().sendMessage(new ComponentBuilder("[").event(ce).color(lbracket).append("Sloth").event(ce).color(sloth).bold(true).append("] ").event(ce).bold(false).color(rbracket).append(player.getPlayer().getName()).event(ce).event(he).color(playername).append(" might be using ").reset().event(ce).color(mightbeusing).append(player.getDetectedHack()).event(ce).color(hackname).append(" (").event(ce).color(lparenthesis).append(player.getDetectedHackData()).event(ce).color(hackdata).append(")").event(ce).color(rparenthesis).create());
            }
            catch (Exception ex) {}
        }
    }
    
    public void notifyCheck(final PlayerData player) {
        final API api = SlothPlugin.getAPI();
        final String[] broadcastMessage = api.getConfiguration().readString("Message.BroadcastMessage").split(" ");
        String hoverText = api.getConfiguration().readString("Message.HoverText");
        hoverText = hoverText.replace("%player%", player.getPlayer().getName());
        hoverText = hoverText.replace("%cps%", player.getCPS() + "");
        hoverText = hoverText.replace("%hits%", player.getHits() + "");
        hoverText = hoverText.replace("%misses%", player.getMisses() + "");
        hoverText = hoverText.replace("%ping%", this.getPing(player) + "");
        hoverText = hoverText.replace("\\n", "\n");
        hoverText = this.color(hoverText);
        final ClickEvent ce = new ClickEvent(ClickEvent.Action.RUN_COMMAND, api.getConfiguration().readString("Message.ClickCommand").replace("%player%", player.getPlayer().getName()));
        final HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create());
        final ComponentBuilder formattedMessage = new ComponentBuilder("");
        String lastColor = this.color("&f");
        for (String s : broadcastMessage) {
            if (s.indexOf("&") > -1) {
                lastColor = s.substring(s.indexOf("&"), s.indexOf("&") + 2);
                s = s.replace(lastColor, "");
            }
            String newS = "";
            for (final String s2 : s.split("")) {
                newS = newS + this.color(lastColor) + s2;
            }
            s = newS;
            s = s.replace("%player%", player.getPlayer().getName());
            s = s.replace("%hackname%", player.getDetectedHack());
            s = s.replace("%hackdata%", player.getDetectedHackData());
            s = this.color(s);
            formattedMessage.append(s.replace("%c%", "").replace("%h%", "") + " ");
            formattedMessage.reset();
            if (s.contains("%c%")) {
                formattedMessage.event(ce);
                s = s.replace("%c%", "");
            }
            if (s.contains("%h%")) {
                formattedMessage.event(he);
                s = s.replace("%h%", "");
            }
        }
        final BaseComponent[] message = formattedMessage.create();
        for (final Player p : api.getPlugin().getServer().getOnlinePlayers()) {
            if (p.hasPermission("sloth.notify") || p.isOp()) {
                p.spigot().sendMessage(message);
            }
        }
    }
    
    private int getPing(final PlayerData player) {
        try {
            if (SlothPlugin.getAPI().getServerVersion() == 1.8) {
                final int ping = ((CraftPlayer)player.getPlayer()).getHandle().ping;
                return ping;
            }
            try {
                final int ping = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player.getPlayer()).getHandle().ping;
                return ping;
            }
            catch (NoClassDefFoundError er) {
                return 0;
            }
        }
        catch (Exception e) {
            return 0;
        }
    }
    
    public void removePlayer(final Player player) {
        this.playerDatas.remove(player);
    }
}
