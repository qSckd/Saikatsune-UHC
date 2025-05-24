/*package net.saikatsune.uhc.leaderboards;

import net.saikatsune.uhc.profile.PlayerData;
import net.saikatsune.uhc.util.CC;
import net.saikatsune.uhc.util.ItemBuilder;
import net.saikatsune.uhc.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static java.lang.reflect.Array.set;
import static java.util.Arrays.fill;

public class LeaderboardsMenu extends Menu {

    public LeaderboardsMenu(Player player) {
        super(player, "Leaderboards", 3, true);
        fill(new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).durability(15).name("&4").build());

        this.updateInventory(player);

    }

    @Override
    public void updateInventory(Player player) {
        //getInventory().clear();
        PlayerData data = PlayerData.getByName(player.getName());
        fill(new ItemBuilder(Material.STAINED_GLASS_PANE).setAmount(1).durability(15).name("&4").get());
//        Tasks.runAsyncTimer(() -> set(22, ServerItem.getUpcomingMatches()), 20L, 100L);
        set(10, new ItemBuilder(Material.BEACON).setName(CC.translate("&d&lYour Statistics")).lore(Arrays.asList(
                "&fKills: &a" + data.getKills(),
                "&fWins: &a" + data.getWins(),
                "&fDeaths: &a" +  data.getDeaths(),
                "&fYour ELO: &a" + data.getElo(),
                "&fK/D &a" + data.getKD())).get());

        set(13, new ItemBuilder(Material.NETHER_STAR).setName(CC.translate("&e&lKills ❘ Top 10")).lore(Arrays.asList(
                "&2#1&f: ...",
                "&a#2&f: ...",
                "&6#3&f: ...",
                "&7#4&f: ...",
                "&7#5&f: ...",
                "&7#6&f: ...",
                "&7#7&f: ...",
                "&7#8&f: ...",
                "&7#9&f: ...",
                "&7#10&f: ..."
        )).get());

        set(16, new ItemBuilder(Material.NETHER_STAR).setName(CC.translate("&e&lWins ❘ Top 10")).setLore(Arrays.asList(
                "&2#1&f: ...",
                "&a#2&f: ...",
                "&6#3&f: ...",
                "&7#4&f: ...",
                "&7#5&f: ...",
                "&7#6&f: ...",
                "&7#7&f: ...",
                "&7#8&f: ...",
                "&7#9&f: ...",
                "&7#10&f: ..."
        )).get());
    }

    @Override
    public void onClickItem(Player player, ItemStack itemStack, boolean b) {
        return;
    }

    @Override
    public void onClose() {
    }
}
*/