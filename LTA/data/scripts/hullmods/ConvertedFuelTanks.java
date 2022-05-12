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
import com.fs.starfarer.api.util.Misc;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;

public class ConvertedFuelTanks extends BaseHullMod {
	
        private static final float FUELCAPACITYMOD = 75f;

	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		float effect = 1f; //stats.getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		
                stats.getFuelMod().modifyMult(id, 1f - (FUELCAPACITYMOD * effect) / 100f);

	}
        
        
        @Override
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		float effect = 1f;
		//if (ship != null) effect = ship.getMutableStats().getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		
		//float phaseMult = PHASE_BONUS_MULT + (1f - PHASE_BONUS_MULT) * (1f - effect);
		//float peakMult = PEAK_PERFORMANCE_MULT + (1f - PEAK_PERFORMANCE_MULT) * (1f - effect);
		
		if (index == 0) return "" + (int) Math.round((1f - FUELCAPACITYMOD) * 1f) + "%";
		return null;
	}
}
