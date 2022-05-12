package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import java.util.HashMap;
import java.util.Map;
import org.lazywizard.lazylib.MathUtils;
import static data.scripts.util.Diableavionics_stringsManager.txt;
import java.util.ArrayList;
import java.util.List;

public class DiableAvionicsVirtuous_system extends BaseHullMod {
    
    
//    private final Map<Integer,String> LEFT_SELECTOR = new HashMap<>();
//    {
//        LEFT_SELECTOR.put(0, "diableavionics_versant_harvest_LEFT");
//        LEFT_SELECTOR.put(1, "diableavionics_versant_harvestB_LEFT");
//        LEFT_SELECTOR.put(2, "diableavionics_versant_harvestC_LEFT");
//    }
//    
//    private final Map<Integer,String> RIGHT_SELECTOR = new HashMap<>();
//    {
//        RIGHT_SELECTOR.put(0, "diableavionics_versant_harvest_RIGHT");
//        RIGHT_SELECTOR.put(1, "diableavionics_versant_harvestB_RIGHT");
//        RIGHT_SELECTOR.put(2, "diableavionics_versant_harvestC_RIGHT");
//    }
    
    private final Map<String, Integer> SWITCH_SYSTEM_TO = new HashMap<>();
    {
        SWITCH_SYSTEM_TO.put("diableavionics_virtuous_skirmisher",1);
        SWITCH_SYSTEM_TO.put("diableavionics_virtuous_brawler",2);
        SWITCH_SYSTEM_TO.put("diableavionics_virtuous_defender",3);
        SWITCH_SYSTEM_TO.put("diableavionics_virtuous_scout",0);
    }
    
    private final Map<Integer,String> SWITCH_SYSTEM = new HashMap<>();
    {
        SWITCH_SYSTEM.put(0,"diableavionics_virtuous_skirmisher");
        SWITCH_SYSTEM.put(1,"diableavionics_virtuous_brawler");
        SWITCH_SYSTEM.put(2,"diableavionics_virtuous_defender");
        SWITCH_SYSTEM.put(3,"diableavionics_virtuous_scout");
    }  
    
//    private final List<String> TRIGGERS = new ArrayList<>();
//    {
//        TRIGGERS.add("diableavionics_virtuous_skirmisher");
//        TRIGGERS.add("diableavionics_virtuous_brawler");
//        TRIGGERS.add("diableavionics_virtuous_defender");
//        TRIGGERS.add("diableavionics_virtuous_scout");
//    }
    
//    private final String leftslotID = "GUN_LEFT"; 
//    private final String rightslotID = "GUN_RIGHT";     
    
    @Override
    public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
        
        //trigger a weapon switch if none of the selector hullmods are present
        boolean toSwitch=true;
        for(String h : SWITCH_SYSTEM_TO.keySet()){
            if(stats.getVariant().getHullMods().contains(h)){
                toSwitch=false;
            }
        }
        
        
        
        
        //remove the weapons to change and swap the hullmod for the next fire mode
        if(toSwitch){        
            
            int switchTo = SWITCH_SYSTEM_TO.get(((ShipAPI)stats.getEntity()).getHullSpec().getHullId());

            ShipHullSpecAPI ship = Global.getSettings().getHullSpec(SWITCH_SYSTEM.get(switchTo));
            ((ShipAPI)stats.getEntity()).getVariant().setHullSpecAPI(ship);
            
            //add the proper hullmod
            stats.getVariant().addMod(SWITCH_SYSTEM.get(switchTo));

//            //clear the weapons to replace
//            stats.getVariant().clearSlot(leftslotID);
//            stats.getVariant().clearSlot(rightslotID);
//            //select and place the proper weapon
//            String toInstallLeft=LEFT_SELECTOR.get(selected);                
//            String toInstallRight=RIGHT_SELECTOR.get(selected);
//
//            stats.getVariant().addWeapon(leftslotID, toInstallLeft);
//            stats.getVariant().addWeapon(rightslotID, toInstallRight);
            
//            if(random){
//                stats.getVariant().autoGenerateWeaponGroups();
//            }
        }
    }
    
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id){
        //only check for undo in refit to avoid issues
//        if(ship.getOriginalOwner()<0){
//            //undo fix for harvests put in cargo
//            if(
//                    Global.getSector()!=null && 
//                    Global.getSector().getPlayerFleet()!=null && 
//                    Global.getSector().getPlayerFleet().getCargo()!=null && 
//                    Global.getSector().getPlayerFleet().getCargo().getStacksCopy()!=null &&
//                    !Global.getSector().getPlayerFleet().getCargo().getStacksCopy().isEmpty()
//                    ){
//                for (CargoStackAPI s : Global.getSector().getPlayerFleet().getCargo().getStacksCopy()){
//                    if(
//                            s.isWeaponStack() && (
//                                LEFT_SELECTOR.containsValue(s.getWeaponSpecIfWeapon().getWeaponId()) || 
//                                RIGHT_SELECTOR.containsValue(s.getWeaponSpecIfWeapon().getWeaponId())
//                                ) 
//                            ){
//                        Global.getSector().getPlayerFleet().getCargo().removeStack(s);
//                    }
//                }
//            }
//        }
    }
    
    @Override
    public String getDescriptionParam(int index, HullSize hullSize) { 
//        if (index == 0) return txt("hm_tuning_0");
//        if (index == 1) return txt("hm_tuning_1");
//        if (index == 2) return txt("hm_tuning_2");
//        if (index == 3) return txt("hm_tuning_3");        
                
        return null;
    }
    
    @Override
    public boolean isApplicableToShip(ShipAPI ship) {
        // Allows any ship with a diableavionics hull id
        return ( ship.getHullSpec().getHullId().startsWith("diableavionics_"));	
    }
}
