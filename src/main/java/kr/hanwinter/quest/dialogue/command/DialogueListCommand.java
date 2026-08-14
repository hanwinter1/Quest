package kr.hanwinter.quest.dialogue.command;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.dialogue.Dialogue;
import kr.hanwinter.quest.dialogue.manager.DialogueManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DialogueListCommand implements CommandExecutor {
    private final Main serverInstance;

    public DialogueListCommand(Main serverInstance) {
        this.serverInstance = serverInstance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        Player player = (Player) commandSender;
        if(!player.isOp()) {
            return false;
        }
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(" - 대화문 리스트 -"));
        player.sendMessage(Component.text(""));

        for(Map.Entry<String, Dialogue> entry : serverInstance.getDialogueManager().getDialogueMap().entrySet()) {
            Dialogue dialogue = entry.getValue();
            player.sendMessage(Component.text(" 이름 - " + dialogue.getName()));
            player.sendMessage(Component.text(" 대화문"));
            for(String text : dialogue.getDialogue()) {
                player.sendMessage(Component.text(" - " + text));
            }
            player.sendMessage(Component.text(" 등록된 퀘스트 - " + dialogue.getQuest()));
            player.sendMessage(Component.text(" 선행 퀘스트"));
            for(String quest : dialogue.getPreQuest()) {
                player.sendMessage(Component.text(" - " + quest));
            }
            player.sendMessage(Component.text(" 레벨 제한 - " + dialogue.getReqLevel()));
            player.sendMessage(Component.text(""));
        }
        return false;
    }
}
