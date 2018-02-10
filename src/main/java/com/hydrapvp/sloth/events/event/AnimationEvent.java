package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;

public class AnimationEvent
  extends Event
{
  public AnimationEvent(PlayerData player, boolean cancelled)
  {
    super(player, cancelled, "AnimationEvent");
  }
}
