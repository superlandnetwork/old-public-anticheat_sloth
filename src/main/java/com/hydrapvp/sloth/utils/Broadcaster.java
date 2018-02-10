package com.hydrapvp.sloth.utils;

import com.hydrapvp.sloth.SlothPlugin;
import org.bukkit.entity.Player;;

class Broadcaster
{
  public static void broadcast(String broadcast)
  {
    for (Player p : SlothPlugin.getAPI().getPlugin().getServer().getOnlinePlayers()) {
      if (p.hasPermission("sloth.notify")) {
        p.sendMessage(broadcast);
      }
    }
  }
}
