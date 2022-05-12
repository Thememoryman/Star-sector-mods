package data.scripts.plugins;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.IntervalUtil;

import org.lwjgl.util.vector.Vector2f;

public class LTA_EpattcudXIUpcycled extends BaseEveryFrameCombatPlugin {
    
    private CombatEngineAPI engine;
    
    private final IntervalUtil DUMBER = new IntervalUtil(1.5f, 1.5f);   
    
    private final IntervalUtil interval = new IntervalUtil(1.5f, 1.5f);
    private float timer = 0f;       //Timer
    private float duration = 0.05f; //Duration of the effect
    
    private static final String LTA_XILeftAssaultShipID = "LTA_Epattcudxi_Upcycled_DroneLeft_Assault";
    private static final String LTA_XILeftAssaultProjectileID = "LTA_upcycledlampreyleftassaultspec";
    
    private static final String LTA_XILeftStrikeShipID = "LTA_Epattcudxi_Upcycled_DroneLeft_Strike";
    private static final String LTA_XILeftStrikeProjectileID = "LTA_upcycledlampreyleftstrikespec";

    private static final String LTA_XILeftSupportShipID = "LTA_Epattcudxi_Upcycled_DroneLeft_Support";
    private static final String LTA_XILeftSupportProjectileID = "LTA_upcycledlampreyleftsupportspec";
    
    private static final String LTA_XILeftLostShipID = "LTA_Epattcudxi_Upcycled_DroneLeft_Lost";
    private static final String LTA_XILeftLostProjectileID = "LTA_upcycledlampreyleftlostspec";
    
    private static final String LTA_XIRightAssaultShipID = "LTA_Epattcudxi_Upcycled_DroneRight_Assault";
    private static final String LTA_XIRightAssaultProjectileID = "LTA_upcycledlampreyrightassaultspec";
    
    private static final String LTA_XIRightStrikeShipID = "LTA_Epattcudxi_Upcycled_DroneRight_Strike";
    private static final String LTA_XIRightStrikeProjectileID = "LTA_upcycledlampreyrightstrikespec";

    private static final String LTA_XIRightSupportShipID = "LTA_Epattcudxi_Upcycled_DroneRight_Support";
    private static final String LTA_XIRightSupportProjectileID = "LTA_upcycledlampreyrightsupportspec";
    
    private static final String LTA_XIRightLostShipID = "LTA_Epattcudxi_Upcycled_DroneRight_Lost";
    private static final String LTA_XIRightLostProjectileID = "LTA_upcycledlampreyrightlostspec";
    
    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (engine == null) {
            return;
        }
        if (engine.isPaused()) {
            return;
        }        
                                                                                              
        List<DamagingProjectileAPI> projectiles = engine.getProjectiles();
        List<DamagingProjectileAPI> projectiles_copy = new ArrayList(projectiles);

