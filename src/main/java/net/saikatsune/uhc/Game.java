package net.saikatsune.uhc;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.saikatsune.uhc.board.SimpleBoardManager;
import net.saikatsune.uhc.board.UHCBoardProvider;
import net.saikatsune.uhc.commands.*;
import net.saikatsune.uhc.commands.editor.BorderEditorCommand;
import net.saikatsune.uhc.commands.editor.ConfigEditorCommand;
import net.saikatsune.uhc.commands.editor.ScenariosEditorCommand;
import net.saikatsune.uhc.commands.editor.WorldEditorCommand;
import net.saikatsune.uhc.enums.GameType;
import net.saikatsune.uhc.enums.PlayerState;
import net.saikatsune.uhc.enums.ServerVersion;
import net.saikatsune.uhc.gamestate.GameState;
import net.saikatsune.uhc.gamestate.manager.GameStateManager;
import net.saikatsune.uhc.handler.FileHandler;
import net.saikatsune.uhc.handler.InventoryHandler;
import net.saikatsune.uhc.listener.*;
import net.saikatsune.uhc.listener.config.*;
import net.saikatsune.uhc.listener.scenarios.*;
import net.saikatsune.uhc.manager.*;
import net.saikatsune.uhc.populators.CanePopulator;
import net.saikatsune.uhc.profile.ProfileManager;
import net.saikatsune.uhc.support.GlassBorderSupport;
import net.saikatsune.uhc.support.LegacySupport;
//import net.saikatsune.uhc.tasks.ButcherTask;
import net.saikatsune.uhc.tasks.RelogTask;
import net.saikatsune.uhc.tasks.ScatterTask;
import net.saikatsune.uhc.tasks.TimeTask;
import net.saikatsune.uhc.util.uuid.UUIDCache;
import net.saikatsune.uhc.util.uuid.UniqueIDCache;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Game extends JavaPlugin {

    private static Game instance;

    private String prefix;

    private String mColor;
    private String sColor;

    private String teamSizeInString;

    private FileHandler fileHandler;
    private InventoryHandler inventoryHandler;

    private WorldManager worldManager;
    private GameStateManager gameStateManager;
    private LocationManager locationManager;
    private ConfigManager configManager;
    private GameManager gameManager;
    private TeamManager teamManager;
    private ScoreboardManager scoreboardManager;
    private SimpleBoardManager simpleBoardManager;

    private CanePopulator canePopulator;
    @Getter
    public UUIDCache uuidCache;
    @Getter
    private ProfileManager profileManager;

    private HashMap<String, GameType> gameType;
    private HashMap<Player, PlayerState> playerState;
    private HashMap<UUID, Integer> teamNumber;
    private HashMap<Player, Player> teamInvitation;
    private HashMap<UUID, Integer> playerKills;

    private HashMap<UUID, UUID> combatVillagerUUID;

    private HashMap<Player, Location> scatterLocation;

    private HashMap<UUID, Location> deathLocation;
    private HashMap<UUID, ItemStack[]> deathInventory;
    private HashMap<UUID, ItemStack[]> deathArmor;
    private HashMap<UUID, Integer> deathLevels;
    private HashMap<UUID, Integer> deathTeamNumber;

    private HashMap<UUID, Integer> logoutTimer;

    private HashMap<Villager, UUID> playerBoundToVillager;

    private ArrayList<UUID> deadPlayersByUUID;

    private HashMap<Location, UUID>protectedChest;

    private ArrayList<UUID> players;
    private ArrayList<Player> spectators;

    private ArrayList<UUID> whitelisted;
    private ArrayList<UUID> loggedPlayers;
    private ArrayList<UUID> arenaPlayers;

    private ArrayList<UUID> loggedOutPlayers;

    private ArrayList<UUID> receivePvpAlerts;
    private ArrayList<UUID> receiveDiamondAlerts;
    private ArrayList<UUID> receiveGoldAlerts;

    private ArrayList<UUID> helpopMuted;

    private List<String> scenariosInList;

    private boolean inGrace;
    private boolean finalHealHappened;
    private boolean chatMuted;
    private boolean databaseActive;
    private boolean arenaEnabled;

    private int relogTimeInMinutes;

    private String gameHost;

    private ScatterTask scatterTask;
    private TimeTask timeTask;
    //private ButcherTask butcherTask;
    private RelogTask relogTask;
    @Getter
    private static boolean mongo;

    private MongoClient mongoClient;
    @Getter private MongoDatabase mongoDatabase;
    @Getter private MongoCollection profilesCollection;

    private LimitationsListener limitationsListener;
    private NoCleanListener noCleanListener;

    private List<UUID> bestPvePlayers;

    private String serverVersion;

//    private void setupMongo() {
//        mongoClient = new MongoClient(
//                "172.18.0.1",
//                27017
//        );
//
//        String databaseId = "UHC";
//        mongoDatabase = mongoClient.getDatabase(databaseId);
//    }

    @Override
    public void onEnable() {
        this.createConfigFile();

        instance = this;
        mongo = false;
        setupMongo();
        UniqueIDCache.init();
        profilesCollection = mongoDatabase.getCollection("Profiles");
        profileManager = new ProfileManager();

        prefix = getConfig().getString("SETTINGS.PREFIX").replace("&", "§")
                .replace(">>", "»");

        mColor = getConfig().getString("SETTINGS.MAIN-COLOR").replace("&", "§");
        sColor = getConfig().getString("SETTINGS.SECONDARY-COLOR").replace("&", "§");

        fileHandler = new FileHandler();
        inventoryHandler = new InventoryHandler();

        worldManager = new WorldManager();
        gameStateManager = new GameStateManager();
        locationManager = new LocationManager();
        configManager = new ConfigManager();
        gameManager = new GameManager();
        teamManager = new TeamManager();
        scoreboardManager = new ScoreboardManager();

        canePopulator = new CanePopulator();

        if(!scoreboardManager.getScoreboardsFile().exists()) {
            saveResource("scoreboards.yml", false);
        }

        gameStateManager.setGameState(GameState.LOBBY);

        gameType = new HashMap<>();
        playerState = new HashMap<>();
        teamNumber = new HashMap<>();
        teamInvitation = new HashMap<>();
        playerKills = new HashMap<>();

        combatVillagerUUID = new HashMap<>();

        scatterLocation = new HashMap<>();

        deathLocation = new HashMap<>();
        deathTeamNumber = new HashMap<>();
        deathInventory = new HashMap<>();
        deathArmor = new HashMap<>();
        deathLevels = new HashMap<>();

        logoutTimer = new HashMap<>();

        playerBoundToVillager = new HashMap<>();

        players = new ArrayList<>();
        spectators = new ArrayList<>();

        whitelisted = new ArrayList<>();
        loggedPlayers = new ArrayList<>();
        arenaPlayers = new ArrayList<>();

        loggedOutPlayers = new ArrayList<>();

        receivePvpAlerts = new ArrayList<>();
        receiveDiamondAlerts = new ArrayList<>();
        receiveGoldAlerts = new ArrayList<>();

        helpopMuted = new ArrayList<>();

        scenariosInList = new ArrayList<>();

        deadPlayersByUUID = new ArrayList<>();

        protectedChest = new HashMap<>();

        scatterTask = new ScatterTask();
        timeTask = new TimeTask();
        //butcherTask = new ButcherTask();
        relogTask = new RelogTask();

        inGrace = true;
        finalHealHappened = false;
        chatMuted = false;
        arenaEnabled = true;

        bestPvePlayers = new ArrayList<>();

        gameHost = "None";

        teamSizeInString = "FFA";

        relogTimeInMinutes = getConfig().getInt("SETUP.LOGOUT-TIMER");

        databaseActive = getConfig().getBoolean("MYSQL.ENABLED");

        limitationsListener = new LimitationsListener();
        noCleanListener = new NoCleanListener();

        //worldManager.deleteWorld("uhc_practice");
        worldManager.deleteWorld("uhc_world");
        worldManager.deleteWorld("uhc_nether");

        gameType.put("GameType", GameType.SOLO);

        this.init(Bukkit.getPluginManager());

        gameManager.setWhitelisted(true);

        worldManager.createWorld("uhc_world", World.Environment.NORMAL, WorldType.NORMAL);
        worldManager.createWorld("uhc_nether", World.Environment.NETHER, WorldType.NORMAL);
        worldManager.createWorld("uhc_practice", World.Environment.NORMAL, WorldType.FLAT);

        World practiceWorld = Bukkit.getWorld("uhc_practice");

        practiceWorld.setGameRuleValue("doMobSpawning", "false");
        practiceWorld.setDifficulty(Difficulty.PEACEFUL);

        worldManager.createBorderLayer("uhc_practice", 50, 4, null);

        new BukkitRunnable() {
            @Override
            public void run() {
                worldManager.loadWorld("uhc_practice", 50, 5000);
            }
        }.runTaskLater(this, 20);

        for (Entity entity : Bukkit.getWorld("uhc_practice").getEntities()) {
            entity.remove();
        }

        for (Entity entity : Bukkit.getWorld("uhc_world").getEntities()) {
            if(entity instanceof Villager) {
                entity.remove();
            }
        }

        for (Entity entity : Bukkit.getWorld("uhc_nether").getEntities()) {
            if(entity instanceof Villager) {
                entity.remove();
            }
        }

        String packageName = getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        getLogger().info("Configuring crypto mining system to your version...");

        if(version.contains("v1_7_R")) {
            getLogger().info("You are using Spigot version 1.7.X.");
            serverVersion = ServerVersion.V1_7_X.toString();
        } else if(version.contains("v1_8_R")) {
            getLogger().info("You are using Spigot version 1.8.X.");
            serverVersion = ServerVersion.V1_8_X.toString();
        }
    }

    @Override
    public void onDisable() {
        instance = null;

        this.players.clear();
        this.spectators.clear();
        this.whitelisted.clear();
        this.helpopMuted.clear();
    }

    private void init(PluginManager pluginManager) {
        getCommand("setup").setExecutor(new SetupCommand());
        getCommand("whitelist").setExecutor(new WhitelistCommand());
        getCommand("team").setExecutor(new TeamCommand());
        getCommand("mutechat").setExecutor(new MuteChatCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("time").setExecutor(new TimeCommand());
        getCommand("health").setExecutor(new HealthCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("list").setExecutor(new ListCommand());
        getCommand("helpop").setExecutor(new HelpopCommand());
        getCommand("rates").setExecutor(new RatesCommand());
        getCommand("config").setExecutor(new ConfigCommand());
        getCommand("staff").setExecutor(new StaffCommand());
        getCommand("scatter").setExecutor(new LatescatterCommand());
        getCommand("teamchat").setExecutor(new TeamChatCommand());
        getCommand("scenarios").setExecutor(new ScenariosCommand());
        getCommand("sendcoords").setExecutor(new SendCoordsCommand());
        getCommand("backpack").setExecutor(new BackpackCommand());
        getCommand("respawn").setExecutor(new RespawnCommand());
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("alerts").setExecutor(new AlertsCommand());
        getCommand("killstop").setExecutor(new KillsTopCommand());
        getCommand("starterfood").setExecutor(new StarterFoodCommand());
        getCommand("giveall").setExecutor(new GiveAllCommand());
        getCommand("helpopmute").setExecutor(new HelpopMuteCommand());
        getCommand("helpopunmute").setExecutor(new HelpopMuteCommand());
        getCommand("practice").setExecutor(new PracticeCommand());
        getCommand("forceenable").setExecutor(new ForceEnableCommand());
        getCommand("host").setExecutor(new HostCommand());
        getCommand("disqualify").setExecutor(new DisqualifyCommand());
        //getCommand("leaderboards").setExecutor(new LeaderboardsCommand());

        getCommand("worldeditor").setExecutor(new WorldEditorCommand());
        getCommand("configeditor").setExecutor(new ConfigEditorCommand());
        getCommand("bordereditor").setExecutor(new BorderEditorCommand());
        getCommand("scenarioseditor").setExecutor(new ScenariosEditorCommand());

        this.simpleBoardManager = new SimpleBoardManager(this, new UHCBoardProvider(this));
        pluginManager.registerEvents(this.simpleBoardManager, this);

        pluginManager.registerEvents(new LegacySupport(), this);
        pluginManager.registerEvents(new GlassBorderSupport(), this);

        pluginManager.registerEvents(new SetupCommand(), this);
        pluginManager.registerEvents(new StatsCommand(), this);
        pluginManager.registerEvents(new WorldEditorCommand(), this);
        pluginManager.registerEvents(new ConfigEditorCommand(), this);
        pluginManager.registerEvents(new BorderEditorCommand(), this);
        pluginManager.registerEvents(new ScenariosEditorCommand(), this);
        pluginManager.registerEvents(new DisqualifyCommand(), this);

        pluginManager.registerEvents(new AlertsCommand(), this);

        pluginManager.registerEvents(new ConnectionListener(), this);
        pluginManager.registerEvents(new BlockChangeListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new EntityDamageListener(), this);
        pluginManager.registerEvents(new PlayerChatListener(), this);
        pluginManager.registerEvents(new WeatherChangeListener(), this);

        pluginManager.registerEvents(new PlayerPortalListener(), this);
        pluginManager.registerEvents(new PlayerTeleportListener(), this);
        pluginManager.registerEvents(new ItemCraftListener(), this);
        pluginManager.registerEvents(new PlayerConsumeListener(), this);
        pluginManager.registerEvents(new LeavesDecayListener(), this);
        pluginManager.registerEvents(new PlayerExitVehicleListener(), this);

        pluginManager.registerEvents(new CutCleanListener(), this);
        pluginManager.registerEvents(new TimebombListener(), this);
        pluginManager.registerEvents(new TimberListener(), this);
        pluginManager.registerEvents(new SoupListener(), this);
        pluginManager.registerEvents(new BowlessListener(), this);
        pluginManager.registerEvents(new FirelessListener(), this);
        pluginManager.registerEvents(new NoFallListener(), this);
        pluginManager.registerEvents(new RodlessListener(), this);
        pluginManager.registerEvents(new BloodDiamondsListener(), this);
        pluginManager.registerEvents(new LongShotsListener(), this);
        pluginManager.registerEvents(new DiamondlessListener(), this);
        pluginManager.registerEvents(new GoldlessListener(), this);
        pluginManager.registerEvents(new HasteyBoysListener(), this);
        pluginManager.registerEvents(new LimitationsListener(), this);
        pluginManager.registerEvents(new BarebonesListener(), this);
        pluginManager.registerEvents(new FlowerPowerListener(), this);
        pluginManager.registerEvents(new BleedingSweetsListener(), this);
        pluginManager.registerEvents(new SwitcherooListener(), this);
        pluginManager.registerEvents(new HorselessListener(), this);
        pluginManager.registerEvents(new AbsorptionlessListener(), this);
        //pluginManager.registerEvents(new BetaZombiesListener(), this);
        pluginManager.registerEvents(new VeinMinerListener(), this);
        pluginManager.registerEvents(new BestPVEListener(), this);
        pluginManager.registerEvents(new SwordlessListener(), this);
        pluginManager.registerEvents(new SafeLootListener(), this);

        pluginManager.registerEvents(new WebCageListener(), this);
        pluginManager.registerEvents(new NoCleanListener(), this);
        pluginManager.registerEvents(new DoubleOresListener(), this);
        pluginManager.registerEvents(new OreFrenzyListener(), this);

        configManager.setBorderSize(getConfig().getInt("SETUP.MAP-RADIUS"));
        configManager.setAppleRate(2);
        configManager.setFlintRate(50);
        configManager.setEnderpearlDamage(true);
        configManager.setBorderTime(getConfig().getInt("SETUP.FIRST-SHRINK"));
        configManager.setFinalHeal(getConfig().getInt("SETUP.HEAL-TIME"));
        configManager.setGraceTime(getConfig().getInt("SETUP.GRACE-TIME"));
        configManager.setGoldenHeads(true);
        configManager.setNether(false);
        configManager.setShears(false);
        configManager.setSpeed1(false);
        configManager.setSpeed2(false);
        configManager.setStrength1(false);
        configManager.setStrength2(false);
        configManager.setStarterFood(16);

        teamManager.setTeamSize(2);

        PluginManager manager = this.getServer().getPluginManager();
        if (Bukkit.getPluginManager().isPluginEnabled("LunarClient-API")) {
            manager.registerEvents(new LunarListener(), this);
        }
        if (Bukkit.getPluginManager().isPluginEnabled("CheatBreakerAPI")) {
            if (getConfig().getBoolean("NAMETAGS.CHEATBREAKER.ENABLED")) {
                manager.registerEvents(new CBNametagListener(), this);
            }
        }
    }

    private void createConfigFile() {
        FileConfiguration config = getConfig();

        config.addDefault("SETTINGS.PREFIX", "&7[&6UHC&7] ");
        config.addDefault("SETTINGS.MAIN-COLOR", "&6");
        config.addDefault("SETTINGS.SECONDARY-COLOR", "&f");

        config.addDefault("SETUP.MAP-RADIUS", 1500);
        config.addDefault("SETUP.HEAL-TIME", 10);
        config.addDefault("SETUP.GRACE-TIME", 20);
        config.addDefault("SETUP.FIRST-SHRINK", 45);
        config.addDefault("SETUP.LOGOUT-TIMER", 10);

        config.addDefault("NAMETAGS.CHEATBREAKER.ENABLED", true);
        config.addDefault("NAMETAGS.CHEATBREAKER.1", "§7Ping: §f{player_ping}ms");

        config.addDefault("MYSQL.ENABLED", false);
        config.addDefault("MYSQL.HOST", "localhost");
        config.addDefault("MYSQL.DATABASE", "uhc");
        config.addDefault("MYSQL.USERNAME", "root");
        config.addDefault("MYSQL.PASSWORD", "password");
        config.addDefault("MYSQL.PORT", 3306);

        config.addDefault("MONGO.URI", "");
        config.addDefault("MONGO.SERVER_IP", "biolspv93lrutvu8bx1c-mongodb.services.clever-cloud.com:2090");
        config.addDefault("MONGO.DATABASE", "biolspv93lrutvu8bx1c");
        config.addDefault("MONGO.AUTH.ENABLED", true);
        config.addDefault("MONGO.AUTH.USERNAME", "uuh17gd7mkm2vrdqwb77");
        config.addDefault("MONGO.AUTH.PASSWORD", "f5uw108EBiuNMYxJD0M");
        config.addDefault("MONGO.AUTH.AUTH_SOURCE", "admin");

        config.addDefault("CHAT.HOST-PREFIX", "&6&l[Host] ");
        config.addDefault("CHAT.MOD-PREFIX", "&3&l[UHC-Mod] ");
        config.addDefault("CHAT.SPECTATOR-PREFIX", "&7[Spectator] ");
        config.addDefault("CHAT.TEAM-PREFIX", "&7[&aTeam %teamNumber%&7] ");

        config.addDefault("POPULATORS.SUGARCANE.ENABLED", true);
        config.addDefault("POPULATORS.SUGARCANE.PERCENTAGE", 50);

        config.options().copyDefaults(true);
        saveConfig();
    }

    public void registerPlayerDeath(Player player) {
        deathLocation.put(player.getUniqueId(), player.getLocation());
        deathInventory.put(player.getUniqueId(), player.getInventory().getContents());
        deathArmor.put(player.getUniqueId(), player.getInventory().getArmorContents());
        deathLevels.put(player.getUniqueId(), player.getLevel());
        deathTeamNumber.put(player.getUniqueId(), this.getTeamNumber().get(player.getUniqueId()));
    }

    public void setDatabaseActive(boolean databaseActive) {
        this.databaseActive = databaseActive;

        if(databaseActive) {
            getConfig().set("MYSQL.ENABLED", false);
        } else {
            getConfig().set("MYSQL.ENABLED", true);
        }
    }

    private void setupMongo() {
        String URI;
        String configURI = getConfig().getString("MONGO.URI", "");

        if (configURI.isEmpty()) {
            String ip = getConfig().getString("MONGO.SERVER_IP", "biolspv93lrutvu8bx1c-mongodb.services.clever-cloud.com:2090");
            String dbName = getConfig().getString("MONGO.DATABASE", "biolspv93lrutvu8bx1c");
            boolean authEnabled = getConfig().getBoolean("MONGO.AUTH.ENABLED", true);

            if (authEnabled) {
                String user = getConfig().getString("MONGO.AUTH.USERNAME", "uuh17gd7mkm2vrdqwb77");
                String pass = getConfig().getString("MONGO.AUTH.PASSWORD", "f5uw108EBiuNMYxJD0M");
                String authSource = getConfig().getString("MONGO.AUTH.AUTH_SOURCE", "admin");
                URI = "mongodb://" + user + ":" + pass + "@" + ip + "/" + dbName + "?authSource=" + authSource;
            } else {
                URI = "mongodb://" + ip + "/" + dbName;
            }
        } else {
            URI = configURI;
        }

        try {
            MongoClientURI clientURI = new MongoClientURI(URI);
            mongoClient = new MongoClient(clientURI);
            mongoDatabase = mongoClient.getDatabase(getConfig().getString("MONGO.DATABASE"));
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setGameHost(String gameHost) {
        this.gameHost = gameHost;
    }

    public void setTeamSizeInString(String teamSizeInString) {
        this.teamSizeInString = teamSizeInString;
    }

    public String getTeamSizeInString() {
        return teamSizeInString;
    }

    public boolean isDeathRegistered(Player player) {
        return deathLocation.get(player.getUniqueId()) != null;
    }

    public void setInGrace(boolean inGrace) {
        this.inGrace = inGrace;
    }

    public void setChatMuted(boolean chatMuted) {
        this.chatMuted = chatMuted;
    }

    public static Game getInstance() {
        return instance;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getmColor() {
        return mColor;
    }

    public String getsColor() {
        return sColor;
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public HashMap<Player, PlayerState> getPlayerState() {
        return playerState;
    }

    public HashMap<String, GameType> getGameType() {
        return gameType;
    }

    public InventoryHandler getInventoryHandler() {
        return inventoryHandler;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }

    public ArrayList<UUID> getWhitelisted() {
        return whitelisted;
    }

    public HashMap<UUID, Integer> getTeamNumber() {
        return teamNumber;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public HashMap<Player, Player> getTeamInvitation() {
        return teamInvitation;
    }

    public boolean isInGrace() {
        return inGrace;
    }

    public boolean isChatMuted() {
        return chatMuted;
    }

    public ScatterTask getScatterTask() {
        return scatterTask;
    }

    public HashMap<UUID, Integer> getPlayerKills() {
        return playerKills;
    }

    public TimeTask getTimeTask() {
        return timeTask;
    }

    public ArrayList<UUID> getLoggedPlayers() {
        return loggedPlayers;
    }

    public HashMap<UUID, Integer> getDeathLevels() {
        return deathLevels;
    }

    public HashMap<UUID, Integer> getDeathTeamNumber() {
        return deathTeamNumber;
    }

    public HashMap<UUID, ItemStack[]> getDeathArmor() {
        return deathArmor;
    }

    public HashMap<UUID, ItemStack[]> getDeathInventory() {
        return deathInventory;
    }

    public HashMap<UUID, Location> getDeathLocation() {
        return deathLocation;
    }

    public LimitationsListener getLimitationsListener() {
        return limitationsListener;
    }

    public NoCleanListener getNoCleanListener() {
        return noCleanListener;
    }

    public ArrayList<UUID> getReceivePvpAlerts() {
        return receivePvpAlerts;
    }

    public ArrayList<UUID> getReceiveDiamondAlerts() {
        return receiveDiamondAlerts;
    }

    public ArrayList<UUID> getReceiveGoldAlerts() {
        return receiveGoldAlerts;
    }

    public boolean isDatabaseActive() {
        return databaseActive;
    }

    public ArrayList<UUID> getHelpopMuted() {
        return helpopMuted;
    }

    public ArrayList<UUID> getArenaPlayers() {
        return arenaPlayers;
    }

    public boolean isArenaEnabled() {
        return arenaEnabled;
    }

    public void setArenaEnabled(boolean arenaEnabled) {
        this.arenaEnabled = arenaEnabled;
    }

    public HashMap<Player, Location> getScatterLocation() {
        return scatterLocation;
    }

    public HashMap<UUID, UUID> getCombatVillagerUUID() {
        return combatVillagerUUID;
    }

    //public ButcherTask getButcherTask() {
    //    return butcherTask;
    //}

    public HashMap<UUID, Integer> getLogoutTimer() {
        return logoutTimer;
    }

    public int getRelogTimeInMinutes() {
        return relogTimeInMinutes;
    }

    public ArrayList<UUID> getLoggedOutPlayers() {
        return loggedOutPlayers;
    }

    public RelogTask getRelogTask() {
        return relogTask;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public boolean isFinalHealHappened() {
        return finalHealHappened;
    }

    public void setFinalHealHappened(boolean finalHealHappened) {
        this.finalHealHappened = finalHealHappened;
    }

    public List<String> getScenariosInList() {
        return scenariosInList;
    }

    public ArrayList<UUID> getDeadPlayersByUUID() {
        return deadPlayersByUUID;
    }

    public HashMap<Villager, UUID> getPlayerNameBoundToVillager() {
        return playerBoundToVillager;
    }

    public String getGameHost() {
        return gameHost;
    }

    public HashMap<Location, UUID> getProtectedChest() {
        return protectedChest;
    }

    public List<UUID> getBestPvePlayers() {
        return bestPvePlayers;
    }

    public String getServerVersion() {
        return serverVersion;
    }
}

