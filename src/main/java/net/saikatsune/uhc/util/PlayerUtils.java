package net.saikatsune.uhc.util;

import org.bukkit.entity.Player;

public class PlayerUtils {

    public static int getPing(Player player) {
        try {
            Object craftPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return (int) craftPlayer.getClass().getField("ping").get(craftPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
