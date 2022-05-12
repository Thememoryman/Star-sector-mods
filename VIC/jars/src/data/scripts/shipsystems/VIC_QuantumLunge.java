package data.scripts.shipsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import data.scripts.plugins.MagicFakeBeamPlugin;
import data.scripts.util.MagicSettings;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lazywizard.lazylib.combat.CombatUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Made by PureTilt
public class VIC_QuantumLunge extends BaseShipSystemScript {

    public static final float SPEED_BOOST = 8000f;
    public final float DistPerExp = 50f;
    public final boolean ArcAimAtShips = true; //for secondary arcs
    public final float EmpArcDmg = 800;
    public final float EmpArcEmp = 100;
    public final float AllyMult = 0.1f;
    public final DamageType EmpArmDmgType = DamageType.ENERGY;
    public final float TimeBonus = 2f;
    public final Map<ShipAPI.HullSize, Float> MULT = new HashMap<>();
    public final ArrayList<String> cheesyLinesList = new ArrayList<>();
    public boolean isActive = false;
    public boolean isActive2 = false;
    public boolean DoOnce = true;
    public float PseudoRandom = 0f;
    public float StarFacing = 0f;
    public float shipTimeMult = 1f;
    public Vector2f StartPos = new Vector2f();
    public Vector2f EndPos = new Vector2f();
    public IntervalUtil MaxTime = new IntervalUtil(1f, 1f);
    public float SHTURM_COOLDOWN_MULT = 0.66f;

    {
        MULT.put(ShipAPI.HullSize.DEFAULT, 1.0F);
        MULT.put(ShipAPI.HullSize.FIGHTER, 0.75F);
        MULT.put(ShipAPI.HullSize.FRIGATE, 0.5F);
        MULT.put(ShipAPI.HullSize.DESTROYER, 0.3F);
        MULT.put(ShipAPI.HullSize.CRUISER, 0.2F);
        MULT.put(ShipAPI.HullSize.CAPITAL_SHIP, 0.1F);

        cheesyLinesList.add("Nothing personal, kid");
        cheesyLinesList.add("Another excellent cut");
        cheesyLinesList.add("Executed in a singular strike!");
        cheesyLinesList.add("Dodge this!");
        cheesyLinesList.add("Aw-w-w, that must've hurt...");
        cheesyLinesList.add("God, I love this ship!");
        cheesyLinesList.add("YEEEEEHAAAAAW!!!");
        cheesyLinesList.add("BOOYAH!!!");
        cheesyLinesList.add("Quantum physics, son");
        cheesyLinesList.add("Get good or get dead, scrub");
        cheesyLinesList.add("Pierce the Heavens!");
        cheesyLinesList.add("You're already dead");
        cheesyLinesList.add("Commencing Quantum Lunge");
        cheesyLinesList.add("Field destabilization successful");
        cheesyLinesList.add("Right behind you");
        cheesyLinesList.add("Too fast for you?");
    }

