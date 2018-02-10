package com.hydrapvp.sloth.checks.movement;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.TeleportEvent;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
public class Enderpearl
  extends Check
  implements IEventListener
{
  public Enderpearl()
  {
    super("Enderpearl");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "true"));
    super.init();
  }
  
  private boolean isValidMove(PlayerData player, Material m)
  {
    return player.checkSolid(m);
  }
  
  public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if (((e instanceof TeleportEvent)) && 
      (((TeleportEvent)e).getReason().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) && 
      (player.getLocation() != null) && 
      (player.getLocation().getWorld().equals(((TeleportEvent)e).getTo().getWorld())) && 
      (player.getLocation().distance(((TeleportEvent)e).getTo()) < 5.0D)) {
      if ((isValidMove(player, ((TeleportEvent)e).getTo().getBlock().getType())) || (isValidMove(player, ((TeleportEvent)e).getTo().clone().add(0.0D, 1.1D, 0.0D).getBlock().getType())))
      {
        if (Math.abs(((TeleportEvent)e).getTo().getY() - player.getLocation().getY()) < 1.5D)
        {
          player.epearlvl += 1;
          if (player.epearlvl > 5)
          {
            player.epearlvl = 0;
            player.fail("Phase", "Enderpearls");
          }
          e.setCancelled(true);
          player.getPlayer().getInventory().addItem(new ItemStack[] { new ItemStack(Material.ENDER_PEARL) });
        }
      }
      else
      {
        player.epearlvl -= 2;
        if (player.epearlvl < 0) {
          player.epearlvl = 0;
        }
      }
    }
  }
  
  public void onEvent(Event e)
  {
    if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".Enabled")) {
      runCheck(e);
    }
  }
}
