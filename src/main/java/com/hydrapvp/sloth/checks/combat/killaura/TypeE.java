package com.hydrapvp.sloth.checks.combat.killaura;

import org.bukkit.entity.Player;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.AttackEvent;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.SwingEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;
import com.hydrapvp.sloth.utils.AverageCollector;
import com.hydrapvp.sloth.utils.TrigUtils;

public class TypeE
  extends Check
  implements IEventListener
{
  public TypeE()
  {
    super("KillAura-TypeE");
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
    if ((player.getTarget() != null) && 
      (player.getLastTarget() != null) && 
      (!player.getTarget().equals(player.getLastTarget())))
    {
      player.kaTypeEMisses = 0;
      player.kaTypeEHits = 0;
      player.kaTypeEAimVL = 0;
      player.kaTypeEDirectionVL = 0;
      player.kaTypeDVL = 0;
      if (player.kaTypeEConsistantCollector != null) {
        player.kaTypeEConsistantCollector.clear();
      }
    }
    if (((e instanceof AttackEvent)) && 
      (e.isCancelled()))
    {
      player.kaTypeEMisses = 0;
      player.kaTypeEHits = 0;
      player.kaTypeEAimVL = 0;
      player.kaTypeEDirectionVL = 0;
      player.kaTypeDVL = 0;
      if (player.kaTypeEConsistantCollector != null) {
        player.kaTypeEConsistantCollector.clear();
      }
    }
    if ((e instanceof UseEntityEvent))
    {
      player.kaTypeEHits += 1;
      player.kaTypeEMisses -= 1;
      if ((player.getTarget() != null) && 
        (player.getLocation() != null) && (player.getWorld() != null) && 
        (player.getTarget().getWorld().equals(player.getWorld())) && 
        (player.getTarget().getLocation().distance(player.getLocation()) > 1.2D) && (player.getTarget().getLocation().distance(player.getLocation()) < 4.5D))
      {
        double aimValue = Math.abs(TrigUtils.getDirection(player.getLocation(), player.getTarget().getLocation()));
        double yawValue = Math.abs(TrigUtils.wrapAngleTo180_float(player.getYaw()));
        if (Math.abs(aimValue - yawValue) < 20.0D) {
          player.kaTypeEAimVL += 1;
        } else {
          player.kaTypeEDirectionVL += 1;
        }
      }
    }
    if ((e instanceof SwingEvent))
    {
      player.kaTypeEMisses += 1;
      player.kaTypeELastSwingTime = player.kaTypeECurrentSwingTime;
      player.kaTypeECurrentSwingTime = System.currentTimeMillis();
      player.kaTypeELastDelay = player.kaTypeECurrentDelay;
      player.kaTypeECurrentDelay = Math.abs(player.kaTypeELastSwingTime - player.kaTypeECurrentSwingTime);
      double difference = Math.abs(player.kaTypeELastDelay - player.kaTypeECurrentDelay);
      if (player.kaTypeEConsistantCollector == null) {
        player.kaTypeEConsistantCollector = new AverageCollector();
      }
      if (difference < 200.0D) {
        player.kaTypeEConsistantCollector.add(difference);
      }
    }
    if (((e instanceof MovementEvent)) && 
      (player.kaTypeEConsistantCollector != null))
    {
      double consistantAverage = player.kaTypeEConsistantCollector.getAverage();
      if (player.kaTypeEMisses <= 0) {
        player.kaTypeEMisses = 1;
      }
      double hitMissRatio = player.kaTypeEHits / player.kaTypeEMisses;
      double aimVL = player.kaTypeEAimVL;
      double dirVL = player.kaTypeEDirectionVL;
      
      double fullVL = 0.0D;
      if (consistantAverage < 50.0D) {
        fullVL += Math.abs(consistantAverage - 50.0D);
      }
      fullVL *= hitMissRatio;
      if (aimVL > 40.0D) {
        fullVL *= 2.0D;
      }
      if (dirVL > 40.0D) {
        fullVL *= 2.0D;
      }
      if (player.kaTypeEConsistantCollector.getDataAmount() > 50)
      {
        player.kaTypeEMisses = 0;
        player.kaTypeEHits = 0;
        player.kaTypeEAimVL = 0;
        player.kaTypeEDirectionVL = 0;
        player.kaTypeEVL -= 1;
        player.kaTypeEConsistantCollector.clear();
      }
      if (fullVL > 2000.0D)
      {
        player.kaTypeEVL += 1;
        if (player.kaTypeEVL > 1)
        {
          player.fail("Kill Aura", "Overall");
          if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
            player.addBanVL("Kill Aura", 0.01D);
          }
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
