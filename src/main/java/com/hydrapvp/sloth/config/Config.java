package com.hydrapvp.sloth.config;

public class Config
{
    private final String name;
    private final String root;
    private Object value;
    
    public Config(final String root, final String name, final Object value) {
        this.name = name;
        this.root = root;
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getRoot() {
        return this.root;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void updateValue(final Object value) {
        this.value = value;
    }
}