package net.akashi.weaponmod.Enchantments;

import net.akashi.weaponmod.Bows.PurgatoryItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MeltDownEnchantment extends Enchantment {
	public MeltDownEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
		super(pRarity, pCategory, pApplicableSlots);
	}

	@Override
	public int getMinCost(int pEnchantmentLevel) {
		return pEnchantmentLevel * 75;
	}

	@Override
	public int getMaxCost(int pEnchantmentLevel) {
		return this.getMinCost(pEnchantmentLevel) + 100;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}
}
