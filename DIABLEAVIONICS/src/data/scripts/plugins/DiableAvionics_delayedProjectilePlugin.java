package data.scripts.plugins;

import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lazywizard.lazylib.FastTrig;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.entities.AnchoredEntity;
import com.fs.starfarer.api.Global;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import data.scripts.util.MagicRender;

// TODO: Check for nearby dprojectiles and merge them for better performance
public class DiableAvionics_delayedProjectilePlugin extends BaseEveryFrameCombatPlugin {

    public Vector2f particleVelocity1;
    public Vector2f particleVelocity2;
    public float damageAmount = 50f; //set this to whatever the damage should be
    public float empAmount = 0f; //set this to whatever the EMP damage should be
    public Color particleColor = new Color(245, 48, 58, 225);
    public float particleSize = 2.5f;
    public float particleBrightness = 1.28f;
    public float particleDuration = 1.45f;
    public float explosionSize = 120f;
    public float explosionSize2 = 60f;
    public float explosionDuration = 0.33f;
    public float explosionDuration2 = 0.9f;
    public float pitch = 1.0f; //sound pitch. Default seems to be 1
    public float volume = 1.0f; //volume, scale from 0-1
    public boolean dealsSoftFlux = false;
    // Stores the currently DEBRIS_MAP dprojectiles
    // Having the Set backed by a WeakHashMap helps prevent memory leaks
    private static final List<DelayedProjectileData> DEBRIS_MAP = new ArrayList<>();
    private CombatEngineAPI engine;

    @Override
    public void advance(float amount, List events) {

        if (engine != Global.getCombatEngine()) {
            this.engine = Global.getCombatEngine();
        }

        if (engine.isPaused() || DEBRIS_MAP.isEmpty()) {
            return;
        }

        // Deal dprojectile damage for all actively DEBRIS_MAP projectiles
        for (Iterator iter = DEBRIS_MAP.iterator(); iter.hasNext();) {
            DelayedProjectileData dprojectile = (DelayedProjectileData) iter.next();

            // Check if the dprojectile has gone out
            if (engine.getTotalElapsedTime(false) >= dprojectile.getDelayTime()
                    || !engine.isEntityInPlay(dprojectile.getAnchor())) {

                /**
                 * @param ship The ship launching this projectile. Can be null.
                 * @param weapon Firing weapon. Can be null. If not, used for
                 * figuring out range/damage bonuses, etc.
                 * @param weaponId ID of the weapon whose projectile to use.
                 * Required.
                 * @param point Location where the projectile will spawn.
                 * Required.
                 * @param angle Initial facing, in degrees (0 = 3 o'clock, 90 =
                 * 12 o'clock).
                 * @param shipVelocity Can be null. Otherwise, will be imparted
                 * to projectile.
                 * @return Projectile that was created, or null.
                 */

                //for (int i = 0; i < 5; i++) {
//                CombatEntityAPI outProjectile = engine.spawnProjectile(null,
//                        null,
//                        //dprojectile.getWeaponID(),
//                        "diableavionics_thunderboltcharge",
//                        dprojectile.getLocation(),
//                        dprojectile.getExitAngle(),
//                        dprojectile.hitLoc.getVelocity()
//                );
                //}\
                if(MagicRender.screenCheck(0.25f, dprojectile.getLocation())){
                    particleVelocity1 = dprojectile.missile.getVelocity();
                    particleVelocity2 = dprojectile.missile.getVelocity();
                    particleVelocity1.scale(0.02f);
                    particleVelocity2.scale(0.06f);

                    //spawn exit projectiles
                    for (int i = 0; i < 10; i++) {
                        String projID = "debris_launcher" + Integer.toString((int) MathUtils.getRandomNumberInRange(0f, 8f));
                        engine.spawnProjectile(null, null, projID, dprojectile.getLocation(), dprojectile.getExitAngle() + MathUtils.getRandomNumberInRange(-45f, 45f), null);

                        /*Optionally the debris can do damage, each projectile does 2dmg in the CSV file. 
                        This can cause massive issues in gameplay balance when using the shortest distance vector method for determining the nearest boundary
                        What will happen with ships like the Paragon for example, is the projectile will exit, spewing debris.
                        In a confined inside of a ship this can add up rather quickly in terms of damage, which I think is a bug.
                        */

                        //CombatEntityAPI debrisProj = engine.spawnProjectile(null, null, projID, dprojectile.getLocation(), dprojectile.getExitAngle() + MathUtils.getRandomNumberInRange(-45f, 45f), null);

                        //debrisProj.setCollisionClass(CollisionClass.SHIP);
                        //debrisProj.setAngularVelocity(MathUtils.getRandomNumberInRange(50f, 200f));


                        //void addHitParticle(Vector2f loc, Vector2f vel, float size, float brightness, float duration, Color color);
                        engine.addHitParticle(
                                dprojectile.getLocation(),
                                new Vector2f(dprojectile.getExitVector().x * 130f + (float) (Math.random() * 100 - 30),
                                        dprojectile.getExitVector().y * 130f + (float) (Math.random() * 100 - 30)),
                                damageAmount / 80,
                                0.5f,
                                3f,
                                new Color(225, 200, 50, 220)
                        );//Color(225,200,50,200)
                    }
                }

//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity1, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity2, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity1, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity2, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity1, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity2, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity1, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity2, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity2, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity1, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity2, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity1, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.addHitParticle(dprojectile.getLocation(), particleVelocity2, particleSize, particleBrightness, particleDuration, particleColor);
//                engine.spawnExplosion(dprojectile.getLocation(), particleVelocity1, particleColor, explosionSize, explosionDuration);
//                engine.spawnExplosion(dprojectile.getLocation(), particleVelocity2, particleColor, explosionSize2, explosionDuration2);

                //Vector2f projV = outProjectile.getVelocity();
//                projV.x =  dprojectile.getExitVector().x * dprojectile.newSpeed;
//                projV.y =  dprojectile.getExitVector().y * dprojectile.newSpeed;
                iter.remove();

            }

        }
    }

