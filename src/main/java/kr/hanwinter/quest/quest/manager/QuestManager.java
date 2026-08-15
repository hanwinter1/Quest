package kr.hanwinter.quest.quest.manager;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.quest.Quest;
import kr.hanwinter.quest.quest.QuestStep;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QuestManager {
    private final Map<String, Quest> questMap = new HashMap<>();
    private final File dataFile;
    private final Main serverInstance;

    public Map<String, Quest> getQuestMap() {
        return questMap;
    }

    public QuestManager(Main serverInstance) {
        this.serverInstance = serverInstance;
        dataFile = new File(serverInstance.getDataFolder(), "questList");
    }

    public void loadAllQuestFile() {
        File[] files = dataFile.listFiles();
        if (files != null) {
            for(File questFile : files) {
                FileConfiguration questConfigFile = YamlConfiguration.loadConfiguration(questFile);
                String name = questConfigFile.getString("name");
                List<String> stringList = questConfigFile.getStringList("preQuest");
                List<?> list = questConfigFile.getList("steps");
                List<QuestStep> steps = new ArrayList<>();

                if (list != null) {
                    for (Object obj : list) {
                        if (obj instanceof QuestStep) {
                            steps.add((QuestStep) obj);
                        }
                    }
                }
                int experienceReward = questConfigFile.getInt("experienceReward");
                int moneyReward = questConfigFile.getInt("moneyReward");
                boolean isSequential = questConfigFile.getBoolean("isSequential");

                Quest quest = new Quest(name, steps, experienceReward, moneyReward, isSequential);
                questMap.put(name, quest);
            }
        }
    }

    public void basicFileSet() {
        if(!serverInstance.getDataFolder().exists()) {
            serverInstance.getDataFolder().mkdirs();
        }

        if(!dataFile.exists()) {
            dataFile.mkdirs();
        }
    }
}
