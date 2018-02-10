package com.hydrapvp.sloth.checks.combat;

import org.bukkit.entity.Player;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.AttackEvent;
import com.hydrapvp.sloth.events.event.SwingEvent;

public class NoSwing
  extends Check
  implements IEventListener
{
  public NoSwing()
  {
    super("NoSwing");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "true"));
    super.init();
  }
  
  public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if ((e instanceof SwingEvent)) {
      player.noSwingHasSwung = true;
    }
    if (((e instanceof AttackEvent)) && 
      (player.getTarget() != null) && ((player.getTarget() instanceof Player))) {
      if (player.noSwingHasSwung)
      {
        player.noSwingHasSwung = false;
        player.noSwingVL *= 0.3D;
      }
      else
      {
        player.noSwingVL += 1.0D;
        if (player.noSwingVL > 40.0D)
        {
          player.fail("Kill Aura", "Not Swinging Arm!");
          if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
            player.addBanVL("Kill Aura", 0.9D);
          }
        }
      }
    }
  }
  
  public void onEvent(Event e)
  {
    if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".Enabled")) {
      runCheck(e);
    }
  }
}
