package com.hydrapvp.sloth.listener;

import com.hydrapvp.sloth.SlothPlugin;
import com.hydrapvp.sloth.api.API;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.event.AnimationEvent;
import com.hydrapvp.sloth.events.event.AttackEvent;
import com.hydrapvp.sloth.events.event.InteractEvent;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.PistonEvent;
import com.hydrapvp.sloth.events.event.SBlockPlaceEvent;
import com.hydrapvp.sloth.events.event.TeleportEvent;
import com.hydrapvp.sloth.events.event.VehicleEvent;
import com.hydrapvp.sloth.events.event.VelocityEvent;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
public class BukkitListener
  implements Listener
{
  private API api;
  
  public BukkitListener()
  {
    this.api = SlothPlugin.getAPI();
  }
  
  private PlayerData getPlayer(Player player)
  {
    return this.api.getDataManager().getPlayerDataByPlayer(player);
  }
  
  @EventHandler
  public void onTeleport(PlayerTeleportEvent event)
  {
    TeleportEvent te = new TeleportEvent(getPlayer(event.getPlayer()), false, event.getFrom(), event.getTo(), event.getCause());
    this.api.getEventManager().hook(te);
    if (te.isCancelled()) {
      event.setCancelled(true);
    }
  }
  
  @EventHandler
  public void onPiston(BlockPistonExtendEvent event)
  {
    Location location = event.getBlock().getLocation();
    for (Player player : this.api.getPlugin().getServer().getOnlinePlayers()) {
      if ((player.getLocation().getWorld().equals(location.getWorld())) && 
        (player.getLocation().distance(location) < 5.0D))
      {
        PistonEvent pe = new PistonEvent(getPlayer(player), event.isCancelled(), event.getBlocks());
        this.api.getEventManager().hook(pe);
      }
    }
  }
  
  @EventHandler
  public void onRespawn(PlayerRespawnEvent event)
  {
    this.api.getDataManager().removePlayer(event.getPlayer());
  }
  
  @EventHandler
  public void onWorldChange(PlayerChangedWorldEvent event)
  {
    TeleportEvent te = new TeleportEvent(getPlayer(event.getPlayer()), false, getPlayer(event.getPlayer()).getLastLocation(), event.getPlayer().getLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
    this.api.getEventManager().hook(te);
  }
  
  @EventHandler
  public void onVehicleLeave(VehicleExitEvent event)
  {
    if ((event.getExited() instanceof Player))
    {
      VehicleEvent ve = new VehicleEvent(getPlayer((Player)event.getExited()), event.isCancelled());
      this.api.getEventManager().hook(ve);
    }
  }
  
  @EventHandler
  public void onVelocity(PlayerVelocityEvent event)
  {
    if (this.api.getServerVersion() == 1.7D)
    {
      double x = event.getVelocity().getX();
      double y = event.getVelocity().getY();
      double z = event.getVelocity().getZ();
      
      VelocityEvent ve = new VelocityEvent(getPlayer(event.getPlayer()), event.isCancelled(), x, y, z);
      this.api.getEventManager().hook(ve);
    }
  }
  
  @EventHandler
  public void onMove(PlayerMoveEvent event)
  {
    if ((!event.isAsynchronous()) && (!event.isCancelled()))
    {
      MovementEvent me = new MovementEvent(getPlayer(event.getPlayer()), event.isCancelled(), event.getTo());
      this.api.getEventManager().hook(me);
    }
  }
  
  @EventHandler
  public void onPlace(BlockPlaceEvent event)
  {
    boolean cancelled = event.isCancelled();
    if ((!cancelled) && 
      (!event.canBuild())) {
      cancelled = true;
    }
    SBlockPlaceEvent bpe = new SBlockPlaceEvent(getPlayer(event.getPlayer()), cancelled);
    this.api.getEventManager().hook(bpe);
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent event)
  {
    PlayerData player = this.api.getDataManager().getPlayerDataByPlayer(event.getPlayer());
    MovementEvent me = new MovementEvent(player, false, event.getPlayer().getLocation());
    this.api.getEventManager().hook(me);
  }
  
  @EventHandler
  public void onAnimation(PlayerAnimationEvent event)
  {
    PlayerData player = this.api.getDataManager().getPlayerDataByPlayer(event.getPlayer());
    AnimationEvent ae = new AnimationEvent(player, false);
    this.api.getEventManager().hook(ae);
  }
  
  @EventHandler
  public void onHit(EntityDamageByEntityEvent event)
  {
    if (((event.getDamager() instanceof Player)) && ((event.getEntity() instanceof LivingEntity)))
    {
      AttackEvent ae = new AttackEvent(getPlayer((Player)event.getDamager()), event.isCancelled(), (LivingEntity)event.getEntity());
      this.api.getEventManager().hook(ae);
    }
  }
  
  @EventHandler
  public void onInteract(PlayerInteractEvent event)
  {
    if (event.getClickedBlock() != null)
    {
      InteractEvent ie = new InteractEvent(getPlayer(event.getPlayer()), event.isCancelled(), event.getAction(), event.getClickedBlock().getLocation(), event.getBlockFace());
      this.api.getEventManager().hook(ie);
      if (ie.isCancelled()) {
        event.setCancelled(true);
      }
    }
  }
}