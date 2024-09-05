package dev.xkmc.l2magic.content.menu.curios;

import dev.xkmc.l2magic.init.registrate.LMItems;
import dev.xkmc.l2tabs.compat.api.AccessoriesMultiplex;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record EntityCuriosMenuPvd(LivingEntity e, int page) implements MenuProvider {

	@Override
	public Component getDisplayName() {
		return e.getName();
	}

	public void writeBuffer(FriendlyByteBuf buf) {
		buf.writeInt(e.getId());
		buf.writeInt(page);
	}

	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player player) {
		return new EntityCuriosListMenu(LMItems.CURIOS.get(), wid, inv, AccessoriesMultiplex.get().wrap(e, page));
	}

	public void open(ServerPlayer player) {
		player.openMenu(this, this::writeBuffer);
	}

}
