package com.hydrapvp.sloth.events.manager;

import java.util.concurrent.CopyOnWriteArrayList;
import com.hydrapvp.sloth.events.event.TeleportEvent;
import com.hydrapvp.sloth.SlothPlugin;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;

import java.util.List;

public class EventManager
{
    private List<IEventListener> playerListeners;
    private List<IEventListener> checkListeners;
    
    public void addPlayerListener(final IEventListener listener) {
        if (this.playerListeners == null) {
            throw new IllegalStateException("Listener was accessed before it was initialized!");
        }
        if (this.playerListeners.contains(listener)) {
            throw new IllegalStateException("Can not add existing listener!");
        }
        this.playerListeners.add(listener);
    }
    
    public void removePlayerListener(final IEventListener listener) {
        if (this.playerListeners == null) {
            throw new IllegalStateException("Listener was accessed before it was initialized!");
        }
        if (!this.playerListeners.contains(listener)) {
            throw new IllegalStateException("Cannot remove a listener that doesn't exist!");
        }
        this.playerListeners.remove(listener);
    }
    
    public void addCheckListener(final IEventListener listener) {
        if (this.checkListeners == null) {
            throw new IllegalStateException("Listener was accessed before it was initialized!");
        }
        if (this.checkListeners.contains(listener)) {
            throw new IllegalStateException("Can not add existing listener!");
        }
        this.checkListeners.add(listener);
    }
    
    public void removeCheckListener(final IEventListener listener) {
        if (this.checkListeners == null) {
            throw new IllegalStateException("Listener was accessed before it was initialized!");
        }
        if (!this.checkListeners.contains(listener)) {
            throw new IllegalStateException("Cannot remove a listener that doesn't exist!");
        }
        this.checkListeners.remove(listener);
    }
    
    public void hook(final Event event) {
        if (SlothPlugin.getAPI().isPluginEnabled()) {
            if (event.getPlayer() == null) {
                return;
            }
            event.getPlayer().onEvent(event);
            if ((event.getPlayer().getLocation() != null && !event.getPlayer().isTeleporting()) || event instanceof TeleportEvent) {
                this.checkListeners.stream().forEach(listener -> listener.onEvent(event));
            }
        }
    }
    
    public void init() {
        this.playerListeners = new CopyOnWriteArrayList<IEventListener>();
        this.checkListeners = new CopyOnWriteArrayList<IEventListener>();
    }
}