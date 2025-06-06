package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.GameState;
import net.saikatsune.uhc.gamestate.states.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String mColor = game.getmColor();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("start")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
                    if (Bukkit.getWorld("uhc_world") != null) {
                        if (Bukkit.getWorld("uhc_nether") != null) {
                            if(game.isArenaEnabled()) {
                                game.setArenaEnabled(false);
                                player.sendMessage(prefix + mColor + "You have started the game!");
                                game.getGameStateManager().setGameState(GameState.SCATTERING);
                                game.getScatterTask().runTask();
                            } else {
                                player.sendMessage(prefix + mColor + "You have started the game!");
                                game.getGameStateManager().setGameState(GameState.SCATTERING);
                                game.getScatterTask().runTask();
                            }
                        } else {
                            player.sendMessage(prefix + ChatColor.RED + "The UHC nether doesn't exist!");
                        }
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "The UHC world doesn't exist!");
                    }
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "The game has already started!");
                }
            }
        }
        return false;
    }
}
