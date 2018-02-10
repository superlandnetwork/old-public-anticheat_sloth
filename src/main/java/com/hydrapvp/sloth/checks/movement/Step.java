package com.hydrapvp.sloth.checks.movement;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.PistonEvent;
import java.text.DecimalFormat;

public class Step
  extends Check
  implements IEventListener
{
  public Step()
  {
    super("Step");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "true"));
    super.init();
  }
  
  public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if ((e instanceof PistonEvent)) {
      player.pistonedTicks = 5;
    }
    if ((e instanceof MovementEvent))
    {
      if (player.pistonedTicks > 0)
      {
        player.pistonedTicks -= 1;
        return;
      }
      if (player.getY() > 255.0D) {
        return;
      }
      if (!player.isOnGround()) {
        player.ticksOnGround = 0;
      } else {
        player.ticksOnGround += 1;
      }
      if ((player.ticksOnGround > 0) && (player.getPlayer().isOnGround()) && 
        (player.getDeltaY() > 0.99D) && (player.yFreedom < 20.0D))
      {
        player.stepVL += player.getDeltaY();
        if (player.stepVL > 0.0D)
        {
          player.stepVL = 0.0D;
          Double format = Double.valueOf(player.getDeltaY());
          DecimalFormat decim = new DecimalFormat("0.0");
          Double jh = Double.valueOf(Double.parseDouble(decim.format(format)));
          player.fail("Step", "Stepped " + jh + " blocks");
          if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
            player.addBanVL("Step", 0.3D);
          }
        }
      }
      player.lastOnGround = player.isOnGround();
    }
  }
  
  public void onEvent(Event e)
  {
    if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".Enabled")) {
      runCheck(e);
    }
  }
}
