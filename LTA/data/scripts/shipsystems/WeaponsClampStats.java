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

public class WeaponsClampStats extends BaseShipSystemScript {
    
        public boolean isActive = false;
	public static final float RANGE_BONUS = 0.50f;
	public static final float TURNRATE_MULT = 0.1f;
	//public static float SPEED_BONUS = 200f;
	//public static float TURN_BONUS = -10f;
        
	private Color color = new Color(220, 50, 0,255);

	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		float mult = 1f + RANGE_BONUS * effectLevel;
		stats.getBallisticWeaponRangeBonus().modifyMult(id, mult);
                stats.getEnergyWeaponRangeBonus().modifyMult(id, mult);
                stats.getMissileWeaponRangeBonus().modifyMult(id, mult);
		stats.getWeaponTurnRateBonus().modifyMult(id, TURNRATE_MULT);
                stats.getBeamWeaponTurnRateBonus().modifyMult(id, TURNRATE_MULT);
                
		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
		} else {
		//	stats.getMaxSpeed().modifyFlat(id, 200f * effectLevel);
		//	stats.getAcceleration().modifyFlat(id, 200f * effectLevel);
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
                            //ShipAPI empTarget = ship;
                            //for (int x = 0; x < 30; x++) {
                            //    Global.getCombatEngine().spawnEmpArc(ship, ship.getLocation(),
                            //                       empTarget,
                            //                       empTarget, DamageType.ENERGY, 0, 200,
                            //                       2000, null, 30f, new Color(230,40,40,0),
                            //                       new Color(255,255,255,0));
                            //}	
                               
                            }
                            
                                  isActive = true;
                                }//end once
                                }
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getBallisticRoFMult().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
		stats.getWeaponTurnRateBonus().unmodify(id);
                stats.getBeamWeaponTurnRateBonus().unmodify(id);
                isActive = false;
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float mult = 1f + RANGE_BONUS * effectLevel;
		float bonusPercent = (int) ((mult - 1f) * 100f);
		if (index == 0) {
			return new StatusData("range bonus +5" + (int) RANGE_BONUS + "%", false);
		}
		if (index == 1) {
			return new StatusData("weapon turn rate reduced to 1" + (int) TURNRATE_MULT + "%", false);
		}
		//if (index == 1) {
		//	return new StatusData("ballistic flux use -" + (int) FLUX_REDUCTION + "%", false);
		//}
		return null;
	}
}
