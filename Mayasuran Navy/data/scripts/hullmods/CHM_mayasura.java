package data.scripts.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.util.Misc;

public class CHM_mayasura extends BaseHullMod {
    public static final float MAX_CR_BONUS = 10f;
    
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
                stats.getMaxCombatReadiness().modifyFlat(id, MAX_CR_BONUS * 0.01f, "Mayasuran Readiness");
	}
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
                if (ship.getVariant().hasHullMod("CHM_commission")) {
                    ship.getVariant().removeMod("CHM_commission");
                }   
    }	
      @Override
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + "+" + (int) MAX_CR_BONUS + "%";
		return null;
	}
}
