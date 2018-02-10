package com.hydrapvp.sloth.commands;

import com.hydrapvp.sloth.data.PlayerData;
import org.bukkit.entity.Player;
import com.hydrapvp.sloth.api.API;
import java.util.HashMap;
import com.hydrapvp.sloth.utils.StorageUtils;
import org.bukkit.util.Vector;
import org.bukkit.ChatColor;
import com.hydrapvp.sloth.SlothPlugin;
import org.bukkit.command.CommandSender;

public class SlothCommand extends Command
{
    public SlothCommand() {
        super("sloth");
    }
    
    @Override
    public void onCommand(final CommandSender sender, final String[] args) {
        final API api = SlothPlugin.getAPI();
        if (sender.hasPermission("sloth.command") || sender.isOp()) {
            if (args.length > 0) {
                final String subCommand = args[0];
                if (subCommand.equalsIgnoreCase("enable")) {
                    if (!api.isPluginEnabled()) {
                        api.setPluginEnabled(true);
                        sender.sendMessage(api.getTextManager().getString("core.slothenabled"));
                        api.isDisabledDueToTPS = false;
                    }
                    else {
                        sender.sendMessage(api.getTextManager().getString("core.slothalreadyenabled"));
                    }
                }
                if (subCommand.equalsIgnoreCase("disable")) {
                    if (api.isPluginEnabled()) {
                        api.setPluginEnabled(false);
                        sender.sendMessage(api.getTextManager().getString("core.slothdisabled"));
                    }
                    else {
                        sender.sendMessage(api.getTextManager().getString("core.slothalreadydisabled"));
                    }
                }
                if (subCommand.equalsIgnoreCase("stopban") && args.length > 0) {
                    final Player player = api.getPlugin().getServer().getPlayer(args[1]);
                    if (player != null) {
                        final PlayerData pd = api.getDataManager().getPlayerDataByPlayer(player);
                        if (pd.autobanning) {
                            pd.autobanning = false;
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', api.getTextManager().getString("core.SlothBanStoppedBan").replace("%player%", player.getName())));
                        }
                        else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', api.getTextManager().getString("core.SlothBanNotBanning").replace("%player%", player.getName())));
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', api.getTextManager().getString("core.SlothBanNoPlayer").replace("%player%", args[1])));
                    }
                }
                if (subCommand.equalsIgnoreCase("ban")) {
                    if (args.length > 1) {
                        if (sender.hasPermission("sloth.ban")) {
                            final String playerName = args[1];
                            final Player player2 = api.getPlugin().getServer().getPlayer(playerName);
                            if (player2 != null) {
                                sender.sendMessage(this.color("&eInitializing ban for " + playerName) + "...");
                                api.getDataManager().getPlayerDataByPlayer(player2).addBanVL("Manual", 100.0);
                            }
                            else {
                                sender.sendMessage(this.color("&cCould not find player."));
                            }
                        }
                        else {
                            sender.sendMessage(this.color("&cYou do not have permission to ban anyone!"));
                        }
                    }
                    else {
                        sender.sendMessage(this.color("&cUsage: /sloth ban [player]"));
                    }
                }
                if (subCommand.equalsIgnoreCase("testknockback")) {
                    if (args.length > 1) {
                        final String playerName = args[1];
                        final Player player2 = api.getPlugin().getServer().getPlayer(playerName);
                        if (player2 != null) {
                            sender.sendMessage(this.color("&aApplied knockback to " + playerName + ". If they move, they do not have antiknockback."));
                            player2.setVelocity(new Vector(0.0, 0.5, 0.0));
                        }
                        else {
                            sender.sendMessage(this.color("&cCould not find player."));
                        }
                    }
                    else {
                        sender.sendMessage(this.color("&cUsage: /sloth testknockback [player]"));
                    }
                }
                if (subCommand.equalsIgnoreCase("kb")) {
                    if (args.length > 1) {
                        final String playerName = args[1];
                        final Player player2 = api.getPlugin().getServer().getPlayer(playerName);
                        if (player2 != null) {
                            sender.sendMessage(this.color("&aApplied knockback to " + playerName + ". If they move, they do not have antiknockback."));
                            player2.setVelocity(new Vector(0.0, 0.5, 0.0));
                        }
                        else {
                            sender.sendMessage(this.color("&cCould not find player."));
                        }
                    }
                    else {
                        sender.sendMessage(this.color("&cUsage: /sloth kb [player]"));
                    }
                }
                if (subCommand.equalsIgnoreCase("lookup")) {
                    if (args.length > 1) {
                        final String playerCheck = args[1].toLowerCase();
                        final String latestLogs = StorageUtils.getFromFile("plugins/Sloth/Logs/" + playerCheck + "/logs.log");
                        if (latestLogs.equals("-1")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', api.getTextManager().getString("core.NoLogsFound")));
                        }
                        else {
                            sender.sendMessage(this.color("&a&lInformation for " + playerCheck + ":"));
                            final HashMap<String, Integer> flags = new HashMap<String, Integer>();
                            String latestTime = "";
                            for (final String s : latestLogs.split("@")) {
                                latestTime = s.split(",")[0];
                                String hackName = s.split(",")[1];
                                hackName = hackName.replace("an Autoclicker", "Autoclicker");
                                hackName = hackName.replace("Phase/Regen/Speed/Other", "Packet");
                                if (flags.containsKey(hackName)) {
                                    flags.replace(hackName, flags.get(hackName) + 1);
                                }
                                else {
                                    flags.put(hackName, 1);
                                }
                            }
                            String displayed = "";
                            sender.sendMessage(this.color("&7Last flag time: " + latestTime));
                            HashMap<String, Integer> highest = this.getHighestEntry(flags, displayed);
                            int checks = 0;
                            while (highest != null) {
                                ++checks;
                                for (final String s2 : highest.keySet()) {
                                    sender.sendMessage(this.color("&e" + s2 + " [x" + highest.get(s2) + "]"));
                                    displayed += s2;
                                }
                                highest = this.getHighestEntry(flags, displayed);
                                if (checks > 100) {
                                    sender.sendMessage(this.color("&cAn error occurred trying to retrieve check information."));
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        sender.sendMessage(this.color("&cUsage: /sloth lookup [player]"));
                    }
                }
                if (subCommand.equalsIgnoreCase("checklogs")) {
                    if (args.length > 1) {
                        final String playerCheck = args[1].toLowerCase();
                        final String latestLogs = StorageUtils.getFromFile("plugins/Sloth/Logs/" + playerCheck + "/logs.log");
                        if (latestLogs.equals("-1")) {
                            sender.sendMessage(this.color(api.getTextManager().getString("core.NoLogsFound")));
                        }
                        else {
                            sender.sendMessage(this.color("&a&lInformation for " + playerCheck + ":"));
                            final HashMap<String, Integer> flags = new HashMap<String, Integer>();
                            String latestTime = "";
                            for (final String s : latestLogs.split("@")) {
                                latestTime = s.split(",")[0];
                                String hackName = s.split(",")[1];
                                hackName = hackName.replace("an Autoclicker", "Autoclicker");
                                hackName = hackName.replace("Phase/Regen/Speed/Other", "Packet");
                                if (flags.containsKey(hackName)) {
                                    flags.replace(hackName, flags.get(hackName) + 1);
                                }
                                else {
                                    flags.put(hackName, 1);
                                }
                            }
                            String displayed = "";
                            sender.sendMessage(this.color("&7Last flag time: " + latestTime));
                            HashMap<String, Integer> highest = this.getHighestEntry(flags, displayed);
                            int checks = 0;
                            while (highest != null) {
                                ++checks;
                                for (final String s2 : highest.keySet()) {
                                    sender.sendMessage(this.color("&e" + s2 + " [x" + highest.get(s2) + "]"));
                                    displayed += s2;
                                }
                                highest = this.getHighestEntry(flags, displayed);
                                if (checks > 100) {
                                    sender.sendMessage(this.color("&cAn error occurred trying to retrieve check information."));
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        sender.sendMessage(this.color("&cUsage: /sloth checklogs [player]"));
                    }
                }
            }
            else {
                sender.sendMessage(this.color("&cUsages: "));
                sender.sendMessage(this.color("&7enable, disable, lookup, stopban, ban, testknockback"));
            }
        }
        else {
            sender.sendMessage(this.color(api.getTextManager().getString("core.nopermission")));
        }
    }
    
    private HashMap<String, Integer> getHighestEntry(final HashMap<String, Integer> entries, final String exclusions) {
        final HashMap<String, Integer> returnEntry = new HashMap<String, Integer>();
        int highestEntry = 0;
        String entry = null;
        for (final String s : entries.keySet()) {
            if (!exclusions.contains(s) && entries.get(s) > highestEntry) {
                highestEntry = entries.get(s);
                entry = s;
            }
        }
        if (entry == null || highestEntry == 0) {
            return null;
        }
        returnEntry.put(entry, highestEntry);
        return returnEntry;
    }
    
    private String color(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}