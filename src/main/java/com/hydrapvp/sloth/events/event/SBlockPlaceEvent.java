package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;

public class SBlockPlaceEvent
  extends Event
{
  public SBlockPlaceEvent(PlayerData player, boolean cancelled)
  {
    super(player, cancelled, "FlyingEvent");
  }
}
