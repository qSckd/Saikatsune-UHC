package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.states.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StarterFoodCommand implements CommandExecutor {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    @Override
    public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("starterfood")) {
            if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
                if(args.length == 1) {
                    if(game.getFileHandler().isNumeric(args[0])) {
                        game.getConfigManager().setStarterFood(Integer.parseInt(args[0]));
                        Bukkit.broadcastMessage(prefix + mColor + "Starter Food " + sColor + "has been changed to " + mColor +
                                args[0] + sColor + " Steaks!");
                    } else {
                        player.sendMessage(prefix + ChatColor.RED + "The argument given is not a number or is too high.");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /starterfood (number)");
                }
            } else {
                player.sendMessage(prefix + ChatColor.RED + "This game has already started!");
            }
        }
        return false;
    }
}
