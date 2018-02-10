package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;

public class VehicleEvent
  extends Event
{
  public VehicleEvent(PlayerData player, boolean cancelled)
  {
    super(player, cancelled, "VehicleEvent");
  }
}
