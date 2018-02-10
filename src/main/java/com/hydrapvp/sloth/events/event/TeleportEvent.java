package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportEvent
  extends Event
{
  private Location from;
  private Location to;
  private PlayerTeleportEvent.TeleportCause reason;
  
  public TeleportEvent(PlayerData player, boolean cancelled, Location from, Location to, PlayerTeleportEvent.TeleportCause teleportCause)
  {
    super(player, cancelled, "TeleportEvent");
    this.from = from;
    this.to = to;
    this.reason = teleportCause;
  }
  
  public PlayerTeleportEvent.TeleportCause getReason()
  {
    return this.reason;
  }
  
  public Location getFrom()
  {
    return this.from;
  }
  
  public Location getTo()
  {
    return this.to;
  }
}
