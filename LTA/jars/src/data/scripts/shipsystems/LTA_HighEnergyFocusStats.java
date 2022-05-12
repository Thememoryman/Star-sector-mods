package data.scripts.shipsystems;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lazywizard.lazylib.combat.AIUtils;

public class LTA_HighEnergyFocusStats extends BaseShipSystemScript {

            public boolean isActive = false;
    
	public static final float DAMAGE_BONUS_PERCENT = 1400f;
	public static final float EXTRA_DAMAGE_TAKEN_PERCENT = 100f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		
		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		stats.getEnergyWeaponDamageMult().modifyPercent(id, bonusPercent);
		//stats.getEnergyWeaponRangeBonus().modifyPercent(id, bonusPercent);
		
		//float damageTakenPercent = EXTRA_DAMAGE_TAKEN_PERCENT * effectLevel;
//		stats.getArmorDamageTakenMult().modifyPercent(id, damageTakenPercent);
//		stats.getHullDamageTakenMult().modifyPercent(id, damageTakenPercent);
//		stats.getShieldDamageTakenMult().modifyPercent(id, damageTakenPercent);
		//stats.getWeaponDamageTakenMult().modifyPercent(id, damageTakenPercent);
		//stats.getEngineDamageTakenMult().modifyPercent(id, damageTakenPercent);
		
		//stats.getBeamWeaponFluxCostMult().modifyMult(id, 10f);
                		if (state == ShipSystemStatsScript.State.OUT) {
			//stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
		} else {
			//stats.getMaxSpeed().modifyFlat(id, SPEED_BONUS * effectLevel);
			//stats.getAcceleration().modifyFlat(id, 200f * effectLevel);
                        //stats.getMaxTurnRate().modifyFlat(id, TURN_BONUS * effectLevel);
                        //stats.getTurnAcceleration().modifyFlat(id, TURN_BONUS * effectLevel);
			//stats.getAcceleration().modifyPercent(id, 200f * effectLevel);
		}
                
		if (stats.getEntity() instanceof ShipAPI) {
			ShipAPI ship = (ShipAPI) stats.getEntity();
			
			//ship.getEngineController().fadeToOtherColor(this, color, new Color(0,0,0,0), effectLevel, 0.67f);
			//ship.getEngineController().fadeToOtherColor(this, Color.white, new Color(0,0,0,0), effectLevel, 0.67f);
			//ship.getEngineController().extendFlame(this, 2f * effectLevel, 0f * effectLevel, 0f * effectLevel);
			
                                if (state == State.OUT) {
                                //once
                                if (!isActive) {
                            ShipAPI empTarget = ship;
                            for (int x = 0; x < 1; x++) {
                                Global.getCombatEngine().spawnEmpArc(ship, ship.getLocation(),
                                                   empTarget,
                                                   empTarget, DamageType.ENERGY, 0, 150,
                                                   2000, null, 30f, new Color(230,40,40,0),
                                                   new Color(255,255,255,0));
                            }	
                               
                            }
                            
                                  isActive = true;
                                }//end once
                                }
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getEnergyWeaponDamageMult().unmodify(id);
//		stats.getEnergyWeaponRangeBonus().unmodify(id);
//		stats.getArmorDamageTakenMult().unmodify(id);
//		stats.getHullDamageTakenMult().unmodify(id);
//		stats.getShieldDamageTakenMult().unmodify(id);
//		stats.getWeaponDamageTakenMult().unmodify(id);
//		stats.getEngineDamageTakenMult().unmodify(id);
                isActive = false;
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		float damageTakenPercent = EXTRA_DAMAGE_TAKEN_PERCENT * effectLevel;
		if (index == 0) {
			return new StatusData("+" + (int) bonusPercent + "% energy weapon damage" , false);
		} else if (index == 1) {
			//return new StatusData("+" + (int) damageTakenPercent + "% weapon/engine damage taken", false);
			return null;
		} else if (index == 2) {
			//return new StatusData("shield damage taken +" + (int) damageTakenPercent + "%", true);
			return null;
		}
		return null;
	}
}
