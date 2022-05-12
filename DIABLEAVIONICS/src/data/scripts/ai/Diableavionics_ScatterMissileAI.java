package data.scripts.ai;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CollisionClass;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.GuidedMissileAI;
import com.fs.starfarer.api.combat.MissileAIPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipCommand;
import data.scripts.util.MagicTargeting;
import java.awt.Color;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

public class Diableavionics_ScatterMissileAI implements MissileAIPlugin, GuidedMissileAI{

    //////////////////////
    //     SETTINGS     //
    //////////////////////
    
    private final float DAMPING=0.1f;
    private final int SEARCH_CONE=360, MAX_SCATTER=30;
    private float PRECISION_RANGE=500, ECCM=2f;
    
    private CombatEngineAPI engine;
    private final MissileAPI missile;
    private CombatEntityAPI target=null;
    private Vector2f lead = new Vector2f();
    private boolean launch=true;
    private float timer=0, check=0f, scatter=0, random, correctAngle;

    public Diableavionics_ScatterMissileAI(MissileAPI missile, ShipAPI launchingShip){	
        
        if (engine != Global.getCombatEngine()) {
            this.engine = Global.getCombatEngine();
        }
        
        this.missile = missile;
        
        if (missile.getSource().getVariant().getHullMods().contains("eccm")){
            ECCM=1;
        }
        
        //calculate the precision range factor
        PRECISION_RANGE=(float)Math.pow((5*PRECISION_RANGE),2);
        
        random = MathUtils.getRandomNumberInRange(-1f, 1f);        
    }

    @Override
    public void advance(float amount) {
        
        //skip the AI if the game is paused
        if (engine.isPaused()) {return;}
        
        //fading failsafe
        if(missile.isFizzling() || missile.isFading()){
            engine.applyDamage(missile, missile.getLocation(), missile.getHitpoints()*2, DamageType.FRAGMENTATION, 0, true, false, missile.getSource());
        }
        
        //assigning a target if there is none or it got destroyed
        if (target == null
                || (target instanceof ShipAPI && ((ShipAPI)target).isHulk())
                || !engine.isEntityInPlay(target)
                || target.getCollisionClass()==CollisionClass.NONE
                ){     
            if(Math.random()<0.5){
                setTarget(MagicTargeting.pickTarget(missile,
                            MagicTargeting.targetSeeking.NO_RANDOM,
                            (int)missile.getWeapon().getRange(),
                            SEARCH_CONE,
                            1,1,1,1,1,
                            true
                        )
                );
            } else {                
                setTarget(MagicTargeting.pickTarget(missile,
                                MagicTargeting.targetSeeking.LOCAL_RANDOM,
                                (int)missile.getWeapon().getRange()/2,
                                SEARCH_CONE,
                                0,1,2,3,4,
                                true
                        )
                );
            }
            launch=true;
            //forced acceleration by default
            missile.giveCommand(ShipCommand.ACCELERATE);
            return;
        }
        
        timer+=amount;
        
        //finding lead point to aim to        
        if(launch || timer>=check){
            launch=false;
            
            timer -=check;
            
            //set the next check time
            float dist = (MathUtils.getDistanceSquared(missile.getLocation(), target.getLocation())+400000)/PRECISION_RANGE;
            check = Math.min(
                    0.5f,
                    Math.max(
                            0.1f,
                            dist)
            );
            
            scatter = ECCM * MAX_SCATTER * random * check;
        }
            
        lead = target.getLocation();

        //best velocity vector angle for interception
        correctAngle = VectorUtils.getAngle(
                        missile.getLocation(),
                        lead
                );

        //scatter
        correctAngle+=scatter;
        
        
//        //debug
//        engine.addHitParticle(MathUtils.getPoint(missile.getLocation(), 50, correctAngle), new Vector2f(), 2, 2, 2, Color.BLUE);
        
        /*
        //OVERSTEER
        ///////////////////////////////////////////////////////////////////////

        //velocity angle correction
        float offCourseAngle = MathUtils.getShortestRotation(
                VectorUtils.getFacing(missile.getVelocity()),
                correctAngle
                );

        float correction = MathUtils.getShortestRotation(                
                correctAngle,
                VectorUtils.getFacing(missile.getVelocity())+180
                ) 
                * 0.5f * //oversteer
                (float)((FastTrig.sin(MathUtils.FPI/90*(Math.min(Math.abs(offCourseAngle),45))))); //damping when the correction isn't important

        //modified optimal facing to correct the velocity vector angle as soon as possible
        correctAngle = correctAngle+correction;
        */
        
        float correction = MathUtils.getShortestRotation(VectorUtils.getFacing(missile.getVelocity()),correctAngle);
        if(correction>0){
            correction= -11.25f * ( (float)Math.pow(FastTrig.cos(MathUtils.FPI*correction/90)+1, 2) -4 );
        } else {
            correction= 11.25f * ( (float)Math.pow(FastTrig.cos(MathUtils.FPI*correction/90)+1, 2) -4 );
        }
        correctAngle+= correction;
        
//        //debug
//        engine.addHitParticle(MathUtils.getPoint(missile.getLocation(), 50, VectorUtils.getFacing(missile.getVelocity())), new Vector2f(), 2, 2, 2, Color.RED);
//        //debug
//        engine.addHitParticle(MathUtils.getPoint(missile.getLocation(), 50, correctAngle), new Vector2f(), 2, 2, 2, Color.GREEN);
        
        ///////////////////////////////////////////////////////////////////////     
        
        //target angle for interception        
        float aimAngle = MathUtils.getShortestRotation( missile.getFacing(), correctAngle);
        
        if (aimAngle < 0) {
            missile.giveCommand(ShipCommand.TURN_RIGHT);
        } else {
            missile.giveCommand(ShipCommand.TURN_LEFT);
        }  
        
        // Damp angular velocity if the missile aim is getting close to the targeted angle
        if (Math.abs(aimAngle) < Math.abs(missile.getAngularVelocity()) * DAMPING) {
            missile.setAngularVelocity(aimAngle / DAMPING);
        }
        
        //always accelerate
        missile.giveCommand(ShipCommand.ACCELERATE);  
    }

    @Override
    public CombatEntityAPI getTarget(){
        return target;
    }

    @Override
    public void setTarget(CombatEntityAPI target){
        this.target = target;
    }
}