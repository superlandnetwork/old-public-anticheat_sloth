package com.hydrapvp.sloth.checks.movement;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.TeleportEvent;
import com.hydrapvp.sloth.events.event.VelocityEvent;
import org.bukkit.Location;
import org.bukkit.Material;

public class Jesus
  extends Check
  implements IEventListener
{
  public Jesus()
  {
    super("Jesus");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "true"));
    super.init();
  }
  
  private boolean inLiquid(Material mat)
  {
    Material[] material = { Material.WATER, Material.LAVA, Material.STATIONARY_LAVA, Material.STATIONARY_WATER };
    for (Material m : material) {
      if (m.equals(mat)) {
        return true;
      }
    }
    return false;
  }
  
  public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if ((e instanceof VelocityEvent))
    {
      player.jesusVL -= 2;
      if (player.jesusVL < 0) {
        player.jesusVL = 0;
      }
    }
    if ((e instanceof TeleportEvent)) {
      player.jesusVL = 0;
    }
    if (((e instanceof MovementEvent)) && 
      (player.getLocation() != null))
    {
      Location water = player.getLocation().clone().add(0.0D, -0.3D, 0.0D);
      if ((!player.isOnGround()) && (!player.inWater()) && (!player.inLava()) && (inLiquid(water.getBlock().getType())))
      {
        if ((!player.inVehicle()) && 
          (!player.getPlayer().getAllowFlight()))
        {
          player.jesusVL += 1;
          if (player.jesusVL > 15)
          {
            player.fail("Jesus", "*");
            if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
              player.addBanVL("Jesus", 0.1D);
            }
            player.jesusVL = 10;
          }
        }
      }
      else if ((player.isOnGround()) || (player.inWater()) || (player.inLava())) {
        player.jesusVL = 0;
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
