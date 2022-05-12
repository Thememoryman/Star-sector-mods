package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import org.lwjgl.util.vector.Vector2f;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.OnHitEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import data.scripts.plugins.DiableAvionics_projectileEffectPlugin;

public class Diableavionics_hexafireEffect implements OnHitEffectPlugin {
    
    private final String SOUND_ID = "diableavionics_crackle";

    @Override
    public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
        if (shieldHit && !projectile.isFading()){

            DiableAvionics_projectileEffectPlugin.addDisruption((ShipAPI)target, 0.1f*projectile.getSource().getMutableStats().getEnergyWeaponDamageMult().getModifiedValue());
            
            if(Math.random()<0.25f){
                Global.getSoundPlayer().playSound(SOUND_ID, (float)((Math.random()*0.2f)+0.8f), 0.5f*(float)((Math.random()*0.2f)+0.8f),target.getLocation(), target.getVelocity());
            }
        }
    }
}