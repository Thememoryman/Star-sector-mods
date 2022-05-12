package data.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import static data.scripts.util.Diableavionics_stringsManager.txt;

public class DiableAvionicsSemi extends BaseHullMod {    
    
    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        if (index == 0) return txt("hm_selector_2");
        if (index == 1) return txt("hm_selector");        
        return null;
    }
}
