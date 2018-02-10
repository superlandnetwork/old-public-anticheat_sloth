package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;

public class FlyingEvent
  extends Event
{
  private boolean ground;
  
  public FlyingEvent(PlayerData player, boolean cancelled, boolean ground)
  {
    super(player, cancelled, "FlyingEvent");
    this.ground = ground;
  }
  
  public boolean isOnGround()
  {
    return this.ground;
  }
}
