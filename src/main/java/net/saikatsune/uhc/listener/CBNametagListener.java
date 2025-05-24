package net.saikatsune.uhc.listener;

import com.cheatbreaker.api.CheatBreakerAPI;
import com.cheatbreaker.nethandler.server.CBPacketOverrideNametags;
import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.util.CC;
import net.saikatsune.uhc.util.PlayerUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CBNametagListener implements Listener {

    private final String nametagLine;

    public CBNametagListener() {
        FileConfiguration config = Game.getInstance().getConfig();
        this.nametagLine = config.getString("NAMETAGS.CHEATBREAKER.1");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setNametag(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        resetNametag(player);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        setNametag(player);
    }

    private void setNametag(Player player) {
        UUID playerId = player.getUniqueId();

        int playerPing = PlayerUtils.getPing(player);

        String formattedNametagLine = nametagLine.replace("%player%", player.getName())
                .replace("%player_name%", player.getName())
                //.replace("%rank%", String.valueOf(api.getRank())
                //.replace("%tag%", tag.getColor() + tag.getPrefix())
                .replace("{player_ping}", String.valueOf(playerPing));
//        String formattedPlayerLine = playerLine.replace("%player%", player.getName())
//                .replace("%player_name%", player.getName())
//                //.replace("%rank%", String.valueOf(api.getRank())
//                //.replace("%tag%", tag.getColor() + tag.getPrefix())
//                .replace("{player_ping}", String.valueOf(playerPing));


        List<String> lines = new ArrayList<>();
        lines.add(CC.translate(CC.Placeholders(player, formattedNametagLine)));
//        lines.add(CC.translate(CC.Placeholders(player, formattedPlayerLine)));

        CBPacketOverrideNametags packet = new CBPacketOverrideNametags(playerId, lines);

        CheatBreakerAPI.getInstance().sendPacket(player, packet);
    }

    private void resetNametag(Player player) {
        List<String> emptyLines = new ArrayList<>();
        CBPacketOverrideNametags packet = new CBPacketOverrideNametags(player.getUniqueId(), emptyLines);

        CheatBreakerAPI.getInstance().sendPacket(player, packet);
    }
}