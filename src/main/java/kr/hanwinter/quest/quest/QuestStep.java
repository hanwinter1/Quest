package kr.hanwinter.quest.quest;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class QuestStep implements ConfigurationSerializable {
    public enum QuestType {
        HUNT("사냥"), LOCATION("이동");

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

    public QuestStep(QuestType type, String targetName, int countGoal, Location locationGoal) {
        this.type = type;
        this.targetName = targetName;
        this.countGoal = countGoal;
        this.locationGoal = locationGoal;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("questType", type.name());
        map.put("targetName", targetName);
        map.put("countGoal", countGoal);
        map.put("locationGoal", locationGoal);
        return map;
    }

    public static QuestStep deserialize(Map<String, Object> map) {
        return new QuestStep(
                QuestType.valueOf((String) map.get("questType")),
                (String) map.get("targetName"),
                (int) map.get("countGoal"),
                (Location) map.get("locationGoal")
        );
    }
}
