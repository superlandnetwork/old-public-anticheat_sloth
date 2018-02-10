package com.hydrapvp.sloth.data;

import org.bukkit.Material;
import com.hydrapvp.sloth.utils.StorageUtils;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import com.hydrapvp.sloth.events.event.MovementEvent;
import com.hydrapvp.sloth.events.event.SwingEvent;
import com.hydrapvp.sloth.events.event.AttackEvent;
import com.hydrapvp.sloth.events.event.UseEntityEvent;
import org.bukkit.event.block.Action;
import com.hydrapvp.sloth.events.event.InteractEvent;
import com.hydrapvp.sloth.events.event.FlyingEvent;
import com.hydrapvp.sloth.events.event.ASyncMovementEvent;
import com.hydrapvp.sloth.events.event.TeleportEvent;
import com.hydrapvp.sloth.events.event.VehicleEvent;
import com.hydrapvp.sloth.events.Event;
import com.hydrapvp.sloth.events.IEventListener;
import com.hydrapvp.sloth.utils.TrigUtils;
import com.hydrapvp.sloth.SlothPlugin;
import java.util.ArrayList;
import java.util.List;
import com.hydrapvp.sloth.utils.Timer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import org.bukkit.Location;
import com.hydrapvp.sloth.utils.AverageCollector;
import com.hydrapvp.sloth.utils.SmallAverageCollector;
import com.hydrapvp.sloth.api.API;
import org.bukkit.entity.Player;


