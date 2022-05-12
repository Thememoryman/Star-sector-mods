package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.combat.ShipAPI;
import data.scripts.util.MagicIncompatibleHullmods;

public class ModularPlating extends BaseHullMod {
    
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains("frontshield")) {
			return "Incompatible with Makeshift Shield Generator";
		}
		return null;
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {

			if(stats.getVariant().getHullMods().contains("frontshield")){
			MagicIncompatibleHullmods.removeHullmodWithWarning(stats.getVariant(), "frontshield", "modularplating");
			}
		}	
}








