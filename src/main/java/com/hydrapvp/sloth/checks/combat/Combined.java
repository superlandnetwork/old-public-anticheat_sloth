package com.hydrapvp.sloth.checks.combat;

import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.data.PlayerData;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.events.event.MovementEvent;

public class Combined
  extends Check
  implements IEventListener
{
  public Combined()
  {
    super("Combined");
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
    if ((e instanceof MovementEvent))
    {
      double acVar1 = player.acTypeBVL * 20.0D;
      double acVar2 = player.acTypeCVL / 15.0D;
      double kaVar1 = player.kaTypeAFails * 6.6D;
      double kaVar2 = player.kaTypeBVL * 3.5D;
      double kaVar3 = player.kaTypeCVL * 10.0D;
      double kaVar4 = player.kaTypeDVL * 4;
      double hmr1 = player.hmrRatioConsistancyVL * 4;
      double hmr2 = player.hmrPacketVL * 15;
      double hmr3 = player.hmrLookVL * 8;
      double dc = player.acTypeBDoubleClickVL / 5;
      if (dc < 0.0D) {
        dc = 0.0D;
      }
      double totalVL = acVar1 + kaVar1 + kaVar2 + kaVar3 + kaVar4 + hmr1 + hmr2 + hmr3 + dc + acVar2;
      if (totalVL > 150.0D)
      {
        player.combinedVL += 1;
        player.fail("Combat Hacks", "Combined");
        if ((player.combinedVL > 5) && 
          (this.api.getConfiguration().readBoolean("checks." + getCheckName() + ".AutoBan"))) {
          player.addBanVL("Combat Hacks", totalVL / 10000.0D);
        }
        if (player.combinedVL > 10)
        {
          player.acTypeBVL *= 0.9D;
          player.kaTypeAFails *= 0.9D;
          player.kaTypeBVL *= 0.9D;
          player.kaTypeEHits -= 10;
          if (player.kaTypeEHits < 0) {
            player.kaTypeEHits = 0;
          }
          player.kaTypeCVL *= 0.9D;
          player.kaTypeDVL -= 1;
          if (player.kaTypeDVL < 0) {
            player.kaTypeDVL = 0;
          }
          player.hmrRatioConsistancyVL -= 1;
          if (player.hmrRatioConsistancyVL < 0) {
            player.hmrRatioConsistancyVL = 0;
          }
          player.hmrPacketVL -= 1;
          if (player.hmrPacketVL < 0) {
            player.hmrPacketVL = 0;
          }
          if (player.acTypeBDoubleClickVL > 0) {
            player.acTypeBDoubleClickVL -= 1;
          }
          if (player.hmrLookVL > 0) {
            player.hmrLookVL -= 1;
          }
          player.combinedVL = 0;
        }
      }
      else
      {
        player.combinedVL = 0;
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
