package kr.hanwinter.quest.dialogue.gui;

import kr.hanwinter.quest.item.ItemUtil;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DialogueManagementGUI implements InventoryHolder {
    private Inventory inventory;

    public DialogueManagementGUI() {
        inventory = Bukkit.createInventory(this, 54, LegacyComponentSerializer.legacySection().deserialize("§b§l[대화문]§r §f관리 §71§f페이지"));
        ItemStack blackStainedGlassPane = ItemUtil.createItem(Material.BLACK_STAINED_GLASS_PANE, null, null);
        for(int i=0; i<9; i++) {
            inventory.setItem(i, blackStainedGlassPane);
        }
        for(int i=45; i<54; i++) {
            inventory.setItem(i, blackStainedGlassPane);
        }
        ItemStack makeDialogueItem = ItemUtil.createItem(Material.WRITABLE_BOOK, LegacyComponentSerializer.legacySection().deserialize("§a§l대화문 생성").decoration(TextDecoration.ITALIC, false), List.of(LegacyComponentSerializer.legacySection().deserialize(" §a§l>§r §7대화문을 새로 생성합니다.").decoration(TextDecoration.ITALIC, false)));
        inventory.setItem(49, makeDialogueItem);
        ItemStack nextPage = ItemUtil.createItem(Material.END_CRYSTAL, LegacyComponentSerializer.legacySection().deserialize("§e다음 페이지").decoration(TextDecoration.ITALIC, false), List.of(LegacyComponentSerializer.legacySection().deserialize(" §e§l>§r §7클릭 시 §f2§7페이지로 이동합니다.").decoration(TextDecoration.ITALIC, false)));
        ItemStack previousPage = ItemUtil.createItem(Material.BARRIER, LegacyComponentSerializer.legacySection().deserialize("§e이전 페이지").decoration(TextDecoration.ITALIC, false), List.of(LegacyComponentSerializer.legacySection().deserialize(" §c§l>§r §7이전 페이지가 없습니다.").decoration(TextDecoration.ITALIC, false)));
        inventory.setItem(52, previousPage);
        inventory.setItem(53, nextPage);
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    public static class DialogueManagementGUIListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if(event.getInventory().getHolder() instanceof DialogueManagementGUI) {
                event.setCancelled(true);
            }
        }
    }
}
