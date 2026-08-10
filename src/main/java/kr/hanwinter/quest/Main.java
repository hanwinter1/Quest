package kr.hanwinter.quest;

import kr.hanwinter.quest.quest.QuestStep;
import kr.hanwinter.quest.quest.command.QuestManagementCommand;
import kr.hanwinter.quest.quest.gui.QuestManagementGUI;
import kr.hanwinter.quest.quest.manager.QuestManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerCommands();
        registerEvents();
        QuestManager questManager = new QuestManager(this);
        questManager.basicFileSet();
        questManager.loadAllQuestFile();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {
        this.getServer().getPluginCommand("퀘스트관리").setExecutor(new QuestManagementCommand());
    }

    public void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new QuestManagementGUI.QuestManagementGUIListener(), this);
    }
}
