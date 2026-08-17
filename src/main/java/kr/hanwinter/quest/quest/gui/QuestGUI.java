package kr.hanwinter.quest.quest.gui;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.dialogue.Dialogue;
import kr.hanwinter.quest.npc.NPC;
import kr.hanwinter.quest.quest.Quest;
import kr.hanwinter.quest.quest.QuestStep;
import kr.hanwinter.quest.user.User;
import kr.hanwinter.quest.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static kr.hanwinter.quest.npc.gui.NPCConversationGUI.AIR;
import static kr.hanwinter.quest.npc.gui.NPCConversationGUI.BLACK_STAINED_GLASS_PANE;

public class QuestGUI implements InventoryHolder {
    private final Inventory inventory;
    private final Main serverInstance;

    public QuestGUI(Player player, Main serverInstance) {
        this.serverInstance = serverInstance;
        inventory = Bukkit.createInventory(this, 54, Component.text("퀘스트"));

        for(int i=0; i<9; i++) {
            inventory.setItem(i, BLACK_STAINED_GLASS_PANE);
        }
        for(int i=45; i<54; i++) {
            inventory.setItem(i, BLACK_STAINED_GLASS_PANE);
        }

        reloadItemGoal(player);
        reloadInventory(player);
    }

    private void reloadItemGoal(Player player) {
        User user = serverInstance.getUserManager().getUserMap().get(player.getUniqueId());
        for(Quest quest : user.getQuestList()) {
            int index = 0;
            int activeIndex = -1;
            boolean isSequential = quest.getSequential();
            List<Integer> progressList = user.getProgressMap().get(quest.getName());
            if(isSequential) {
                for(int i = 0; i<quest.getSteps().size(); i++) {
                    QuestStep questStep = quest.getSteps().get(i);
                    int current = progressList.get(i);
                    int goal = switch (questStep.getType()) {
                        case HUNT -> questStep.getCountGoal();
                        case ITEM -> questStep.getItemGoal().getAmount();
                        case LOCATION -> 1;
                    };
                    if(current < goal) {
                        activeIndex = i;
                        break;
                    }
                }
                if(activeIndex == -1) {
                    continue;
                }
            }
            for(QuestStep questStep : quest.getSteps()) {
                if(activeIndex != -1) {
                    if(index != activeIndex) {
                        index++;
                        continue;
                    }
                }
                if(questStep.getType() == QuestStep.QuestType.ITEM) {
                    int itemAmount = 0;
                    for (ItemStack itemStack : player.getInventory()) {
                        if (itemStack == null || itemStack == AIR) {
                            continue;
                        }

                        if (questStep.getItemGoal().isSimilar(itemStack)) {
                            itemAmount += itemStack.getAmount();
                        }
                    }
                    if(itemAmount > questStep.getItemGoal().getAmount()) {
                        itemAmount = questStep.getItemGoal().getAmount();
                    }
                    progressList.set(index, itemAmount);
                }
                index++;
            }
        }
    }


