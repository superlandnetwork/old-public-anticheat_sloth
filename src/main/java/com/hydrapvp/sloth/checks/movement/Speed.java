package com.hydrapvp.sloth.checks.movement;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.VelocityEvent;

public class Speed
  extends Check
  implements IEventListener
{
  public Speed()
  {
    super("Speed");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "true"));
    super.init();
  }
  
  private double getMaxMovementSpeed(PlayerData player)
  {
    double baseMovementSpeed = 0.287D;
    
    baseMovementSpeed *= player.getPlayer().getWalkSpeed() / 0.2D;
    for (PotionEffect potionEffect : player.getPlayer().getActivePotionEffects()) {
      if ((potionEffect != null) && 
        (potionEffect.getType().equals(PotionEffectType.SPEED)))
      {
        int amplifier = potionEffect.getAmplifier() + 1;
        baseMovementSpeed += baseMovementSpeed * 0.2D * amplifier;
        baseMovementSpeed *= 3.0D;
      }
    }
    if ((player.isUnderBlock()) && 
      (player.hFreedom < 1.7175D)) {
      player.hFreedom = 1.7175D;
    }
    if ((player.onIce()) && 
      (player.hFreedom < 2.175D)) {
      player.hFreedom = 2.175D;
    }
    if ((player.onSlime()) && 
      (player.hFreedom < 1.2D)) {
      player.hFreedom = 1.2D;
    }
    player.lastSpeedLowJumpVL = player.speedLowJumpVL;
    
    player.speedLowJumpVL *= 0.95D;
    player.speedLowJumpVL += player.getDeltaY();
    if (player.speedLowJumpVL < 0.0D) {
      player.speedLowJumpVL = 0.0D;
    }
    if ((player.getDeltaY() > 0.0D) && (player.getDeltaY() < 0.6D) && 
      (player.speedLowJumpVL > 0.0D) && (player.lastSpeedLowJumpVL > 0.0D) && 
      (player.hFreedom < player.getDeltaY() * 1.3D)) {
      player.hFreedom = (player.getDeltaY() * 1.3D);
    }
    if (player.hFreedom > 10.0D) {
      player.hFreedom = 10.0D;
    }
    return baseMovementSpeed;
  }
  
  private double hDistanceAfterFailure(PlayerData player, double hDistance)
  {
    double bhopSpeed = bunnyHop(player);
    hDistance -= bhopSpeed;
    if (hDistance > 0.0D)
    {
      player.hFreedom -= hDistance / 2.0D;
      if (player.hFreedom > 0.0D) {
        hDistance = 0.0D;
      }
    }
    return hDistance;
  }
  
  private double bunnyHop(PlayerData player)
  {
    double[] knownBhopVals = { 0.313D, 0.612D, 0.36D, 0.353D, 0.347D, 0.341D, 0.336D, 0.331D, 0.327D, 0.323D, 0.32D, 0.316D };
    if (player.bunnyHopDelay > 11) {
      return 0.33D;
    }
    return knownBhopVals[player.bunnyHopDelay];
  }
  
  public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if ((e instanceof VelocityEvent))
    {
      double x = Math.abs(((VelocityEvent)e).getX()) * 5.0D;
      double z = Math.abs(((VelocityEvent)e).getZ()) * 5.0D;
      if (x + z > player.hFreedom) {
        player.hFreedom = (x + z);
      }
    }
    if ((e instanceof MovementEvent))
    {
      if (player.vehicleTicks > 0) {
        return;
      }
      if (player.isOnGround()) {
        player.bunnyHopDelay = 0;
      } else {
        player.bunnyHopDelay += 1;
      }
      if ((player.getPlayer().getVehicle() == null) && (!player.getPlayer().getAllowFlight()))
      {
        double maxSpeed = getMaxMovementSpeed(player);
        double speed = player.getLastHorizontalDistance();
        if ((speed > maxSpeed) && (speed < 10.0D))
        {
          speed = hDistanceAfterFailure(player, speed);
          if (speed > 0.0D)
          {
            player.speedVL += speed;
            if (player.speedVL > 6.0D)
            {
              player.speedVL = 6.0D;
              player.fail("Speed", "*");
              if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
                player.addBanVL("Speed", 0.1D);
              }
            }
          }
        }
        else
        {
          player.speedVL *= 0.95D;
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
