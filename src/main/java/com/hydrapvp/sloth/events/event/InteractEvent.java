package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;

public class InteractEvent
  extends Event
{
  private Location block;
  private Action action;
  private BlockFace sentFace;
  
  public InteractEvent(PlayerData player, boolean cancelled, Action action, Location block, BlockFace sentFace)
  {
    super(player, cancelled, "InteractEvent");
    this.action = action;
    this.block = block;
    this.sentFace = sentFace;
  }
  
  public BlockFace getBlockFace()
  {
    return this.sentFace;
  }
  
  public Location getBlock()
  {
    return this.block;
  }
  
  public Action getAction()
  {
    return this.action;
  }
}
