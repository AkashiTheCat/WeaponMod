package net.akashi.weaponmod.Registry;

import net.akashi.weaponmod.Block.FurnaceCoreBlock;
import net.akashi.weaponmod.WeaponMod;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS =
			DeferredRegister.create(ForgeRegistries.BLOCKS, WeaponMod.MODID);
	public static final RegistryObject<FurnaceCoreBlock> FURNACE_CORE = BLOCKS.register("furnace_core",
			()->new FurnaceCoreBlock(BlockBehaviour.Properties.copy(Blocks.FURNACE)));
}
