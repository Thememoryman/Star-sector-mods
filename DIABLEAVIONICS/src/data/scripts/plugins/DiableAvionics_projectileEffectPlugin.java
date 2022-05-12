package data.scripts.plugins;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
//import com.fs.starfarer.api.util.IntervalUtil;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DiableAvionics_projectileEffectPlugin extends BaseEveryFrameCombatPlugin {

    private static final Map<ShipAPI, ShieldDisruptionData> SHIELD_DISRUPTION = new HashMap<>();
//    private final IntervalUtil tic = new IntervalUtil(0.05f,0.1f);
    private final String ID = "hexafire_debuff";
    private static final float MAX_DURATION=4;
    private static final float DEBUFF=100;
    private final Color DISRUPTED_COLOR = new Color(210,180,50,96);

    @Override
    public void advance(float amount, List events) {
        
        if(Global.getCombatEngine().isPaused())return;
        
//        tic.advance(amount);
//        if(tic.intervalElapsed()){
            for (Iterator<Map.Entry< ShipAPI , ShieldDisruptionData >> iter = SHIELD_DISRUPTION.entrySet().iterator(); iter.hasNext();) {                
                Map.Entry< ShipAPI , ShieldDisruptionData > entry = iter.next();   
                
                //remove expended debuffs
                if( !entry.getKey().isAlive() || entry.getValue().duration-amount <= 0f ){
                    entry.getKey().getMutableStats().getShieldUpkeepMult().unmodify(ID);
                    entry.getKey().getShield().setInnerColor(entry.getValue().color);
                    iter.remove();
                    continue;
                }
                
                //apply the debuff otherwise
                entry.setValue(new ShieldDisruptionData(entry.getValue().color, entry.getValue().duration-amount));
                entry.getKey().getMutableStats().getShieldUpkeepMult().modifyPercent(ID, DEBUFF * entry.getValue().duration/MAX_DURATION);
                entry.getKey().getShield().setInnerColor(BlendColors(entry.getValue().color, DISRUPTED_COLOR, entry.getValue().duration/MAX_DURATION));
                if(entry.getKey()==Global.getCombatEngine().getPlayerShip()){
                    Global.getCombatEngine().maintainStatusForPlayerShip(ID, "graphics/icons/hullsys/fortress_shield.png", "Shield Disruption", "Shield upkeep: +"+(int)(DEBUFF * entry.getValue().duration/MAX_DURATION)+"%", true);
                }
            }
//        }
    }

    private Color BlendColors(Color from, Color to, float ratio){
        int r = from.getRed();
        int g = from.getGreen();
        int b = from.getBlue();
        int a = from.getAlpha();
        
        r+= (int)(ratio * (to.getRed()-r));
        g+= (int)(ratio * (to.getGreen()-g));
        b+= (int)(ratio * (to.getBlue()-b));
        a+= (int)(ratio * (to.getAlpha()-a));
        
        return new Color(r,g,b,a);
    }
    
    public static void addDisruption(ShipAPI target, float duration) {
        if(SHIELD_DISRUPTION.containsKey(target)){
            SHIELD_DISRUPTION.put(
                    target,
                    new ShieldDisruptionData(SHIELD_DISRUPTION.get(target).color, Math.min(MAX_DURATION, SHIELD_DISRUPTION.get(target).duration + duration))
            );
        } else {
            SHIELD_DISRUPTION.put(
                    target,
                    new ShieldDisruptionData(target.getShield().getInnerColor(),duration)
            );
        }
    }
    
    private static class ShieldDisruptionData{
        private final Color color;
        private Float duration;
        public ShieldDisruptionData (Color color, Float duration){
            this.color = color;
            this.duration = duration;
        }
    }

    @Override
    public void init(CombatEngineAPI engine) {  
        SHIELD_DISRUPTION.clear();
    }
}
