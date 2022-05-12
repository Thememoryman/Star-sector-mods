package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.loading.DamagingExplosionSpec;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

import static com.fs.starfarer.api.util.Misc.ZERO;

public class LTA_NixieOnHitEffect implements OnHitEffectPlugin {
    
    private static final float MIN_DAMAGE = 5f;
    private static final float MAX_DAMAGE = 10f;

    private static final Color NIXIE_EXPLOSION_CORE_COLOR = new Color(255, 255, 125);
    private static final Color NIXIE_EXPLOSION_COLOUR = new Color(255, 240, 180, 0);
    private static final Color NIXIE_EXPLOSION_FLASH_COLOR = new Color(255, 100, 0);
    private static final Color NIXIE_PARTICLE_COLOUR = new Color(255, 240, 180, 80);
    private static final int NIXIE_PARTICLES_AMOUNT = 10;

    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {

        //MagicLensFlare.createSharpFlare(engine, projectile.getSource(), projectile.getLocation(), 8, 400, 0, new Color(186, 240, 255), new Color(255, 255, 255));

        engine.spawnExplosion(point, ZERO, NIXIE_PARTICLE_COLOUR, 25f, 0.25f);
        engine.spawnExplosion(point, ZERO, NIXIE_EXPLOSION_CORE_COLOR, 12.5f, 0.25f);

        DamagingExplosionSpec blast = new DamagingExplosionSpec(0.025f, 50f, 25f, MAX_DAMAGE, MIN_DAMAGE, CollisionClass.PROJECTILE_FF, CollisionClass.PROJECTILE_FIGHTER, 0f, 0f, 0.01f, 0, NIXIE_EXPLOSION_COLOUR, null);
        blast.setDamageType(DamageType.HIGH_EXPLOSIVE);
        blast.setShowGraphic(false);
        engine.spawnDamagingExplosion(blast,projectile.getSource(),point,false);

        engine.addSmoothParticle(point, ZERO, 25f, 0.1f, 0.1f, NIXIE_PARTICLE_COLOUR);
        engine.addHitParticle(point, ZERO, 25f, 0.3f, 0.25f, NIXIE_EXPLOSION_FLASH_COLOR);
        for (int x = 0; x < NIXIE_PARTICLES_AMOUNT; x++) 
        {
            engine.addHitParticle(point, MathUtils.getPointOnCircumference(null, MathUtils.getRandomNumberInRange(10f, 50f), (float) Math.random() * 360f), 2f, 0.5f, MathUtils.getRandomNumberInRange(0.1f, 0.2f), NIXIE_PARTICLE_COLOUR);
        }
    }
}