package dev.xkmc.l2magic.content.menu.equipments;

import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record EquipmentsMenuPvd(Mob e) implements MenuProvider {

	@Override
	public Component getDisplayName() {
		return e.getDisplayName();
	}

	public void writeBuffer(FriendlyByteBuf buf) {
		buf.writeInt(e.getId());
	}

	public void open(ServerPlayer player) {
		player.openMenu(this, this::writeBuffer);
	}

	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
		return new EquipmentsMenu(LMItems.EQUIPMENTS.get(), wid, inv, e);
	}
}
