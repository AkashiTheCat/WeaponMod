package net.akashi.weaponmod.Config;

import net.akashi.weaponmod.Config.Properties.ConduitGuardProperties;
import net.akashi.weaponmod.Config.Properties.DragonStrikeProperties;
import net.akashi.weaponmod.Config.Properties.PiglinsWarSpearProperties;
import net.akashi.weaponmod.Config.Properties.SpearProperties;
import net.akashi.weaponmod.Registry.ModItems;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.common.ForgeConfigSpec;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonConfigs {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static SpearProperties IRON_SPEAR_PROPERTIES;
	public static SpearProperties GOLDEN_SPEAR_PROPERTIES;
	public static SpearProperties DIAMOND_SPEAR_PROPERTIES;
	public static SpearProperties NETHERITE_SPEAR_PROPERTIES;
	public static SpearProperties MEGALODON_PROPERTIES;
	public static ConduitGuardProperties CONDUIT_GUARD_PROPERTIES;
	public static PiglinsWarSpearProperties PIGLINS_WARSPEAR_PROPERTIES;
	public static DragonStrikeProperties DRAGON_STRIKE_PROPERTIES;

	static {
		IRON_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Iron Spear",
				8, 1.1,
				7, 2.5F, true);
		GOLDEN_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Golden Spear",
				5, 1.6,
				6, 2.5F, true);
		DIAMOND_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Diamond Spear",
				9, 1.1,
				8, 2.5F, true);
		NETHERITE_SPEAR_PROPERTIES = new SpearProperties(BUILDER, "Netherite Spear",
				10, 1.1,
				9, 2.5F, true);
		MEGALODON_PROPERTIES = new SpearProperties(BUILDER, "Megalodon",
				9, 1.2,
				6, 2.5F, true);
		CONDUIT_GUARD_PROPERTIES = new ConduitGuardProperties(BUILDER, "Conduit Guard",
				9, 1.2,
				6, 2.5F,
				5.0F, 45,
				0.5, 80);
		PIGLINS_WARSPEAR_PROPERTIES = new PiglinsWarSpearProperties(BUILDER, "Piglin's WarSpear",
				5, 1.2,
				8, 2.5F,
				0.1, 0.1);
		DRAGON_STRIKE_PROPERTIES = new DragonStrikeProperties(BUILDER, "Dragon Strike",
				10, 1.2,
				10, 2.5F,
				4.0, 6.0,
				60, 5,
				0.5, 40,
				200);
		SPEC = BUILDER.build();
	}

	@SubscribeEvent
	public static void onConfigLoad(ModConfigEvent event) {
		if (event.getConfig().getSpec() != SPEC)
			return;
		ModItems.IRON_SPEAR.get().updateAttributesFromConfig(IRON_SPEAR_PROPERTIES);
		ModItems.GOLDEN_SPEAR.get().updateAttributesFromConfig(GOLDEN_SPEAR_PROPERTIES);
		ModItems.DIAMOND_SPEAR.get().updateAttributesFromConfig(DIAMOND_SPEAR_PROPERTIES);
		ModItems.NETHERITE_SPEAR.get().updateAttributesFromConfig(NETHERITE_SPEAR_PROPERTIES);
		ModItems.SPEAR_MEGALODON.get().updateAttributesFromConfig(MEGALODON_PROPERTIES);
		ModItems.SPEAR_CONDUIT_GUARD.get().updateAttributesFromConfig(CONDUIT_GUARD_PROPERTIES);
		ModItems.PIGLINS_WARSPEAR.get().updateAttributesFromConfig(PIGLINS_WARSPEAR_PROPERTIES);
		ModItems.DRAGON_STRIKE.get().updateAttributesFromConfig(DRAGON_STRIKE_PROPERTIES);
	}
}
