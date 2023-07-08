package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.api.item.CustomModelDataManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class CMDHelper {
    public static ItemStack itemStackFromManager(CustomModelDataManager manager, Item[] profile) {
        var pair = manager.requestCMD(profile);
        var stack = new ItemStack(pair.getLeft());
        addCmdToNbt(stack, pair.getRight());
        return stack;
    }

    public static void addCmdToNbt(ItemStack stack, int cmd) {
        var nbt = new NbtCompound();
        nbt.putInt("CustomModelData", cmd);
        stack.setNbt(nbt);
    }
}
