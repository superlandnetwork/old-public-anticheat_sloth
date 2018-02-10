package com.hydrapvp.sloth.checks.movement;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.ASyncMovementEvent;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.PistonEvent;
import com.hydrapvp.sloth.events.event.SBlockPlaceEvent;
import com.hydrapvp.sloth.events.event.TeleportEvent;
import com.hydrapvp.sloth.events.event.VelocityEvent;
import java.text.DecimalFormat;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Fly
  extends Check
  implements IEventListener
{
  public Fly()
  {
    super("Fly");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "MaxJumpHeight", Double.valueOf(1.3D)));
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "true"));
    this.config.add(new Config("checks." + getCheckName(), "PreventClaimBlockGlitching", "false"));
    super.init();
  }
  
  private double getMaxJumpHeight(PlayerData player)
  {
    double jumpHeight = this.api.getConfiguration().readDouble("checks.Fly.MaxJumpHeight");
    for (PotionEffect e : player.getPlayer().getActivePotionEffects()) {
      if ((e != null) && 
        (e.getType().equals(PotionEffectType.JUMP))) {
        jumpHeight += Math.pow(e.getAmplifier() + 4.2D, 2.0D) / 16.0D;
      }
    }
    return jumpHeight;
  }
  
  private boolean shouldCheckFly(PlayerData player)
  {
    return (!player.getPlayer().getAllowFlight()) && (!player.inLava()) && (!player.inWater()) && (!player.inVehicle()) && (!player.inWeb()) && (!player.onLadder()) && (!player.onVine());
  }
  
  private double getPredictedHeightFromFirstVelocity(double firstVelocity)
  {
    double curVelocity = firstVelocity * 1.3D;
    double jumpHeight = 0.0D;
    double gravity = -0.11839996340669459D;
    
    int iterations = 0;
    while (curVelocity > 0.0D)
    {
      iterations++;
      if (iterations > 20) {
        break;
      }
      gravity *= 0.9800000191D;
      jumpHeight += curVelocity;
      curVelocity += gravity;
    }
    return jumpHeight;
  }
  
  @SuppressWarnings("deprecation")
