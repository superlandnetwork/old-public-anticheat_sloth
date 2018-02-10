package com.hydrapvp.sloth.events.event;

import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import org.bukkit.entity.LivingEntity;

public class AttackEvent
  extends Event
{
  private LivingEntity target;
  
  public AttackEvent(PlayerData player, boolean cancelled, LivingEntity target)
  {
    super(player, cancelled, "AttackEvent");
    this.target = target;
  }
  
  public LivingEntity getTarget()
  {
    return this.target;
  }
}
