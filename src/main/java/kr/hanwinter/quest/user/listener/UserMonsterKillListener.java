package kr.hanwinter.quest.user.listener;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.quest.Quest;
import kr.hanwinter.quest.quest.QuestStep;
import kr.hanwinter.quest.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

import static kr.hanwinter.quest.npc.gui.NPCConversationGUI.AIR;

public class UserMonsterKillListener implements Listener {

    private final Main serverInstance;

    public UserMonsterKillListener(Main serverInstance) {
        this.serverInstance = serverInstance;
    }

    @EventHandler
    public void onMonsterKill(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if(player != null) {
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
                    if (questStep.getType() == QuestStep.QuestType.HUNT) {
                        if (questStep.getTargetName().equals(event.getEntity().getName())) {
                            progressList.set(index, progressList.get(index) + 1);
                        }
                    }
                    index++;
                }
            }
        }
    }
}