    public static void startFire(CombatEntityAPI target,
            Vector2f hitLoc,
            float totalDamage,
            CombatEntityAPI source,
            float dprojectileAngle,
            float delay,
            String id,
            float speed,
            DamagingProjectileAPI projectile
    ) {
        // TODO: merge with nearby dprojectiles on the same target
        DEBRIS_MAP.add(new DelayedProjectileData(target, hitLoc, totalDamage, source, dprojectileAngle, delay, id, speed, projectile));
    }

    @Override
    public void init(CombatEngineAPI engine) {
    }

    private static class DelayedProjectileData {

        private final AnchoredEntity hitLoc;
        private final CombatEntityAPI source;
        private final float dmg, start, shipStartFacing, deltaAngle, newSpeed;
        private final String weaponID;
        private final DamagingProjectileAPI missile;

        public DelayedProjectileData(CombatEntityAPI target, Vector2f hitLoc,
                float totalDamage,
                CombatEntityAPI source,
                float dprojectileAngle,
                float delay, String id,
                float speed,
                DamagingProjectileAPI projectile
        ) {
            this.hitLoc = new AnchoredEntity(target, hitLoc);
            this.source = source;
            this.deltaAngle = dprojectileAngle;
            this.shipStartFacing = source.getFacing();
            this.dmg = totalDamage;
            this.start = Global.getCombatEngine().getTotalElapsedTime(false)
                    + delay;
            this.weaponID = id;
            this.newSpeed = speed;
            this.missile = projectile;
        }

        public Vector2f getLocation() {
            float projectedLocationX = (float) (7.5f * FastTrig.cos(Math.toRadians(getExitAngle())) + hitLoc.getLocation().x);
            float projectedLocationY = (float) (7.5f * FastTrig.sin(Math.toRadians(getExitAngle())) + hitLoc.getLocation().y);
            return new Vector2f(projectedLocationX, projectedLocationY);
        }

        public CombatEntityAPI getAnchor() {
            return hitLoc.getAnchor();
        }

        private float getExitAngle() {
            return deltaAngle + (shipStartFacing - source.getFacing());
        }

        private float getDelayTime() {
            return start;
        }

        private float getDamage() {
            return dmg;
        }

        private String getWeaponID() {
            return weaponID;
        }

        //this is actually the unit vector returned
        public Vector2f getExitVector() {
            float x = (float) FastTrig.cos(Math.toRadians(getExitAngle()));
            float y = (float) FastTrig.sin(Math.toRadians(getExitAngle()));
            return new Vector2f(x, y);
        }
    }

}
