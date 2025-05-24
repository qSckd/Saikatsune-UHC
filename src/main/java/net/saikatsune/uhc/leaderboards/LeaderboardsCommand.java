package net.saikatsune.uhc.leaderboards;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.profile.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class LeaderboardsCommand implements CommandExecutor {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("leaderboards")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                Bukkit.getScheduler().runTaskAsynchronously(game, () -> {
                    if(args.length == 1) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                        if(PlayerData.getPlayerDatas().containsKey(player)) {
                            game.getInventoryHandler().handleLeaderboardsInventory(player);
                        } else {
                            player.sendMessage(prefix + ChatColor.RED + target.getName() + " is not registered in the database.");
                        }
                    } else if(args.length == 0) {
                        game.getInventoryHandler().handleLeaderboardsInventory(player);
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /stats (player)");
                    }
                });
            }
        }
        return false;
    }
}

