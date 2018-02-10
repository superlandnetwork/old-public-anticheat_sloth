package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import org.bukkit.Location;

public class MovementEvent
  extends Event
{
  private Location to;
  
  public MovementEvent(PlayerData player, boolean cancelled, Location to)
  {
    super(player, cancelled, "MovementEvent");
    this.to = to;
  }
  
  public Location getTo()
  {
    return this.to;
  }
}