public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if ((e instanceof SBlockPlaceEvent)) {
      if ((e.isCancelled()) && (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".PreventClaimBlockGlitching")))
      {
        player.jumpHeight = 0.5D;
        player.yFreedom = this.api.getConfiguration().readDouble("checks." + getCheckName() + ".MaxJumpHeight");
        player.blockCancelled = 10;
      }
      else
      {
        player.jumpHeight = 0.0D;
        player.airTicks = 0;
        player.fallSpeed = 0.0D;
        player.fallTicks = 0;
      }
    }
    if ((e instanceof PistonEvent)) {
      for (Block b : ((PistonEvent)e).getBlocks()) {
        if (b.getType().getId() == 165) {
          player.pistonTicks = 5;
        }
      }
    }
    if ((e instanceof ASyncMovementEvent))
    {
      player.blockCancelled -= 1;
      if (player.blockCancelled < 0) {
        player.blockCancelled = 0;
      }
      if (player.blockCancelled <= 0)
      {
        if (player.isOnGround()) {
          player.lastTickOrdinal = "Neutral";
        }
        if (player.getDeltaY() < 0.0D)
        {
          player.lastTickOrdinal = "Down";
        }
        else if ((player.getDeltaY() > 0.0D) && (player.jumpHeight > 0.625D) && 
          (player.lastTickOrdinal == "Down"))
        {
          player.jumpHeight = 0.0D;
          player.airTicks = 0;
          player.fallSpeed = 0.0D;
          player.fallTicks = 0;
          player.yFreedom += 1.3D;
          player.lastTickOrdinal = "Up";
        }
      }
    }
    if ((e instanceof TeleportEvent))
    {
      player.jumpHeight = 0.0D;
      player.airTicks = 0;
      player.fallSpeed = 0.0D;
      player.fallTicks = 0;
    }
    if ((e instanceof VelocityEvent))
    {
      if (((VelocityEvent)e).getY() > 0.0D) {
        player.yFreedom += getPredictedHeightFromFirstVelocity(((VelocityEvent)e).getY());
      }
      player.fallTicks = 0;
      player.fallSpeed = 0.0D;
    }
    if ((e instanceof MovementEvent))
    {
      if ((Math.abs(player.getX()) < 0.1D) && (Math.abs(player.getZ()) < 0.1D)) {
        player.glideVL = 0.0D;
      }
      if (!shouldCheckFly(player))
      {
        player.jumpHeight = 0.0D;
        player.airTicks = 0;
        player.fallSpeed = 0.0D;
        player.fallTicks = 0;
        player.glideVL = 0.0D;
        return;
      }
      if (player.vehicleTicks > 0) {
        return;
      }
      if (player.getDeltaY() > 0.0D) {
        player.jumpHeight += player.getDeltaY();
      }
      if (player.isOnGround())
      {
        player.jumpHeight = 0.0D;
        player.fallSpeed = 0.0D;
        player.fallTicks = 0;
        player.airTicks = 0;
        player.flySlime = player.onSlime();
        if (player.pistonTicks > 0) {
          player.pistonTicks -= 1;
        }
      }
      else
      {
        player.airTicks += 1;
      }
      if ((player.flySlime) || (player.pistonTicks > 0))
      {
        player.jumpHeight = 0.0D;
        player.fallSpeed = 0.0D;
        player.fallTicks = 0;
        player.airTicks = 0;
      }
      if ((player.getDeltaY() < 0.0D) && (player.jumpHeight > player.yFreedom / 3.0D) && (player.blockCancelled <= 0))
      {
        player.yFreedom = 0.0D;
        player.jumpHeight = 0.0D;
      }
      if ((player.airTicks > 3) && (player.getDeltaY() < 0.1D) && (player.getLocation().clone().add(0.0D, -1.6D, 0.0D).getBlock().isEmpty()))
      {
        player.fallTicks += 1;
        if ((player.fallTicks > 2) || (player.getDeltaY() < -0.035D))
        {
          player.fallSpeed -= 0.035D;
          if (player.fallSpeed > 1.0D) {
            player.fallSpeed = 1.0D;
          }
          if ((Math.abs(player.getDeltaY()) < 0.232D) && (Math.abs(player.getDeltaY()) > 0.23D))
          {
            player.glideVL = 0.0D;
            player.fallTicks = 0;
            player.airTicks = 0;
            player.fallSpeed = 0.0D;
          }
          if (Math.abs(-0.1D - player.getDeltaY()) < 0.01D)
          {
            player.glideVL += 1.0D;
            if (player.glideVL > 10.0D)
            {
              player.glideVL = 0.0D;
              player.fail("Glide", "Fall speed (Could be lag?)");
              player.fallSpeed = 0.0D;
              player.fallTicks = 0;
              player.airTicks = 0;
            }
          }
          else if (player.getDeltaY() > player.fallSpeed)
          {
            player.glideVL += Math.abs(player.getDeltaY() - player.fallSpeed);
            if (player.glideVL > 5.0D)
            {
              player.glideFlags += 1;
              if (player.glideFlags > 1)
              {
                if (this.api.getConfiguration().readBoolean("core.FlySetBack"))
                {
                  if (player.blockGlitchSetbackLocation == null) {
                    player.blockGlitchSetbackLocation = player.getLocation();
                  }
                  player.getPlayer().teleport(player.blockGlitchSetbackLocation);
                }
                player.glideVL = 3.0D;
                player.fail("Glide", "Fall Speed");
                if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
                  player.addBanVL("Glide", 0.1D);
                }
              }
            }
          }
          else
          {
            player.glideFlags = 0;
            player.glideVL -= 1.0D;
            if (player.glideVL < 0.0D) {
              player.glideVL = 0.0D;
            }
          }
        }
      }
      double maxJumpHeight = getMaxJumpHeight(player);
      
      Material inPlayer = player.getPlayer().getLocation().getBlock().getType();
      if ((inPlayer.name().contains("STAIR")) || (inPlayer.name().contains("STEP")) || (inPlayer.name().contains("SLAB"))) {
        player.jumpHeight = 0.0D;
      }
      if (player.yFreedom < maxJumpHeight) {
        player.yFreedom = maxJumpHeight;
      }
      if (player.jumpHeight > player.yFreedom)
      {
        player.flyVL += Math.abs(player.jumpHeight - player.yFreedom);
        if (player.blockCancelled > 0)
        {
          if (player.blockGlitchSetbackLocation == null) {
            player.blockGlitchSetbackLocation = player.getLocation();
          }
          player.getPlayer().teleport(player.blockGlitchSetbackLocation);
        }
        if (player.flyVL > 5.0D)
        {
          if (this.api.getConfiguration().readBoolean("core.FlySetBack"))
          {
            if (player.blockGlitchSetbackLocation == null) {
              player.blockGlitchSetbackLocation = player.getLocation();
            }
            player.getPlayer().teleport(player.blockGlitchSetbackLocation);
          }
          Double format = Double.valueOf(player.jumpHeight);
          DecimalFormat decim = new DecimalFormat("0.0");
          Double jh = Double.valueOf(Double.parseDouble(decim.format(format)));
          if (player.blockCancelled <= 0)
          {
            if (jh.doubleValue() < 1.3D) {
              jh = Double.valueOf(1.3D);
            }
            player.fail("Fly", "Jumped " + jh + " blocks");
            if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
              player.addBanVL("Fly", 0.1D);
            }
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