    @Override
    public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {

        //Lines list
        ShipAPI ship = (ShipAPI) stats.getEntity();
        CombatEngineAPI engine = Global.getCombatEngine();

        boolean player;
        if (ship != null) {
            ship = (ShipAPI) stats.getEntity();
            player = ship == Global.getCombatEngine().getPlayerShip();
        } else {
            return;
        }
        
        if (DoOnce) {
            MaxTime = new IntervalUtil(1f, 1f);
            DoOnce = false;
        }

        stats.getTimeMult().unmodify(id);
        float current = ship.getMutableStats().getTimeMult().getModifiedValue();
        ship.getMutableStats().getTimeMult().modifyMult(id, 1 / current);

        if (state == State.OUT) {
            //once
            if (!isActive2) {

                EndPos = new Vector2f(ship.getLocation());
                isActive2 = true;

                if (MagicSettings.getBoolean("vic", "Apollyon_oneLiners") && Math.random() < 0.15f) {
                    String line = cheesyLinesList.get(MathUtils.getRandomNumberInRange(0, cheesyLinesList.size() - 1));
                    if (line.equals("Nothing personal, kid") && Math.random() < 0.15f) line = "Nothing personnel, kid";
                    engine.addFloatingText(ship.getLocation(), line, 60, Color.WHITE, ship, 1, 2);
                }

            }//end once
            shipTimeMult = 1 + TimeBonus * (float) Math.pow(effectLevel, 5);
            ship.setExtraAlphaMult(0.25f + (0.75f * (1 - effectLevel)));
            stats.getMaxSpeed().modifyFlat(id, 50f);
            stats.getMaxTurnRate().modifyMult(id, 3f);
            stats.getTurnAcceleration().modifyMult(id, 6f);
        }

        if (state == State.ACTIVE) {
            shipTimeMult = 1 + TimeBonus;
            stats.getMaxSpeed().modifyFlat(id, SPEED_BOOST);
            stats.getAcceleration().modifyFlat(id, 80000f);
            ship.setPhased(true);
            ship.setExtraAlphaMult(0.25f);

            if (!isActive) {
                StartPos = new Vector2f(ship.getLocation().x, ship.getLocation().y);
                StarFacing = ship.getFacing();
                isActive = true;
            }
            stats.getCombatEngineRepairTimeMult().modifyMult(id, 0);
            ship.addAfterimage(new Color(255, 255, 255, 40), 0f, 0f, -ship.getVelocity().x * 0.5f, -ship.getVelocity().y * 0.5f, 0, 0, 0.2f, 0f, false, false, false);
        }

        if (state == State.IN) {
            float speedLevel = (effectLevel - 0.5f) * 2;
            if (speedLevel < 0) speedLevel = 0;
            ship.setJitterShields(false);
            shipTimeMult = 1 + TimeBonus * (float) Math.pow(effectLevel, 10);
            float visualLevel = (0.5f + (0.5f * effectLevel));
            stats.getCombatEngineRepairTimeMult().modifyMult(id, 0);
            stats.getAcceleration().modifyFlat(id, 300f * visualLevel);
            ship.getEngineController().extendFlame(this, 3f * effectLevel, 2f * effectLevel, 3f * effectLevel);
            ship.setJitterUnder(ship, new Color(100, 165, 255, 155), 7f * effectLevel, 20, 4);
            ship.setAngularVelocity(0);
        }
        if (player) {
            Global.getCombatEngine().getTimeMult().modifyMult(id, 1f / shipTimeMult);
        }
    }


    @Override
    public boolean isUsable(ShipSystemAPI system, ShipAPI ship) {
        if ((ship != null) && (system != null) && ship.getVariant().hasHullMod("vic_ShturmSolution")) {
            system.setCooldown(45f * SHTURM_COOLDOWN_MULT);
        }
        return true;
    }




    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getTimeMult().unmodify(id);
        stats.getMaxSpeed().unmodify(id);
        stats.getMaxTurnRate().unmodify(id);
        stats.getTurnAcceleration().unmodify(id);
        stats.getAcceleration().unmodify(id);
        stats.getDeceleration().unmodify(id);
        stats.getCombatEngineRepairTimeMult().unmodify(id);
        stats.getTimeMult().unmodify(id);

