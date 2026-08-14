package kr.hanwinter.quest.npc.manager;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.npc.NPC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class NPCManager {
    private final Map<Integer, NPC> npcMap = new HashMap<>();
    private final File dataFile;
    private final Main serverInstance;

    public NPCManager(Main serverInstance) {
        this.serverInstance = serverInstance;
        dataFile = new File(serverInstance.getDataFolder(), "npcList");
    }

    public Map<Integer, NPC> getNPCMap() {
        return npcMap;
    }

    public void loadAllNpcFile() {
        File[] files = dataFile.listFiles();
        if (files != null) {
            for(File npcFile : files) {
                FileConfiguration npcConfigFile = YamlConfiguration.loadConfiguration(npcFile);
                int id = npcConfigFile.getInt("id");
                String basicDialogue = npcConfigFile.getString("basicDialogue");
                List<String> dialogueList = npcConfigFile.getStringList("dialogueList");
                NPC npc = new NPC(id, basicDialogue, dialogueList);
                npcMap.put(id, npc);
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
