package data.scripts.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;

public class RecklessEngineeringNonPhase extends BaseHullMod {
	
        public static String MR_DATA_KEY = "core_reload_data_key";
    
    	public static class PeriodicMissileReloadData {
		IntervalUtil interval = new IntervalUtil(0.25f, 7.5f);
	}
    
	private static final float WEAPON_DAMAGE_MULT = 1.2f;
        public static final float ROF_BONUS = 1.33f;
	private static final float SUPPLIES_TO_RECOVER = 1.2f;
	private static final float SUPPLIES_PER_MONTH = 1.2f;	
	private static final float DEGRADE_INCREASE_PERCENT = 30f;
        

	private static final float RECOIL_MULT = 2f;
	private static final float WEAPON_MALFUNCTION_PROB = 0.025f;
	private static final float ENGINE_MALFUNCTION_PROB = 0.0025f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		float effect = 1f; //stats.getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		
		stats.getCriticalMalfunctionChance().modifyFlat(id, 0.01f * effect);
		stats.getSuppliesToRecover().modifyMult(id, SUPPLIES_TO_RECOVER);
		stats.getSuppliesPerMonth().modifyMult(id, SUPPLIES_PER_MONTH);
		stats.getWeaponMalfunctionChance().modifyFlat(id, WEAPON_MALFUNCTION_PROB * effect);
		stats.getBallisticWeaponDamageMult().modifyMult(id, WEAPON_DAMAGE_MULT);
		stats.getEnergyWeaponDamageMult().modifyMult(id, WEAPON_DAMAGE_MULT);
		stats.getMissileWeaponDamageMult().modifyMult(id, WEAPON_DAMAGE_MULT);
		//stats.getEngineMalfunctionChance().modifyFlat(id, ENGINE_MALFUNCTION_PROB);
                stats.getMissileRoFMult().modifyMult(id, ROF_BONUS);
		
		//float phaseMult = PHASE_BONUS_MULT + (1f - PHASE_BONUS_MULT) * (1f - effect);
		//float peakMult = PEAK_PERFORMANCE_MULT + (1f - PEAK_PERFORMANCE_MULT) * (1f - effect);
		
		//stats.getDynamic().getStat(Stats.PHASE_TIME_BONUS_MULT).modifyMult(id, phaseMult);
		//stats.getPeakCRDuration().modifyMult(id, peakMult);
		stats.getCRLossPerSecondPercent().modifyPercent(id, DEGRADE_INCREASE_PERCENT * effect);
		
		//CompromisedStructure.modifyCost(hullSize, stats, id);
	}
        
        
        @Override
	public void advanceInCombat(ShipAPI ship, float amount) {
		super.advanceInCombat(ship, amount);

		if (!ship.isAlive()) return;
		
		CombatEngineAPI engine = Global.getCombatEngine();
		
		String key = MR_DATA_KEY + "_" + ship.getId();
		PeriodicMissileReloadData data = (PeriodicMissileReloadData) engine.getCustomData().get(key);
		if (data == null) {
			data = new PeriodicMissileReloadData();
			engine.getCustomData().put(key, data);
		}
		
		data.interval.advance(amount);
		if (data.interval.intervalElapsed()) {
			for (WeaponAPI w : ship.getAllWeapons()) {
				if (w.getType() != WeaponType.MISSILE) continue;
				
				if (w.usesAmmo() && w.getAmmo() < w.getMaxAmmo()) {
					w.setAmmo(w.getAmmo() + 1);
				}
			}
		}
		
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		float effect = 1f;
		//if (ship != null) effect = ship.getMutableStats().getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		
		//float phaseMult = PHASE_BONUS_MULT + (1f - PHASE_BONUS_MULT) * (1f - effect);
		//float peakMult = PEAK_PERFORMANCE_MULT + (1f - PEAK_PERFORMANCE_MULT) * (1f - effect);
		
		if (index == 0) return "" + (int) Math.round((WEAPON_DAMAGE_MULT - 1f) * 100f) + "%";
                if (index == 1) return "" + (int) Math.round((ROF_BONUS - 1f) * 100f) + "%";
		if (index == 2) return "" + (int) Math.round((SUPPLIES_TO_RECOVER - 1f) * 100f) + "%";
		if (index == 3) return "" + (int) Math.round((SUPPLIES_PER_MONTH - 1f) * 100f) + "%";
		if (index == 4) return "" + (int) Math.round(DEGRADE_INCREASE_PERCENT * effect) + "%";
		return null;
	}
}
