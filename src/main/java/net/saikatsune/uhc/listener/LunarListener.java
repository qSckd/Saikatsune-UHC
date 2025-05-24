package net.saikatsune.uhc.listener;

import com.lunarclient.bukkitapi.LunarClientAPI;
import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LunarListener implements Listener {

    public void updateNameTag(Player player) {
        try {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                Player player2 = onlinePlayer.getPlayer();
                scheduler.scheduleSyncRepeatingTask(Game.getInstance(), () ->
                        LunarClientAPI.getInstance().overrideNametag(player2,
                                Arrays.asList(NameTag(player2, onlinePlayer).stream().map(CC::translate).collect(Collectors.toList()).toArray(new String[0])), player), 0L, 20L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> NameTag(Player p, Player v) {
        List<String> tag = new ArrayList<>();
        if (Game.getInstance().getConfig().getBoolean("NAMETAGS.LUNAR.ENABLED")) {
            if (Game.getInstance().getNoCleanListener().noCleanPlayers.contains(p.getUniqueId())) {
                Game.getInstance().getConfig().getStringList("NAMETAGS.LUNAR.NO_CLEAN").stream()
                        .map(CC::translate)
                        .map(line -> CC.Placeholders(p, line))
                        .forEach(tag::add);
//            } else if (Hub.getInst().getPvPListener().getEnablePvP().contains(p.getUniqueId())) {
//                Config.LUNAR_API_NAME_TAGS_PVP_MODE_TAG.stream()
//                        .map(CC::translate)
//                        .map(line -> CC.Placeholders(p, line))
//                        .forEach(tag::add);
//            } else if (Hub.getInst().getQueues().inQueue(p)) {
//                Config.LUNAR_API_NAME_TAGS_QUEUE_TAG.stream()
//                        .map(CC::translate)
//                        .map(line -> CC.Placeholders(p, line))
//                        .forEach(tag::add);
            } else {
                Game.getInstance().getConfig().getStringList("NAMETAGS.LUNAR.DEFAULT").stream()
                        .map(CC::translate)
                        .map(line -> CC.Placeholders(p, line))
                        .forEach(tag::add);
            }
        }
        return tag;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
//        if (Config.LUNAR_API_JOIN_PLAYER_ENABLE) {
//            int time = Config.LUNAR_API_JOIN_PLAYER_TIME;
//            LunarClientAPI.getInstance().sendPacket(p, new LCPacketTitle("TITLE",
//                    CC.translate(PlaceholderAPI.setPlaceholders(p, Config.LUNAR_API_JOIN_PLAYER_TITLE_MESSAGE)),
//                    TimeUnit.MILLISECONDS.toSeconds(time),
//                    TimeUnit.MILLISECONDS.toSeconds(time),
//                    TimeUnit.MILLISECONDS.toSeconds(time)));
//            LunarClientAPI.getInstance().sendPacket(p, new LCPacketTitle("SUBTITLE",
//                    CC.translate(PlaceholderAPI.setPlaceholders(p, Config.LUNAR_API_JOIN_PLAYER_SUBTITLE_MESSAGE)),
//                    TimeUnit.MILLISECONDS.toSeconds(time),
//                    TimeUnit.MILLISECONDS.toSeconds(time),
//                    TimeUnit.MILLISECONDS.toSeconds(time)));
//        }
        this.updateNameTag(p);
    }
}