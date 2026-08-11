package kr.hanwinter.quest.dialogue.command;

import kr.hanwinter.quest.dialogue.gui.DialogueManagementGUI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DialogueManagementCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) commandSender;
        if (!player.isOp()) {
            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize("§c권한이 없습니다."));
            return false;
        }
        player.openInventory(new DialogueManagementGUI().getInventory());
        return false;
    }
}
