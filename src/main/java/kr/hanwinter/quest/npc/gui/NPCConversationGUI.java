package kr.hanwinter.quest.npc.gui;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.dialogue.Dialogue;
import kr.hanwinter.quest.dialogue.manager.DialogueManager;
import kr.hanwinter.quest.npc.NPC;
import kr.hanwinter.quest.quest.Quest;
import kr.hanwinter.quest.quest.QuestStep;
import kr.hanwinter.quest.user.User;
import kr.hanwinter.quest.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
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
        Quest quest = serverInstance.getQuestManager().getQuestMap().get(dialogue.getQuest());
        String sequential = "§r§7(목표 순서대로 진행 §cX§7)";
        if(quest.getSequential()) {
            sequential = "§r§7(목표 순서대로 진행 §aO§7)";
        }

        List<Component> lore = new ArrayList<>(List.of(
                Component.text(""),
                LegacyComponentSerializer.legacySection().deserialize(" §e§l[퀘스트 목표]§r " + sequential).decoration(TextDecoration.ITALIC, false)));
        for(QuestStep questStep : quest.getSteps()) {
            if(questStep.getType() == QuestStep.QuestType.HUNT) {
                lore.add(LegacyComponentSerializer.legacySection().deserialize(" §e§l>§r §e" + questStep.getTargetName() + "§f 몬스터 사냥하기 §7(0/" + questStep.getCountGoal() + ")").decoration(TextDecoration.ITALIC, false));
            }
            if(questStep.getType() == QuestStep.QuestType.ITEM) {
                lore.add(LegacyComponentSerializer.legacySection().deserialize(" §e§l>§r §e" + questStep.getTargetName() + "§f 아이템 모으기 §7(0/" + questStep.getItemGoal().getAmount() + ")").decoration(TextDecoration.ITALIC, false));
            }
            if(questStep.getType() == QuestStep.QuestType.LOCATION) {
                lore.add(LegacyComponentSerializer.legacySection().deserialize(" §e§l>§r §e" + questStep.getTargetName() + "§f(으)로 이동하기 §7(0/1)").decoration(TextDecoration.ITALIC, false));
                int x = questStep.getLocationGoal().getBlockX();
                int y = questStep.getLocationGoal().getBlockY();
                int z = questStep.getLocationGoal().getBlockZ();
                lore.add(LegacyComponentSerializer.legacySection().deserialize(" §8(" + x + " " + y + " " + z + " 좌표로 이동하기)").decoration(TextDecoration.ITALIC, false));
            }
        }

        lore.add(Component.text(""));
        lore.add(LegacyComponentSerializer.legacySection().deserialize(" §a§l[퀘스트 보상]§r").decoration(TextDecoration.ITALIC, false));
        lore.add(LegacyComponentSerializer.legacySection().deserialize(" §a§l>§r§f §7" + quest.getMoneyReward() + "§f원").decoration(TextDecoration.ITALIC, false));
        lore.add(LegacyComponentSerializer.legacySection().deserialize(" §a§l>§r§f §7" + quest.getExperienceReward() + " §f경험치").decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(""));
        lore.add(LegacyComponentSerializer.legacySection().deserialize(" §b§l>§r§f 클릭 시 퀘스트를 수락합니다!").decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(""));
        ItemStack questItem = ItemUtil.createItem(Material.KNOWLEDGE_BOOK, LegacyComponentSerializer.legacySection().deserialize("§6§l[퀘스트]§r §f" + dialogue.getQuest()).decoration(TextDecoration.ITALIC, false), lore);
        ItemMeta questItemMeta = questItem.getItemMeta();
        questItemMeta.getPersistentDataContainer().set(serverInstance.getOriginalNameKey(), PersistentDataType.STRING, quest.getName());
        questItem.setItemMeta(questItemMeta);
        inventory.setItem(13, questItem);
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
                        if(event.getCurrentItem().getType() == Material.WRITABLE_BOOK) {
                            boolean hasNext = gui.nextDialogue();
                            if (!hasNext) {
                                if (gui.dialogue.getQuest() != null) {
                                    gui.getQuest();
                                } else {
                                    gui.selectNextDialogue(player);
                                }
                            }
                        } else if(event.getCurrentItem().getType() == Material.KNOWLEDGE_BOOK) {
                            ItemStack currentItemStack = event.getCurrentItem();
                            ItemMeta currentItemMeta = currentItemStack.getItemMeta();
                            String name = currentItemMeta.getPersistentDataContainer().get(gui.serverInstance.getOriginalNameKey(), PersistentDataType.STRING);
                            User user = gui.serverInstance.getUserManager().getUserMap().get(player.getUniqueId());
                            Quest quest = gui.serverInstance.getQuestManager().getQuestMap().get(name);
                            if(user.getQuestList().contains(quest)) {
                                player.closeInventory();
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
                                Component mainTitle = LegacyComponentSerializer.legacySection().deserialize("§6§l[퀘스트]");
                                Component subTitle = LegacyComponentSerializer.legacySection().deserialize("§c이미 진행중인 퀘스트입니다!");
                                Title.Times times = Title.Times.times(Ticks.duration(10), Ticks.duration(40), Ticks.duration(10));
                                player.showTitle(Title.title(mainTitle, subTitle, times));
                                return;
                            }
                            if(user.getClearedQuests().contains(quest)) {
                                player.closeInventory();
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
                                Component mainTitle = LegacyComponentSerializer.legacySection().deserialize("§6§l[퀘스트]");
                                Component subTitle = LegacyComponentSerializer.legacySection().deserialize("§c이미 완료한 퀘스트입니다!");
                                Title.Times times = Title.Times.times(Ticks.duration(10), Ticks.duration(40), Ticks.duration(10));
                                player.showTitle(Title.title(mainTitle, subTitle, times));
                                return;
                            }
                            user.getQuestList().add(quest);
                            List<Integer> progressList = new ArrayList<>();
                            for(QuestStep questStep : quest.getSteps()) {
                                progressList.add(0);
                            }
                            user.getProgressMap().put(quest.getName(), progressList);
                            player.closeInventory();
                            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            Component mainTitle = LegacyComponentSerializer.legacySection().deserialize("§6§l[퀘스트]");
                            Component subTitle = LegacyComponentSerializer.legacySection().deserialize("§7" + quest.getName() + "§r 퀘스트를 받았습니다!");
                            Title.Times times = Title.Times.times(Ticks.duration(10), Ticks.duration(40), Ticks.duration(10));
                            player.showTitle(Title.title(mainTitle, subTitle, times));
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
