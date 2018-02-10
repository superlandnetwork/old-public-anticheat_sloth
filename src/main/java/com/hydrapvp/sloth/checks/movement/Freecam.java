package com.hydrapvp.sloth.checks.movement;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.InteractEvent;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;

public class Freecam
  extends Check
  implements IEventListener
{
  public Freecam()
  {
    super("Freecam");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    super.init();
  }
  
  @SuppressWarnings("deprecation")
public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if ((e instanceof InteractEvent))
    {
      boolean isValid = false;
      
      Location scanLocation = ((InteractEvent)e).getBlock().getBlock().getRelative(((InteractEvent)e).getBlockFace()).getLocation();
      double x = scanLocation.getX();
      double y = scanLocation.getY();
      double z = scanLocation.getZ();
      for (double sX = x; sX < x + 2.0D; sX += 1.0D) {
        for (double sY = y; sY < y + 2.0D; sY += 1.0D) {
          for (double sZ = z; sZ < z + 2.0D; sZ += 1.0D)
          {
            Location relative = new Location(scanLocation.getWorld(), sX, sY, sZ);
            List<Location> blocks = rayTrace(player.getLocation(), relative);
            boolean valid = true;
            for (Location l : blocks) {
              if (!player.checkPhase(l.getBlock().getType())) {
                valid = false;
              }
            }
            if (valid) {
              isValid = true;
            }
          }
        }
      }
      if ((!isValid) && 
        (!player.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL))) {
        e.setCancelled(true);
      }
    }
  }
  
  private List<Location> rayTrace(Location from, Location to)
  {
    @SuppressWarnings({ "unchecked", "rawtypes" })
	List<Location> a = new ArrayList();
    if ((from == null) || (to == null)) {
      return a;
    }
    if (!from.getWorld().equals(to.getWorld())) {
      return a;
    }
    if (from.distance(to) > 10.0D) {
      return a;
    }
    double x1 = from.getX();
    double y1 = from.getY() + 1.62D;
    double z1 = from.getZ();
    double x2 = to.getX();
    double y2 = to.getY();
    double z2 = to.getZ();
    
    boolean scanning = true;
    while (scanning)
    {
      a.add(new Location(from.getWorld(), x1, y1, z1));
      x1 += (x2 - x1) / 10.0D;
      y1 += (y2 - y1) / 10.0D;
      z1 += (z2 - z1) / 10.0D;
      if ((Math.abs(x1 - x2) < 0.01D) && (Math.abs(y1 - y2) < 0.01D) && (Math.abs(z1 - z2) < 0.01D)) {
        scanning = false;
      }
    }
    return a;
  }
  
  public void onEvent(Event e)
  {
    if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".Enabled")) {
      runCheck(e);
    }
  }
}