        Iterator<DamagingProjectileAPI> iter = projectiles_copy.iterator();        
        while (iter.hasNext()) {
            DamagingProjectileAPI projectile = iter.next();
            
            if (projectile.getProjectileSpecId() == null) {
                continue;
            }

            if (DUMBER.getElapsed() >= 1.5f) {
            switch (projectile.getProjectileSpecId()) {
                case LTA_XILeftAssaultProjectileID: {

                    Vector2f location = new Vector2f(projectile.getLocation());
                    ShipAPI ship = projectile.getSource();
                    float angle = projectile.getFacing();
                    int owner = projectile.getOwner();

                    engine.removeEntity(projectile);
                    CombatFleetManagerAPI FleetManager = engine.getFleetManager(ship.getOwner());
                    FleetManager.setSuppressDeploymentMessages(true);
                    FleetMemberAPI missileMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, LTA_XILeftAssaultShipID);
                    missileMember.getRepairTracker().setCrashMothballed(false);
                    missileMember.getRepairTracker().setMothballed(false);
                    missileMember.getRepairTracker().setCR(1f);
                    missileMember.setOwner(owner);
                    missileMember.setAlly(ship.isAlly());
                    ShipAPI missile = engine.getFleetManager(owner).spawnFleetMember(missileMember, location, angle, 2.5f);
                    missile.setCollisionClass(CollisionClass.SHIP);
                    missile.getVelocity().set(ship.getVelocity());
                    missile.setAngularVelocity(ship.getAngularVelocity());
                                        
                    interval.advance(amount);
                    timer += amount;

                    if (timer>=duration) {
                        timer-=timer;
                        FleetManager.setSuppressDeploymentMessages(false);
                    }
                }
                break;
                
                case LTA_XILeftStrikeProjectileID: {

                    Vector2f location = new Vector2f(projectile.getLocation());
                    ShipAPI ship = projectile.getSource();
                    float angle = projectile.getFacing();
                    int owner = projectile.getOwner();

                    engine.removeEntity(projectile);
                    CombatFleetManagerAPI FleetManager = engine.getFleetManager(ship.getOwner());
                    FleetManager.setSuppressDeploymentMessages(true);
                    FleetMemberAPI missileMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, LTA_XILeftStrikeShipID);
                    missileMember.getRepairTracker().setCrashMothballed(false);
                    missileMember.getRepairTracker().setMothballed(false);
                    missileMember.getRepairTracker().setCR(1f);
                    missileMember.setOwner(owner);
                    missileMember.setAlly(ship.isAlly());
                    ShipAPI missile = engine.getFleetManager(owner).spawnFleetMember(missileMember, location, angle, 2.5f);
                    missile.setCollisionClass(CollisionClass.SHIP);
                    missile.getVelocity().set(ship.getVelocity());
                    missile.setAngularVelocity(ship.getAngularVelocity());
                                        
                    interval.advance(amount);
                    timer += amount;

                    if (timer>=duration) {
                        timer-=timer;
                        FleetManager.setSuppressDeploymentMessages(false);
                    }
                }
                break;
                
                case LTA_XILeftSupportProjectileID: {

                    Vector2f location = new Vector2f(projectile.getLocation());
                    ShipAPI ship = projectile.getSource();
                    float angle = projectile.getFacing();
                    int owner = projectile.getOwner();

                    engine.removeEntity(projectile);
                    CombatFleetManagerAPI FleetManager = engine.getFleetManager(ship.getOwner());
                    FleetManager.setSuppressDeploymentMessages(true);
                    FleetMemberAPI missileMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, LTA_XILeftSupportShipID);
                    missileMember.getRepairTracker().setCrashMothballed(false);
                    missileMember.getRepairTracker().setMothballed(false);
                    missileMember.getRepairTracker().setCR(1f);
                    missileMember.setOwner(owner);
                    missileMember.setAlly(ship.isAlly());
                    ShipAPI missile = engine.getFleetManager(owner).spawnFleetMember(missileMember, location, angle, 2.5f);
                    missile.setCollisionClass(CollisionClass.SHIP);
                    missile.getVelocity().set(ship.getVelocity());
                    missile.setAngularVelocity(ship.getAngularVelocity());
                                        
                    interval.advance(amount);
                    timer += amount;

                    if (timer>=duration) {
                        timer-=timer;
                        FleetManager.setSuppressDeploymentMessages(false);
                    }
                }
                break;
                
                case LTA_XILeftLostProjectileID: {

                    Vector2f location = new Vector2f(projectile.getLocation());
                    ShipAPI ship = projectile.getSource();
                    float angle = projectile.getFacing();
                    int owner = projectile.getOwner();

                    engine.removeEntity(projectile);
                    CombatFleetManagerAPI FleetManager = engine.getFleetManager(ship.getOwner());
                    FleetManager.setSuppressDeploymentMessages(true);
                    FleetMemberAPI missileMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, LTA_XILeftLostShipID);
                    missileMember.getRepairTracker().setCrashMothballed(false);
                    missileMember.getRepairTracker().setMothballed(false);
                    missileMember.getRepairTracker().setCR(1f);
                    missileMember.setOwner(owner);
                    missileMember.setAlly(ship.isAlly());
                    ShipAPI missile = engine.getFleetManager(owner).spawnFleetMember(missileMember, location, angle, 2.5f);
                    missile.setCollisionClass(CollisionClass.SHIP);
                    missile.getVelocity().set(ship.getVelocity());
                    missile.setAngularVelocity(ship.getAngularVelocity());
                                        
                    interval.advance(amount);
                    timer += amount;

