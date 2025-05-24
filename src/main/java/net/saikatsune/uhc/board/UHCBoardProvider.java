package net.saikatsune.uhc.board;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.states.LobbyState;
import net.saikatsune.uhc.gamestate.states.ScatteringState;
import net.saikatsune.uhc.profile.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class UHCBoardProvider implements BoardProvider {

    private final Game game;

    private final Game instance = Game.getInstance();
    private final String mColor = instance.getmColor();

    public UHCBoardProvider(Game game) {
        this.game = game;
    }

    @Override
    public String getTitle(Player player) {
        FileConfiguration config = game.getScoreboardManager().getConfig();
        String uhcType =
                Game.getInstance().getConfig().getInt("SETUP.FIRST-SHRINK") == 5 ? mColor + "&lUHC &7┃ &fMini"
                        : game.getConfigManager().isSpeed1() ? mColor + "&lUHC &7┃ &fFast"
                        : game.getConfigManager().isSpeed2() ? mColor + "&lUHC &7┃ &fFast"
                        : game.getConfigManager().isNether() ? mColor + "&lUHC &7┃ &fNether"
                        : Game.getInstance().getConfig().getInt("SETUP.GRACE-TIME") == 30 ? mColor + "&lUHC &7┃ &fCup"
                        : mColor + "&lUltra Hardcore";
        return ChatColor.translateAlternateColorCodes('&', config.getString("SCOREBOARDS.TITLE").replace("%uhcType%", uhcType));
    }

    @Override
    public List<String> getBoardLines(Player player) {
        List<String> lines = new ArrayList<>();

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat timeFormat = new SimpleDateFormat("MM/dd/yyyy");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        FileConfiguration config = game.getScoreboardManager().getConfig();

        if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
            if(!game.getGameManager().isTeamGame()) {
                for (int i = 0; i < config.getStringList("SCOREBOARDS.SOLO.LOBBY.LINES").size(); i++) {
                    List<String> path = config.getStringList("SCOREBOARDS.SOLO.LOBBY.LINES");

                    lines.add(path.get(i).replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                            replace("%your_elo%", String.valueOf(PlayerData.getByName(player.getName()).getElo())).
                            replace("%spectators%", String.valueOf(game.getSpectators().size()))).
                            replace("%gameHost%", game.getGameHost()).
                            replace("%scenarios%", "Scenarios:")
                            .replace("&", "§"));

                    if(config.getStringList("SCOREBOARDS.SOLO.LOBBY.LINES").get(i).contains("%scenarios%")) {
                        for (int y = 0; y < game.getScenariosInList().size(); y++) {
                            if (game.getScenariosInList().size() == 0) {
                                lines.add(ChatColor.WHITE + " - No Scenarios");
                            }
                            if (y <= 2) {
                                lines.add(ChatColor.WHITE + " - " + game.getScenariosInList().get(y));
                            } else {
                                lines.removeIf(line -> line.contains("more..."));

                                int scenariosMore = game.getScenariosInList().size() - 3;

                                lines.add(ChatColor.WHITE + "  - and " + scenariosMore + " more...");
                            }
                        }
                    }
                }
            } else {
                for (int i = 0; i < config.getStringList("SCOREBOARDS.TEAMS.LOBBY.LINES").size(); i++) {
                    List<String> path = config.getStringList("SCOREBOARDS.TEAMS.LOBBY.LINES");

                    lines.add(path.get(i).replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%your_elo%", String.valueOf(PlayerData.getByName(player.getName()).getElo())).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", String.valueOf(game.getSpectators().size()))).
                            replace("%gameHost%", game.getGameHost()).
                            replace("%scenarios%", "Scenarios:")
                            .replace("&", "§"));

                    if(config.getStringList("SCOREBOARDS.TEAMS.LOBBY.LINES").get(i).contains("%scenarios%")) {
                        for (int y = 0; y < game.getScenariosInList().size(); y++) {
                            if (y <= 2) {
                                lines.add(ChatColor.WHITE + " - " + game.getScenariosInList().get(y));
                            } else {
                                lines.removeIf(line -> line.contains("more..."));

                                int scenariosMore = game.getScenariosInList().size() - 3;

                                lines.add(ChatColor.WHITE + " - +" + scenariosMore + " more...");
                            }
                        }
                    }
                }
            }

            /*
            lines.add(ChatColor.RED + "Game Host: ");
                    lines.add(ChatColor.WHITE + " - " + game.getGameHost());
                    lines.add("");
                    lines.add(ChatColor.RED + "Teams: " + ChatColor.WHITE + game.getTeamSizeInString());
                    lines.add("");
                    lines.add(ChatColor.RED + "Gamemodes: ");

                    lines.add("");
                    lines.add(ChatColor.RED + "Players: " + ChatColor.WHITE + game.getPlayers().size());
                    lines.add("");
                    lines.add(ChatColor.GRAY + "fladepvp.cc");
             */

            /*
            if(!game.getGameManager().isTeamGame()) {
                for (String string : config.getStringList("SCOREBOARDS.SOLO.LOBBY.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%gameHost%", game.getGameHost()).
                            replace("%scenarios%", "");
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            } else {
                for (String string : config.getStringList("SCOREBOARDS.TEAMS.LOBBY.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                                    replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%teamsAlive%", String.valueOf(game.getTeamManager().getTeams().size())).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            }
             */
        }
        else if(game.getGameStateManager().getCurrentGameState() instanceof ScatteringState) {
            lines.clear();
            if(!game.getGameManager().isTeamGame()) {
                for (String string : config.getStringList("SCOREBOARDS.SOLO.SCATTERING.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%scattering%", String.valueOf(game.getScatterTask().getPlayersToScatter().size())).
                            replace("%scattered%", String.valueOf(game.getScatterTask().getPlayersScattered().size())).
                            replace("%your_elo%", String.valueOf(PlayerData.getByName(player.getName()).getElo())).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                            replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            } else {
                for (String string : config.getStringList("SCOREBOARDS.TEAMS.SCATTERING.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%scattering%", String.valueOf(game.getScatterTask().getPlayersToScatter().size())).
                            replace("%scattered%", String.valueOf(game.getScatterTask().getPlayersScattered().size())).
                            replace("%your_elo%", String.valueOf(PlayerData.getByName(player.getName()).getElo())).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                            replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            }
        } else {
            lines.clear();
            if(!game.getGameManager().isTeamGame()) {
                for (String string : config.getStringList("SCOREBOARDS.SOLO.INGAME.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                            replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            } else {
                for (String string : config.getStringList("SCOREBOARDS.TEAMS.INGAME.LINES")) {
                    string = string.replace("%players%", String.valueOf(game.getPlayers().size())).
                            replace("%gameType%", game.getTeamSizeInString()).
                            replace("%gameTime%", game.getTimeTask().getFormattedTime()).
                            replace("%borderSize%", String.valueOf(game.getConfigManager().getBorderSize())).
                            replace("%kills%", String.valueOf(game.getPlayerKills().get(player.getUniqueId())).
                            replace("%spectators%", "" + game.getSpectators().size())).
                            replace("%teamsAlive%", String.valueOf(game.getTeamManager().getTeams().size())).
                            replace("%teamKills%", String.valueOf(game.getTeamManager().getTeamKills(player))).
                            replace("%gameHost%", game.getGameHost());
                    lines.add(string.replace("&", "§").replace("%spectators%", String.valueOf(game.getSpectators().size())));
                }
            }
        }

        return lines;
    }
}
