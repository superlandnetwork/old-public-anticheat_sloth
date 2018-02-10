package com.hydrapvp.sloth.config.values.autoban;

import com.hydrapvp.sloth.config.Config;

public class AutoBanMessage
  extends Config
{
  public AutoBanMessage()
  {
    super("AutoBan", "Message", "&c[ACH] Hacked Client (%reason%)");
  }
}
