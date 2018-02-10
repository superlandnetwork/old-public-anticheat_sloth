package com.hydrapvp.sloth.commands;

import org.bukkit.command.CommandSender;

public class Command
{
  private String label;
  
  Command(String label)
  {
    this.label = label;
  }
  
  public String getLabel()
  {
    return this.label;
  }
  
  public void onCommand(CommandSender sender, String[] args) {}
}
