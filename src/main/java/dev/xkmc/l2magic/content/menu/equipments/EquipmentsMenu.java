package dev.xkmc.l2magic.content.menu.equipments;

import dev.xkmc.l2core.base.menu.base.BaseContainerMenu;
import dev.xkmc.l2core.base.menu.base.PredSlot;
import dev.xkmc.l2core.base.menu.base.SpriteManager;
import dev.xkmc.l2core.util.Proxy;
import dev.xkmc.l2magic.init.L2Magic;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class EquipmentsMenu extends BaseContainerMenu<EquipmentsMenu> {

	public static EquipmentsMenu fromNetwork(MenuType<EquipmentsMenu> type, int wid, Inventory plInv, FriendlyByteBuf buf) {
		var level = Proxy.getLevel();
		Entity entity = level == null ? null : level.getEntity(buf.readInt());
		return new EquipmentsMenu(type, wid, plInv, entity instanceof Mob golem ? golem : null);
	}

	public static EquipmentSlot[] SLOTS = {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

	public static final SpriteManager MANAGER = new SpriteManager(L2Magic.MODID, "equipments");

	@Nullable
	protected final Mob mob;

	protected EquipmentsMenu(MenuType<?> type, int wid, Inventory plInv, @Nullable Mob mob) {
		super(type, wid, plInv, MANAGER, EquipmentsContainer::new, false);
		this.mob = mob;
		addSlot("hand", (i, e) -> isValid(SLOTS[i], e));
		addSlot("armor", (i, e) -> isValid(SLOTS[i + 2], e));
	}

	private boolean isValid(EquipmentSlot slot, ItemStack stack) {
		if (mob == null || !stillValid(inventory.player)) {
			return false;
		}
		EquipmentSlot exp = mob.getEquipmentSlotForItem(stack);
		if (exp == slot) return true;
		return !exp.isArmor();
	}

	@Override
	public boolean stillValid(Player player) {
		return mob != null && !mob.isRemoved();
	}

	@Override
	public PredSlot getAsPredSlot(String name, int i, int j) {
		return super.getAsPredSlot(name, i, j);
	}

	@Override
	public ItemStack quickMoveStack(Player pl, int id) {
		if (mob != null) {
			ItemStack stack = this.slots.get(id).getItem();
			if (id >= 36) {
				this.moveItemStackTo(stack, 0, 36, true);
			} else {
				for (int i = 0; i < 6; i++) {
					if (SLOTS[i] == mob.getEquipmentSlotForItem(stack)) {
						this.moveItemStackTo(stack, 36 + i, 37 + i, false);
					}
				}
			}
			this.container.setChanged();
		}
		return ItemStack.EMPTY;
	}
}
