package kr.hanwinter.quest.npc.gui;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.dialogue.Dialogue;
import kr.hanwinter.quest.dialogue.manager.DialogueManager;
import kr.hanwinter.quest.npc.NPC;
import kr.hanwinter.quest.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NPCConversationGUI implements InventoryHolder {
    private final Inventory inventory;
    private Dialogue dialogue;
    private final NPC npc;
    private final Main serverInstance;
    private int currentIndex = 0;
    public static final ItemStack AIR = ItemUtil.createItem(Material.AIR, null, null);
    public static final ItemStack BLACK_STAINED_GLASS_PANE = ItemUtil.createItem(Material.BLACK_STAINED_GLASS_PANE, Component.text(""), null);

    public NPCConversationGUI(Dialogue dialogue, NPC npc, Main serverInstance) {
        this.dialogue = dialogue;
        this.npc = npc;
        this.serverInstance = serverInstance;
        inventory = Bukkit.createInventory(this, 27, Component.text("대화문"));

        updateDialogueItem();
        for(int i=0; i<9; i++) {
            inventory.setItem(i, BLACK_STAINED_GLASS_PANE);
        }
        for(int i=18; i<27; i++) {
            inventory.setItem(i, BLACK_STAINED_GLASS_PANE);
        }
    }

    private void updateDialogueItem() {
        String text = dialogue.getDialogue().get(currentIndex);
        inventory.setItem(13, ItemUtil.createItem(Material.WRITABLE_BOOK, LegacyComponentSerializer.legacySection().deserialize(text).decoration(TextDecoration.ITALIC, false), null));
    }

    public boolean nextDialogue() {
        currentIndex++;
        if (currentIndex+1 > dialogue.getDialogue().size()) {
            return false;
        }
        updateDialogueItem();
        return true;
    }

    public void selectNextDialogue(Player player) {
        List<Dialogue> dialogueList = new ArrayList<>();
        for(String dialogue1 : npc.getDialogueList()) {
            Dialogue dialogue2 = serverInstance.getDialogueManager().getDialogueMap().get(dialogue1);
            if(dialogue2.getReqLevel() <= player.getLevel()) {
                //퀘스트 클리어 확인은 나중에 구현
                dialogueList.add(dialogue2);
            }
        }
        if(!dialogueList.isEmpty()) {
            inventory.setItem(11, inventory.getItem(13));
            inventory.setItem(13, ItemUtil.createItem(Material.AIR, null, null));
            List<Integer> slot = new ArrayList<>(List.of(15, 16, 14, 5, 6, 7, 23, 24, 25));
            int index = 0;
            for(Dialogue dialogue1 : dialogueList) {
                ItemStack dialogueItem = ItemUtil.createItem(Material.WRITABLE_BOOK, LegacyComponentSerializer.legacySection().deserialize(dialogue1.getDisplayName()).decoration(TextDecoration.ITALIC, false), null);
                ItemMeta dialogueItemMeta = dialogueItem.getItemMeta();
                dialogueItemMeta.getPersistentDataContainer().set(serverInstance.getOriginalNameKey(), PersistentDataType.STRING, dialogue1.getName());
                dialogueItem.setItemMeta(dialogueItemMeta);
                inventory.setItem(slot.get(index), dialogueItem);
                index++;
            }
        } else {
            player.closeInventory();
        }
    }

    public void newDialogue(Dialogue dialogue1) {
        currentIndex = 0;
        dialogue = dialogue1;
        for(int i=0; i<9; i++) {
            inventory.setItem(i, BLACK_STAINED_GLASS_PANE);
        }
        for(int i=9; i<18; i++) {
            inventory.setItem(i, AIR);
        }
        for(int i=18; i<27; i++) {
            inventory.setItem(i, BLACK_STAINED_GLASS_PANE);
        }
        updateDialogueItem();
    }

    public void getQuest() {

    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public static class NPCConversationGUIListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if(event.getInventory().getHolder() instanceof NPCConversationGUI gui) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();

                if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                    return;
                }

                switch(event.getRawSlot()) {
                    case 13:
                        boolean hasNext = gui.nextDialogue();
                        if (!hasNext) {
                            if (gui.dialogue.getQuest() != null) {
                                gui.getQuest();
                            } else {
                                gui.selectNextDialogue(player);
                            }
                        }
                        break;
                    case 5, 6, 7, 14, 15, 16, 23, 24, 25:
                        ItemStack currentItemStack = event.getCurrentItem();
                        ItemMeta currentItemMeta = currentItemStack.getItemMeta();
                        String name = currentItemMeta.getPersistentDataContainer().get(gui.serverInstance.getOriginalNameKey(), PersistentDataType.STRING);
                        gui.newDialogue(gui.serverInstance.getDialogueManager().getDialogueMap().get(name));
                        break;
                }
            }
        }
    }
}
