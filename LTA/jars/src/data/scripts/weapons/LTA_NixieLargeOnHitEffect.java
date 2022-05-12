package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static com.fs.starfarer.api.util.Misc.ZERO;

public class LTA_NixieLargeOnHitEffect implements OnHitEffectPlugin {
    
    private static final float MIN_DAMAGE = 20f;
    private static final float MAX_DAMAGE = 20f;

    private static final Color NIXIE_EXPLOSION_CORE_COLOR = new Color(255, 255, 225);
    private static final Color NIXIE_EXPLOSION_COLOUR = new Color(255, 240, 180, 0);
    private static final Color NIXIE_EXPLOSION_FLASH_COLOR = new Color(255, 100, 0);
    private static final Color NIXIE_PARTICLE_COLOUR = new Color(255, 240, 180, 150);
    private static final int NIXIE_PARTICLES_AMOUNT = 30;

    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, CombatEngineAPI engine) {

        //MagicLensFlare.createSharpFlare(engine, projectile.getSource(), projectile.getLocation(), 8, 400, 0, new Color(186, 240, 255), new Color(255, 255, 255));

        engine.spawnExplosion(point, ZERO, NIXIE_PARTICLE_COLOUR, 100f, 1f);
        engine.spawnExplosion(point, ZERO, NIXIE_EXPLOSION_CORE_COLOR, 50f, 1f);

        DamagingExplosionSpec blast = new DamagingExplosionSpec(0.025f, 20f, 10f, MAX_DAMAGE, MIN_DAMAGE, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 5f, 5f, 0.075f, 0, NIXIE_EXPLOSION_COLOUR, null);
        blast.setDamageType(DamageType.ENERGY);
        blast.setShowGraphic(false);
        engine.spawnDamagingExplosion(blast,projectile.getSource(),point,false);

        engine.addSmoothParticle(point, ZERO, 200f, 0.2f, 0.1f, NIXIE_PARTICLE_COLOUR);
        engine.addHitParticle(point, ZERO, 200f, 0.5f, 0.25f, NIXIE_EXPLOSION_FLASH_COLOR);
        for (int x = 0; x < NIXIE_PARTICLES_AMOUNT; x++) 
        {
            engine.addHitParticle(point, MathUtils.getPointOnCircumference(null, MathUtils.getRandomNumberInRange(50f, 300f), (float) Math.random() * 360f), 10f, 1f, MathUtils.getRandomNumberInRange(0.3f, 0.6f), NIXIE_PARTICLE_COLOUR);
        }
    }
}