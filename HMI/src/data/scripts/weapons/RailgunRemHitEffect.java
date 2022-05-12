package data.scripts.weapons;

import java.awt.Color;

import org.lwjgl.util.vector.Vector2f;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;

public class RailgunRemHitEffect implements OnHitEffectPlugin {


	public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target,
					  Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
		if ((float) Math.random() > 0.5f && !shieldHit && target instanceof ShipAPI) {

			
			engine.spawnEmpArc(projectile.getSource(), point, target, target,
							   DamageType.ENERGY, 
							   0f,
							   0f, // emp 
							   100000f, // max range 
							   "tachyon_lance_emp_impact",
							   10f, // thickness
							   new Color(0,205,255,255),
							   new Color(255,255,255,200)
							   );
			
			//engine.spawnProjectile(null, null, "plasma", point, 0, new Vector2f(0, 0));
		}
	}
}
