package net.saikatsune.uhc.commands;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.PlayerState;
import net.saikatsune.uhc.gamestate.states.LobbyState;
import net.saikatsune.uhc.gamestate.states.ScatteringState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.github.paperspigot.Title;
import net.saikatsune.uhc.util.CC;

public class StaffCommand implements CommandExecutor {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("staff")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;

                if(!(game.getGameStateManager().getCurrentGameState() instanceof ScatteringState)) {
                    if(game.getPlayerState().get(player) != PlayerState.SPECTATOR) {

                        if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
                            if(!game.getArenaPlayers().contains(player.getUniqueId())) {
                                if(game.getGameManager().isTeamGame()) {
                                    if(game.getTeamNumber().get(player.getUniqueId()) != -1) {
                                        game.getTeamManager().removePlayerFromTeam(game.getTeamNumber().get(player.getUniqueId()), player.getUniqueId());
                                        game.getTeamNumber().put(player.getUniqueId(), -1);
                                    }
                                }

                                game.getGameManager().setPlayerState(player, PlayerState.SPECTATOR);
                                player.getInventory().clear();
                                game.getInventoryHandler().handleStaffInventory(player);

                                game.getReceivePvpAlerts().add(player.getUniqueId());
                                game.getReceiveDiamondAlerts().add(player.getUniqueId());
                                game.getReceiveGoldAlerts().add(player.getUniqueId());
                            } else {
                                player.sendMessage(prefix + ChatColor.RED + "You cannot go into staff-mode when in arena.");
                            }
                        } else {
                            player.damage(20);
                        }

                        if(!game.getArenaPlayers().contains(player.getUniqueId())) {
                            player.sendTitle(new Title(CC.translate("&6&lStaff Mode"),CC.translate("&aenabled."),20,70,20));
                            player.sendMessage(prefix + ChatColor.GREEN + "You are now in Staff-Mode!");
                        }
                    } else {
                        if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
                            game.getGameManager().setPlayerState(player, PlayerState.PLAYER);
                            game.getGameManager().resetPlayer(player);

                            game.getReceivePvpAlerts().remove(player.getUniqueId());
                            game.getReceiveDiamondAlerts().remove(player.getUniqueId());
                            game.getReceiveGoldAlerts().remove(player.getUniqueId());

                            player.teleport(game.getLocationManager().getLocation("Spawn-Location"));
                            player.sendTitle(new Title(CC.translate("&6&lStaff Mode"),CC.translate("&cdisabled."),20,70,20));
                            player.sendMessage(prefix + ChatColor.RED + "You are no longer in Staff-Mode!");

                            game.getInventoryHandler().handleLobbyInventory(player);
                        } else {
                            player.sendMessage(prefix + ChatColor.RED + "The game has already started!");
                        }
                    }
                } else {
                    player.sendMessage(prefix + ChatColor.RED + "You cannot go into staff-mode right now.");
                }
            }
        }
        return false;
    }
}
