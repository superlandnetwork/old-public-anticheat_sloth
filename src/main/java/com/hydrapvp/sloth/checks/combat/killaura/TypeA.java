package com.hydrapvp.sloth.checks.combat.killaura;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.event.ASyncMovementEvent;
import com.hydrapvp.sloth.events.event.AttackEvent;
import com.hydrapvp.sloth.events.event.FlyingEvent;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.SwingEvent;
import com.hydrapvp.sloth.utils.TrigUtils;
import org.bukkit.entity.Player;

public class TypeA
  extends Check
  implements com.hydrapvp.sloth.events.IEventListener
{
  public TypeA()
  {
    super("KillAura-TypeA");
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
    if ((e instanceof SwingEvent)) {
      player.kaTypeALastSwingTime = System.currentTimeMillis();
    }
    if (((e instanceof FlyingEvent)) || ((e instanceof ASyncMovementEvent)))
    {
      if (Math.abs(player.kaTypeALastSwingTime - System.currentTimeMillis()) > 500L)
      {
        player.kaTypeATotalTheoreticalYawDelta = 0.0D;
        player.kaTypeATotalYawDelta = 0.0F;
        player.kaTypeAVL = 0.0D;
        player.kaTypeAFails = 0.0D;
      }
      if (this.api.getDataManager().getPlayerDataByPlayer((Player)player.getTarget()).getLastHorizontalDistance() < 0.1D)
      {
        player.kaTypeATicksNotMoving += 1;
        if (player.kaTypeATicksNotMoving > 20)
        {
          player.kaTypeATotalTheoreticalYawDelta = 0.0D;
          player.kaTypeATotalYawDelta = 0.0F;
          player.kaTypeAVL = 0.0D;
          player.kaTypeAFails = 0.0D;
        }
      }
      else
      {
        player.kaTypeATicksNotMoving = 0;
      }
      if (player.kaTypeATotalTheoreticalYawDelta > 25.0D)
      {
        double difference = Math.abs(player.kaTypeATotalYawDelta - player.kaTypeATotalTheoreticalYawDelta);
        if (difference < 35.0D)
        {
          player.kaTypeAVL += Math.abs(difference - 35.0D) * (player.kaTypeATotalYawDelta / 50.0F);
        }
        else
        {
          player.kaTypeAVL -= difference / 1.5D;
          if (player.kaTypeAVL < 0.0D) {
            player.kaTypeAVL = 0.0D;
          }
        }
        if (player.kaTypeAVL > 0.0D)
        {
          double increase = player.kaTypeAVL / 10.0D;
          if (increase > 1.5D) {
            increase = 1.5D;
          }
          player.kaTypeAFails += increase;
          if (player.kaTypeAFails >= 15.0D)
          {
            player.fail("Kill Aura", "Aimbot");
            if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
              player.addBanVL("Kill Aura", 0.2D);
            }
          }
          player.kaTypeAVL = 0.0D;
        }
        else
        {
          player.kaTypeAFails -= 1.5D;
          if (player.kaTypeAFails < 0.0D) {
            player.kaTypeAFails = 0.0D;
          }
        }
        player.kaTypeATotalYawDelta = 0.0F;
        player.kaTypeATotalTheoreticalYawDelta = 0.0D;
      }
    }
    if (((e instanceof MovementEvent)) && 
      (player.getTarget() != null) && 
      (player.getLocation() != null) && (player.getWorld() != null) && 
      (player.getTarget().getWorld().equals(player.getWorld()))) {
      if ((player.getTarget().getLocation().distance(player.getLocation()) > 1.2D) && (player.getTarget().getLocation().distance(player.getLocation()) < 4.5D))
      {
        double aimValue = Math.abs(TrigUtils.getDirection(player.getLocation(), player.getTarget().getLocation()));
        if ((player.kaTypeALastAimValue != 0.0D) && 
          (Math.abs(player.getDeltaYaw()) > 5.0F))
        {
          player.kaTypeATotalYawDelta += Math.abs(player.getDeltaYaw());
          player.kaTypeATotalTheoreticalYawDelta += Math.abs(aimValue - player.kaTypeALastAimValue);
          player.kaTypeATicks += 1;
        }
        player.kaTypeALastAimValue = aimValue;
      }
      else
      {
        player.kaTypeALastAimValue = 0.0D;
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
