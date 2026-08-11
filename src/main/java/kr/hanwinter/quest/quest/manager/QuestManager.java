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
    private final Set<Quest> questList = new HashSet<>();
    private final File dataFile;
    private final Main serverInstance;

    public QuestManager(Main serverInstance) {
        this.serverInstance = serverInstance;
        dataFile = new File(serverInstance.getDataFolder(), "questList");
    }

    public void addQuest(String name, Set<String> preQuest, List<QuestStep> steps, Integer experienceReward, Integer moneyReward, Boolean isSequential) {
        Quest quest = new Quest(name, preQuest, steps, experienceReward, moneyReward, isSequential);
        questList.add(quest);
        saveQuestFile(quest);
    }

    public void editQuest() {
        // 이후 추가
    }

    public void saveQuestFile(Quest quest) {
        File questFile = new File(dataFile, quest.getName() + ".yml");
        FileConfiguration questConfigFile = YamlConfiguration.loadConfiguration(questFile);
        questConfigFile.set("name", quest.getName());
        questConfigFile.set("preQuest", quest.getPreQuest());
        questConfigFile.set("steps", quest.getSteps());
        questConfigFile.set("experienceReward", quest.getExperienceReward());
        questConfigFile.set("moneyReward", quest.getExperienceReward());
        questConfigFile.set("isSequential", quest.getSequential());
        try {
            questConfigFile.save(questFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAllQuestFile() {
        File[] files = dataFile.listFiles();
        if (files != null) {
            for(File questFile : files) {
                FileConfiguration questConfigFile = YamlConfiguration.loadConfiguration(questFile);
                String name = questConfigFile.getString("name");
                List<String> stringList = questConfigFile.getStringList("preQuest");
                Set<String> preQuest = new HashSet<>(stringList);
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

                Quest quest = new Quest(name, preQuest, steps, experienceReward, moneyReward, isSequential);
                questList.add(quest);
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
