package kr.hanwinter.quest.user.manager;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.npc.NPC;
import kr.hanwinter.quest.quest.Quest;
import kr.hanwinter.quest.quest.manager.QuestManager;
import kr.hanwinter.quest.user.User;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserManager {
    private final Map<UUID, User> userMap = new HashMap<>();
    private final File dataFile;
    private final Main serverInstance;
    private final QuestManager questManager;

    public UserManager(Main serverInstance) {
        this.serverInstance = serverInstance;
        dataFile = new File(serverInstance.getDataFolder(), "userData");
        this.questManager = serverInstance.getQuestManager();
    }

    public Map<UUID, User> getUserMap() {
        return userMap;
    }

    public void saveUserDataFile(Player player) {
        UUID uuid = player.getUniqueId();
        File userFile = new File(dataFile, uuid + ".yml");
        FileConfiguration userDataFile = YamlConfiguration.loadConfiguration(userFile);
        User user = userMap.get(uuid);
        if (user == null) return;

        List<String> stringQuestList = new ArrayList<>();
        for(Quest quest : user.getQuestList()) {
            stringQuestList.add(quest.getName());
        }
        userDataFile.set("questList", stringQuestList);

        List<String> stringClearedList = new ArrayList<>();
        for(Quest quest : user.getClearedQuests()) {
            stringClearedList.add(quest.getName());
        }
        userDataFile.set("clearedQuests", stringClearedList);

        for(Map.Entry<String, List<Integer>> entry : user.getProgressMap().entrySet()) {
            userDataFile.set("progress." + entry.getKey(), entry.getValue());
        }

        try {
            userDataFile.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUserData(Player player) {
        UUID uuid = player.getUniqueId();
        File userFile = new File(dataFile, uuid + ".yml");
        if (!userFile.exists()) {
            userMap.put(uuid, new User(new ArrayList<>(), new ArrayList<>(), new HashMap<>()));
            return;
        }
        FileConfiguration userDataFile = YamlConfiguration.loadConfiguration(userFile);

        List<Quest> questList = new ArrayList<>();
        for(String quest : userDataFile.getStringList("questList")) {
            questList.add(questManager.getQuestMap().get(quest));
        }

        List<Quest> clearedQuests = new ArrayList<>();
        for(String quest : userDataFile.getStringList("clearedQuests")) {
            clearedQuests.add(questManager.getQuestMap().get(quest));
        }

        Map<String, List<Integer>> progressMap = new HashMap<>();
        if(userDataFile.isConfigurationSection("progress")) {
            for(String questName : userDataFile.getConfigurationSection("progress").getKeys(false)) {
                List<Integer> progressList = userDataFile.getIntegerList("progress." + questName);
                progressMap.put(questName, progressList);
            }
        }

        userMap.put(uuid, new User(questList, clearedQuests, progressMap));
    }

    public void loadAllUserFile() {
        for(Player player : serverInstance.getServer().getOnlinePlayers()) {
            loadUserData(player);
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
