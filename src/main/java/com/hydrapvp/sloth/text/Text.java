package com.hydrapvp.sloth.text;

public class Text
{
  private String root;
  private String name;
  private String text;
  
  protected Text(String root, String name, String value)
  {
    this.root = root;
    this.name = name;
    this.text = value;
  }
  
  public String getRoot()
  {
    return this.root;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setText(String text)
  {
    this.text = text;
  }
  
  public String getText()
  {
    return this.text;
  }
}