public class PlayerData implements IEventListener
{
    private Player player;
    private API api;
    public long kaTypeBLastMoveTime;
    public int kaTypeBExcusedCooldown;
    public int combinedBVL;
    public long acTypeCLastSwingTime;
    public long acTypeCCurrentSwingTime;
    public long acTypeCLastSwingDelay;
    public long acTypeCCurrentSwingDelay;
    public SmallAverageCollector acTypeCCollector;
    public double acTypeCLastAverage;
    public double acTypeCMaxVL;
    public long acTypeClastHitTime;
    public int kaTypeEAimVL;
    public int kaTypeEHits;
    public int kaTypeEMisses;
    public long kaTypeECurrentSwingTime;
    public long kaTypeELastSwingTime;
    public long kaTypeECurrentDelay;
    public long kaTypeELastDelay;
    public AverageCollector kaTypeEConsistantCollector;
    public int kaTypeEDirectionVL;
    public double fullVL;
    public int hmrLookHits;
    public int hmrLookMisses;
    public int hmrLookVL;
    public int kaTypeBPackets;
    public long acTypeBLastSwingTime;
    public int acTypeBDoubleClickVL;
    public double acTypeBLastDelay;
    public float hmrLastYaw;
    public float hmrCurrentYaw;
    public float hmrYawDelta;
    public int hmrYawPackets;
    public long hmrLastHitTime;
    public boolean hmrInBattle;
    public long acTypeBLastPacketTime;
    public int kaTypeEVL;
    public int kaTypeFMisses;
    public int kaTypeFHits;
    public int kaTypeFOverall;
    public SmallAverageCollector kaTypeFReachCollector;
    public SmallAverageCollector kaTypeFViolationCollector;
    public int kaTypeFVL;
    private boolean ground;
    private boolean web;
    private boolean ladder;
    private boolean vehicle;
    private boolean vine;
    private boolean water;
    private boolean lava;
    private boolean slime;
    private boolean piston;
    private boolean ice;
    private boolean underBlock;
    private boolean playerGround;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private double lastX;
    private double lastY;
    private double lastZ;
    private float lastYaw;
    private float lastPitch;
    private double xDelta;
    private double yDelta;
    private double zDelta;
    private float yawDelta;
    private float pitchDelta;
    private Location currentLocation;
    private Location previousLocation;
    private Location currentTargetLocation;
    private Location previousTargetLocation;
    public Location blockGlitchSetbackLocation;
    private World currentWorld;
    private World previousWorld;
    private LivingEntity target;
    private LivingEntity lastTarget;
    private int clicks;
    private int swings;
    private int cps;
    private double lastReachDistance;
    private int hits;
    private int misses;
    private long lastPacketTime;
    private long lastPacketID;
    private String detectedHack;
    private String detectedHackData;
    private Timer cpsTimer;
    private Timer attackTimer;
    private Timer coolDownTimer;
    private Timer checkTimer;
    private Timer addTimer;
    public double jumpHeight;
    public double flyVL;
    public double fallSpeed;
    public int airTicks;
    public double glideVL;
    public double yFreedom;
    public int fallTicks;
    @SuppressWarnings("unused")
	private int allowanceVL;
    public String lastTickOrdinal;
    public boolean flySlime;
    public double stepVL;
    public double hFreedom;
    public double lastSpeedLowJumpVL;
    public double speedLowJumpVL;
    public int bunnyHopDelay;
    public double speedVL;
    public double packetBuffer;
    public boolean noSwingHasSwung;
    public double noSwingVL;
    private double banVL;
    private String banCommand;
    private long banTime;
    private long actualBanTime;
    public boolean autobanning;
    private boolean notifiedAutoban;
    private long autobanCooldown;
    private int cpsPackets;
    @SuppressWarnings("unused")
	private double lag;
    private Timer packetTimer;
    private int lagPackets;
    private Location teleportLocation;
    private AverageCollector lagCollector;
    private List<String> failedQueue;
    public int hmrPackets;
    public double hmrYawTotal;
    public double hmrSpeedTotal;
    public int hmrYawCumulator;
    public double hmrSpeedCumulator;
    public int hmrPacketSwings;
    public int hmrPacketHits;
    public int hmrCamVL;
    public float aimbotBuffer;
    public int aimbotSwings;
    public long directionLastPacketTime;
    public Timer mpPacketTimer;
    public int mpPackets;
    public double mpVL;
    public long acLastSwingCPS;
    public double acincvl;
    public double acLastCPS;
    public double acConsBuf;
    public float lastYawDelta;
    public double trigLookDifs;
    public double trigNotLookDifs;
    public int trigSwings;
    public long trigLastSwingCPS;
    public double trigVL;
    public double lookVL;
    public int lookBuffer;
    public int aimbotPackets;
    public int notLookBuffer;
    public float totalYawDelta;
    public long aimbotLastPacketTime;
    public long trigLastPacketTime;
    public double failVL;
    public int combinedVL;
    public int hmrpacketBuffer;
    public double lastTargetSpeed;
    public double hmrLastTargetSpeed;
    public float hmrLastYawDelta;
    public double hmrLastPlayerSpeed;
    public float lastAimbotYaw;
    public float totalAimbot;
    public float exactLastAimbotYaw;
    public float totalAimbotYaw;
    public float totalAimbotDifference;
    public int aimbotVL;
    public int hmrPacketMisses;
    public float aimbotLastYawDelta;
    public double lastRatio;
    public int hmrRatioConsistancyVL;
    public int hmrPacketVL;
    public double combinedMax;
    public float aimbotTotalYaw;
    public long ignorePlayerLeniency;
    private boolean inBlock;
    public boolean wasInBlock;
    public int noclipVL;
    public int jesusVL;
    public long lastPhaseTime;
    public boolean lastOnGround;
    public Location phaseSetback;
    public Location locationFromBlock;
    public int ticksOnGround;
    public int hoverVL;
    public int flyActions;
    public double lastDeltaY;
    public int acFailTimes;
    public boolean wasPistoned;
    public boolean pistonSlime;
    public int pistonTicks;
    public int pistonedTicks;
    public int speedPhaseFlags;
    public int acinccooldown;
    public int bigCPSVL;
    public int genericACVL;
    public int directionCooldown;
    @SuppressWarnings("unused")
	private int acInteractions;
    public int epearlvl;
    public int blockCancelled;
    public int vehicleTicks;
    public long checkCPS;
    public int acTypeAleftclicks;
    public int acTypeArightclicks;
    public SmallAverageCollector acTypeACollectorRC;
    public SmallAverageCollector acTypeACollectorLC;
    public int glideFlags;
    public long acTypeALastPacketTime;
    public double acTypeAFreedomRC;
    public double acTypeAFreedomLC;
    public int acTypeBSwings;
    public double acTypeBSwingFullCPS;
    public double acTypeBVL;
    public double acTypeCVL;
    public int acTypeATotalLag;
    public double kaTypeAVL;
    public double kaTypeAFails;
    public double kaTypeBVL;
    public float kaTypeATotalYawDelta;
    public double kaTypeATotalTheoreticalYawDelta;
    public double kaTypeALastAimValue;
    public int kaTypeATicks;
    public long kaTypeALastSwingTime;
    public int kaTypeATicksNotMoving;
    public long kaTypeBLastHitTime;
    public float kaTypeCTotalYawDelta;
    public long kaTypeCLastHitTime;
    public double kaTypeCVL;
    public int kaTypeCStandingTicks;
    public int kaTypeDVL;
    public long kaTypeDLastHitTime;
    public long kaTypeBLastPacketTime;
    public double kaTypeBExcusedViolations;
    public double kaTypeBLastHDist;
    public int acTypeCLastCPS;
    public SmallAverageCollector combinedCollector;
    public long acTypeBLastHitTime;
    
