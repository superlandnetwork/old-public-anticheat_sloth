package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;

public class SwingEvent
  extends Event
{
  public SwingEvent(PlayerData player, boolean cancelled)
  {
    super(player, cancelled, "SwingEvent");
  }
}
