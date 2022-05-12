package data.scripts.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import data.scripts.util.MagicRender;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.combat.entities.AnchoredEntity;
import org.lwjgl.util.vector.Vector2f;

// TODO: Check for nearby debriss and merge them for better performance
public class DiableAvionics_debrisPlugin extends BaseEveryFrameCombatPlugin {

    // How long between damage/particle effect ticks
    private static final float TIME_BETWEEN_DAMAGE_TICKS = .2f;
    private static final float TIME_BETWEEN_PARTICLE_TICKS = .33f;
    // Stores the currently DEBRIS_MAP debriss
    // Having the Set backed by a WeakHashMap helps prevent memory leaks
    private static final List<DebrisData> DEBRIS_MAP = new ArrayList<>();
    private CombatEngineAPI engine;
    private float lastDamage, lastParticle;

    @Override
    public void advance(float amount, List events) {

        if (engine != Global.getCombatEngine()) {
            this.engine = Global.getCombatEngine();
        }

        if (engine.isPaused() || DEBRIS_MAP.isEmpty()) {
            return;
        }

        lastDamage += amount;
        lastParticle += amount;

        float damageMod = lastDamage;

        boolean dealDamage = false;
        if (lastDamage >= TIME_BETWEEN_DAMAGE_TICKS) {
            lastDamage -= TIME_BETWEEN_DAMAGE_TICKS;
            dealDamage = true;
        }

        boolean showParticle = false;
        if (lastParticle >= TIME_BETWEEN_PARTICLE_TICKS) {
            lastParticle -= TIME_BETWEEN_PARTICLE_TICKS;
            showParticle = true;
        }

        // Deal debris damage for all actively DEBRIS_MAP projectiles
        for (Iterator iter = DEBRIS_MAP.iterator(); iter.hasNext();) {
            DebrisData debris = (DebrisData) iter.next();

            // Check if the debris has gone out
            if (engine.getTotalElapsedTime(false) >= debris.expiration
                    || !engine.isEntityInPlay(debris.getAnchor())) {
                iter.remove();
            } else if (engine.getTotalElapsedTime(false) < debris.start) {
                continue;
            } else {
                if (dealDamage) {
                    engine.applyDamage(debris.getAnchor(), debris.getLocation(),
                            debris.dps * damageMod, DamageType.FRAGMENTATION,
                            debris.dps * damageMod, true, true, debris.source);
                }

                // Draw smoke effect to show where the debris is DEBRIS_MAP
                float intensity = debris.getRemainingDuration() / debris.getDuration();
                float intensity2 = debris.getRemainingDuration() / (debris.getDuration() * debris.getDuration());
                
                //screencheck
                if(MagicRender.screenCheck(0.25f, debris.getLocation())){
                    
                    if (showParticle) {
                        Vector2f particleVelocity = new Vector2f(intensity2 * (debris.getDebrisVector().x),
                                //* 5f
                                //* debris.dps
                                //* debris.getRemainingDuration()
                                //add the x velocity of the host ship
                                //+ debris.getAnchor().getVelocity().x
                                //add some randomness
                                //+ MathUtils.getRandomNumberInRange(-25f, 25f)),
                                intensity2 * (debris.getDebrisVector().y)
                                //* 5f
                                //* debris.dps
                                //* debris.getRemainingDuration()
                                //add the y velocity of the host ship
                                //+ debris.getAnchor().getVelocity().y
                                ////add some randomness
                               // + MathUtils.getRandomNumberInRange(-25f, 25f))
                        );

                        engine.addSmokeParticle(debris.getLocation(), // Location
                                particleVelocity, // Velocity
                                MathUtils.getRandomNumberInRange(20f, 45f), // Size
                                MathUtils.getRandomNumberInRange(.05f, .15f), // Brightness
                                2.2f, new Color(33, 33, 33, 60)); // Duration, color


                        engine.addSmokeParticle(debris.getLocation(), // Location
                                particleVelocity, // Velocity
                                MathUtils.getRandomNumberInRange(15f, 35f), // Size
                                MathUtils.getRandomNumberInRange(.05f, .15f), // Brightness
                                1.6f, new Color(124, 33, 22, 50)); // Duration, color

    //                    engine.spawnExplosion(debris.getLocation(), //location
    //                            particleVelocity,//velocity
    //                            Color.yellow, //colour
    //                            MathUtils.getRandomNumberInRange(2f, 5f), //size
    //                            MathUtils.getRandomNumberInRange(.01f, .05f)); //duration
                    }
                    //spawn debris from exit hole
                    for (int i = 0; i < 2; i++) {
                        Vector2f debrisVelocity = new Vector2f(1200f * intensity2 * debris.getDebrisVector().x
                                //* 5f
                                //* debris.dps
                                //* debris.getRemainingDuration()
                                //add the x velocity of the host ship
                                + debris.getAnchor().getVelocity().x,
                                //add some randomness
                                //+ MathUtils.getRandomNumberInRange(intensity * -15f, intensity * 15f),
                                1200f * intensity2 * debris.getDebrisVector().y
                                //* 5f
                                //* debris.dps
                                //* debris.getRemainingDuration()
                                //add the y velocity of the host ship
                                + debris.getAnchor().getVelocity().y
                        ////add some randomness
                        //+ MathUtils.getRandomNumberInRange(intensity * -15f, intensity * 15f)
                        );

                        float tempKelvin = intensity * 4800f;
                        Color debrisColor = KelvinCalc.getRGB(tempKelvin);

                        engine.addHitParticle(
                                debris.getLocation(), //location, 
                                debrisVelocity, //velocity
                                MathUtils.getRandomNumberInRange(1f, 4f) * debris.getRemainingDuration(), //size
                                intensity, debris.getRemainingDuration() * 0.11f, //, new Color(225, 200, 50, 200)
                                debrisColor
                        );
                    }
                }
            }
        }
    }

