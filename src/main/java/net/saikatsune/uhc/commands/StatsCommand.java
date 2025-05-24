package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
//import net.saikatsune.uhc.leaderboards.LeaderboardsMenu;
import net.saikatsune.uhc.profile.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

@SuppressWarnings("deprecation")
public class StatsCommand implements CommandExecutor, Listener {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("stats")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Bukkit.getScheduler().runTaskAsynchronously(game, () -> {
                    if (args.length == 1) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                        if (PlayerData.getPlayerDatas().containsKey(player)) {
                            game.getInventoryHandler().handleStatsInventory(player, target);
                        } else {
                            player.sendMessage(prefix + ChatColor.RED + target.getName() + " is not registered in the database.");
                        }
                    } else if (args.length == 0) {
                        game.getInventoryHandler().handleStatsInventory(player, player);
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /stats (player)");
                    }
                });
            }
        }
        return false;
    }

    @EventHandler
    public void handleInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() != null) {
                if (event.getCurrentItem().getType() == Material.EMERALD) {
                    //new LeaderboardsMenu(player).openInventory();
                    event.setCancelled(true);
                }
            }
        }
    }
