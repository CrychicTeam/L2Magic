package dev.xkmc.l2magic.content.menu.equipments;

import dev.xkmc.l2core.base.menu.base.BaseContainerMenu;
import net.minecraft.world.item.ItemStack;

public class EquipmentsContainer extends BaseContainerMenu.BaseContainer<EquipmentsMenu> {

	public EquipmentsContainer(EquipmentsMenu menu) {
		super(0, menu);
	}

	@Override
	public ItemStack getItem(int index) {
		if (parent.mob == null) return ItemStack.EMPTY;
		return parent.mob.getItemBySlot(EquipmentsMenu.SLOTS[index]);
	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if (parent.mob == null) return;
		parent.mob.setItemSlot(EquipmentsMenu.SLOTS[index], stack);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		if (parent.mob == null) return ItemStack.EMPTY;
		return parent.mob.getItemBySlot(EquipmentsMenu.SLOTS[index]).split(count);
	}

}
