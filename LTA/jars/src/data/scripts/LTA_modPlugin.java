package data.scripts;
import data.scripts.world.LTA_gen;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;


public class LTA_modPlugin extends BaseModPlugin {


    
    ////////////////////////////////////////
    //                                    //
    //        ON NEW GAME CREATION        //
    //                                    //
    ////////////////////////////////////////
    
    @Override
    public void onNewGame() {
//        if (!Global.getSettings().getModManager().isModEnabled("nexerelin") || SectorManager.getCorvusMode()){
            new LTA_gen().generate(Global.getSector());
//        }
    }
    

    ////////////////////////////////////////
    //                                    //
    //            ON GAME LOAD            //
    //                                    //
    ////////////////////////////////////////
    
    @Override
    public void onGameLoad(boolean newGame){
    }    
}
