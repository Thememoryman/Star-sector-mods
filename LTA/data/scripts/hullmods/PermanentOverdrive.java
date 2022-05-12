package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.combat.ShipAPI;

public class PermanentOverdrive extends BaseHullMod {
    
	public static final float COST_ADDITION_SMALL  = 5.0f;    
    	public static final float COST_ADDITION_MEDIUM  = 10.0f;
	public static final float COST_ADDITION_LARGE  = 20.0f;
        public static final float ROF_BONUS = 2.0f;
        public static final float TURNRATE_MULT = 0.5f;
	//public static final float HEALTH_BONUS = 50f;
	
	public void applyEffectsBeforeShipCreation(com.fs.starfarer.api.combat.ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
                stats.getDynamic().getMod(Stats.SMALL_BALLISTIC_MOD).modifyFlat(id, COST_ADDITION_SMALL);
                stats.getDynamic().getMod(Stats.MEDIUM_BALLISTIC_MOD).modifyFlat(id, COST_ADDITION_MEDIUM);
                stats.getDynamic().getMod(Stats.LARGE_BALLISTIC_MOD).modifyFlat(id, COST_ADDITION_LARGE);
                stats.getDynamic().getMod(Stats.SMALL_ENERGY_MOD).modifyFlat(id, COST_ADDITION_SMALL);
                stats.getDynamic().getMod(Stats.MEDIUM_ENERGY_MOD).modifyFlat(id, COST_ADDITION_MEDIUM);
                stats.getDynamic().getMod(Stats.LARGE_ENERGY_MOD).modifyFlat(id, COST_ADDITION_LARGE);
                stats.getDynamic().getMod(Stats.SMALL_MISSILE_MOD).modifyFlat(id, COST_ADDITION_SMALL);
                stats.getDynamic().getMod(Stats.MEDIUM_MISSILE_MOD).modifyFlat(id, COST_ADDITION_MEDIUM);
                stats.getDynamic().getMod(Stats.LARGE_MISSILE_MOD).modifyFlat(id, COST_ADDITION_LARGE);
                stats.getBallisticRoFMult().modifyMult(id, ROF_BONUS);
                stats.getEnergyRoFMult().modifyMult(id, ROF_BONUS);
                stats.getMissileRoFMult().modifyMult(id, ROF_BONUS);
                //stats.getSensorProfile().modifyMult(id, PROFILE_MULT);
		stats.getWeaponTurnRateBonus().modifyMult(id, TURNRATE_MULT);
                stats.getBeamWeaponTurnRateBonus().modifyMult(id, TURNRATE_MULT);

	}
        
	public String getDescriptionParam(int index, HullSize hullSize) {
 		if (index == 0) return "" + (int) Math.round((ROF_BONUS - 1f) * 100f) + "%"; 
                if (index == 1) return "" + (int) ((1f - TURNRATE_MULT) * 100f) + "%";
		if (index == 2) return "" + (int) COST_ADDITION_SMALL + "";
		if (index == 3) return "" + (int) COST_ADDITION_MEDIUM + "";
                if (index == 4) return "" + (int) COST_ADDITION_LARGE + "";    
                return null;
	}

	@Override
	public boolean affectsOPCosts() {
		return true;
	}

}








