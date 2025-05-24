package net.saikatsune.uhc.leaderboards;

import net.saikatsune.uhc.util.CC;
import net.saikatsune.uhc.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardsButton extends Button {
    @Override
    public String getName(Player player) {
        return CC.translate("&6&lLeaderboards");
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();

        lines.add(CC.translate("&7Click to open Leaderboards"));

        return lines;
    }

    @Override
    public Material getMaterial(Player player) {
        return Material.EMERALD;
    }
}
