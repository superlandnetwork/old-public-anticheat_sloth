package com.hydrapvp.sloth.checks.combat;

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
import com.hydrapvp.sloth.utils.TrigUtils;

public class HitMissRatio
  extends Check
  implements IEventListener
{
  public HitMissRatio()
  {
    super("HitMissRatio");
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
      player.hmrPacketHits = 0;
      player.hmrPacketMisses = 0;
      player.hmrRatioConsistancyVL = 0;
      player.hmrPacketVL = 0;
      player.hmrLookVL = 0;
      player.hmrInBattle = false;
    }
    if (((e instanceof AttackEvent)) && 
      (player.getTarget() != null) && ((player.getTarget() instanceof Player)) && 
      (e.isCancelled()))
    {
      player.hmrPacketHits = 0;
      player.hmrPacketMisses = 0;
      player.hmrRatioConsistancyVL = 0;
      player.hmrPacketVL = 0;
      player.hmrLookVL = 0;
      player.hmrInBattle = false;
    }
    if (((e instanceof SwingEvent)) && 
      (player.getTarget() != null) && ((player.getTarget() instanceof Player))) {
      player.hmrPacketMisses += 1;
    }
    if ((e instanceof UseEntityEvent))
    {
      player.hmrLastHitTime = System.currentTimeMillis();
      if ((player.getTarget() != null) && ((player.getTarget() instanceof Player)))
      {
        player.hmrPacketHits += 1;
        player.hmrPacketMisses -= 1;
        if (player.hmrPacketMisses < 0) {
          player.hmrPacketMisses = 0;
        }
        if (player.getTarget() != player.getLastTarget())
        {
          player.hmrPacketHits = 0;
          player.hmrPacketMisses = 0;
          player.hmrRatioConsistancyVL = 0;
          player.hmrPacketVL = 0;
        }
      }
    }
    if (((e instanceof SwingEvent)) && 
      (player.getTarget() != null) && 
      (player.getLocation() != null) && (player.getWorld() != null) && 
      (player.getTarget().getWorld().equals(player.getWorld())) && 
      (player.getTarget().getLocation().distance(player.getLocation()) > 1.2D) && (player.getTarget().getLocation().distance(player.getLocation()) < 4.5D))
    {
      double aimValue = Math.abs(TrigUtils.getDirection(player.getLocation(), player.getTarget().getLocation()));
      double yawValue = Math.abs(TrigUtils.wrapAngleTo180_float(player.getYaw()));
      if (Math.abs(aimValue - yawValue) < 25.0D)
      {
        player.hmrLookHits += 1;
        player.hmrLookMisses -= 1;
      }
      else
      {
        player.hmrLookMisses += 2;
      }
    }
    if ((e instanceof MovementEvent))
    {
      if (player.hmrLastYawDelta != player.getDeltaYaw())
      {
        player.hmrLastYawDelta = player.getDeltaYaw();
        player.hmrYawTotal += 1.0D; PlayerData 
          tmp488_487 = player;tmp488_487.hmrYawCumulator = ((int)(tmp488_487.hmrYawCumulator + player.getDeltaYaw()));
      }
      player.hmrLastYaw = player.hmrCurrentYaw;
      player.hmrCurrentYaw = Math.abs(TrigUtils.wrapAngleTo180_float(player.getYaw()));
      
      player.hmrYawDelta += Math.abs(player.hmrLastYaw - player.hmrCurrentYaw);
      
      player.hmrYawPackets += 1;
      if (player.hmrYawPackets > 40)
      {
        player.hmrYawPackets = 0;
        player.hmrInBattle = ((player.hmrYawDelta > 50.0F) && (player.getLastHorizontalDistance() > 0.15D) && (Math.abs(player.hmrLastHitTime - System.currentTimeMillis()) < 5000L));
        player.hmrYawDelta = 0.0F;
      }
      if (player.getTarget() != null) {
        player.hmrSpeedCumulator += Math.abs(player.getLastTargetHorizontalDistance());
      }
      if ((player.hmrYawTotal > 20.0D) && (player.hmrYawCumulator > 20) && (player.getTarget() != null))
      {
        player.hmrYawTotal = 0.0D;
        if (player.hmrPacketMisses <= 0) {
          player.hmrPacketMisses = 1;
        }
        if (player.hmrLookMisses <= 0) {
          player.hmrLookMisses = 1;
        }
        double ratio = player.hmrPacketHits / player.hmrPacketMisses;
        double lookRatio = player.hmrLookHits / player.hmrLookMisses;
        if ((Math.abs(ratio - player.lastRatio) < 1.0D) && (ratio != 0.0D) && (ratio >= 0.25D))
        {
          if (player.hmrInBattle) {
            player.hmrRatioConsistancyVL += 1;
          }
          if (player.hmrRatioConsistancyVL > 15)
          {
            player.hmrRatioConsistancyVL = 15;
            player.fail("Kill Aura", "Hit/miss ratio[B]");
            if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
              player.addBanVL("Kill Aura", 0.5D);
            }
          }
        }
        else
        {
          player.hmrRatioConsistancyVL -= 2;
          if (player.hmrRatioConsistancyVL < 0) {
            player.hmrRatioConsistancyVL = 0;
          }
        }
        ratio *= player.hmrSpeedCumulator / 2.0D;
        lookRatio *= player.hmrSpeedCumulator / 2.0D;
        if (ratio > 10.0D)
        {
          if (player.hmrInBattle) {
            player.hmrPacketVL += 1;
          }
          if (player.hmrPacketVL > 5)
          {
            player.fail("Kill Aura", "Hit/miss ratio[C]");
            if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
              player.addBanVL("Kill Aura", 0.5D);
            }
            player.hmrPacketVL = 3;
          }
        }
        else
        {
          player.hmrPacketVL = 0;
        }
        if (lookRatio > 15.0D)
        {
          if (player.hmrInBattle) {
            player.hmrLookVL += 1;
          }
          if (player.hmrLookVL > 10)
          {
            player.fail("Kill Aura", "Hit/miss ratio[A]");
            if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
              player.addBanVL("Kill Aura", 0.2D);
            }
          }
        }
        else
        {
          player.hmrLookVL -= 3;
          if (player.hmrLookVL < 0) {
            player.hmrLookVL = 0;
          }
        }
        player.lastRatio = ratio;
        player.hmrPacketHits = 0;
        player.hmrPacketMisses = 0;
        player.hmrLookMisses = 0;
        player.hmrLookHits = 0;
        player.hmrSpeedCumulator = 0.0D;
        player.hmrYawCumulator = 0;
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