                    if (timer>=duration) {
                        timer-=timer;
                        FleetManager.setSuppressDeploymentMessages(false);
                    }
                }
                break;
                
                case LTA_XIRightAssaultProjectileID: {

                    Vector2f location = new Vector2f(projectile.getLocation());
                    ShipAPI ship = projectile.getSource();
                    float angle = projectile.getFacing();
                    int owner = projectile.getOwner();

                    engine.removeEntity(projectile);
                    CombatFleetManagerAPI FleetManager = engine.getFleetManager(ship.getOwner());
                    FleetManager.setSuppressDeploymentMessages(true);
                    FleetMemberAPI missileMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, LTA_XIRightAssaultShipID);
                    missileMember.getRepairTracker().setCrashMothballed(false);
                    missileMember.getRepairTracker().setMothballed(false);
                    missileMember.getRepairTracker().setCR(1f);
                    missileMember.setOwner(owner);
                    missileMember.setAlly(ship.isAlly());
                    ShipAPI missile = engine.getFleetManager(owner).spawnFleetMember(missileMember, location, angle, 2.5f);
                    missile.setCollisionClass(CollisionClass.SHIP);
                    missile.getVelocity().set(ship.getVelocity());
                    missile.setAngularVelocity(ship.getAngularVelocity());
                                        
                    interval.advance(amount);
                    timer += amount;

                    if (timer>=duration) {
                        timer-=timer;
                        FleetManager.setSuppressDeploymentMessages(false);
                    }
                }
                break;
                
                case LTA_XIRightStrikeProjectileID: {

                    Vector2f location = new Vector2f(projectile.getLocation());
                    ShipAPI ship = projectile.getSource();
                    float angle = projectile.getFacing();
                    int owner = projectile.getOwner();

                    engine.removeEntity(projectile);
                    CombatFleetManagerAPI FleetManager = engine.getFleetManager(ship.getOwner());
                    FleetManager.setSuppressDeploymentMessages(true);
                    FleetMemberAPI missileMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, LTA_XIRightStrikeShipID);
                    missileMember.getRepairTracker().setCrashMothballed(false);
                    missileMember.getRepairTracker().setMothballed(false);
                    missileMember.getRepairTracker().setCR(1f);
                    missileMember.setOwner(owner);
                    missileMember.setAlly(ship.isAlly());
                    ShipAPI missile = engine.getFleetManager(owner).spawnFleetMember(missileMember, location, angle, 2.5f);
                    missile.setCollisionClass(CollisionClass.SHIP);
                    missile.getVelocity().set(ship.getVelocity());
                    missile.setAngularVelocity(ship.getAngularVelocity());
                                        
                    interval.advance(amount);
                    timer += amount;

                    if (timer>=duration) {
                        timer-=timer;
                        FleetManager.setSuppressDeploymentMessages(false);
                    }
                }
                break;

                case LTA_XIRightSupportProjectileID: {

                    Vector2f location = new Vector2f(projectile.getLocation());
                    ShipAPI ship = projectile.getSource();
                    float angle = projectile.getFacing();
                    int owner = projectile.getOwner();

                    engine.removeEntity(projectile);
                    CombatFleetManagerAPI FleetManager = engine.getFleetManager(ship.getOwner());
                    FleetManager.setSuppressDeploymentMessages(true);
                    FleetMemberAPI missileMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, LTA_XIRightSupportShipID);
                    missileMember.getRepairTracker().setCrashMothballed(false);
                    missileMember.getRepairTracker().setMothballed(false);
                    missileMember.getRepairTracker().setCR(1f);
                    missileMember.setOwner(owner);
                    missileMember.setAlly(ship.isAlly());
                    ShipAPI missile = engine.getFleetManager(owner).spawnFleetMember(missileMember, location, angle, 2.5f);
                    missile.setCollisionClass(CollisionClass.SHIP);
                    missile.getVelocity().set(ship.getVelocity());
                    missile.setAngularVelocity(ship.getAngularVelocity());
                                        
                    interval.advance(amount);
                    timer += amount;

                    if (timer>=duration) {
                        timer-=timer;
                        FleetManager.setSuppressDeploymentMessages(false);
                    }
                }
                break;

                case LTA_XIRightLostProjectileID: {

                    Vector2f location = new Vector2f(projectile.getLocation());
                    ShipAPI ship = projectile.getSource();
                    float angle = projectile.getFacing();
                    int owner = projectile.getOwner();

                    engine.removeEntity(projectile);
                    CombatFleetManagerAPI FleetManager = engine.getFleetManager(ship.getOwner());
                    FleetManager.setSuppressDeploymentMessages(true);
                    FleetMemberAPI missileMember = Global.getFactory().createFleetMember(FleetMemberType.SHIP, LTA_XIRightLostShipID);
                    missileMember.getRepairTracker().setCrashMothballed(false);
                    missileMember.getRepairTracker().setMothballed(false);
                    missileMember.getRepairTracker().setCR(1f);
                    missileMember.setOwner(owner);
                    missileMember.setAlly(ship.isAlly());
                    ShipAPI missile = engine.getFleetManager(owner).spawnFleetMember(missileMember, location, angle, 2.5f);
                    missile.setCollisionClass(CollisionClass.SHIP);
                    missile.getVelocity().set(ship.getVelocity());
                    missile.setAngularVelocity(ship.getAngularVelocity());
                                        
                    interval.advance(amount);
                    timer += amount;

                    if (timer>=duration) {
                        timer-=timer;
                        FleetManager.setSuppressDeploymentMessages(false);
                    }
                }
                break;

                default:
                }
            }
            DUMBER.advance(amount);
        }
    }
    
    @Override
    public void init(CombatEngineAPI engine) {
        this.engine = engine;
    }
}