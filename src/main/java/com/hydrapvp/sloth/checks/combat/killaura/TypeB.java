package com.hydrapvp.sloth.checks.combat.killaura;

import org.bukkit.entity.Player;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.ASyncMovementEvent;
import com.hydrapvp.sloth.events.event.AttackEvent;
import com.hydrapvp.sloth.events.event.FlyingEvent;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;
import com.hydrapvp.sloth.utils.TrigUtils;

public class TypeB
  extends Check
  implements IEventListener
{
  public TypeB()
  {
    super("KillAura-TypeB");
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
    if ((player.getTarget() != null) && (!(player.getTarget() instanceof Player)))
    {
      player.kaTypeBLastPacketTime = 0L;
      return;
    }
    if ((player.getTarget() != null) && 
      (player.getLastTarget() != null) && 
      (!player.getTarget().equals(player.getLastTarget()))) {
      player.kaTypeBVL = 0.0D;
    }
    if (((e instanceof AttackEvent)) && 
      (e.isCancelled()))
    {
      player.kaTypeBExcusedViolations = 10.0D;
      player.kaTypeBVL = 0.0D;
    }
    if (((e instanceof FlyingEvent)) || ((e instanceof ASyncMovementEvent)))
    {
      double difference = Math.abs(player.kaTypeBLastPacketTime - System.currentTimeMillis());
      
      player.kaTypeBLastPacketTime = System.currentTimeMillis();
      if ((difference > 65.0D) && (difference < 5000.0D)) {
        player.kaTypeBExcusedViolations += difference / 70.0D;
      }
      player.kaTypeBLastHDist = player.getLastHorizontalDistance();
    }
    if ((e instanceof MovementEvent))
    {
      player.kaTypeBPackets += 1;
      if (player.kaTypeBPackets > 20)
      {
        player.kaTypeBPackets = 0;
        if (player.kaTypeBExcusedViolations < 20.0D) {
          player.kaTypeBExcusedViolations += 2.0D;
        }
      }
      if ((player.kaTypeBExcusedViolations < 1.0D) && (player.kaTypeBExcusedViolations > 0.0D)) {
        player.kaTypeBExcusedViolations = 20.0D;
      }
      if (Math.abs(player.kaTypeBLastMoveTime - System.currentTimeMillis()) < 3000L)
      {
        if (Math.abs(player.kaTypeBLastHitTime - System.currentTimeMillis()) > 3000L) {
          player.kaTypeBExcusedViolations = 10.0D;
        }
      }
      else {
        player.kaTypeBLastHitTime = System.currentTimeMillis();
      }
      player.kaTypeBLastMoveTime = System.currentTimeMillis();
    }
    if ((e instanceof UseEntityEvent))
    {
      player.kaTypeBLastHitTime = System.currentTimeMillis();
      if ((player.getTarget() != null) && 
        (player.getLocation() != null) && (player.getWorld() != null) && 
        (player.getTarget().getWorld().equals(player.getWorld())) && 
        (player.getTarget().getLocation().distance(player.getLocation()) > 1.5D) && (player.getTarget().getLocation().distance(player.getLocation()) < 4.5D))
      {
        double aimValue = Math.abs(TrigUtils.getDirection(player.getLocation(), player.getTarget().getLocation()));
        double yawValue = Math.abs(TrigUtils.wrapAngleTo180_float(player.getYaw()));
        double difference = Math.abs(aimValue - yawValue);
        if (difference > 30.0D)
        {
          player.kaTypeBExcusedViolations -= 1.2D;
          if (player.kaTypeBExcusedViolations < 1.0D) {
            player.kaTypeBExcusedViolations = -1.0D;
          }
          player.kaTypeBExcusedCooldown = 0;
          if (player.kaTypeBExcusedViolations <= 0.0D)
          {
            player.kaTypeBVL += 1.0D;
            if (player.kaTypeBVL > 5.0D)
            {
              player.fail("Kill Aura", "Not looking at target");
              if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
                player.addBanVL("Kill Aura", 0.3D);
              }
            }
            player.kaTypeBExcusedViolations = -1.0D;
          }
        }
        else
        {
          player.kaTypeBVL -= 2.0D;
          if (player.kaTypeBVL < 0.0D) {
            player.kaTypeBVL = 0.0D;
          }
          player.kaTypeBExcusedCooldown += 1;
          if (player.kaTypeBExcusedCooldown > 5) {
            player.kaTypeBExcusedViolations *= 0.9D;
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
