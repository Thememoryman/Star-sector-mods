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

public class ThunderbirdsCapital extends BaseHullMod {
	
        public static String MR_DATA_KEY = "core_reload_data_key";
    
    	public static class PeriodicMissileReloadData {
		IntervalUtil interval = new IntervalUtil(1.0f, 1.0f);
	}
        private static final float HULL_MULT = 0.9f;
        private static final float ARMOR_BONUS = 100f;
	private static final float CAPACITY_MULT = 1.1f;
	private static final float DISSIPATION_MULT = 1.1f;
	private static final float HANDLING_MULT = 1.0f;
        
        public static final float RELOAD_SECOND = 240f;
        
	private static final float WEAPON_DAMAGE_MULT = 1.0f;
        public static final float  ROF_BONUS = 1.0f;
	private static final float SUPPLIES_TO_RECOVER = 1.2f;
	private static final float SUPPLIES_PER_MONTH = 1.2f;
	private static final float DEGRADE_INCREASE_PERCENT = 0f;
        

	private static final float RECOILMAX_MULT = 0.85f;
        private static final float RECOILPERSHOT_MULT = 0.85f;
        private static final float WEAPON_DAMAGE_TAKEN_MULT = 0.6f;
        
	private static final float WEAPON_MALFUNCTION_PROB = 0.0f;
	private static final float ENGINE_MALFUNCTION_PROB = 0.0f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		float effect = 1f; //stats.getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		
		stats.getCriticalMalfunctionChance().modifyFlat(id, 0.01f * effect);
                
                stats.getHullBonus().modifyMult(id, (Float) HULL_MULT);
		stats.getArmorBonus().modifyFlat(id, (Float) ARMOR_BONUS);                
                
                stats.getFluxCapacity().modifyMult(id, CAPACITY_MULT);
		stats.getFluxDissipation().modifyMult(id, DISSIPATION_MULT);
                
		stats.getMaxSpeed().modifyMult(id, HANDLING_MULT);
		stats.getAcceleration().modifyMult(id, HANDLING_MULT);
		stats.getDeceleration().modifyMult(id, HANDLING_MULT);
		stats.getMaxTurnRate().modifyMult(id, HANDLING_MULT);
		stats. getTurnAcceleration().modifyMult(id, HANDLING_MULT);
                
		stats.getSuppliesToRecover().modifyMult(id, SUPPLIES_TO_RECOVER);
		stats.getSuppliesPerMonth().modifyMult(id, SUPPLIES_PER_MONTH);
		stats.getWeaponMalfunctionChance().modifyFlat(id, WEAPON_MALFUNCTION_PROB * effect);
		stats.getBallisticWeaponDamageMult().modifyMult(id, WEAPON_DAMAGE_MULT);
		stats.getEnergyWeaponDamageMult().modifyMult(id, WEAPON_DAMAGE_MULT);
		stats.getMissileWeaponDamageMult().modifyMult(id, WEAPON_DAMAGE_MULT);
		//stats.getEngineMalfunctionChance().modifyFlat(id, ENGINE_MALFUNCTION_PROB);
                stats.getMissileRoFMult().modifyMult(id, ROF_BONUS);
                
                stats.getMaxRecoilMult().modifyMult(id, RECOILMAX_MULT);
                stats.getRecoilPerShotMult().modifyMult(id, RECOILPERSHOT_MULT);
                //stats.getRecoilDecayMult().modifyMult(id, HANDLING_MULT);
                
		stats.getWeaponDamageTakenMult().modifyMult(id, WEAPON_DAMAGE_TAKEN_MULT);
		//float phaseMult = PHASE_BONUS_MULT + (1f - PHASE_BONUS_MULT) * (1f - effect);
		//float peakMult = PEAK_PERFORMANCE_MULT + (1f - PEAK_PERFORMANCE_MULT) * (1f - effect);
		
		//stats.getDynamic().getStat(Stats.PHASE_TIME_BONUS_MULT).modifyMult(id, phaseMult);
		//stats.getPeakCRDuration().modifyMult(id, peakMult);
		stats.getCRLossPerSecondPercent().modifyPercent(id, DEGRADE_INCREASE_PERCENT * effect);
		
		//CompromisedStructure.modifyCost(hullSize, stats, id);
	}
        
        
        @Override
	public void advanceInCombat(ShipAPI ship, float amount) {
            
            int second = (int)Math.floor(Global.getCombatEngine().getTotalElapsedTime(false));
            for(WeaponAPI weapon:  ship.getAllWeapons()){
            
            if(weapon.getType() == WeaponType.MISSILE){
            String key = ship.getId() + "_" + weapon.getSlot().getId();
            
            float second_per_ammo = weapon.getMaxAmmo() / RELOAD_SECOND;
            
            if(!Global.getCombatEngine().getCustomData().containsKey(key)){
              Global.getCombatEngine().getCustomData().put(key, 0f);
            }
                
            
            float reloaded = (float)Global.getCombatEngine().getCustomData().get(key);
            System.out.println("SPA:" + second_per_ammo + "_" + reloaded);
            if((int)(second_per_ammo * second - reloaded) > 1 && weapon.getAmmo() < weapon.getMaxAmmo()){
            weapon.setAmmo(weapon.getAmmo() + 1);
            Global.getCombatEngine().getCustomData().put(key, second_per_ammo * second);
                }
            }
        }
}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		float effect = 1f;
		//if (ship != null) effect = ship.getMutableStats().getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		
		//float phaseMult = PHASE_BONUS_MULT + (1f - PHASE_BONUS_MULT) * (1f - effect);
		//float peakMult = PEAK_PERFORMANCE_MULT + (1f - PEAK_PERFORMANCE_MULT) * (1f - effect);
		
		if (index == 0) return "" + (int) Math.round((1f - HULL_MULT) * 100f) + "%";
                if (index == 1) return Misc.getRoundedValue(ARMOR_BONUS);
        	if (index == 2) return "" + (int) Math.round((1f - WEAPON_DAMAGE_TAKEN_MULT) * 100f) + "%";
                if (index == 3) return "" + (int) Math.round((1f - RECOILMAX_MULT) * 100f) + "%";
                if (index == 4) return "" + (int) Math.round((DISSIPATION_MULT - 1f) * 100f) + "%";
		if (index == 5) return "" + (int) Math.round((SUPPLIES_TO_RECOVER - 1f) * 100f) + "%";
		if (index == 6) return "" + (int) Math.round((SUPPLIES_PER_MONTH - 1f) * 100f) + "%";
		return null;
	}
}
