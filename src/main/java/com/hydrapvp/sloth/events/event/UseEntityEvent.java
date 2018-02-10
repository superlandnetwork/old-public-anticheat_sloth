package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;

public class UseEntityEvent
  extends Event
{
  public UseEntityEvent(PlayerData player, boolean cancelled)
  {
    super(player, cancelled, "UseEntityEvent");
  }
}
