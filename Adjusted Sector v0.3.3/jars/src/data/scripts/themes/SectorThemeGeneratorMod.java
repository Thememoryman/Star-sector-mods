package data.scripts.themes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import data.scripts.themes.DerelictThemeGeneratorMod;
import data.scripts.themes.RemnantThemeGeneratorMod;
import data.scripts.themes.RuinsThemeGeneratorMod;
import data.scripts.themes.MiscellaneousThemeGeneratorMod;

import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;

public class SectorThemeGeneratorMod {

	public static List<ThemeGeneratorMod> generators = new ArrayList<ThemeGeneratorMod>();
	
	static {
		generators.add(new DerelictThemeGeneratorMod());
		generators.add(new RemnantThemeGeneratorMod());
		generators.add(new RuinsThemeGeneratorMod());
		generators.add(new MiscellaneousThemeGeneratorMod());
	}
	
	public static void generate(ThemeGenContextMod context) {
		Collections.sort(generators, new Comparator<ThemeGeneratorMod>() {
			public int compare(ThemeGeneratorMod o1, ThemeGeneratorMod o2) {
				int result = o1.getOrder() - o2.getOrder();
				if (result == 0) return o1.getThemeId().compareTo(o2.getThemeId());
				return result;
			}
		});
		
		float totalWeight = 0f;
		for (ThemeGeneratorMod g : generators) {
			totalWeight += g.getWeight();
			g.setRandom(StarSystemGenerator.random);
		}
		
		for (ThemeGeneratorMod g : generators) {
			float w = g.getWeight();
			
			float f = 0f;
			if (totalWeight > 0) {
				f = w / totalWeight; 
			} else {
				if (w > 0) f = 1f;
			}
			//g.setRandom(StarSystemGenerator.random);
			g.generateForSector(context, f);
			
			//float used = context.majorThemes.size();
			totalWeight -= w;

		}
	}
	
	
	
	
}
