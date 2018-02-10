package com.hydrapvp.sloth.checks.combat.killaura;

import org.bukkit.entity.Player;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.AttackEvent;
import com.hydrapvp.sloth.events.event.SwingEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;
import com.hydrapvp.sloth.utils.SmallAverageCollector;
import com.hydrapvp.sloth.utils.TrigUtils;

public class TypeF
  extends Check
  implements IEventListener
{
  public TypeF()
  {
    super("KillAura-TypeF");
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
    if ((player.getTarget() != null) && (!(player.getTarget() instanceof Player))) {
      return;
    }
    if (((e instanceof AttackEvent)) && 
      (e.isCancelled()))
    {
      player.kaTypeAFails = 0.0D;
      player.kaTypeAVL = 0.0D;
    }
    if ((player.getTarget() != null) && 
      (player.getLastTarget() != null) && 
      (!player.getTarget().equals(player.getLastTarget())))
    {
      player.kaTypeAFails = 0.0D;
      player.kaTypeAVL = 0.0D;
    }
    if ((e instanceof UseEntityEvent))
    {
      if (player.kaTypeFReachCollector == null) {
        player.kaTypeFReachCollector = new SmallAverageCollector(10);
      }
      if ((player.getLocation() != null) && (player.getTarget() != null)) {
        player.kaTypeFReachCollector.add(player.getLocation().distance(player.getTarget().getLocation()));
      }
      player.kaTypeFMisses -= 1;
      if (player.kaTypeFMisses < 0) {
        player.kaTypeFMisses = 0;
      }
      player.kaTypeFOverall += 1;
      if (player.kaTypeFOverall > 20)
      {
        if (player.kaTypeFViolationCollector == null) {
          player.kaTypeFViolationCollector = new SmallAverageCollector(5);
        }
        if (player.kaTypeFMisses > 100) {
          player.kaTypeFViolationCollector.add(player.kaTypeFMisses);
        } else {
          player.kaTypeFViolationCollector.add(player.kaTypeEMisses / 3);
        }
        if (player.kaTypeFViolationCollector.size() > 4) {
          if (player.kaTypeFViolationCollector.getAverage() > 100.0D)
          {
            player.kaTypeFVL += 1;
            if (player.kaTypeFVL > 5) {
              player.fail("Kill Aura", "Missed while looking");
            }
          }
          else
          {
            player.kaTypeFVL = 0;
          }
        }
        player.kaTypeFOverall = 0;
        player.kaTypeFMisses = 0;
      }
    }
    if ((e instanceof SwingEvent))
    {
      if (player.kaTypeFReachCollector == null) {
        return;
      }
      if ((player.getTarget() != null) && 
        (player.getLocation() != null) && (player.getWorld() != null) && 
        (player.getTarget().getWorld().equals(player.getWorld())) && 
        (player.getTarget().getLocation().distance(player.getLocation()) < player.kaTypeFReachCollector.getAverage()))
      {
        double aimValue = Math.abs(TrigUtils.getDirection(player.getLocation(), player.getTarget().getLocation()));
        double yawValue = Math.abs(TrigUtils.wrapAngleTo180_float(player.getYaw()));
        if (Math.abs(aimValue - yawValue) < 20.0D) {
          player.kaTypeFMisses += 10;
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
