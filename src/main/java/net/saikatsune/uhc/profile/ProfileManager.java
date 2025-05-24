package net.saikatsune.uhc.profile;

import lombok.Getter;
import net.saikatsune.uhc.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager implements Listener {

    public ProfileManager() {
        Bukkit.getPluginManager().registerEvents(this, Game.getInstance());
    }

    @Getter
    private final Map<UUID, PlayerData> profiles = new ConcurrentHashMap<>();

    public PlayerData getProfile(UUID uniqueId) {
        for (PlayerData profile : profiles.values()) {
            if (profile.getUuid() == uniqueId)
                return profile;
        }
        return new PlayerData(Bukkit.getPlayer(uniqueId).getName());
    }

    public PlayerData getProfile(Player player) {
        for (PlayerData profile : profiles.values()) {
            if (profile.getUuid() == player.getUniqueId())
                return profile;
        }
        return new PlayerData(player.getName());
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        PlayerData profile = new PlayerData(event.getName());

        Game.getInstance().getProfileManager().getProfiles().put(event.getUniqueId(), profile);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CompletableFuture.runAsync(() -> getProfile(event.getPlayer().getUniqueId()).save());
    }
}