    private void reloadInventory(Player player) {
        User user = serverInstance.getUserManager().getUserMap().get(player.getUniqueId());
        int index = 9;
        for(Quest quest : user.getQuestList()) {
            String sequential = "§r§7(목표 순서대로 진행 §cX§7)";
            if(quest.getSequential()) {
                sequential = "§r§7(목표 순서대로 진행 §aO§7)";
            }

            List<Component> lore = new ArrayList<>(List.of(
                    Component.text(""),
                    LegacyComponentSerializer.legacySection().deserialize(" §e§l[퀘스트 목표]§r " + sequential).decoration(TextDecoration.ITALIC, false)));
            int stepIndex = 0;
            int activeIndex = -1;
            boolean isSequential = quest.getSequential();
            List<Integer> progressList = user.getProgressMap().get(quest.getName());
            for(int i = 0; i<quest.getSteps().size(); i++) {
                QuestStep questStep = quest.getSteps().get(i);
                int current = progressList.get(i);
                int goal = switch (questStep.getType()) {
                    case HUNT -> questStep.getCountGoal();
                    case ITEM -> questStep.getItemGoal().getAmount();
                    case LOCATION -> 1;
                };
                if(current < goal) {
                    activeIndex = i;
                    break;
                }
            }

            for(QuestStep questStep : quest.getSteps()) {
                int current = progressList.get(stepIndex);
                int goal = switch (questStep.getType()) {
                    case HUNT -> questStep.getCountGoal();
                    case ITEM -> questStep.getItemGoal().getAmount();
                    case LOCATION -> 1;
                };

                String actionText = switch (questStep.getType()) {
                    case HUNT -> "몬스터 사냥하기";
                    case ITEM -> "아이템 모으기";
                    case LOCATION -> "(으)로 이동하기";
                };

                boolean isCompleted = current >= goal;

                if(isCompleted) {
                    String text = "§8§l >§8 " + questStep.getTargetName() + " " + actionText + " (" + current + "/" + goal + ")";
                    lore.add(createQuestComponent(text, true));
                } else if(isSequential && activeIndex != -1 && stepIndex > activeIndex) {
                    String text = "§8§l >§8 " + questStep.getTargetName() + " " + actionText + " (" + current + "/" + goal + ")";
                    lore.add(createQuestComponent(text, false));
                } else {
                    String text = " §e§l>§r §e" + questStep.getTargetName() + "§f " + actionText + " §7(" + current + "/" + goal + ")";
                    lore.add(createQuestComponent(text, false));

                    if (questStep.getType() == QuestStep.QuestType.LOCATION) {
                        Location location = questStep.getLocationGoal();
                        String locText = " §8(" + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + " 좌표로 이동하기)";
                        lore.add(createQuestComponent(locText, false));
                    }
                }

                stepIndex++;
            }

            lore.add(Component.text(""));
            lore.add(LegacyComponentSerializer.legacySection().deserialize(" §a§l[퀘스트 보상]§r").decoration(TextDecoration.ITALIC, false));
            lore.add(LegacyComponentSerializer.legacySection().deserialize(" §a§l>§r§f §7" + quest.getMoneyReward() + "§f원").decoration(TextDecoration.ITALIC, false));
            lore.add(LegacyComponentSerializer.legacySection().deserialize(" §a§l>§r§f §7" + quest.getExperienceReward() + " §f경험치").decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(""));
            lore.add(LegacyComponentSerializer.legacySection().deserialize(" §c§l>§r§f 쉬프트 + 클릭 시 퀘스트를 포기합니다.").decoration(TextDecoration.ITALIC, false));
            lore.add(Component.text(""));
            ItemStack questItem = ItemUtil.createItem(Material.KNOWLEDGE_BOOK, LegacyComponentSerializer.legacySection().deserialize("§6§l[퀘스트]§r §f" + quest.getName()).decoration(TextDecoration.ITALIC, false), lore);
            ItemMeta questItemMeta = questItem.getItemMeta();
            if(activeIndex == -1) {
                questItemMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
                questItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            questItemMeta.getPersistentDataContainer().set(serverInstance.getOriginalNameKey(), PersistentDataType.STRING, quest.getName());
            questItem.setItemMeta(questItemMeta);
            inventory.setItem(index, questItem);
            index++;
        }

        for(int i=index; i<45; i++) {
            inventory.setItem(i, AIR);
        }
    }

    private Component createQuestComponent(String text, boolean completed) {
        Component component = LegacyComponentSerializer.legacySection().deserialize(text)
                .decoration(TextDecoration.ITALIC, false);
        if (completed) {
            component = component.decoration(TextDecoration.STRIKETHROUGH, true);
        }
        return component;
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    public static class QuestGUIListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if(event.getInventory().getHolder() instanceof QuestGUI gui) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                if(event.getRawSlot() >= 9 && event.getRawSlot() <= 44) {
                    if(event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                        if(event.getCurrentItem() != null && event.getCurrentItem() != AIR) {
                            ItemStack currentItemStack = event.getCurrentItem();
                            ItemMeta currentItemMeta = currentItemStack.getItemMeta();
                            String name = currentItemMeta.getPersistentDataContainer().get(gui.serverInstance.getOriginalNameKey(), PersistentDataType.STRING);
                            User user = gui.serverInstance.getUserManager().getUserMap().get(player.getUniqueId());
                            user.getQuestList().remove(gui.serverInstance.getQuestManager().getQuestMap().get(name));
                            user.getProgressMap().remove(name);
                            gui.reloadInventory(player);
                            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize("§6§l[퀘스트]§r §e" + name + " §r퀘스트를 포기했습니다."));
                        }
                    }
                }
            }
        }

    }
}
