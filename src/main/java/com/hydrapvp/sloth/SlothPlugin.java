package com.hydrapvp.sloth;

import com.hydrapvp.sloth.api.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SlothPlugin
  extends JavaPlugin
{
  private static API api;
  public static String debugPlayer = "import";
  
  public static void main(String[] args) {}
  
  public void onEnable()
  {
    api = new API(this);
    api.init();
  }
  
  public void onDisable()
  {
    if (api != null) {
      api.destroy();
    }
  }
  
  public static API getAPI()
  {
    return api;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    api.getCommandManager().handleCommand(sender, label, args);
    return true;
  }
}
