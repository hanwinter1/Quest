package kr.hanwinter.quest;

import kr.hanwinter.quest.quest.QuestStep;
import kr.hanwinter.quest.quest.command.QuestManagementCommand;
import kr.hanwinter.quest.quest.gui.QuestManagementGUI;
import kr.hanwinter.quest.quest.manager.QuestManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main serverInstance;
    private static QuestManager questManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        serverInstance = this;
        registerCommands();
        registerEvents();
        questManager = new QuestManager(serverInstance);
        questManager.basicFileSet();
        questManager.loadAllQuestFile();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        serverInstance = null;
    }

    public void registerCommands() {
        serverInstance.getServer().getPluginCommand("퀘스트관리").setExecutor(new QuestManagementCommand());
    }

    public void registerEvents() {
        serverInstance.getServer().getPluginManager().registerEvents(new QuestManagementGUI.QuestManagementGUIListener(), this);
    }
}
