package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class DomainMissilePackageMining extends BaseHullMod {

	//public static final float EW_PENALTY_MULT = 0.5f;
	public static final float DEBILITATION = 0.66f;
        public static final float DAMAGE_MULT = 1.5f;
	//public static final float ECCM_CHANCE = 0.5f;
	//public static final float GUIDANCE_IMPROVEMENT = 1f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getEccmChance().modifyFlat(id, ECCM_CHANCE);
		//stats.getMissileGuidance().modifyFlat(id, GUIDANCE_IMPROVEMENT);
		
		stats.getMissileMaxSpeedBonus().modifyMult(id, DEBILITATION);
		stats.getMissileAccelerationBonus().modifyMult(id, DEBILITATION);
		stats.getMissileMaxTurnRateBonus().modifyMult(id, DEBILITATION);
		stats.getMissileTurnAccelerationBonus().modifyMult(id, DEBILITATION);
                stats.getMissileRoFMult().modifyMult(id, DEBILITATION);
                
		stats.getMissileWeaponDamageMult().modifyMult(id, DAMAGE_MULT);
		
		//stats.getDynamic().getStat(Stats.ELECTRONIC_WARFARE_PENALTY_MULT).modifyMult(id, EW_PENALTY_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) ((1f - DEBILITATION) * 100f) + "%";
		//if (index == 1) return "" + (int) (GUIDANCE_IMPROVEMENT * 100f) + "%";
		if (index == 1) return "" + (int) ((DAMAGE_MULT - 1f) * 100f) + "%";
		return null;
	}
}