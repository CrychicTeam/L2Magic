package dev.xkmc.l2magic.content.item.spell;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import dev.xkmc.l2magic.init.L2Magic;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2magic.init.registrate.LMItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CreativeWandItem extends WandItem {

	public CreativeWandItem(Properties prop) {
		super(prop);
	}

	@Override
	public Component getName(ItemStack stack) {
		return super.getName(stack).copy().append(": " + LMItems.MODID.getOrDefault(stack, L2Magic.MODID));
	}

	public void fillTab(CreativeModeTabModifier m) {
		var reg = m.getParameters().holders();
		var spells = reg.lookupOrThrow(EngineRegistry.SPELL);
		var ids = spells.listElementIds().map(e -> e.location().getNamespace()).distinct().toList();
		for (var id : ids) {
			m.accept(LMItems.MODID.set(getDefaultInstance(), id));
		}
	}

}