    public PlayerData(final Player player) {
        this.notifiedAutoban = true;
        this.failedQueue = new ArrayList<String>();
        this.player = player;
        this.api = SlothPlugin.getAPI();
        this.api.getEventManager().addPlayerListener(this);
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public void addBanVL(final String reason, double banVL) {
        if (banVL > 0.3) {
            banVL = 0.3;
        }
        this.banVL += banVL;
        this.autobanCooldown = System.currentTimeMillis() + 5000L;
        if (this.banVL > this.api.getConfiguration().readDouble("AutoBan.VL")) {
            this.autoBan(reason);
            this.banVL = 0.0;
        }
    }
    
    public boolean inWeb() {
        return this.web;
    }
    
    public boolean onLadder() {
        return this.ladder;
    }
    
    public boolean inVehicle() {
        return this.vehicle;
    }
    
    public boolean onVine() {
        return this.vine;
    }
    
    public boolean inWater() {
        return this.water;
    }
    
    public boolean inLava() {
        return this.lava;
    }
    
    public boolean isUnderBlock() {
        return this.underBlock;
    }
    
    public boolean onIce() {
        return this.ice;
    }
    
    public boolean onSlime() {
        return this.slime;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public double getDeltaX() {
        return this.xDelta;
    }
    
    public double getDeltaY() {
        return this.yDelta;
    }
    
    public double getDeltaZ() {
        return this.zDelta;
    }
    
    public float getDeltaYaw() {
        return Math.abs(Math.abs(TrigUtils.wrapAngleTo180_float(this.getYaw())) - Math.abs(TrigUtils.wrapAngleTo180_float(this.getLastYaw())));
    }
    
    public float getDeltaPitch() {
        return this.pitchDelta;
    }
    
    public Location getLocation() {
        return this.currentLocation;
    }
    
    public Location getLastLocation() {
        return this.previousLocation;
    }
    
    public World getWorld() {
        return this.currentWorld;
    }
    
    public World getLastWorld() {
        return this.previousWorld;
    }
    
    public LivingEntity getTarget() {
        return this.target;
    }
    
    public boolean isOnGround() {
        return this.ground;
    }
    
    public double getLastReachDistance() {
        return this.lastReachDistance;
    }
    
    public int getHits() {
        return this.hits;
    }
    
    public LivingEntity getLastTarget() {
        return this.lastTarget;
    }
    
    public int getMisses() {
        return this.misses;
    }
    
    public long getLastPacketID() {
        return this.lastPacketID;
    }
    
    public long getLastPacketTime() {
        return this.lastPacketTime;
    }
    
    public int getCPS() {
        return this.cps;
    }
    
    public boolean isPlayerOnGround() {
        return this.playerGround;
    }
    
    private void setPositionAndUpdate(final double x, final double y, final double z) {
        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        this.x = x;
        this.y = y;
        this.z = z;
        if (this.lastX != 0.0) {
            this.xDelta = this.x - this.lastX;
        }
        if (this.lastY != 0.0) {
            this.yDelta = this.y - this.lastY;
        }
        if (this.lastZ != 0.0) {
            this.zDelta = this.z - this.lastZ;
        }
        if (Math.abs(this.xDelta) > 10.0) {
            this.xDelta = 0.0;
        }
        if (Math.abs(this.zDelta) > 10.0) {
            this.zDelta = 0.0;
        }
        if (Math.abs(this.yDelta) > 10.0) {
            this.yDelta = 0.0;
        }
    }
    
    private void setRotationsAndUpdate(final float yaw, final float pitch) {
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        this.yaw = yaw;
        this.pitch = pitch;
        this.setYawDelta(Math.abs(this.yaw - this.lastYaw));
        this.pitchDelta = Math.abs(this.pitch - this.lastPitch);
    }
    
    private void updateLocation(final Location location) {
        if (location == null) {
            return;
        }
        if (this.currentLocation == null) {
            this.currentLocation = location;
        }
        if (this.previousLocation == null) {
            this.previousLocation = location;
        }
        if (this.currentTargetLocation == null && this.getTarget() != null) {
            this.currentTargetLocation = this.getTarget().getLocation();
        }
        if (this.previousTargetLocation == null && this.currentTargetLocation != null) {
            this.previousTargetLocation = this.currentTargetLocation;
        }
        this.previousLocation = this.currentLocation;
        this.previousTargetLocation = this.currentTargetLocation;
        this.previousWorld = this.currentLocation.getWorld();
        this.setPositionAndUpdate(location.getX(), location.getY(), location.getZ());
        this.setRotationsAndUpdate(location.getYaw(), location.getPitch());
        this.currentLocation = location;
        this.currentWorld = location.getWorld();
        if (this.getTarget() != null) {
            this.currentTargetLocation = this.getTarget().getLocation();
        }
    }
    
    private void autoBan(final String reason) {
        if (this.api.getConfiguration().readBoolean("AutoBan.Enabled") && !this.autobanning) {
            final String banMessage = this.color(this.api.getConfiguration().readString("AutoBan.Message").replace("%reason%", reason));
            this.banCommand = this.color(this.api.getConfiguration().readString("AutoBan.Command").replace("%player%", this.getPlayer().getName()).replace("%message%", banMessage));
            this.banTime = this.api.getConfiguration().readInteger("AutoBan.Delay") * 1000;
            this.actualBanTime = System.currentTimeMillis() + this.banTime;
            this.autobanning = true;
            this.notifiedAutoban = false;
        }
    }
    
    private void resetData(final double x, final double y, final double z) {
        this.ground = true;
        this.playerGround = true;
        this.web = false;
        this.ladder = false;
        this.vehicle = false;
        this.vine = false;
        this.water = false;
        this.lava = false;
        this.slime = false;
        this.piston = false;
        this.ice = false;
        this.underBlock = false;
        this.x = x;
        this.y = y;
        this.z = z;
        this.lastX = 0.0;
        this.lastY = 0.0;
        this.lastZ = 0.0;
        this.lastYaw = 0.0f;
        this.lastPitch = 0.0f;
        this.xDelta = 0.0;
        this.yDelta = 0.0;
        this.zDelta = 0.0;
        this.setYawDelta(0.0f);
        this.pitchDelta = 0.0f;
        this.currentLocation = null;
        this.previousLocation = null;
        this.currentTargetLocation = null;
        this.previousTargetLocation = null;
        this.currentWorld = null;
        this.previousWorld = null;
        this.target = null;
        this.lastTarget = null;
        this.jumpHeight = 0.0;
        this.yFreedom = 0.0;
        this.flyVL = 0.0;
        this.fallSpeed = 0.0;
        this.airTicks = 0;
        this.glideVL = 0.0;
        this.yFreedom += 1.3;
        this.fallTicks = 0;
        this.allowanceVL = 0;
        this.lastTickOrdinal = "Neutral";
        this.flySlime = false;
        this.stepVL = 0.0;
        this.hFreedom = 0.0;
        this.lastSpeedLowJumpVL = 0.0;
        this.speedLowJumpVL = 0.0;
        this.bunnyHopDelay = 0;
        this.speedVL = 0.0;
    }
    
    public boolean isTeleporting() {
        return this.teleportLocation != null;
    }
    
    @Override
    public void onEvent(final Event e) {
        if (!e.getPlayer().equals(this)) {
            return;
        }
        if (e instanceof VehicleEvent) {
            this.vehicleTicks = 10;
        }
        if (e instanceof TeleportEvent) {
            this.teleportLocation = ((TeleportEvent)e).getTo();
        }
        if (e instanceof ASyncMovementEvent && this.teleportLocation != null) {
            final double x = ((ASyncMovementEvent)e).getX();
            final double y = ((ASyncMovementEvent)e).getY();
            final double z = ((ASyncMovementEvent)e).getZ();
            final double tX = this.teleportLocation.getX();
            final double tY = this.teleportLocation.getY();
            final double tZ = this.teleportLocation.getZ();
            final double difX = Math.abs(x - tX);
            final double difY = Math.abs(y - tY);
            final double difZ = Math.abs(z - tZ);
            if (difX == 0.0 && difZ == 0.0 && difY < 1.0E-5) {
                this.teleportLocation = null;
                this.resetData(x, y, z);
            }
            return;
        }
        if (e instanceof FlyingEvent || e instanceof ASyncMovementEvent) {
            ++this.lagPackets;
            if (this.lagCollector == null) {
                this.lagCollector = new AverageCollector();
            }
            if (this.packetTimer != null) {
                if (this.packetTimer.hasReached(1000L)) {
                    this.packetTimer.reset();
                    double lag = Math.abs(this.lagPackets / 20);
                    if (this.packetTimer.getTime() > 1000.0) {
                        lag -= this.packetTimer.getTime() / 1000.0;
                    }
                    this.lagCollector.add(lag);
                    if (this.lagCollector.getDataAmount() > 1) {
                        this.lag = this.lagCollector.getAverage();
                        this.lagCollector.clear();
                    }
                    this.lagPackets = 0;
                }
            }
            else {
                this.packetTimer = new Timer();
            }
            this.lastPacketTime = System.currentTimeMillis();
            ++this.lastPacketID;
            ++this.cpsPackets;
            if (this.attackTimer == null) {
                this.attackTimer = new Timer();
            }
            if (this.attackTimer.hasReached(5000L) && this.target != null) {
                this.attackTimer.reset();
                this.target = null;
                this.lastReachDistance = 0.0;
                this.cps = 0;
                this.hits = 0;
                this.misses = 0;
            }
            if (System.currentTimeMillis() > this.autobanCooldown) {
                this.autobanCooldown = System.currentTimeMillis() + 5000L;
                this.banVL *= 0.9;
            }
            if (this.cpsTimer != null) {
                if (this.cpsTimer.hasReached(1000L)) {
                    this.cpsTimer.reset();
                    int rightClicks = this.clicks - this.swings;
                    if (rightClicks < 0) {
                        rightClicks = 0;
                    }
                    this.cps = this.swings;
                    this.acInteractions = 0;
                    if (this.cpsPackets < 20) {
                        this.cps -= Math.abs(this.cpsPackets - 20);
                    }
                    if (this.cps < 0) {
                        this.cps = 0;
                    }
                    this.cpsPackets = 0;
                    this.clicks = 0;
                    this.swings = 0;
                }
            }
            else {
                this.cpsTimer = new Timer();
            }
        }
        if (e instanceof InteractEvent && ((InteractEvent)e).getAction().equals((Object)Action.LEFT_CLICK_BLOCK)) {
            this.swings -= 2;
        }
        if (e instanceof UseEntityEvent) {
            ++this.clicks;
            ++this.hits;
            --this.misses;
            if (this.misses < 0) {
                this.misses = 0;
            }
        }
        if (e instanceof AttackEvent) {
            this.lastTarget = this.target;
            final LivingEntity target = ((AttackEvent)e).getTarget();
            if (target instanceof Player) {
                this.target = target;
                if (target.getWorld() != null && this.player.getWorld() != null && target.getWorld().equals(this.player.getWorld())) {
                    this.lastReachDistance = target.getLocation().distance(this.player.getLocation());
                }
                if (this.attackTimer == null) {
                    this.attackTimer = new Timer();
                }
                this.attackTimer.reset();
                if (!this.target.equals(this.lastTarget)) {
                    this.hits = 0;
                    this.misses = 0;
                }
            }
            else {
                this.target = null;
            }
        }
        if (e instanceof SwingEvent) {
            ++this.swings;
            ++this.misses;
        }
        if (e instanceof MovementEvent) {
            --this.vehicleTicks;
            if (this.vehicleTicks < 0) {
                this.vehicleTicks = 0;
            }
            if (!this.notifiedAutoban && this.banTime > 0L) {
                final BaseComponent[] formattedMessage = new ComponentBuilder(ChatColor.RESET.toString()).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sloth stopban " + this.getPlayer().getName())).append(this.api.getTextManager().getString("core.SlothBanMessage").replace("%seconds%", "" + this.banTime / 1000L).replace("%player%", this.getPlayer().getName())).create();
                for (final Player p : this.api.getPlugin().getServer().getOnlinePlayers()) {
                    if (p.hasPermission("sloth.notify") || p.isOp()) {
                        p.spigot().sendMessage(formattedMessage);
                    }
                }
                this.notifiedAutoban = true;
            }
            if (this.autobanning && System.currentTimeMillis() > this.actualBanTime) {
                SlothPlugin.getAPI().getPlugin().getServer().dispatchCommand((CommandSender)SlothPlugin.getAPI().getPlugin().getServer().getConsoleSender(), this.banCommand);
                final String broadcastMessage = ChatColor.translateAlternateColorCodes('&', this.api.getConfiguration().readString("AutoBan.BroadcastMessage").replace("%player%", this.player.getName()));
                if (broadcastMessage.length() > 0) {
                    for (final Player p : this.api.getPlugin().getServer().getOnlinePlayers()) {
                        p.sendMessage(broadcastMessage);
                    }
                }
                this.autobanning = false;
            }
            this.updateLocation(((MovementEvent)e).getTo());
            this.updateTerrain();
            if (!this.isInBlock() && !this.player.getLocation().getBlock().getType().isSolid() && this.isOnGround() && Math.abs(this.lastPhaseTime - System.currentTimeMillis()) > 2000L) {
                this.phaseSetback = this.player.getLocation();
            }
            if (this.checkTimer == null) {
                this.checkTimer = new Timer();
            }
            if (this.checkTimer.hasReached(this.api.getConfiguration().readInteger("core.NotifyDelayMS"))) {
                this.checkTimer.reset();
                if (this.failedQueue == null) {
                    this.failedQueue = new ArrayList<String>();
                }
                if (this.failedQueue.size() > 0) {
                    final String queueMessage = this.failedQueue.get(0);
                    this.detectedHack = queueMessage.split(",")[0].replace("%%comma%%", ",");
                    this.detectedHackData = queueMessage.split(",")[1].replace("%%comma%%", ",");
                    this.api.getDataManager().legacyNotifyCheck(this);
                    this.failedQueue.remove(0);
                }
            }
        }
        if (e instanceof ASyncMovementEvent) {
            this.playerGround = ((ASyncMovementEvent)e).isOnGround();
            if (this.coolDownTimer == null) {
                this.coolDownTimer = new Timer();
            }
            if (this.coolDownTimer.hasReached(this.api.getConfiguration().readInteger("core.CoolDownDelay"))) {
                this.flyVL *= 0.99;
                this.glideVL *= 0.99;
                this.stepVL *= 0.99;
                this.speedVL *= 0.99;
                this.coolDownTimer.reset();
            }
        }
    }
    
    private String color(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public void fail(String hackName, String data) {
        if (hackName.contains(",")) {
            hackName = hackName.replace(",", "%%comma%%");
        }
        if (data.contains(",")) {
            data = data.replace(",", "%%comma%%");
        }
        if (this.addTimer == null) {
            this.addTimer = new Timer();
        }
        if (hackName.equals("Fly") || hackName.equals("Phase") || hackName.equals("Step")) {
            StorageUtils.log(this.player, new SimpleDateFormat("MM/dd/yy hh:mm a").format(new Date()) + " EST," + hackName + "@");
        }
        else {
            StorageUtils.log(this.player, new SimpleDateFormat("MM/dd/yy hh:mm a").format(new Date()) + " EST," + hackName + " (" + data + ")@");
        }
        if (this.addTimer.hasReached(600L)) {
            this.addTimer.reset();
            this.failedQueue.add(hackName + "," + data);
        }
    }
    
    @SuppressWarnings("deprecation")
	private void updateTerrain() {
        if (this.getWorld() == null) {
            return;
        }
        final boolean b = false;
        this.underBlock = b;
        this.ice = b;
        this.piston = b;
        this.slime = b;
        this.lava = b;
        this.water = b;
        this.vine = b;
        this.vehicle = b;
        this.ladder = b;
        this.web = b;
        this.inBlock = b;
        this.ground = b;
        for (double x = -0.3; x <= 0.3; x += 0.3) {
            for (double z = -0.3; z <= 0.3; z += 0.3) {
                final Location playerLocation = this.player.getLocation();
                final Material belowPlayer = playerLocation.clone().add(x, -1.0E-13, z).getBlock().getType();
                final Material liquidCheck = playerLocation.clone().add(x, 0.0625, z).getBlock().getType();
                final Material specialGround = playerLocation.clone().add(x, -0.5000000000001, z).getBlock().getType();
                final Material abovePlayer = playerLocation.clone().add(x, 2.2, z).getBlock().getType();
                if (!this.ground) {
                    this.ground = (this.checkSolid(belowPlayer) || this.isSpecialGround(specialGround));
                }
                if (!this.web) {
                    this.web = (belowPlayer.equals((Object)Material.WEB) || abovePlayer.equals((Object)Material.WEB));
                }
                if (!this.ladder) {
                    this.ladder = (belowPlayer.equals((Object)Material.LADDER) || abovePlayer.equals((Object)Material.LADDER));
                }
                if (!this.vine) {
                    this.vine = (belowPlayer.equals((Object)Material.VINE) || abovePlayer.equals((Object)Material.VINE));
                }
                if (!this.water) {
                    this.water = (liquidCheck.equals((Object)Material.STATIONARY_WATER) || liquidCheck.equals((Object)Material.WATER) || abovePlayer.equals((Object)Material.STATIONARY_WATER) || abovePlayer.equals((Object)Material.WATER));
                }
                if (!this.lava) {
                    this.lava = (liquidCheck.equals((Object)Material.STATIONARY_LAVA) || liquidCheck.equals((Object)Material.LAVA) || abovePlayer.equals((Object)Material.STATIONARY_LAVA) || abovePlayer.equals((Object)Material.LAVA));
                }
                if (!this.slime) {
                    this.slime = (belowPlayer.getId() == 165);
                }
                if (!this.piston) {
                    this.piston = (belowPlayer.equals((Object)Material.PISTON_BASE) || belowPlayer.equals((Object)Material.PISTON_EXTENSION) || belowPlayer.equals((Object)Material.PISTON_MOVING_PIECE) || belowPlayer.equals((Object)Material.PISTON_STICKY_BASE));
                }
                if (!this.ice) {
                    this.ice = (belowPlayer.equals((Object)Material.ICE) || belowPlayer.equals((Object)Material.PACKED_ICE));
                }
                if (!this.underBlock) {
                    this.underBlock = this.checkSolid(abovePlayer);
                }
                if (!this.inBlock) {
                    this.inBlock = (this.checkSolid(belowPlayer) && !this.checkPhase(liquidCheck));
                }
            }
        }
        this.vehicle = (this.player.getVehicle() != null);
        if (this.ground) {
            this.blockGlitchSetbackLocation = this.getLocation();
        }
    }
    
    public boolean checkLiquid(final Material m) {
        final Material[] array;
        @SuppressWarnings("unused")
		final Material[] solids = array = new Material[] { Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA };
        for (final Material mat : array) {
            if (mat.equals((Object)m)) {
                return true;
            }
        }
        return m.isSolid();
    }
    
    @SuppressWarnings({ "deprecation", "unused" })
	public boolean checkPhase(final Material m) {
        final int[] array;
        final int[] whitelist = array = new int[] { 355, 196, 194, 197, 195, 193, 64, 96, 187, 184, 186, 107, 185, 183, 192, 189, 139, 191, 85, 101, 190, 113, 188, 160, 102, 163, 157, 0, 145, 49, 77, 135, 108, 67, 164, 136, 114, 156, 180, 128, 143, 109, 134, 53, 126, 44, 416, 8, 425, 138, 26, 397, 372, 13, 135, 117, 108, 39, 81, 92, 71, 171, 141, 118, 144, 54, 139, 67, 127, 59, 115, 330, 164, 151, 178, 32, 28, 93, 94, 175, 122, 116, 130, 119, 120, 51, 140, 147, 154, 148, 136, 65, 10, 69, 31, 105, 114, 372, 33, 34, 36, 29, 90, 142, 27, 104, 156, 66, 40, 330, 38, 180, 149, 150, 75, 76, 55, 128, 6, 295, 323, 63, 109, 78, 88, 134, 176, 11, 9, 44, 70, 182, 83, 50, 146, 132, 131, 106, 177, 68, 8, 111, 30, 72, 53, 126, 37 };
        for (final int ids : array) {
            if (m.getId() == ids) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkSolid(final Material m) {
        final Material[] array;
        @SuppressWarnings("unused")
		final Material[] solids = array = new Material[] { Material.SNOW, Material.SNOW_BLOCK, Material.CARPET, Material.DIODE, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.SKULL, Material.SKULL_ITEM, Material.LADDER, Material.WATER_LILY };
        for (final Material mat : array) {
            if (mat.equals((Object)m)) {
                return true;
            }
        }
        return m.isSolid();
    }
    
    @SuppressWarnings("deprecation")
	private boolean isSpecialGround(final Material material) {
        final int[] array;
        @SuppressWarnings("unused")
		final int[] specialGroundIDs = array = new int[] { 85, 188, 189, 190, 191, 192, 113, 107, 183, 184, 185, 186, 187, 139, 65 };
        for (final int i : array) {
            if (i == material.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public String getDetectedHack() {
        return this.detectedHack;
    }
    
    public String getDetectedHackData() {
        return this.detectedHackData;
    }
    
    public double getLastHorizontalDistance() {
        if (this.lastX == 0.0 || this.lastZ == 0.0 || this.x == 0.0 || this.z == 0.0) {
            return 0.0;
        }
        return TrigUtils.getDistance(this.x, this.z, this.lastX, this.lastZ);
    }
    
    public double getLastTargetHorizontalDistance() {
        if (this.getTarget() instanceof Player) {
            final PlayerData pd = this.api.getDataManager().getPlayerDataByPlayer((Player)this.getTarget());
            return pd.getLastHorizontalDistance();
        }
        return 0.28;
    }
    
    public boolean isInBlock() {
        return this.inBlock;
    }
    
    public boolean setInBlock(final boolean inBlock) {
        return this.inBlock = inBlock;
    }
    
    public void setWorld(final World world) {
        this.currentWorld = world;
    }
    
    private float getLastYaw() {
        return this.lastYaw;
    }
    
    public float getUnusedYawDelta() {
        return this.yawDelta;
    }
    
    private void setYawDelta(final float yawDelta) {
        this.yawDelta = yawDelta;
    }
    
    public void setCPS(final int CPS) {
        this.cps = CPS;
    }
    
    public double getBanVL() {
        return this.banVL;
    }
}