        if (isActive) {
            ShipAPI ship = (ShipAPI) stats.getEntity();
            CombatEngineAPI engine = Global.getCombatEngine();
            ship.setPhased(false);
            ship.setExtraAlphaMult(1f);

            Vector2f expDir = VectorUtils.getDirectionalVector(StartPos, EndPos);
            float TravDist = MathUtils.getDistance(StartPos, EndPos) + 200f;
            int ExpNum = Math.round(TravDist / DistPerExp);

            for (int i = 0; i < ExpNum; i++) {
                Vector2f ExpPos = new Vector2f(StartPos.x + (DistPerExp * expDir.x * i), StartPos.y + (DistPerExp * expDir.y * i));
                List<CombatEntityAPI> entitiesMain = CombatUtils.getEntitiesWithinRange(ExpPos, 10f);
                Global.getSoundPlayer().playSound("vic_quantum_lunge_explosion", 1, 0.5f, ExpPos, new Vector2f(0, 0));

                for (CombatEntityAPI EmpTarget : entitiesMain) {

                    if (EmpTarget instanceof DamagingProjectileAPI && !(EmpTarget instanceof MissileAPI)) continue;
                    if (EmpTarget == ship) continue;
                    if (EmpTarget instanceof ShipAPI && ((ShipAPI) EmpTarget).getVariant().hasHullMod("vastbulk")) continue;

                    Vector2f damagePos = ExpPos;
                    if (EmpTarget instanceof MissileAPI) damagePos = new Vector2f(EmpTarget.getLocation());

                    float EmpArcDmgFinal = EmpArcDmg;
                    float EmpArcEmpFinal = EmpArcEmp;

                    if (EmpTarget.getOwner() == ship.getOwner()) {
                        EmpArcDmgFinal *= AllyMult;
                        EmpArcEmpFinal *= AllyMult;
                    }

                    engine.applyDamage(EmpTarget,damagePos,EmpArcDmgFinal,EmpArmDmgType,EmpArcEmpFinal,false,false,ship,true);
                }

                if (Math.random() < 0.25f + PseudoRandom) {
                    PseudoRandom = 0f;
                    Vector2f randomPoint;
                    if (Math.random() < 0.5) {
                        randomPoint = MathUtils.getRandomPointInCone(ExpPos, 450, StarFacing - 65, StarFacing - 115);
                    } else {
                        randomPoint = MathUtils.getRandomPointInCone(ExpPos, 450, StarFacing + 65, StarFacing + 115);
                    }

                    List<CombatEntityAPI> entities = CombatUtils.getEntitiesWithinRange(randomPoint, 40f);

                    if (entities.size() > 0 && ArcAimAtShips) {
                        CombatEntityAPI EmpTarget = entities.get(MathUtils.getRandomNumberInRange(0, entities.size() - 1));
                        if (!(EmpTarget instanceof DamagingProjectileAPI) && EmpTarget != ship) {
                            float EmpArcDmgFinal = EmpArcDmg * 0.5f;
                            float EmpArcEmpFinal = EmpArcEmp * 0.5f;
                            if (EmpTarget.getOwner() == ship.getOwner()) {
                                EmpArcDmgFinal *= AllyMult;
                                EmpArcEmpFinal *= AllyMult;
                            }
                            spawnEMPArc(engine, ship, EmpTarget, ExpPos, EmpArcDmgFinal, EmpArcEmpFinal);
                        }
                    } else {
                        CombatEntityAPI Steroid = engine.spawnAsteroid(0, randomPoint.x, randomPoint.y, 0, 0);
                        spawnEMPArc(engine, ship, Steroid, ExpPos, 0, 0);
                        engine.removeEntity(Steroid);
                    }
                } else {
                    PseudoRandom += 0.15;
                }
            }

            MagicFakeBeamPlugin.addBeam(0.25f,
                    0.2f + (float) Math.random() * 0.2f,
                    120,
                    StartPos,
                    StarFacing,
                    TravDist,
                    Color.WHITE,
                    Color.CYAN);

            //Global.getSoundPlayer().playSound("vic_quantum_lunge_explosion",1,1,ship.getLocation(),new Vector2f(0,0));

        }
        isActive = false;
        isActive2 = false;
    }

    public void spawnEMPArc(CombatEngineAPI engine, ShipAPI ship, CombatEntityAPI target, Vector2f Pos, float EmpArcDmg, float EmpArcEmp) {
        engine.spawnEmpArc(ship,
                Pos,
                null,
                target,
                EmpArmDmgType,
                EmpArcDmg,
                EmpArcEmp,
                3000,
                "tachyon_lance_emp_impact",
                4,
                Color.WHITE,
                Color.CYAN);
    }

    @Override
    public StatusData getStatusData(int index, State state, float effectLevel) {
        if (index == 0) {
            if (state == State.IN)
                return new StatusData("Warm up the engines", false);
            return new StatusData("Reaping spacetime", false);
        }
        return null;
    }

    protected float getMaxRange(ShipAPI ship) {
        return ship.getSystem().getChargeActiveDur() * SPEED_BOOST;
    }

    @Override
    public String getInfoText(ShipSystemAPI system, ShipAPI ship) {
        if (system.isOutOfAmmo()) return null;
        if (system.getState() != ShipSystemAPI.SystemState.IDLE) return null;

        Vector2f target = ship.getMouseTarget();
        if (target != null) {
            float dist = Misc.getDistance(ship.getLocation(), target);
            float max = getMaxRange(ship) + ship.getCollisionRadius();
            if (dist > max) {
                return "OUT OF RANGE";
            } else {
                return "READY";
            }
        }
        return null;
    }
}
