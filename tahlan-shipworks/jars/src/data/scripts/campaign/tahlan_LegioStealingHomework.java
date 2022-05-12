//code by Vayra, kudos

package data.scripts.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.util.IntervalUtil;
import org.apache.log4j.Logger;

public class tahlan_LegioStealingHomework implements EveryFrameScript {

    public static Logger log = Global.getLogger(tahlan_LegioStealingHomework.class);

    public static final String LEGIO_ID = "tahlan_legioinfernalis";

    // only check every couple days
    private final IntervalUtil timer = new IntervalUtil(2f, 3f);
    private IntervalUtil t;

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {

        float days = Global.getSector().getClock().convertToDays(amount);
        timer.advance(days);
        if (timer.intervalElapsed()) {
            log.info(String.format("Interval elapsed, the space fascists gonna learn today"));
            stealPirateBlueprints();
        }
    }

    public void stealPirateBlueprints() {

        for (String weapon : Global.getSector().getFaction(Factions.PIRATES).getKnownWeapons()) {
            if (!Global.getSector().getFaction(LEGIO_ID).knowsWeapon(weapon)) {
                Global.getSector().getFaction(LEGIO_ID).addKnownWeapon(weapon, true);
            }
        }
        
        for (String ship : Global.getSector().getFaction(Factions.PIRATES).getKnownShips()) {
            if (!Global.getSector().getFaction(LEGIO_ID).knowsShip(ship)) {
                Global.getSector().getFaction(LEGIO_ID).addKnownShip(ship, true);
            }
        } 
        
        for (String baseShip : Global.getSector().getFaction(Factions.PIRATES).getAlwaysKnownShips()) {
            if (!Global.getSector().getFaction(LEGIO_ID).useWhenImportingShip(baseShip)) {
                Global.getSector().getFaction(LEGIO_ID).addUseWhenImportingShip(baseShip);
            }
        }

        for (String fighter : Global.getSector().getFaction(Factions.PIRATES).getKnownFighters()) {
            if (!Global.getSector().getFaction(LEGIO_ID).knowsFighter(fighter)) {
                Global.getSector().getFaction(LEGIO_ID).addKnownFighter(fighter, true);
            }
        }
    }
}
