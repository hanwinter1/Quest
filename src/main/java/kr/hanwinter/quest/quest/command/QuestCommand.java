package kr.hanwinter.quest.quest.command;

import kr.hanwinter.quest.Main;
import kr.hanwinter.quest.quest.gui.QuestGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuestCommand implements CommandExecutor {

    private final Main serverInstance;

    public QuestCommand(Main serverInstance) {
        this.serverInstance = serverInstance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        Player player = (Player) commandSender;
        player.openInventory(new QuestGUI(player, serverInstance).getInventory());
        return false;
    }
}
