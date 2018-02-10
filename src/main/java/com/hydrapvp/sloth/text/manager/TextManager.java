package com.hydrapvp.sloth.text.manager;

import com.hydrapvp.sloth.text.Text;
import com.hydrapvp.sloth.text.values.core.NoLogsFound;
import com.hydrapvp.sloth.text.values.core.NoPermission;
import com.hydrapvp.sloth.text.values.core.SlothAlreadyDisabled;
import com.hydrapvp.sloth.text.values.core.SlothAlreadyEnabled;
import com.hydrapvp.sloth.text.values.core.SlothBanMessage;
import com.hydrapvp.sloth.text.values.core.SlothBanNoPlayer;
import com.hydrapvp.sloth.text.values.core.SlothBanNotBanning;
import com.hydrapvp.sloth.text.values.core.SlothBanStoppedBan;
import com.hydrapvp.sloth.text.values.core.SlothDisabled;
import com.hydrapvp.sloth.text.values.core.SlothEnabled;
import com.hydrapvp.sloth.text.values.tps.TPSBroadcast;
import com.hydrapvp.sloth.text.values.tps.TPSDisabled;
import com.hydrapvp.sloth.text.values.tps.TPSEnabled;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;

public class TextManager
{
  @SuppressWarnings({ "unchecked", "rawtypes" })
private List<Text> text = new ArrayList();
  
  public void init()
  {
    addText(new TPSEnabled());
    addText(new TPSDisabled());
    addText(new TPSBroadcast());
    addText(new NoPermission());
    addText(new SlothAlreadyDisabled());
    addText(new SlothAlreadyEnabled());
    addText(new SlothDisabled());
    addText(new SlothEnabled());
    addText(new SlothBanMessage());
    addText(new SlothBanStoppedBan());
    addText(new SlothBanNotBanning());
    addText(new SlothBanNoPlayer());
    addText(new NoLogsFound());
  }
  
  public void destroy() {}
  
  private String color(String text)
  {
    return ChatColor.translateAlternateColorCodes('&', text);
  }
  
  public String getString(String path)
  {
    for (Text t : this.text) {
      if ((t.getRoot() + "." + t.getName()).equalsIgnoreCase(path)) {
        return color(t.getText());
      }
    }
    return null;
  }
  
  private void addText(Text text)
  {
    this.text.add(text);
  }
  
  public List<Text> getText()
  {
    return this.text;
  }
}