package kr.hanwinter.quest.npc.listener;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.dialogue.Dialogue;
import kr.hanwinter.quest.npc.gui.NPCConversationGUI;
import kr.hanwinter.quest.npc.manager.NPCManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class NPCListener implements Listener {

    private final Main serverInstance;

    public NPCListener(Main serverInstance) {
        this.serverInstance = serverInstance;
    }

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        int id = event.getNPC().getId();
        if(serverInstance.getNpcManager().getNPCMap().containsKey(id)) {
            String basicDialogue = serverInstance.getNpcManager().getNPCMap().get(id).getBasicDialogue();
            if(serverInstance.getDialogueManager().getDialogueMap().containsKey(basicDialogue)) {
                player.openInventory(new NPCConversationGUI(serverInstance.getDialogueManager().getDialogueMap().get(basicDialogue), serverInstance.getNpcManager().getNPCMap().get(id), serverInstance).getInventory());
            }
        }
    }
}
