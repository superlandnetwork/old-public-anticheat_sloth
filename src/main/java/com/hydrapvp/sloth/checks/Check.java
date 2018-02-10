package com.hydrapvp.sloth.checks;

import com.hydrapvp.sloth.SlothPlugin;
import com.hydrapvp.sloth.api.API;
import com.hydrapvp.sloth.config.Config;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;

import java.util.ArrayList;
import java.util.List;

public class Check
  implements IEventListener
{
  private final String checkName;
  @SuppressWarnings({ "unchecked", "rawtypes" })
protected List<Config> config = new ArrayList();
  protected API api;
  
  protected Check(String checkName)
  {
    this.checkName = checkName;
  }
  
  public void init()
  {
    this.api = SlothPlugin.getAPI();
  }
  
  public void destroy() {}
  
  protected String getCheckName()
  {
    return this.checkName;
  }
  
  public void runCheck(Event event) {}
  
  public List<Config> getConfig()
  {
    return this.config;
  }
  
  public void onEvent(Event e) {}
}
