package kr.hanwinter.quest;

import kr.hanwinter.quest.dialogue.manager.DialogueManager;
import kr.hanwinter.quest.npc.gui.NPCConversationGUI;
import kr.hanwinter.quest.npc.listener.NPCListener;
import kr.hanwinter.quest.npc.manager.NPCManager;
import kr.hanwinter.quest.quest.QuestStep;
import kr.hanwinter.quest.quest.command.QuestCommand;
import kr.hanwinter.quest.quest.gui.QuestGUI;
import kr.hanwinter.quest.quest.manager.QuestManager;
import kr.hanwinter.quest.user.listener.UserJoinQuitListener;
import kr.hanwinter.quest.user.listener.UserMonsterKillListener;
import kr.hanwinter.quest.user.listener.UserPickupItemListener;
import kr.hanwinter.quest.user.listener.UserPlayerMoveListener;
import kr.hanwinter.quest.user.manager.UserManager;
import kr.hanwinter.quest.util.PluginReload;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private DialogueManager dialogueManager;
    private NPCManager npcManager;
    private QuestManager questManager;
    private UserManager userManager;
    private NamespacedKey originalNameKey;

    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigurationSerialization.registerClass(QuestStep.class);
        questManager = new QuestManager(this);
        questManager.basicFileSet();
        questManager.loadAllQuestFile();
        dialogueManager = new DialogueManager(this);
        dialogueManager.basicFileSet();
        dialogueManager.loadAllDialogueFile();
        npcManager = new NPCManager(this);
        npcManager.basicFileSet();
        npcManager.loadAllNpcFile();
        userManager = new UserManager(this);
        userManager.basicFileSet();
        originalNameKey = new NamespacedKey(this, "original_name");
        registerCommands();
        registerEvents();
    }

    public NamespacedKey getOriginalNameKey() {
        return originalNameKey;
    }

    public DialogueManager getDialogueManager() {
        return dialogueManager;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void reloadPluginData() {
        questManager.getQuestMap().clear();
        npcManager.getNPCMap().clear();
        dialogueManager.getDialogueMap().clear();
        userManager.getUserMap().clear();

        questManager.loadAllQuestFile();
        npcManager.loadAllNpcFile();
        dialogueManager.loadAllDialogueFile();
        userManager.loadAllUserFile();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers()) {
            userManager.saveUserDataFile(player);
        }
        userManager.getUserMap().clear();
    }

    public void registerCommands() {
        this.getServer().getPluginCommand("quest").setExecutor(new PluginReload(this));
        this.getServer().getPluginCommand("퀘스트").setExecutor(new QuestCommand(this));
    }

    public void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new NPCListener(this), this);
        this.getServer().getPluginManager().registerEvents(new NPCConversationGUI.NPCConversationGUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new UserJoinQuitListener(this.userManager), this);
        this.getServer().getPluginManager().registerEvents(new QuestGUI.QuestGUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new UserMonsterKillListener(this), this);
        this.getServer().getPluginManager().registerEvents(new UserPickupItemListener(this), this);
        this.getServer().getPluginManager().registerEvents(new UserPlayerMoveListener(this), this);
    }
}
