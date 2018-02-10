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
import com.hydrapvp.sloth.events.event.SwingEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;

public class TypeB
  extends Check
  implements IEventListener
{
  public TypeB()
  {
    super("Autoclicker-TypeB");
  }
  
  public void init()
  {
    this.config.add(new Config("checks." + getCheckName(), "Enabled", "true"));
    this.config.add(new Config("checks." + getCheckName(), "AutoBan", "false"));
    this.config.add(new Config("checks." + getCheckName(), "MaxCPS", "18"));
    super.init();
  }
  
  public void runCheck(Event e)
  {
    PlayerData player = e.getPlayer();
    if (((e instanceof InteractEvent)) && 
      (((InteractEvent)e).getAction().equals(Action.LEFT_CLICK_BLOCK)))
    {
      player.acTypeBSwingFullCPS -= 300.0D;
      if (player.acTypeBSwingFullCPS < 0.0D) {
        player.acTypeBSwingFullCPS = 0.0D;
      }
    }
    if (((e instanceof FlyingEvent)) || ((e instanceof ASyncMovementEvent)))
    {
      if (Math.abs(player.acTypeBLastPacketTime - System.currentTimeMillis()) > 80L)
      {
        player.acTypeBDoubleClickVL -= 2;
        if (player.acTypeBDoubleClickVL < -50) {
          player.acTypeBDoubleClickVL = -50;
        }
      }
      player.acTypeBLastPacketTime = System.currentTimeMillis();
    }
    if ((e instanceof UseEntityEvent)) {
      player.acTypeBLastHitTime = System.currentTimeMillis();
    }
    if ((e instanceof SwingEvent))
    {
      if (Math.abs(player.acTypeBLastHitTime - System.currentTimeMillis()) < 200L)
      {
        double delay = Math.abs(player.acTypeBLastSwingTime - System.currentTimeMillis());
        player.acTypeBLastSwingTime = System.currentTimeMillis();
        if ((delay < 50.0D) && (player.acTypeBLastDelay > 50.0D))
        {
          player.acTypeBDoubleClickVL += 4;
        }
        else
        {
          player.acTypeBDoubleClickVL -= 1;
          if (player.acTypeBDoubleClickVL < -50) {
            player.acTypeBDoubleClickVL = -50;
          }
        }
        player.acTypeBLastDelay = delay;
        if (player.acTypeBDoubleClickVL >= 100)
        {
          player.fail("Double Click", "*");
          if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
            player.addBanVL("Autoclicker", 0.08D);
          }
        }
      }
      else if (Math.abs(player.acTypeBLastHitTime - System.currentTimeMillis()) > 2000L)
      {
        player.acTypeBDoubleClickVL = 0;
      }
      if (Math.abs(player.acTypeBLastHitTime - System.currentTimeMillis()) < 2000L)
      {
        player.acTypeBSwings += 1;
        if (Math.abs(player.acLastSwingCPS - System.currentTimeMillis()) > 0L)
        {
          double lastSwingCPS = 1000.0D / Math.abs(player.acLastSwingCPS - System.currentTimeMillis());
          if (lastSwingCPS <= 500.0D) {
            player.acTypeBSwingFullCPS += lastSwingCPS;
          }
        }
        if (player.acTypeBSwings > this.api.getConfiguration().readInteger("checks." + getCheckName() + ".MaxCPS"))
        {
          if (player.acTypeBSwingFullCPS > 1500.0D)
          {
            player.acTypeBVL += Math.abs(player.acTypeBSwingFullCPS - 1000.0D) / 100.0D;
            if (player.acTypeBVL > 10.0D)
            {
              player.fail("an Autoclicker", "Spikes in CPS");
              if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
                player.addBanVL("Autoclicker", 0.2D);
              }
              player.acTypeBVL = 5.0D;
            }
          }
          else
          {
            player.acTypeBVL -= 1.5D;
            if (player.acTypeBVL < 0.0D) {
              player.acTypeBVL = 0.0D;
            }
          }
          player.acTypeBSwingFullCPS = 0.0D;
          player.acTypeBSwings = 0;
        }
        player.acLastSwingCPS = System.currentTimeMillis();
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
