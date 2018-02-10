package com.hydrapvp.sloth.checks.movement;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.ASyncMovementEvent;
import com.hydrapvp.sloth.events.event.FlyingEvent;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.TeleportEvent;
import com.hydrapvp.sloth.utils.Timer;

public class Morepackets
  extends Check
  implements IEventListener
{
  public Morepackets()
  {
    super("Morepackets");
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
    if (player.mpPacketTimer == null) {
      player.mpPacketTimer = new Timer();
    }
    if ((e instanceof TeleportEvent)) {
      player.mpPackets -= 2;
    }
    if (((e instanceof MovementEvent)) && 
      (e.isCancelled())) {
      player.mpPackets -= 2;
    }
    if (((e instanceof ASyncMovementEvent)) || ((e instanceof FlyingEvent)))
    {
      player.mpPackets += 1;
      if (player.mpPackets > 20)
      {
        double time = player.mpPacketTimer.getTime();
        player.mpPacketTimer.reset();
        player.mpPackets = 0;
        if (time < 100000.0D) {
          if (time > 1000.0D) {
            player.packetBuffer += Math.abs(time - 1000.0D);
          } else {
            player.packetBuffer -= Math.abs(time - 1000.0D);
          }
        }
        if (Math.abs(time - 1000.0D) < 300.0D) {
          player.packetBuffer *= 0.8D;
        }
        if (player.packetBuffer < -200.0D)
        {
          player.mpVL += Math.abs(player.packetBuffer) / 400.0D;
          if (player.mpVL > 5.0D)
          {
            player.fail("Phase/Regen/Speed/Other", "Packet Frequency");
            if (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan")) {
              player.addBanVL("Phase/Regen/Speed/Other", Math.abs(player.packetBuffer) / 20000.0D);
            }
          }
          player.packetBuffer = 0.0D;
        }
        else
        {
          player.mpVL -= 1.0D;
          if (player.mpVL < 0.0D) {
            player.mpVL = 0.0D;
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
