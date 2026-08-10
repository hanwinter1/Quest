package kr.hanwinter.quest.quest.command;

import kr.hanwinter.quest.quest.gui.QuestManagementGUI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuestManagementCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) commandSender;
        if (!player.isOp()) {
            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize("§c권한이 없습니다."));
            return false;
        }
        player.openInventory(new QuestManagementGUI().getInventory());
        return false;
    }
}
