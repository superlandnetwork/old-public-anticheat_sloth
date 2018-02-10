package com.hydrapvp.sloth.listener;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.hydrapvp.sloth.SlothPlugin;
import com.hydrapvp.sloth.api.API;
import com.hydrapvp.sloth.events.event.ASyncMovementEvent;
import com.hydrapvp.sloth.events.event.FlyingEvent;
import com.hydrapvp.sloth.events.event.SwingEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;
import com.hydrapvp.sloth.events.event.VelocityEvent;

public class ProtocolLibListener
{
    private final JavaPlugin plugin;
    
    public ProtocolLibListener(final JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void init() {
        final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        final API api = SlothPlugin.getAPI();
        final PacketListener flying = (PacketListener)new PacketAdapter(api.getPlugin(), ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.FLYING }) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType().equals((Object)PacketType.Play.Client.FLYING)) {
                    final boolean ground = event.getPacket().getBooleans().getValues().get(0);
                    final FlyingEvent fe = new FlyingEvent(SlothPlugin.getAPI().getDataManager().getPlayerDataByPlayer(event.getPlayer()), event.isCancelled(), ground);
                    api.getEventManager().hook(fe);
                }
            }
        };
        final PacketListener look = (PacketListener)new PacketAdapter(api.getPlugin(), ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.LOOK }) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType().equals((Object)PacketType.Play.Client.LOOK)) {
                    final boolean ground = event.getPacket().getBooleans().getValues().get(0);
                    final FlyingEvent fe = new FlyingEvent(SlothPlugin.getAPI().getDataManager().getPlayerDataByPlayer(event.getPlayer()), event.isCancelled(), ground);
                    api.getEventManager().hook(fe);
                }
            }
        };
        final PacketListener velocity = (PacketListener)new PacketAdapter(api.getPlugin(), ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Server.ENTITY_VELOCITY }) {
            public void onPacketSending(final PacketEvent event) {
                if (event.getPacketType().equals((Object)PacketType.Play.Server.ENTITY_VELOCITY) && api.getServerVersion() == 1.8) {
                    double x = event.getPacket().getIntegers().getValues().get(1);
                    double y = event.getPacket().getIntegers().getValues().get(2);
                    double z = event.getPacket().getIntegers().getValues().get(3);
                    x /= 8000.0;
                    y /= 8000.0;
                    z /= 8000.0;
                    final VelocityEvent ve = new VelocityEvent(SlothPlugin.getAPI().getDataManager().getPlayerDataByPlayer(event.getPlayer()), false, x, y, z);
                    api.getEventManager().hook(ve);
                }
            }
        };
        final PacketListener velocity2 = (PacketListener)new PacketAdapter(api.getPlugin(), ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Server.EXPLOSION }) {
            public void onPacketSending(final PacketEvent event) {
                if (event.getPacketType().equals((Object)PacketType.Play.Server.EXPLOSION)) {
                    final float x = event.getPacket().getFloat().getValues().get(1);
                    final float y = event.getPacket().getFloat().getValues().get(2);
                    final float z = event.getPacket().getFloat().getValues().get(3);
                    final VelocityEvent ve = new VelocityEvent(SlothPlugin.getAPI().getDataManager().getPlayerDataByPlayer(event.getPlayer()), false, x, y, z);
                    api.getEventManager().hook(ve);
                }
            }
        };
        final PacketListener armAnimation = (PacketListener)new PacketAdapter(api.getPlugin(), ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.ARM_ANIMATION }) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType().equals((Object)PacketType.Play.Client.ARM_ANIMATION)) {
                    final SwingEvent se = new SwingEvent(SlothPlugin.getAPI().getDataManager().getPlayerDataByPlayer(event.getPlayer()), event.isCancelled());
                    api.getEventManager().hook(se);
                }
            }
        };
        final PacketListener pos = (PacketListener)new PacketAdapter(api.getPlugin(), ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.POSITION }) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType().equals((Object)PacketType.Play.Client.POSITION)) {
                    final double x = event.getPacket().getDoubles().getValues().get(0);
                    final double y = event.getPacket().getDoubles().getValues().get(1);
                    final double z = event.getPacket().getDoubles().getValues().get(2);
                    final boolean ground = event.getPacket().getBooleans().getValues().get(0);
                    final ASyncMovementEvent e = new ASyncMovementEvent(SlothPlugin.getAPI().getDataManager().getPlayerDataByPlayer(event.getPlayer()), event.isCancelled(), x, y, z, ground);
                    api.getEventManager().hook(e);
                }
            }
        };
        final PacketListener poslook = (PacketListener)new PacketAdapter(api.getPlugin(), ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.POSITION_LOOK }) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType().equals((Object)PacketType.Play.Client.POSITION_LOOK)) {
                    final double x = event.getPacket().getDoubles().getValues().get(0);
                    final double y = event.getPacket().getDoubles().getValues().get(1);
                    final double z = event.getPacket().getDoubles().getValues().get(2);
                    final boolean ground = event.getPacket().getBooleans().getValues().get(0);
                    final ASyncMovementEvent e = new ASyncMovementEvent(SlothPlugin.getAPI().getDataManager().getPlayerDataByPlayer(event.getPlayer()), event.isCancelled(), x, y, z, ground);
                    api.getEventManager().hook(e);
                }
            }
        };
        final PacketListener useEntity = (PacketListener)new PacketAdapter(api.getPlugin(), ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Client.USE_ENTITY }) {
            @SuppressWarnings("deprecation")
			public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType().equals((Object)PacketType.Play.Client.USE_ENTITY) && !event.getPlayer().getItemInHand().getType().equals((Object)Material.FISHING_ROD)) {
                    final UseEntityEvent uee = new UseEntityEvent(SlothPlugin.getAPI().getDataManager().getPlayerDataByPlayer(event.getPlayer()), event.isCancelled());
                    api.getEventManager().hook(uee);
                }
            }
        };
        manager.addPacketListener(flying);
        manager.addPacketListener(armAnimation);
        manager.addPacketListener(useEntity);
        manager.addPacketListener(velocity);
        manager.addPacketListener(pos);
        manager.addPacketListener(poslook);
        manager.addPacketListener(look);
        manager.addPacketListener(velocity2);
    }
    
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
}
