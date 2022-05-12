package data.scripts.world;

import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import data.scripts.world.systems.Diableavionics_stagging;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.scripts.world.systems.Diableavionics_fob;
import data.scripts.world.systems.Diableavionics_outerTerminus;

@SuppressWarnings("unchecked")
public class DiableavionicsGen implements SectorGeneratorPlugin {

    @Override
    public void generate(SectorAPI sector) {
	
        new Diableavionics_outerTerminus().generate(sector);
        new Diableavionics_stagging().generate(sector);
        new Diableavionics_fob().generate(sector);

        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("diableavionics");                      

        FactionAPI diableavionics = sector.getFaction("diableavionics");
        FactionAPI player = sector.getFaction(Factions.PLAYER);
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI independent = sector.getFaction(Factions.INDEPENDENT); 
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);   	
        FactionAPI diktat = sector.getFaction(Factions.DIKTAT); 
        FactionAPI kol = sector.getFaction(Factions.KOL);	
	FactionAPI persean = sector.getFaction(Factions.PERSEAN);
        FactionAPI guard = sector.getFaction(Factions.LIONS_GUARD);
        FactionAPI remnant = sector.getFaction(Factions.REMNANTS);
        FactionAPI derelict = sector.getFaction(Factions.DERELICT);
        
        //vanilla factions
        diableavionics.setRelationship(guard.getId(), RepLevel.FRIENDLY);    
        
        diableavionics.setRelationship(diktat.getId(), RepLevel.FAVORABLE); 
        
        diableavionics.setRelationship(player.getId(), RepLevel.SUSPICIOUS);
        
        diableavionics.setRelationship(independent.getId(), RepLevel.INHOSPITABLE);  
        diableavionics.setRelationship(tritachyon.getId(), RepLevel.INHOSPITABLE);      
        diableavionics.setRelationship(pirates.getId(), RepLevel.INHOSPITABLE);
        diableavionics.setRelationship(persean.getId(), RepLevel.INHOSPITABLE);	
        diableavionics.setRelationship(kol.getId(), RepLevel.INHOSPITABLE); 
        
        diableavionics.setRelationship(hegemony.getId(), RepLevel.HOSTILE);
        diableavionics.setRelationship(path.getId(), RepLevel.HOSTILE);   
        
        diableavionics.setRelationship(church.getId(), RepLevel.VENGEFUL);   
        
        //environment
        diableavionics.setRelationship(remnant.getId(), RepLevel.HOSTILE);	
        diableavionics.setRelationship(derelict.getId(), RepLevel.FRIENDLY);     
        
        //mods
        diableavionics.setRelationship("cabal", RepLevel.FRIENDLY);    
           
        diableavionics.setRelationship("sun_ici", RepLevel.FAVORABLE);       
        
        diableavionics.setRelationship("crystanite", RepLevel.NEUTRAL); 
        diableavionics.setRelationship("mayorate", RepLevel.NEUTRAL);	 
        diableavionics.setRelationship("pirateAnar", RepLevel.NEUTRAL);	 
        diableavionics.setRelationship("exipirated", RepLevel.NEUTRAL);	 
        
        diableavionics.setRelationship("exigency", RepLevel.SUSPICIOUS);
        diableavionics.setRelationship("syndicate_asp", RepLevel.SUSPICIOUS);	   
        diableavionics.setRelationship("tiandong", RepLevel.SUSPICIOUS);        
        diableavionics.setRelationship("SCY", RepLevel.SUSPICIOUS);     
        diableavionics.setRelationship("neutrinocorp", RepLevel.SUSPICIOUS);           
        
        diableavionics.setRelationship("6eme_bureau", RepLevel.INHOSPITABLE);	
        diableavionics.setRelationship("dassault_mikoyan", RepLevel.INHOSPITABLE);
        diableavionics.setRelationship("pack", RepLevel.INHOSPITABLE);     
        diableavionics.setRelationship("blackrock_driveyards", RepLevel.INHOSPITABLE);	   
        diableavionics.setRelationship("citadeldefenders", RepLevel.INHOSPITABLE);
        diableavionics.setRelationship("pn_colony", RepLevel.INHOSPITABLE);    
        diableavionics.setRelationship("junk_pirates", RepLevel.INHOSPITABLE);  
        diableavionics.setRelationship("sun_ice", RepLevel.INHOSPITABLE);          
            
        diableavionics.setRelationship("shadow_industry", RepLevel.HOSTILE);	
        diableavionics.setRelationship("ORA", RepLevel.HOSTILE);     
        diableavionics.setRelationship("interstellarimperium", RepLevel.HOSTILE);     
        diableavionics.setRelationship("blade_breakers", RepLevel.HOSTILE);   
        	
        diableavionics.setRelationship("new_galactic_order", RepLevel.VENGEFUL);	
        diableavionics.setRelationship("explorer_society", RepLevel.VENGEFUL);
        
        diableavionics.setRelationship("Coalition", -0.2f);      
        diableavionics.setRelationship("metelson", -0.2f);     
        diableavionics.setRelationship("the_deserter", 0.35f);    
        diableavionics.setRelationship("noir", 0.0f);     
        diableavionics.setRelationship("Lte", 0.0f);  
        diableavionics.setRelationship("GKSec", 0.1f); 
        diableavionics.setRelationship("gmda", -0.1f);   
        diableavionics.setRelationship("oculus", -0.25f);     
        diableavionics.setRelationship("nomads", -0.25f); 
        diableavionics.setRelationship("thulelegacy", -0.25f); 
        diableavionics.setRelationship("infected", -0.99f);      
    }
}