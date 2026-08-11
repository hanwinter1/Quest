package kr.hanwinter.quest.quest;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class QuestStep implements ConfigurationSerializable {
    public enum QuestType {
        HUNT("사냥"), LOCATION("이동"), ITEM("아이템");

        private final String displayName;

        QuestType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private QuestType type;
    private String targetName;
    private int countGoal;
    private Location locationGoal;
    private ItemStack itemGoal;

    public QuestStep(QuestType type, String targetName, int countGoal, Location locationGoal, ItemStack itemGoal) {
        this.type = type;
        this.targetName = targetName;
        this.countGoal = countGoal;
        this.locationGoal = locationGoal;
        this.itemGoal = itemGoal;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("questType", type.name());
        map.put("targetName", targetName);
        map.put("countGoal", countGoal);
        map.put("locationGoal", locationGoal);
        map.put("itemGoal", itemGoal);
        return map;
    }

    public static QuestStep deserialize(Map<String, Object> map) {
        return new QuestStep(
                QuestType.valueOf((String) map.get("questType")),
                (String) map.get("targetName"),
                (int) map.get("countGoal"),
                (Location) map.get("locationGoal"),
                (ItemStack) map.get("itemGoal")
        );
    }
}
