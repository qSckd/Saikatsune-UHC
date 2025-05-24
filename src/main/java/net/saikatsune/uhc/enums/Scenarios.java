package net.saikatsune.uhc.enums;

import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.gamestate.states.IngameState;
import net.saikatsune.uhc.handler.ItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public enum Scenarios {

    CutClean(false, new ItemHandler(Material.IRON_ORE).setDisplayName("§6CutClean").build(), new String[]{"§7Food and ores are pre-smelted, removing", "§7the needs of furnaces."}),
    TimeBomb(false, new ItemHandler(Material.TNT).setDisplayName("§6TimeBomb").build(), new String[]{"§7When you kill a player, a chest appears", "§7containing their drops. After 30 ", "§7seconds this chest will explode."}),
    Timber(false, new ItemHandler(Material.SAPLING).setDisplayName("§6Timber").build(), new String[]{"§7The whole tree drops when you mine a log."}),
    NoClean(false, new ItemHandler(Material.DIAMOND_SWORD).setDisplayName("§6NoClean").build(), new String[]{"§7When you kill a player you get 20", "§7seconds of invincibility."}),
    Limitations(false, new ItemHandler(Material.REDSTONE_BLOCK).setDisplayName("§6Limitations").build(), new String[]{"§7You can only mine up to 16 diamonds, ", "§732 gold, and 64 iron during the game."}),
    NoFall(false, new ItemHandler(Material.FEATHER).setDisplayName("§6NoFall").build(), new String[]{"§7You take no fall damage."}),
    Backpacks(false, new ItemHandler(Material.CHEST).setDisplayName("§6Backpacks").build(), new String[]{"§7You get backpacks which you can share with", "§7your team."}),
    Barebones(false, new ItemHandler(Material.BONE).setDisplayName("§6Barebones").build(), new String[]{"§7You can only mine iron, you get 1 diamond,", "§71 gapple, 32 arrows and 2 strings from a kill."}),
    Soup(false, new ItemHandler(Material.MUSHROOM_SOUP).setDisplayName("§6Soup").build(), new String[]{"§7When you right click a soup, you regain", "§73.5 hearts."}),
    BloodDiamonds(false, new ItemHandler(Material.REDSTONE).setDisplayName("§6BloodDiamonds").build(), new String[]{"§7When you mine a diamond, you", "§7lose half a heart."}),
    HasteyBoys(false, new ItemHandler(Material.WOOD_PICKAXE).setDisplayName("§6HasteyBoys").build(), new String[]{"§7Tools automatically enchant", "§7with efficiency 3."}),
    Diamondless(false, new ItemHandler(Material.DIAMOND_ORE).setDisplayName("§6Diamondless").build(), new String[]{"§7You cannot mine diamonds.", "§7Players drop 1 diamond", "§7if they die."}),
    Goldless(false, new ItemHandler(Material.GOLD_ORE).setDisplayName("§6Goldless").build(), new String[]{"§7You cannot mine gold.", "§7Players drop 8 gold", "§7if they die."}),
    FlowerPower(false, new ItemHandler(Material.YELLOW_FLOWER).setDisplayName("§6FlowerPower").build(), new String[]{"§7You gain items by", "§7destroying flowers."}),
    GoneFishing(false, new ItemHandler(Material.RAW_FISH).setDisplayName("§6GoneFishing").build(), new String[]{"§7You get an Unbreaking 250 and ", "§7Luck of the Sea 250 fishing rod along with 20 anvils."}),
    InfiniteEnchant(false, new ItemHandler(Material.ENCHANTMENT_TABLE).setDisplayName("§6InfiniteEnchant").build(), new String[]{"§7You can infinitely enchant, no limitations."}),
    DoubleOres(false, new ItemHandler(Material.ICE).setDisplayName("§6DoubleOres").build(), new String[]{"§7Ore drops are doubled."}),
    TripleOres(false, new ItemHandler(Material.PACKED_ICE).setDisplayName("§6TripleOres").build(), new String[]{"§7Ore drops are tripled."}),
    LongShots(false, new ItemHandler(Material.ARROW).setDisplayName("§6LongShots").build(), new String[]{"§7If you get a shot from more than 50 blocks, ", "§7you get healed 1 heart, and do 1.5x the damage."}),
    Bowless(false, new ItemHandler(Material.BOW).setDisplayName("§6Bowless").build(), new String[]{"§7You cannot use bows."}),
    Rodless(false, new ItemHandler(Material.FISHING_ROD).setDisplayName("§6Rodless").build(), new String[]{"§7You cannot use rods."}),
    WebCage(false, new ItemHandler(Material.WEB).setDisplayName("§6Webcage").build(), new String[]{"§7When you kill a player a sphere of", "§7cobwebs surrounds you"}),
    LuckyLeaves(false, new ItemHandler(Material.LEAVES).setDisplayName("§6LuckyLeaves").build(), new String[]{"§7There's a 0.5% chance of golden apples, ", "§7dropping from decaying leaves"}),
    Fireless(false, new ItemHandler(Material.FLINT_AND_STEEL).setDisplayName("§6Fireless").build(), new String[]{"§7You take no fire damage."}),
    OreFrenzy(false, new ItemHandler(Material.NETHER_STAR).setDisplayName("§6OreFrenzy").build(), new String[]{"§7Mining lapis ore will get you a splash potion of healing.", "§7Mining emeralds will get you 32 arrows.", "§7Mining redstone ore will get you a book.", "§7Mining diamond ore will get you a diamond 4 xp bottles.", "§7Mining quartz will get you a block of TNT."}),
    BleedingSweets(false, new ItemHandler(Material.DIAMOND).setDisplayName("§6BleedingSweets").build(), new String[]{"§7When a player dies, they will drop ", "§71 diamond, 5 gold ingots, 16 arrows and 1 string."}),
    Switcheroo(false, new ItemHandler(Material.ENDER_PEARL).setDisplayName("§6Switcheroo").build(), new String[]{"§7When you shoot someone, you trade places with them."}),
    Horseless(false, new ItemHandler(Material.SADDLE).setDisplayName("§6Horseless").build(), new String[]{"§7You cannot ride horses."}),
    BestPVE(false, new ItemHandler(Material.WATCH).setDisplayName("§6BestPVE").build(), new String[]{"§7You're part of a BestPVE list, if you take damage", "§7you're removed from it and to be added back", "§7you need to kill a player. If you're part of", "§7the list, you'll gain an extra heart."}),
    Absorptionless(false, new ItemHandler(Material.GLOWSTONE_DUST).setDisplayName("§6Absorptionless").build(), new String[]{"§7You do not receive absorption", "§7hearts after eating a", "§7golden apple."}),
    VeinMiner(false, new ItemHandler(Material.IRON_PICKAXE).setDisplayName("§6VeinMiner").build(), new String[]{"§7Mine all blocks in a vein, ", "§7when sneaking."}),
    //BetaZombies(false, new ItemHandler(Material.ROTTEN_FLESH).setDisplayName("§6BetaZombies").build(), new String[]{"§7Zombies drop feathers."}),
    Swordless(false, new ItemHandler(Material.WOOD_SWORD).setDisplayName("§6Swordless").build(), new String[]{"§7You cannot craft/use swords."}),
    SafeLoot(false, new ItemHandler(Material.CHEST).setDisplayName("§6SafeLoot").build(), new String[]{"§7When a player dies, a chest appears", "§7containing their loot.", "§7Only the killer or their team", "§7can access the loot.", "§7After 30 seconds, the", "§7chest will explode."});

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();
    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    private boolean enabled;
    private final ItemStack scenarioItem;
    private final String[] scenarioExplanation;

    Scenarios(boolean enabled, ItemStack scenarioItem, String[] scenarioExplanation) {
        this.enabled = enabled;
        this.scenarioItem = scenarioItem;
        this.scenarioExplanation = scenarioExplanation;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if(enabled) {
            Bukkit.broadcastMessage(prefix + mColor + scenarioItem.getItemMeta().getDisplayName() + sColor + " has been " + ChatColor.GREEN + "enabled" + sColor + ".");

            if(scenarioItem.getItemMeta().getDisplayName().contains("BestPVE")) {
                if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                    if(Scenarios.BestPVE.isEnabled()) {
                        game.getGameManager().startBestPveTimer();

                        for (UUID allPlayers : game.getPlayers()) {
                            game.getBestPvePlayers().add(allPlayers);

                            if(allPlayers != null) {
                                Player allOnlinePlayers = Bukkit.getPlayer(allPlayers);

                                allOnlinePlayers.sendMessage(prefix + ChatColor.GREEN + "You have been added to the BestPVE list.");
                            }
                        }
                    }
                }
            }
        } else {
            Bukkit.broadcastMessage(prefix + mColor + scenarioItem.getItemMeta().getDisplayName() + sColor + " has been " + ChatColor.RED + "disabled" + sColor + ".");

            if(scenarioItem.getItemMeta().getDisplayName().contains("BestPVE")) {
                if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                    for (UUID allPlayers : game.getPlayers()) {
                        game.getBestPvePlayers().remove(allPlayers);
                    }
                }
            }
        }
    }

    public static Inventory getExplanations() {
        Inventory inventory = Bukkit.createInventory(null, 9*4,  "Scenarios");

        inventory.clear();

        for(Scenarios scenarios : Scenarios.values()) {
            if(scenarios.isEnabled()) {
                ItemStack itemStack = scenarios.getScenarioItem();
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(Arrays.asList(scenarios.getScenarioExplanation()));
                itemStack.setItemMeta(itemMeta);
                inventory.addItem(scenarios.getScenarioItem());
            }
        }

        return inventory;
    }

    public static Inventory toToggle() {
        Inventory inventory = Bukkit.createInventory(null, 9*4,  "Scenarios Editor");

        inventory.clear();

        for(Scenarios scenarios : Scenarios.values()) {
            ItemStack itemStack = scenarios.getScenarioItem();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(Arrays.asList(scenarios.getScenarioExplanation()));
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(scenarios.getScenarioItem());
        }

        return inventory;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ItemStack getScenarioItem() {
        return scenarioItem;
    }

    public String[] getScenarioExplanation() {
        return scenarioExplanation;
    }

}
