package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.combat.ShipAPI;

public class PatchworkBallisticsIntegration extends BaseHullMod {

	public static final float COST_REDUCTION  = 8.0f;
        public static final float ROF_BONUS = 0.8f;
	
	public void applyEffectsBeforeShipCreation(com.fs.starfarer.api.combat.ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getDynamic().getMod(Stats.LARGE_BALLISTIC_MOD).modifyFlat(id, -COST_REDUCTION);
                stats.getBallisticRoFMult().modifyMult(id, 0.8f);
	}
        
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) COST_REDUCTION + "";
 		if (index == 1) return "" + (int) Math.round((ROF_BONUS - 1f) * 100f) + "%";      
                return null;
	}

	@Override
	public boolean affectsOPCosts() {
		return true;
	}

}








