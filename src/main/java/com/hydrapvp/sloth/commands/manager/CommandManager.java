package com.hydrapvp.sloth.commands.manager;

import com.hydrapvp.sloth.commands.Command;
import com.hydrapvp.sloth.commands.SlothCommand;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;

public class CommandManager
{
  private List<Command> commands = new ArrayList<>();
  
  public void init()
  {
    addCommand(new SlothCommand());
  }
  
  public void destroy() {}
  
  private List<Command> getCommands()
  {
    return this.commands;
  }
  
  private void addCommand(Command e)
  {
    getCommands().add(e);
  }
  
  public void handleCommand(CommandSender sender, String label, String[] args)
  {
    for (Command c : getCommands()) {
      if (c.getLabel().equalsIgnoreCase(label)) {
        c.onCommand(sender, args);
      }
    }
  }
}