    public static void startFire(CombatEntityAPI target,
            Vector2f hitLoc,
            float totalDamage,
            float burnDuration,
            CombatEntityAPI source,
            float debrisAngle,
            float delay,
            DamagingProjectileAPI projectile
    ) {
        // TODO: merge with nearby debriss on the same target
        DEBRIS_MAP.add(new DebrisData(target, hitLoc, totalDamage, burnDuration, source, debrisAngle, delay, projectile));
    }

    @Override
    public void init(CombatEngineAPI engine) {
    }

    private static class DebrisData {

        private final AnchoredEntity hitLoc;
        private final CombatEntityAPI source;
        private final float dps, expiration, duration, start;
        private final float shipStartFacing, deltaAngle;
        private final DamagingProjectileAPI damagingProjectile;

        public DebrisData(CombatEntityAPI target,
                Vector2f hitLoc,
                float totalDamage,
                float burnDuration,
                CombatEntityAPI source,
                float debrisAngle,
                float delay,
                DamagingProjectileAPI projectile
        ) {
            this.hitLoc = new AnchoredEntity(target, hitLoc);
            this.source = source;
            this.deltaAngle = debrisAngle;
            this.duration = burnDuration;
            this.shipStartFacing = source.getFacing();
            dps = totalDamage / burnDuration;
            expiration = Global.getCombatEngine().getTotalElapsedTime(false)
                    + burnDuration;
            start = Global.getCombatEngine().getTotalElapsedTime(false)
                    + delay;
            this.damagingProjectile = projectile;
        }

        public Vector2f getLocation() {
            return hitLoc.getLocation();
        }

        public CombatEntityAPI getAnchor() {
            return hitLoc.getAnchor();
        }

        private float getDebrisAngle() {
            return deltaAngle + (shipStartFacing - source.getFacing());
        }

        public float getRemainingDuration() {
            return expiration - Global.getCombatEngine().getTotalElapsedTime(false);
        }

        public float getDuration() {
            return duration;
        }

        public Vector2f getDebrisVector() {
            Vector2f debVector = new Vector2f();
            damagingProjectile.getVelocity().normalise(debVector);
            return debVector;
        }
    }

    private static class KelvinCalc {

        //Code adapted from http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/
        static Color getRGB(float tmpKelvin) {

            double tmpCalc = tmpKelvin; //Temperature must fall between 1000 and 40000 degrees
            double r, g, b = 0f;

            if (tmpKelvin < 1000) {
                tmpKelvin = 1000;
            }
            if (tmpKelvin > 40000) {
                tmpKelvin = 40000;
            } //All calculations require tmpKelvin \ 100, so only do the conversion once
            tmpKelvin /= 100;

            //Calculate each color in turn
            //First: red
            if (tmpKelvin <= 66) {
                r = 255;
            } else {//Note: the R-squared value for this approximation is .988
                tmpCalc = tmpKelvin - 60;
                tmpCalc = 329.698727446d * (Math.pow((double) tmpCalc, -0.1332047592d));
                r = tmpCalc;
                if (r < 0) {
                    r = 0;
                }

                if (r > 255) {
                    r = 255;
                }

            }
            if (tmpKelvin <= 66) {
                tmpCalc = tmpKelvin;
                tmpCalc = 99.4708025861d * Math.log(tmpCalc) - 161.1195681661d;
                g = tmpCalc;

                if (g < 0) {
                    g = 0;
                }

                if (g > 255) {
                    g = 255;
                }

            } else {//Note: the R-squared value for this approximation is .987
                tmpCalc = tmpKelvin - 60;

                tmpCalc = 288.1221695283d * (Math.pow(tmpCalc, -0.0755148492d));
                g = tmpCalc;
                if (g < 0) {
                    g = 0;
                    if (g > 255) {
                        g = 255;
                    }
                }

            }

            //blue
            if (tmpKelvin >= 66) {
                b = 255;
            } else {
                if (tmpKelvin <= 19) {
                    b = 0;
                } else {
                    tmpCalc = tmpKelvin - 10;
                    tmpCalc = 138.5177312231d * Math.log(tmpCalc) - 305.0447927307;

                    b = tmpCalc;
                    if (b < 0) {
                        b = 0;
                    }

                    if (b > 255) {
                        b = 255;
                    }

                }
            }

            return new Color((int) r, (int) g, (int) b, 240);
        }
    }
}
