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
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.util.IntervalUtil;
import java.awt.Color;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

public class Diableavionics_cicadaAI implements MissileAIPlugin, GuidedMissileAI {
    
    private CombatEngineAPI engine;
    private final MissileAPI missile;
    private CombatEntityAPI target;
    private IntervalUtil blink = new IntervalUtil(0.4f,0.4f);

    public Diableavionics_cicadaAI(MissileAPI missile, ShipAPI launchingShip) {	        
        if (engine != Global.getCombatEngine()) {
            this.engine = Global.getCombatEngine();
        }
        this.missile = missile;  
        missile.setArmingTime(missile.getArmingTime()-(float)(Math.random()/4));
    }

    @Override
    public void advance(float amount) {        
        //skip the AI if the game is paused, the missile is engineless or fading
        if (engine.isPaused() || missile.isFading()) {return;}
        
        if(missile.getVelocity().lengthSquared()>1600){
            missile.giveCommand(ShipCommand.DECELERATE);
        }
        
        if(missile.isArmed()){
            /*
            public DamagingExplosionSpec(
                float duration,
                float radius,
                float coreRadius,
                float maxDamage, 
                float minDamage, 
                CollisionClass collisionClass,
                CollisionClass collisionClassByFighter,
                float particleSizeMin,
                float particleSizeRange,
                float particleDuration,
                int particleCount,
                Color particleColor,
                Color explosionColor
            )
            */
            DamagingExplosionSpec boom = new DamagingExplosionSpec(
                    0.1f,
                    100,
                    50,
                    500,
                    50,
                    CollisionClass.PROJECTILE_NO_FF,
                    CollisionClass.PROJECTILE_FIGHTER,
                    2,
                    5,
                    5,
                    25,
                    new Color(225,100,0),
                    new Color(200,100,25)
            );
            boom.setDamageType(DamageType.HIGH_EXPLOSIVE);
            boom.setShowGraphic(false);
            boom.setSoundSetId("diableavionics_cicada_hit");
            engine.spawnDamagingExplosion(boom, missile.getSource(), missile.getLocation(),false);
            
            //visual effect
            engine.addSmoothParticle(missile.getLocation(), missile.getVelocity(), 300, 2, 0.1f, Color.white);
            engine.addHitParticle(missile.getLocation(), missile.getVelocity(), 200, 1, 0.4f, new Color(200,100,25));
            engine.spawnExplosion(missile.getLocation(), missile.getVelocity(), Color.DARK_GRAY, 75, 2);
            
            for(int i=0; i<25; i++){
                engine.addHitParticle(
                        missile.getLocation(),
                        MathUtils.getRandomPointInCircle(new Vector2f(), 500),
                        MathUtils.getRandomNumberInRange(3, 6),
                        1,
                        MathUtils.getRandomNumberInRange(0.15f, 0.3f),
                        Color.ORANGE);
            }
            
            //destroy grenade
//            engine.applyDamage(missile, missile.getLocation(), 1000, DamageType.FRAGMENTATION, 0, true, false, missile.getSource());
            engine.removeEntity(missile);
        } else {
            blink.advance(amount);
            if(blink.intervalElapsed()){
                blink.setInterval(Math.max(0.1f, blink.getMinInterval()*0.66f), Math.max(0.1f, blink.getMinInterval()*0.66f));
                engine.addHitParticle(missile.getLocation(), missile.getVelocity(), 50, 0.4f, 0.1f, Color.red);
            }
        }
    }

    @Override
    public CombatEntityAPI getTarget() {
        return target;
    }

    @Override
    public void setTarget(CombatEntityAPI target) {
        this.target = target;
    }
}