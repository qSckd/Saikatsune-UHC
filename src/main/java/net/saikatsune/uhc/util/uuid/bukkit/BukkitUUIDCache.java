package net.saikatsune.uhc.util.uuid.bukkit;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.util.uuid.IUUIDCache;

import java.util.UUID;

public final class BukkitUUIDCache implements IUUIDCache {

    public UUID uuid(String name) {
        return (Game.getInstance().getServer().getOfflinePlayer(name).getUniqueId());
    }

    public String name(UUID uuid) {
        return (Game.getInstance().getServer().getOfflinePlayer(uuid).getName());
    }

    public boolean cached(UUID uuid) {
        return Game.getInstance().getServer().getOfflinePlayer(uuid) != null;
    }

    public boolean cached(String name) {
        return Game.getInstance().getServer().getOfflinePlayer(name) != null;
    }

    public void ensure(UUID uuid) {} // Do nothing, as this class just delegates calls down to Bukkit.

    public void update(UUID uuid, String name) {} // We never need to update this, as this class just delegates calls down to Bukkit.

    public void updateAll(UUID uuid,String name) {} // We never need to update this, as this class just delegates calls down to Bukkit.

}