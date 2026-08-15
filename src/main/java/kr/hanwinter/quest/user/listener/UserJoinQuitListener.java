package kr.hanwinter.quest.user.listener;

import kr.hanwinter.quest.user.manager.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserJoinQuitListener implements Listener {

    private final UserManager userManager;

    public UserJoinQuitListener(UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        userManager.loadUserData(event.getPlayer());
    }

    @EventHandler
    public void onQuitEvent(PlayerQuitEvent event) {
        userManager.saveUserDataFile(event.getPlayer());
        userManager.getUserMap().remove(event.getPlayer().getUniqueId());
    }
}
