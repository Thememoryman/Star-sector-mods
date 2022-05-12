package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;

public class RecklessEngineering extends BaseHullMod {
	
	private static final float PHASE_BONUS_MULT = 1.5f;
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
		//stats.getEngineMalfunctionChance().modifyFlat(id, ENGINE_MALFUNCTION_PROB);
		
		float phaseMult = PHASE_BONUS_MULT + (1f - PHASE_BONUS_MULT) * (1f - effect);
		//float peakMult = PEAK_PERFORMANCE_MULT + (1f - PEAK_PERFORMANCE_MULT) * (1f - effect);
		
		stats.getDynamic().getStat(Stats.PHASE_TIME_BONUS_MULT).modifyMult(id, phaseMult);
		//stats.getPeakCRDuration().modifyMult(id, peakMult);
		stats.getCRLossPerSecondPercent().modifyPercent(id, DEGRADE_INCREASE_PERCENT * effect);
		
		//CompromisedStructure.modifyCost(hullSize, stats, id);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		float effect = 1f;
		//if (ship != null) effect = ship.getMutableStats().getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		
		float phaseMult = PHASE_BONUS_MULT + (1f - PHASE_BONUS_MULT) * (1f - effect);
		//float peakMult = PEAK_PERFORMANCE_MULT + (1f - PEAK_PERFORMANCE_MULT) * (1f - effect);
		
		if (index == 0) return "" + (int) Math.round((PHASE_BONUS_MULT - 1f) * 100f) + "%";
		if (index == 1) return "" + (int) Math.round((SUPPLIES_TO_RECOVER - 1f) * 100f) + "%";
		if (index == 2) return "" + (int) Math.round((SUPPLIES_PER_MONTH - 1f) * 100f) + "%";
		if (index == 3) return "" + (int) Math.round(DEGRADE_INCREASE_PERCENT * effect) + "%";
		return null;
	}
}
