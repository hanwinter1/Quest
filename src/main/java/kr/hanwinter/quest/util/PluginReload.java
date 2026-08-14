package kr.hanwinter.quest.util;

import kr.hanwinter.quest.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PluginReload implements CommandExecutor {

    private final Main serverInstance;

    public PluginReload(Main serverInstance) {
        this.serverInstance = serverInstance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (strings.length > 0 && strings[0].equalsIgnoreCase("reload")) {
            if (!commandSender.isOp()) {
                commandSender.sendMessage(Component.text("권한이 없습니다."));
                return true;
            }

            // 플러그인 리로드 메소드 호출
            serverInstance.reloadPluginData();
            commandSender.sendMessage(Component.text("리로드 성공"));
            return true;
        }

        return false;
    }
}
