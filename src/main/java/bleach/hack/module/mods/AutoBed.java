package bleach.hack.module.mods;

import bleach.hack.event.events.EventTick;
import bleach.hack.event.events.EventWorldRender;
import bleach.hack.module.Category;
import bleach.hack.module.Module;
import bleach.hack.setting.base.SettingSlider;
import bleach.hack.setting.base.SettingToggle;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AutoBed extends Module {

    public AutoBed() {
        super("AutoBed", KEY_UNBOUND, Category.COMBAT, "beds go boom",
                new SettingToggle("AutoMove", true).withDesc("Moves beds in inventory").withChildren(
                        new SettingSlider("MainBedSlot", 1, 9, 2, 0)
                ),
                new SettingToggle("AutoExplode", true));
    }
    @Subscribe
    public void onTick(EventTick event) {
        Integer mainBedSlot = (int)getSetting(0).asToggle().getChild(0).asSlider().getValue();
        if (!(mc.player.inventory.getStack(mainBedSlot).getItem() instanceof BedItem) && !mc.player.isCreative() && dimensionCheck()) {
            Integer bedSlot = null;
            for (int slot = 0; slot < 36; slot++) {
                ItemStack stack = mc.player.inventory.getStack(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof BedItem) || slot == mainBedSlot) {
                    continue;
                } else {
                 bedSlot = slot;
                }
            }
            if (bedSlot == null) {
                return;
            }
            if (bedSlot != mainBedSlot - 1) {
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, mainBedSlot + 35, 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, bedSlot < 9 ? (bedSlot + 36) : (bedSlot), 0, SlotActionType.PICKUP, mc.player);
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, mainBedSlot + 35, 0, SlotActionType.PICKUP, mc.player);
            }
        }
    }
    @Subscribe
    public void allahuAkbar(EventWorldRender worldRender) {
        if (getSetting(1).asToggle().state && dimensionCheck()) {
            for (BlockEntity e : mc.world.blockEntities) {
                if (e instanceof BedBlockEntity) {
                    BlockPos pos = e.getPos();
                    Vec3d posv3d = new Vec3d(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(posv3d, Direction.UP, pos, false));
                }
            }
        }
    }
    public boolean dimensionCheck() {
        if (mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("the_nether")
            || mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("the_end")) {
            return true;
        } else {
            return false;
        }
    }
}