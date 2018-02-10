package com.hydrapvp.sloth.checks.combat.killaura;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.AttackEvent;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;
import com.hydrapvp.sloth.utils.TrigUtils;
import org.bukkit.entity.Player;

public class TypeC extends Check implements IEventListener {
  public TypeC() {
    super("KillAura-TypeC");
  }
  
  public void init() {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "false"));
    super.init();
  }
  
  public void runCheck(Event e) {
    PlayerData player = e.getPlayer();
    if ((player.getTarget() == null) || ((player.getTarget() != null) && (!(player.getTarget() instanceof Player)))) {
      return;
    }
    if ((player.getTarget() != null) && 
      (player.getLastTarget() != null) && 
      (!player.getTarget().equals(player.getLastTarget()))) {
      player.kaTypeCVL = 0.0D;
    }
    if (((e instanceof AttackEvent)) && 
      (e.isCancelled())) {
      player.kaTypeCVL = 0.0D;
    }
    if ((e instanceof UseEntityEvent)) {
      player.kaTypeCLastHitTime = System.currentTimeMillis();
      if (player.getLastTargetHorizontalDistance() < 0.1D) {
        player.kaTypeCStandingTicks += 1;
        if (player.kaTypeCStandingTicks > 20) {
          player.kaTypeCVL = 0.0D;
        }
      }
      else {
        player.kaTypeCStandingTicks = 0;
      }
      if ((player.getTarget() != null) && 
        (player.getLocation() != null) && (player.getWorld() != null) && 
        (player.getTarget().getWorld().equals(player.getWorld())) && 
        (player.getTarget().getLocation().distance(player.getLocation()) > 1.2D) && (player.getTarget().getLocation().distance(player.getLocation()) < 6.0D)) {
        double aimValue = TrigUtils.getDirection(player.getLocation(), player.getTarget().getLocation());
        double yawValue = TrigUtils.wrapAngleTo180_float(player.getYaw());
        if (Math.abs(aimValue - yawValue) < 25.0D) {
          if (player.kaTypeCTotalYawDelta > 30.0F) {
            double increase = player.kaTypeCTotalYawDelta / 50.0F;
            if (increase > 0.8D) {
              increase = 0.8D;
            }
            player.kaTypeCVL += increase;
          }
          else if (Math.abs(aimValue - yawValue) > 18.0D) {
            player.kaTypeCVL -= 3.0D;
            if (player.kaTypeCVL < 0.0D) {
              player.kaTypeCVL = 0.0D;
            }
          }
          if (player.kaTypeCVL > 10.0D) {
            player.fail("Kill Aura", "Head snapping");
            if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
              player.addBanVL("Kill Aura", 0.1D);
            }
          }
        }
      }
      player.kaTypeCTotalYawDelta = 0.0F;
    }
    if ((e instanceof MovementEvent)) {
      if (Math.abs(player.kaTypeCLastHitTime - System.currentTimeMillis()) < 500L) {
        player.kaTypeCTotalYawDelta += player.getDeltaYaw();
      } else {
        player.kaTypeCVL *= 0.8D;
      }
    }
  }
  
  public void onEvent(Event e) {
    if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".Enabled")) {
      runCheck(e);
    }
  }
}
