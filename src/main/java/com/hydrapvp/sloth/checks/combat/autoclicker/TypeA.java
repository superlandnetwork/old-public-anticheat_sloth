package com.hydrapvp.sloth.checks.combat.autoclicker;

import org.bukkit.event.block.Action;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.ASyncMovementEvent;
import com.hydrapvp.sloth.events.event.FlyingEvent;
import com.hydrapvp.sloth.events.event.InteractEvent;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;
import com.hydrapvp.sloth.utils.SmallAverageCollector;

public class TypeA extends Check implements IEventListener {
  public TypeA() {
    super("Autoclicker-TypeA");
  }
  
  public void init() {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "false"));
    this.config.add(new Config("checks." + getCheckName(), "MaxCPS", "18"));
    super.init();
  }
  
  public void runCheck(Event e) {
    PlayerData player = e.getPlayer();
    if (player.acTypeACollectorLC == null) {
      player.acTypeACollectorLC = new SmallAverageCollector(5);
    }
    if (player.acTypeACollectorRC == null) {
      player.acTypeACollectorRC = new SmallAverageCollector(5);
    }
    if ((e instanceof InteractEvent)) {
      Action action = ((InteractEvent)e).getAction();
      if ((action.equals(Action.RIGHT_CLICK_AIR)) || (action.equals(Action.RIGHT_CLICK_BLOCK))) {
        player.acTypeAleftclicks -= 1;
        if (player.acTypeAleftclicks < 0) {
          player.acTypeAleftclicks = 0;
        }
        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) {
          player.acTypeArightclicks += 1;
        }
      }
    }
    if ((e instanceof UseEntityEvent)) {
      player.acTypeAleftclicks += 1;
    }
    if (((e instanceof FlyingEvent)) || ((e instanceof ASyncMovementEvent))) {
      double delay = Math.abs(System.currentTimeMillis() - player.acTypeALastPacketTime);
      if ((delay < 60000.0D) && (delay > 100.0D)) {
        player.acTypeAFreedomLC += delay / 50.0D * 2.0D;
        player.acTypeAFreedomRC += delay / 50.0D * 2.0D; PlayerData 
          tmp217_216 = player;tmp217_216.acTypeATotalLag = ((int)(tmp217_216.acTypeATotalLag + delay));
      }
      if (delay <= 50.0D) {
        PlayerData tmp237_236 = player;tmp237_236.acTypeATotalLag = ((int)(tmp237_236.acTypeATotalLag - Math.abs(delay - 50.0D)));
        if (player.acTypeATotalLag <= 0)  {
          player.acTypeAFreedomLC *= 0.5D;
          player.acTypeAFreedomRC *= 0.5D;
          player.acTypeATotalLag = 0;
        }
      }
      player.acTypeALastPacketTime = System.currentTimeMillis();
    }
    if ((((e instanceof MovementEvent)) || ((e instanceof FlyingEvent)) || ((e instanceof ASyncMovementEvent))) && 
      (System.currentTimeMillis() > player.checkCPS)) {
      player.checkCPS = (System.currentTimeMillis() + 1000L);
      int lcps = player.acTypeAleftclicks;
      int rcps = player.acTypeArightclicks;
      
      player.acTypeACollectorLC.add(lcps);
      player.acTypeACollectorRC.add(rcps);
      
      player.acTypeAleftclicks = 0;
      player.acTypeArightclicks = 0;
      if (lcps > this.api.getConfiguration().readInteger("checks." + getCheckName() + ".MaxCPS")) {
        player.acTypeAFreedomLC -= Math.abs(lcps - this.api.getConfiguration().readInteger("checks." + getCheckName() + ".MaxCPS"));
        if (player.acTypeAFreedomLC < 0.0D) {
          player.setCPS(lcps);
          player.fail("an Autoclicker", lcps + " CPS");
          if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
            player.addBanVL("Autoclicker", 0.8D);
          }
        }
        player.acTypeACollectorLC.clear(); 
      
    } else {
        player.acTypeAFreedomLC *= 0.999D;
      }
      if (rcps > this.api.getConfiguration().readInteger("checks." + getCheckName() + ".MaxCPS")) {
        player.acTypeAFreedomRC -= Math.abs(rcps - this.api.getConfiguration().readInteger("checks." + getCheckName() + ".MaxCPS"));
        if (player.acTypeAFreedomRC < 0.0D) {
          player.fail("an Autoclicker", rcps + " CPS(R)");
        }
        player.acTypeACollectorRC.clear();
      }
      else
      {
        player.acTypeAFreedomRC *= 0.999D;
      }
    }
  }
  
  public void onEvent(Event e) {
    if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".Enabled")) {
      runCheck(e);
    }
  }
}
