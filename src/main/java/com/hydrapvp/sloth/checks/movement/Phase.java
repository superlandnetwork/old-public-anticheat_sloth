package com.hydrapvp.sloth.checks.movement;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.SBlockPlaceEvent;
import com.hydrapvp.sloth.events.event.TeleportEvent;
import com.hydrapvp.sloth.utils.TrigUtils;

public class Phase
  extends Check
  implements IEventListener
{
  public Phase()
  {
    super("Phase");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "true"));
    super.init();
  }
  
  private boolean checkPhase(Player player)
  {
    boolean allowFly = player.getPlayer().getAllowFlight();
    boolean inVehicle = player.getPlayer().isInsideVehicle();
    boolean aboveHeight = player.getPlayer().getLocation().getY() > 128.0D;
    
    return (!allowFly) && (!inVehicle) && (!aboveHeight);
  }
  
  private void updatePhaseSetback(PlayerData player)
  {
    if (!player.isInBlock()) {
      return;
    }
    player.phaseSetback = player.getLocation();
  }
  
  public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if (((e instanceof TeleportEvent)) && 
      (!((TeleportEvent)e).getReason().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)))
    {
      player.wasInBlock = false;
      player.locationFromBlock = null;
    }
    if ((e instanceof SBlockPlaceEvent))
    {
      player.wasInBlock = false;
      player.locationFromBlock = null;
    }
    if ((e instanceof MovementEvent))
    {
      if (!checkPhase(player.getPlayer())) {
        return;
      }
      if (player.getLocation() == null) {
        return;
      }
      if ((player.isInBlock()) && (!player.wasInBlock))
      {
        player.wasInBlock = true;
        player.locationFromBlock = player.getLocation();
      }
      else if ((!player.isInBlock()) && (player.wasInBlock))
      {
        player.wasInBlock = false;
        player.locationFromBlock = null;
      }
      if ((player.phaseSetback != null) && (player.getLocation().getWorld().equals(player.phaseSetback.getWorld())) && (player.getLocation().distance(player.phaseSetback) > 10.0D)) {
        player.phaseSetback = null;
      }
      if (player.phaseSetback != null)
      {
        if ((player.locationFromBlock != null) && (player.isInBlock()) && (TrigUtils.getDistance(player.getLocation().getX(), player.getLocation().getZ(), player.locationFromBlock.getX(), player.locationFromBlock.getZ()) > 1.3D))
        {
          if (this.api.getConfiguration().readBoolean("core.PhaseSetBack")) {
            player.getPlayer().teleport(player.phaseSetback);
          }
          player.fail("Phase", player.getLocation().getBlock().getType().name().toLowerCase());
          player.lastPhaseTime = System.currentTimeMillis();
          if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
            player.addBanVL("Phase", 0.3D);
          }
        }
        if (player.getDeltaY() < -1.0D)
        {
          int interval = 0;
          for (double startPos = player.getY() - player.getDeltaY(); startPos > player.getY(); startPos -= 0.25D)
          {
            interval++;
            if (interval > 100)
            {
              if (this.api.getConfiguration().readBoolean("core.PhaseSetBack")) {
                player.getPlayer().teleport(player.phaseSetback);
              }
              player.fail("Phase", "[C]");
              player.lastPhaseTime = System.currentTimeMillis();
              break;
            }
            Location atPos = new Location(player.getWorld(), player.getX(), startPos, player.getZ());
            if (!player.checkPhase(atPos.getBlock().getType()))
            {
              if (this.api.getConfiguration().readBoolean("core.PhaseSetBack")) {
                player.getPlayer().teleport(player.phaseSetback);
              }
              player.fail("VClip", "*");
              player.lastPhaseTime = System.currentTimeMillis();
              if (!this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
                break;
              }
              player.addBanVL("VClip", 0.3D); break;
            }
          }
        }
      }
      else
      {
        updatePhaseSetback(player);
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
