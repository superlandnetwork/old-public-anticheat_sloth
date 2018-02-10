package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import org.bukkit.Location;

public class ASyncMovementEvent
  extends Event
{
  private double x;
  private double y;
  private double z;
  private boolean ground;
  
  public ASyncMovementEvent(PlayerData player, boolean cancelled, double x, double y, double z, boolean ground)
  {
    super(player, cancelled, "ASyncMovementEvent");
    this.x = x;
    this.y = y;
    this.z = z;
    this.ground = ground;
  }
  
  public double getX()
  {
    return this.x;
  }
  
  public double getY()
  {
    return this.y;
  }
  
  public double getZ()
  {
    return this.z;
  }
  
  public boolean isOnGround()
  {
    return this.ground;
  }
  
  public Location getTo()
  {
    return new Location(null, this.x, this.y, this.z, 0.0F, 0.0F);
  }
}
