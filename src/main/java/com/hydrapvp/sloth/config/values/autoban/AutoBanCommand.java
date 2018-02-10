package com.hydrapvp.sloth.config.values.autoban;

import com.hydrapvp.sloth.config.Config;

public class AutoBanCommand
  extends Config
{
  public AutoBanCommand()
  {
    super("AutoBan", "Command", "ban %player% %message%");
  }
}
