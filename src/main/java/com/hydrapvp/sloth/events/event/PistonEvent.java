package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import java.util.List;
import org.bukkit.block.Block;

public class PistonEvent
  extends Event
{
  private List<Block> blocks;
  
  public PistonEvent(PlayerData player, boolean cancelled, List<Block> list)
  {
    super(player, cancelled, "PistonEvent");
    this.blocks = list;
  }
  
  public List<Block> getBlocks()
  {
    return this.blocks;
  }
}
