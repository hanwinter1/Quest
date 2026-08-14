package kr.hanwinter.quest.dialogue.manager;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.dialogue.Dialogue;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DialogueManager {
    private final Map<String, Dialogue> dialogueMap = new HashMap<>();
    private final File dataFile;
    private final Main serverInstance;

    public DialogueManager(Main serverInstance) {
        this.serverInstance = serverInstance;
        dataFile = new File(serverInstance.getDataFolder(), "dialogueList");
    }

    public Map<String, Dialogue> getDialogueMap() {
        return dialogueMap;
    }

    public void loadAllDialogueFile() {
        File[] files = dataFile.listFiles();
        if (files != null) {
            for(File dialogueFile : files) {
                FileConfiguration dialogueConfigFile = YamlConfiguration.loadConfiguration(dialogueFile);
                String name = dialogueConfigFile.getString("name");
                String displayName = dialogueConfigFile.getString("displayName");
                List<String> dialogueList = dialogueConfigFile.getStringList("dialogue");
                String quest = dialogueConfigFile.getString("quest");
                List<String> preQuest = dialogueConfigFile.getStringList("preQuest");
                int reqLevel = dialogueConfigFile.getInt("reqLevel");

                Dialogue dialogue = new Dialogue(name, displayName, dialogueList, quest, preQuest, reqLevel);
                dialogueMap.put(name, dialogue);
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
