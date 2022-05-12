package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.combat.ShipAPI;

public class RetrofittedAccelerators extends BaseHullMod {
    
	public static final float COST_ADDITION_SMALL  = 2.0f;    
    	public static final float COST_ADDITION_MEDIUM  = 5.0f;
	public static final float COST_ADDITION_LARGE  = 8.0f;
        public static final float ROF_BONUS = 1.2f;
        public static final float PROFILE_MULT = 0.66f;
	public static final float HEALTH_BONUS = 50f;
	
	public void applyEffectsBeforeShipCreation(com.fs.starfarer.api.combat.ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
                stats.getDynamic().getMod(Stats.SMALL_BALLISTIC_MOD).modifyFlat(id, COST_ADDITION_SMALL);
                stats.getDynamic().getMod(Stats.MEDIUM_BALLISTIC_MOD).modifyFlat(id, COST_ADDITION_MEDIUM);
                stats.getDynamic().getMod(Stats.LARGE_BALLISTIC_MOD).modifyFlat(id, COST_ADDITION_LARGE);
                stats.getDynamic().getMod(Stats.SMALL_ENERGY_MOD).modifyFlat(id, COST_ADDITION_SMALL);
                stats.getDynamic().getMod(Stats.MEDIUM_ENERGY_MOD).modifyFlat(id, COST_ADDITION_MEDIUM);
                stats.getDynamic().getMod(Stats.LARGE_ENERGY_MOD).modifyFlat(id, COST_ADDITION_LARGE);
                stats.getBallisticRoFMult().modifyMult(id, ROF_BONUS);
                stats.getEnergyRoFMult().modifyMult(id, ROF_BONUS);
                stats.getSensorProfile().modifyMult(id, PROFILE_MULT);
		stats.getEngineHealthBonus().modifyPercent(id, HEALTH_BONUS);

	}
        
	public String getDescriptionParam(int index, HullSize hullSize) {
 		if (index == 0) return "" + (int) Math.round((ROF_BONUS - 1f) * 100f) + "%"; 
                if (index == 1) return "" + (int) ((1f - PROFILE_MULT) * 100f) + "%";
                if (index == 2) return "" + (int) HEALTH_BONUS + "%";
		if (index == 3) return "" + (int) COST_ADDITION_SMALL + "";
		if (index == 4) return "" + (int) COST_ADDITION_MEDIUM + "";
                if (index == 5) return "" + (int) COST_ADDITION_LARGE + "";    
                return null;
	}

	@Override
	public boolean affectsOPCosts() {
		return true;
	}

}








