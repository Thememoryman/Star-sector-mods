package data.scripts.world.systems;

import java.awt.Color;


import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.CargoAPI.CargoItemType;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI.SurveyLevel;
import com.fs.starfarer.api.impl.campaign.JumpPointInteractionDialogPluginImpl;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin.DerelictShipData;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.procgen.DefenderDataOverride;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.DerelictThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner.ShipRecoverySpecialCreator;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.BaseSalvageSpecial;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.PerShipData;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial.ShipCondition;
import com.fs.starfarer.api.impl.campaign.terrain.BaseTiledTerrain;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldParams;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin.DebrisFieldSource;
import com.fs.starfarer.api.util.Misc;
import static java.time.Clock.system;


public class LTA_Magec{

	public void generate(SectorAPI sector) {
		
		// Ancyra Relay - L4 (ahead)
		//SectorEntityToken relay = system.addCustomEntity("ancyra_relay", // unique id
		//		 "Ancyra Relay", // name - if null, defaultName from custom_entities.json will be used
		//		 Entities.COMM_RELAY, // type of object, defined in custom_entities.json
		//		 Factions.HEGEMONY); // faction
		//relay.setCircularOrbitPointingDown(star, 
		//								   ancyra.getCircularOrbitAngle() - 60f, 
		//								   ancyra.getCircularOrbitRadius(),
		//								   ancyra.getCircularOrbitPeriod());
		//relay.getMemoryWithoutUpdate().set(MemFlags.OBJECTIVE_NON_FUNCTIONAL, true);
		
		
		// Galatia Gate
		////SectorEntityToken station = system.addCustomEntity("LTA_Epattcudvi_UpcycledDerelict", // unique id
		////		 "null", // name - if null, defaultName from custom_entities.json will be used
		////		 Entities.STATION, // type of object, defined in custom_entities.json
		////		 pirates); // faction
		////station.setCircularOrbit(star, 120, 120, 120);
        StarSystemAPI sys = Global.getSector().getStarSystem("Magec");
        if (sys == null) return;
        SectorEntityToken entity = sys.addCustomEntity("LTA_Epattcudvi_UpcycledDerelict", "Derelict Ship", "LTA_Epattcudvi_UpcycledDerelict", "neutral");
        entity.setDiscoverable(true);
        entity.setSensorProfile(2000f);
        
        
        entity.setCircularOrbit(sys.getCenter(), 90, 25000, 750);
    
    //StarSystemAPI _Magec = (StarSystemAPI)_fleet.getContainingLocation();
    //SectorEntityToken _stable = Magec.addCustomEntity(null, null, "LTA_Epattcudvi_UpcycledDerelict", "neutral");
    //_stable.setCircularOrbit(Magec.star, 120, 120, 120);
	}
	
	protected void addDerelict(StarSystemAPI system, SectorEntityToken focus, String variantId, 
								ShipCondition condition, float orbitRadius, boolean recoverable) {
		DerelictShipData params = new DerelictShipData(new PerShipData(variantId, condition), false);
		SectorEntityToken ship = BaseThemeGenerator.addSalvageEntity(system, Entities.WRECK, Factions.NEUTRAL, params);
		ship.setDiscoverable(true);
		
		float orbitDays = orbitRadius / (10f + (float) Math.random() * 5f);
		ship.setCircularOrbit(focus, (float) Math.random() * 360f, orbitRadius, orbitDays);
		
		if (recoverable) {
			ShipRecoverySpecialCreator creator = new ShipRecoverySpecialCreator(null, 0, 0, false, null, null);
			Misc.setSalvageSpecial(ship, creator.createSpecial(ship, null));
		}
		
	}
}









