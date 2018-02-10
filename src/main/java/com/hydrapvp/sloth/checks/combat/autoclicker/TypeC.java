package com.hydrapvp.sloth.checks.combat.autoclicker;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.SwingEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;
import com.hydrapvp.sloth.utils.SmallAverageCollector;

public class TypeC
  extends Check
  implements IEventListener
{
  public TypeC()
  {
    super("Autoclicker-TypeC");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "false"));
    super.init();
  }
  
  public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if ((e instanceof UseEntityEvent)) {
      player.acTypeClastHitTime = System.currentTimeMillis();
    }
    if (((e instanceof SwingEvent)) && 
      (Math.abs(player.acTypeClastHitTime - System.currentTimeMillis()) < 2000L))
    {
      player.acTypeCLastSwingTime = player.acTypeCCurrentSwingTime;
      player.acTypeCCurrentSwingTime = System.currentTimeMillis();
      player.acTypeCLastSwingDelay = player.acTypeCCurrentSwingDelay;
      player.acTypeCCurrentSwingDelay = Math.abs(player.acTypeCLastSwingTime - player.acTypeCCurrentSwingTime);
      double difference = Math.abs(player.acTypeCLastSwingDelay - player.acTypeCCurrentSwingDelay);
      if (player.acTypeCCollector == null) {
        player.acTypeCCollector = new SmallAverageCollector(200);
      }
      if (difference < 200.0D)
      {
        if (difference >= 33.0D) {
          difference *= difference / 33.0D;
        }
        player.acTypeCCollector.add(difference);
      }
      if (player.acTypeCCollector.size() > 190)
      {
        double average = player.acTypeCCollector.getAverage();
        if (average < 60.0D)
        {
          if (Math.abs(player.acTypeCLastAverage - average) < 1.0D) {
            player.acTypeCVL += Math.abs(average - 60.0D) / 5.0D;
          } else {
            player.acTypeCVL /= 8.0D;
          }
          player.acTypeCLastAverage = average;
        }
      }
      if (player.acTypeCVL > 1000.0D)
      {
        player.fail("an Autoclicker", "Consistent");
        if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
          player.addBanVL("Autoclicker", 0.025D);
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
