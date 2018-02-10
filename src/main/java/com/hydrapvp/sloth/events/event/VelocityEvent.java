package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;

public class VelocityEvent
  extends Event
{
  private double x;
  private double y;
  private double z;
  
  public VelocityEvent(PlayerData player, boolean cancelled, double x, double y, double z)
  {
    super(player, cancelled, "VelocityEvent");
    this.x = x;
    this.y = y;
    this.z = z;
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
}
