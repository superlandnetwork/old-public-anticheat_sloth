package com.hydrapvp.sloth.events;

import com.hydrapvp.sloth.events.Event;

public abstract interface IEventListener
{
  public abstract void onEvent(Event paramEvent);
}
