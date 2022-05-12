package data.scripts.world;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.DerelictShipEntityPlugin;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.procgen.PlanetConditionGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.SalvageSpecialAssigner;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.special.ShipRecoverySpecial;
import com.fs.starfarer.api.impl.campaign.terrain.DebrisFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.lazylib.MathUtils;

import static data.scripts.world.tahlan_Lethia.addMarketplace;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class tahlan_Rubicon  {
    public void generate(SectorAPI sector) {

        StarSystemAPI system = sector.createStarSystem("Rubicon");
        system.getLocation().set(-28000, -4500);
        system.setBackgroundTextureFilename("graphics/tahlan/backgrounds/tahlan_rubicon.jpg");

        PlanetAPI rubicon_star = system.initStar("tahlan_rubicon_maw",
                "black_hole",
                140f,
                300f);

        rubicon_star.setName("The Abyssal Maw");

        system.setLightColor(new Color(255, 205, 205));

        system.addRingBand(rubicon_star, "misc", "rings_asteroids0", 256f, 2, new Color(101, 76, 49), 500f, 600f, 200f);
        system.addRingBand(rubicon_star, "misc", "rings_dust0", 256f, 2, Color.gray, 500f, 1000f, 200f);
        system.addAsteroidBelt(rubicon_star, 300, 700, 500, 120, 300, Terrain.ASTEROID_BELT,"");


        // Debris fields
        DebrisFieldTerrainPlugin.DebrisFieldParams params1 = new DebrisFieldTerrainPlugin.DebrisFieldParams(
                130f,
                1.2f,
                10000000f,
                10000000f);
        params1.source = DebrisFieldTerrainPlugin.DebrisFieldSource.BATTLE;
        params1.baseSalvageXP = 550;
        params1.glowColor = Color.white;

        SectorEntityToken debrisRubicon1 = Misc.addDebrisField(system,params1,StarSystemGenerator.random);
        debrisRubicon1.setSensorProfile(1500f);
        debrisRubicon1.setDiscoverable(true);
        debrisRubicon1.setCircularOrbit(rubicon_star,360*(float)Math.random(),700f,250f);
        debrisRubicon1.setId("tahlan_lethia_debrisRubicon1");

        SectorEntityToken debrisRubicon2 = Misc.addDebrisField(system,params1,StarSystemGenerator.random);
        debrisRubicon2.setSensorProfile(1500f);
        debrisRubicon2.setDiscoverable(true);
        debrisRubicon2.setCircularOrbit(rubicon_star,360*(float)Math.random(),1300f,300f);
        debrisRubicon2.setId("tahlan_lethia_debrisRubicon2");


        PlanetAPI rubicon_star2 = system.addPlanet("tahlan_rubicon_heart", rubicon_star, "The Infernal Heart", "star_red_dwarf", 90f, 200f, 2100f, 300f);
        system.addCorona(rubicon_star2, 200f, 5f, 0.2f, 2f);

        addDerelict(system,rubicon_star2,"tahlan_DunScaith_barrage", ShipRecoverySpecial.ShipCondition.WRECKED, 500f, Math.random()<0.1);

        system.addAsteroidBelt(rubicon_star, 1000, 3000, 1000, 120, 500, Terrain.ASTEROID_BELT,"");
        system.addRingBand(rubicon_star, "misc", "rings_asteroids0", 256f, 1, Color.gray, 500f, 3000f, 250f);

        // First Planet
        PlanetAPI rubicon_1 = system.addPlanet("tahlan_rubicon_p01",
                rubicon_star,
                "Melchiresa",
                "cryovolcanic",
                360f*(float)Math.random(),
                150f,
                3600f,
                320f);

        rubicon_1.setCustomDescriptionId("tahlan_rubicon_p01");

        MarketAPI rubicon_1_market = addMarketplace("tahlan_legioinfernalis", rubicon_1, null,
                "Melchiresa",
                5,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_5,
                                Conditions.ORE_RICH,
                                Conditions.RARE_ORE_ABUNDANT,
                                Conditions.COLD,
                                Conditions.LOW_GRAVITY,
                                Conditions.ORGANIZED_CRIME,
                                "tahlan_legiotyranny"
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.GENERIC_MILITARY,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE,
                                Submarkets.SUBMARKET_BLACK
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.MEGAPORT,
                                Industries.MINING,
                                Industries.STARFORTRESS,
                                Industries.HEAVYBATTERIES,
                                Industries.MILITARYBASE,
                                Industries.REFINING,
                                Industries.PLANETARYSHIELD
                        )
                ),
                0.3f,
                true,
                true);

        rubicon_1_market.getIndustry(Industries.MILITARYBASE).setAICoreId(Commodities.ALPHA_CORE);
        rubicon_1_market.getIndustry(Industries.STARFORTRESS).setAICoreId(Commodities.ALPHA_CORE);
        rubicon_1_market.getIndustry(Industries.MEGAPORT).setAICoreId(Commodities.ALPHA_CORE);
        rubicon_1_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.ALPHA_CORE);

        SectorEntityToken stableLoc1 = system.addCustomEntity("tahlan_rubicon_stableloc_1", "Stable Location", "stable_location", Factions.NEUTRAL);
        stableLoc1.setCircularOrbit(rubicon_star, MathUtils.getRandomNumberInRange(0f,360f),4200f, 520);

        // Second Planet
        PlanetAPI rubicon_2 = system.addPlanet("tahlan_rubicon_p02",
                rubicon_star,
                "Ornias",
                "barren3",
                360f*(float)Math.random(),
                210f,
                4800f,
                410f);

        PlanetConditionGenerator.generateConditionsForPlanet(rubicon_2,StarAge.OLD);

        SectorEntityToken relay = system.addCustomEntity("tahlan_rubicon_relay", // unique id
                "Rubicon Relay", // name - if null, defaultName from custom_entities.json will be used
                "comm_relay", // type of object, defined in custom_entities.json
                "tahlan_legioinfernalis"); // faction
        relay.setCircularOrbitPointingDown( rubicon_star, 360f*(float)Math.random(), 5900, MathUtils.getRandomNumberInRange(250, 410));

        // Third Planet - Primary Legio base
        PlanetAPI rubicon_3 = system.addPlanet("tahlan_rubicon_p03",
                rubicon_star,
                "Lucifron",
                "toxic_cold",
                360f*(float)Math.random(),
                320f,
                6000f,
                380f);

        rubicon_3.setCustomDescriptionId("tahlan_rubicon_p03");

        MarketAPI rubicon_3_market = addMarketplace("tahlan_legioinfernalis", rubicon_3, null,
                "Lucifron",
                7,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_7,
                                Conditions.ORE_SPARSE,
                                Conditions.RARE_ORE_SPARSE,
                                Conditions.ORGANICS_COMMON,
                                Conditions.COLD,
                                Conditions.HIGH_GRAVITY,
                                Conditions.ORGANIZED_CRIME,
                                Conditions.REGIONAL_CAPITAL,
                                "tahlan_legiotyranny"
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.GENERIC_MILITARY,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE,
                                Submarkets.SUBMARKET_BLACK
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.MEGAPORT,
                                Industries.MINING,
                                Industries.STARFORTRESS,
                                Industries.HEAVYBATTERIES,
                                Industries.HIGHCOMMAND,
                                Industries.PLANETARYSHIELD,
                                Industries.LIGHTINDUSTRY,
                                "tahlan_CloningFacility"
                        )
                ),
                0.3f,
                true,
                true);

        rubicon_3_market.addIndustry(Industries.ORBITALWORKS,new ArrayList<String>(Arrays.asList(Items.PRISTINE_NANOFORGE)));
        rubicon_3_market.getIndustry(Industries.HIGHCOMMAND).setAICoreId(Commodities.ALPHA_CORE);
        rubicon_3_market.getIndustry(Industries.STARFORTRESS).setAICoreId(Commodities.ALPHA_CORE);
        rubicon_3_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.ALPHA_CORE);


        //debris of failed attacks
        SectorEntityToken debrisRubicon3 = Misc.addDebrisField(system,params1,StarSystemGenerator.random);
        debrisRubicon3.setSensorProfile(1500f);
        debrisRubicon3.setDiscoverable(true);
        debrisRubicon3.setCircularOrbit(rubicon_3,360*(float)Math.random(),600f,200f);
        debrisRubicon3.setId("tahlan_lethia_debrisRubicon3");

        SectorEntityToken debrisRubicon4 = Misc.addDebrisField(system,params1,StarSystemGenerator.random);
        debrisRubicon4.setSensorProfile(1500f);
        debrisRubicon4.setDiscoverable(true);
        debrisRubicon4.setCircularOrbit(rubicon_3,360*(float)Math.random(),800f,250f);
        debrisRubicon4.setId("tahlan_lethia_debrisRubicon4");

        //Jump point for Scorn
        JumpPointAPI jumpPoint1 = Global.getFactory().createJumpPoint("tahlan_rubicon_lucifron_jump", "Lucifron Jump Point");
        jumpPoint1.setCircularOrbit( system.getEntityById("tahlan_rubicon_p03"), 290, 900, 120);
        jumpPoint1.setRelatedPlanet(rubicon_3);
        system.addEntity(jumpPoint1);

        // Let's procgen some stuff here cause fuck doing that manually
        float radiusAfter = StarSystemGenerator.addOrbitingEntities(system, rubicon_star, StarAge.OLD,
                4, 6, // min/max entities to add
                6800, // radius to start adding at
                3, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                true); // whether to use custom or system-name based names


        // Small outpost at system edge

        SectorEntityToken rubicon_outpost = system.addCustomEntity("tahlan_rubicon_outpost", "Adramelech Fortress", "station_side06", "tahlan_legioinfernalis");
        rubicon_outpost.setCircularOrbitPointingDown(rubicon_star,360*(float)Math.random(),radiusAfter+700f,600f);
        rubicon_outpost.setCustomDescriptionId("tahlan_rubicon_outpost");
        MarketAPI rubicon_outpost_market = addMarketplace("tahlan_legioinfernalis", rubicon_outpost, null,
                "Adramelech Fortress",
                5,
                new ArrayList<>(
                        Arrays.asList(
                                Conditions.POPULATION_3,
                                Conditions.NO_ATMOSPHERE,
                                Conditions.OUTPOST,
                                Conditions.ORGANIZED_CRIME,
                                "tahlan_legiotyranny"
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Submarkets.GENERIC_MILITARY,
                                Submarkets.SUBMARKET_OPEN,
                                Submarkets.SUBMARKET_STORAGE,
                                Submarkets.SUBMARKET_BLACK
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                Industries.POPULATION,
                                Industries.SPACEPORT,
                                Industries.STARFORTRESS,
                                Industries.HEAVYBATTERIES,
                                Industries.MILITARYBASE
                        )
                ),
                0.3f,
                true,
                true);

        rubicon_outpost_market.getIndustry(Industries.MILITARYBASE).setAICoreId(Commodities.ALPHA_CORE);
        rubicon_outpost_market.getIndustry(Industries.STARFORTRESS).setAICoreId(Commodities.ALPHA_CORE);
        rubicon_outpost_market.getIndustry(Industries.HEAVYBATTERIES).setAICoreId(Commodities.ALPHA_CORE);

        // Bit more procgen
        float radiusAfter2 = StarSystemGenerator.addOrbitingEntities(system, rubicon_star, StarAge.OLD,
                2, 3, // min/max entities to add
                radiusAfter+1500f, // radius to start adding at
                3, // name offset - next planet will be <system name> <roman numeral of this parameter + 1>
                true); // whether to use custom or system-name based names

        // generates hyperspace destinations for in-system jump points
        system.autogenerateHyperspaceJumpPoints(true, true);

        //Finally cleans up hyperspace
        cleanup(system);


    }

    //Shorthand function for cleaning up hyperspace
    private void cleanup(StarSystemAPI system){
        HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin) Misc.getHyperspaceTerrain().getPlugin();
        NebulaEditor editor = new NebulaEditor(plugin);
        float minRadius = plugin.getTileSize() * 2f;

        float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius * 0.5f, 0f, 360f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0, radius + minRadius, 0f, 360f, 0.25f);
    }

    //Shorthand for adding derelicts, thanks Tart
    protected void addDerelict(StarSystemAPI system, SectorEntityToken focus, String variantId,
                               ShipRecoverySpecial.ShipCondition condition, float orbitRadius, boolean recoverable) {
        DerelictShipEntityPlugin.DerelictShipData params = new DerelictShipEntityPlugin.DerelictShipData(new ShipRecoverySpecial.PerShipData(variantId, condition), false);
        SectorEntityToken ship = BaseThemeGenerator.addSalvageEntity(system, Entities.WRECK, Factions.NEUTRAL, params);
        ship.setDiscoverable(true);

        float orbitDays = orbitRadius / (10f + (float) Math.random() * 5f);
        ship.setCircularOrbit(focus, (float) Math.random() * 360f, orbitRadius, orbitDays);

        if (recoverable) {
            SalvageSpecialAssigner.ShipRecoverySpecialCreator creator = new SalvageSpecialAssigner.ShipRecoverySpecialCreator(null, 0, 0, false, null, null);
            Misc.setSalvageSpecial(ship, creator.createSpecial(ship, null));
        }
    }
}
