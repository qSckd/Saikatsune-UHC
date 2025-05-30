package net.saikatsune.uhc.manager;

import net.saikatsune.uhc.Game;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ScoreboardManager {

    private final Game game = Game.getInstance();

    private final File scoreboardsFile = new File(game.getDataFolder(), "scoreboards.yml");
    private final FileConfiguration config = YamlConfiguration.loadConfiguration(scoreboardsFile);

    public File getScoreboardsFile() {
        return scoreboardsFile;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
