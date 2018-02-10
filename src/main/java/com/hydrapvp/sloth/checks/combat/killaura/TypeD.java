package com.hydrapvp.sloth.checks.combat.killaura;

import org.bukkit.entity.Player;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.AttackEvent;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;

public class TypeD
  extends Check
  implements IEventListener
{
  public TypeD()
  {
    super("KillAura-TypeD");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "false"));
    super.init();
  }
  
  public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if ((player.getTarget() == null) || ((player.getTarget() != null) && (!(player.getTarget() instanceof Player)))) {
      return;
    }
    if ((e instanceof UseEntityEvent)) {
      player.kaTypeDLastHitTime = System.currentTimeMillis();
    }
    if (((e instanceof AttackEvent)) && 
      (e.isCancelled())) {
      player.kaTypeDVL = 0;
    }
    if ((player.getTarget() != null) && 
      (player.getLastTarget() != null) && 
      (!player.getTarget().equals(player.getLastTarget()))) {
      player.kaTypeDVL = 0;
    }
    if (((e instanceof MovementEvent)) && 
      (Math.abs(player.kaTypeDLastHitTime - System.currentTimeMillis()) < 500L))
    {
      double currentDelta = player.lastYawDelta;
      player.lastYawDelta = player.getDeltaYaw();
      if (((currentDelta == 0.0D) || (player.lastYawDelta == 0.0F)) && (currentDelta != player.lastYawDelta))
      {
        player.kaTypeDVL += 1;
      }
      else
      {
        player.kaTypeDVL -= 10;
        if (player.kaTypeDVL < 0) {
          player.kaTypeDVL = 0;
        }
      }
      if (player.kaTypeDVL > 30)
      {
        player.fail("Kill Aura", "Weird head movement");
        if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
          player.addBanVL("Kill Aura", 0.2D);
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
