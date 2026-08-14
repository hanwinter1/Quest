package kr.hanwinter.quest;

import kr.hanwinter.quest.dialogue.command.DialogueListCommand;
import kr.hanwinter.quest.dialogue.manager.DialogueManager;
import kr.hanwinter.quest.npc.gui.NPCConversationGUI;
import kr.hanwinter.quest.npc.listener.NPCListener;
import kr.hanwinter.quest.npc.manager.NPCManager;
import kr.hanwinter.quest.quest.manager.QuestManager;
import kr.hanwinter.quest.util.PluginReload;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private DialogueManager dialogueManager;
    private NPCManager npcManager;
    private QuestManager questManager;
    private NamespacedKey originalNameKey;

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerCommands();
        registerEvents();
        questManager = new QuestManager(this);
        questManager.basicFileSet();
        questManager.loadAllQuestFile();
        dialogueManager = new DialogueManager(this);
        dialogueManager.basicFileSet();
        dialogueManager.loadAllDialogueFile();
        npcManager = new NPCManager(this);
        npcManager.basicFileSet();
        npcManager.loadAllNpcFile();
        originalNameKey = new NamespacedKey(this, "original_name");
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

    public void reloadPluginData() {
        questManager.getQuestSet().clear();
        npcManager.getNPCMap().clear();
        dialogueManager.getDialogueMap().clear();

        questManager.loadAllQuestFile();
        npcManager.loadAllNpcFile();
        dialogueManager.loadAllDialogueFile();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {
        this.getServer().getPluginCommand("대화문목록").setExecutor(new DialogueListCommand(this));
        this.getServer().getPluginCommand("quest").setExecutor(new PluginReload(this));
    }

    public void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new NPCListener(this), this);
        this.getServer().getPluginManager().registerEvents(new NPCConversationGUI.NPCConversationGUIListener(), this);
    }
}
