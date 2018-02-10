package com.hydrapvp.sloth.checks.manager;

import com.hydrapvp.sloth.SlothPlugin;
import com.hydrapvp.sloth.api.API;
import com.hydrapvp.sloth.checks.Check;
import com.hydrapvp.sloth.checks.combat.Combined;
import com.hydrapvp.sloth.checks.combat.HitMissRatio;
import com.hydrapvp.sloth.checks.combat.NoSwing;
import com.hydrapvp.sloth.checks.combat.killaura.TypeD;
import com.hydrapvp.sloth.checks.combat.killaura.TypeE;
import com.hydrapvp.sloth.checks.combat.killaura.TypeF;
import com.hydrapvp.sloth.checks.movement.Enderpearl;
import com.hydrapvp.sloth.checks.movement.Fly;
import com.hydrapvp.sloth.checks.movement.Freecam;
import com.hydrapvp.sloth.checks.movement.Jesus;
import com.hydrapvp.sloth.checks.movement.Morepackets;
import com.hydrapvp.sloth.checks.movement.Phase;
import com.hydrapvp.sloth.checks.movement.Speed;
import com.hydrapvp.sloth.checks.movement.Step;
import java.util.ArrayList;
import java.util.List;

public class CheckManager
{
  @SuppressWarnings({ "unchecked", "rawtypes" })
private List<Check> checks = new ArrayList();
  
  public void init()
  {
    API api = SlothPlugin.getAPI();
    addCheck(new Fly());
    addCheck(new Step());
    addCheck(new Speed());
    addCheck(new Morepackets());
    addCheck(new HitMissRatio());
    addCheck(new Combined());
    addCheck(new NoSwing());
    addCheck(new Phase());
    addCheck(new Jesus());
    addCheck(new Enderpearl());
    addCheck(new Freecam());
    addCheck(new com.hydrapvp.sloth.checks.combat.autoclicker.TypeA());
    addCheck(new com.hydrapvp.sloth.checks.combat.autoclicker.TypeB());
    addCheck(new com.hydrapvp.sloth.checks.combat.autoclicker.TypeC());
    addCheck(new com.hydrapvp.sloth.checks.combat.killaura.TypeA());
    addCheck(new com.hydrapvp.sloth.checks.combat.killaura.TypeB());
    addCheck(new com.hydrapvp.sloth.checks.combat.killaura.TypeC());
    addCheck(new TypeD());
    addCheck(new TypeE());
    addCheck(new TypeF());
    for (Check c : this.checks)
    {
      api.getEventManager().addCheckListener(c);
      c.init();
    }
  }
  
  public void destroy() {}
  
  private void addCheck(Check e)
  {
    this.checks.add(e);
  }
  
  public List<Check> getChecks()
  {
    return this.checks;
  }
}