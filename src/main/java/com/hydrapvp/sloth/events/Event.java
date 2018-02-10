package com.hydrapvp.sloth.events;

import com.hydrapvp.sloth.data.PlayerData;

public class Event
{
  private boolean cancelled;
  private final PlayerData player;
  private String name;
  
  protected Event(PlayerData player, boolean cancelled, String name)
  {
    this.name = name;
    this.cancelled = cancelled;
    this.player = player;
  }
  
  public boolean isCancelled()
  {
    return this.cancelled;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public PlayerData getPlayer()
  {
    return this.player;
  }
  
  public void setCancelled(boolean b)
  {
    this.cancelled = b;
  }
}
