package kr.hanwinter.quest.user.listener;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.quest.Quest;
import kr.hanwinter.quest.quest.QuestStep;
import kr.hanwinter.quest.user.User;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class UserPlayerMoveListener implements Listener {

    private final Main serverInstance;

    public UserPlayerMoveListener(Main serverInstance) {
        this.serverInstance = serverInstance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        Location from = event.getFrom();

        if(from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        Player player = event.getPlayer();
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
                if (questStep.getType() == QuestStep.QuestType.LOCATION) {
                    Location targetLocation = questStep.getLocationGoal();
                    if(player.getWorld() == targetLocation.getWorld()) {
                        if (player.getLocation().distanceSquared(targetLocation) <= 9.0) {
                            if (progressList.get(index) < 1) {
                                progressList.set(index, 1);
                                player.sendMessage(LegacyComponentSerializer.legacySection().deserialize("§6§l[퀘스트]§r §e" + questStep.getTargetName() + "§r 위치에 도착했습니다."));
                            }
                        }
                    }
                }
                index++;
            }
        }
    }
}
