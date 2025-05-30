package net.saikatsune.uhc.listener;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.saikatsune.uhc.Game;
import net.saikatsune.uhc.enums.PlayerState;
import net.saikatsune.uhc.enums.Scenarios;
import net.saikatsune.uhc.gamestate.states.IngameState;
import net.saikatsune.uhc.gamestate.states.LobbyState;
import net.saikatsune.uhc.handler.ItemHandler;
import net.saikatsune.uhc.profile.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;
import java.util.UUID;

public class EntityDamageListener implements Listener {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    @EventHandler
    public void handleEntityDamageEvent(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if(game.getSpectators().contains(player)) event.setCancelled(true);

            if(!(game.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
                if(!game.getArenaPlayers().contains(player.getUniqueId())) {
                    event.setCancelled(true);
                }

                if(event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    player.teleport(game.getLocationManager().getLocation("Spawn-Location"));
                }
            }
        } else {
            if(!(game.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
                event.setCancelled(true);
            } else {
                if(event.getEntity() instanceof Villager) {
                    Villager villager = (Villager) event.getEntity();

                    if(villager.getCustomName().contains("(Logger) ")) {
                        if(game.isInGrace()) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void handleEntityDeathEvent(EntityDeathEvent event) {
        try {
            if(event.getEntity() instanceof Villager) {
                Villager villager = (Villager) event.getEntity();

                if(villager.getCustomName().contains("(Logger) ")) {
                    Player player = event.getEntity().getKiller();
                    OfflinePlayer dyingPlayer = Bukkit.getOfflinePlayer(game.getPlayerNameBoundToVillager().get(villager));

                    game.getPlayerKills().put(player.getUniqueId(), game.getPlayerKills().get(player.getUniqueId()) + 1);

                    if(game.getGameManager().isTeamGame()) {
                        int teamKills = game.getTeamManager().getTeams().get(game.getTeamNumber().get(player.getUniqueId())).getKills();

                        game.getTeamManager().getTeams().get(game.getTeamNumber().get(player.getUniqueId())).setKills(teamKills + 1);
                    }

                    PlayerData.getByName(player.getName()).setKills(PlayerData.getByName(player.getName()).getKills() + 1);

                    Bukkit.broadcastMessage(ChatColor.RED + villager.getCustomName() + ChatColor.GRAY + "[" +
                            game.getPlayerKills().get(dyingPlayer.getUniqueId()) + "]" + ChatColor.YELLOW + " was slain " +
                            "by " + ChatColor.RED + player.getName() + ChatColor.GRAY + "[" +
                            game.getPlayerKills().get(player.getUniqueId()) + "].");

                    for (UUID villagerKey : game.getCombatVillagerUUID().keySet()) {
                        //player.setLevel(player.getLevel() + game.getDeathLevels().get(villagerConnectedToPlayer.getUniqueId()));

                        try {
                            game.getGameManager().dropPlayerDeathInventory(dyingPlayer.getUniqueId(), player);

                            game.getDeathLocation().get(dyingPlayer.getUniqueId()).getWorld().dropItem(
                              game.getDeathLocation().get(dyingPlayer.getUniqueId()), new ItemHandler(Material.GOLDEN_APPLE).setDisplayName(
                                      ChatColor.GOLD + "Golden Head").build());

                            if(Scenarios.BleedingSweets.isEnabled()) {
                                game.getDeathLocation().get(dyingPlayer.getUniqueId()).getWorld().dropItem(
                                        game.getDeathLocation().get(dyingPlayer.getUniqueId()), new ItemStack(Material.DIAMOND));
                                game.getDeathLocation().get(dyingPlayer.getUniqueId()).getWorld().dropItem(
                                        game.getDeathLocation().get(dyingPlayer.getUniqueId()), new ItemStack(Material.GOLD_INGOT, 5));
                                game.getDeathLocation().get(dyingPlayer.getUniqueId()).getWorld().dropItem(
                                        game.getDeathLocation().get(dyingPlayer.getUniqueId()), new ItemStack(Material.ARROW, 16));
                                game.getDeathLocation().get(dyingPlayer.getUniqueId()).getWorld().dropItem(
                                        game.getDeathLocation().get(dyingPlayer.getUniqueId()), new ItemStack(Material.STRING));
                            }

                            if(Scenarios.Goldless.isEnabled()) {
                                game.getDeathLocation().get(dyingPlayer.getUniqueId()).getWorld().dropItem(
                                        game.getDeathLocation().get(dyingPlayer.getUniqueId()), new ItemStack(Material.GOLD_INGOT, 8));
                            }

                            if(Scenarios.Diamondless.isEnabled()) {
                                game.getDeathLocation().get(dyingPlayer.getUniqueId()).getWorld().dropItem(
                                        game.getDeathLocation().get(dyingPlayer.getUniqueId()), new ItemStack(Material.DIAMOND));
                            }
                        } catch (Exception ignored) {}

                        game.getLoggedPlayers().remove(dyingPlayer.getUniqueId());
                        game.getWhitelisted().remove(dyingPlayer.getUniqueId());

                        game.getLoggedOutPlayers().remove(dyingPlayer.getUniqueId());

                        game.getDeadPlayersByUUID().add(dyingPlayer.getUniqueId());

                        PlayerData.getByName(player.getName()).setDeaths(PlayerData.getByName(player.getName()).getDeaths() + 1);

                        game.getPlayers().remove(dyingPlayer.getUniqueId());

                        game.getCombatVillagerUUID().remove(villagerKey);

                        if(game.getGameManager().isTeamGame()) {
                            game.getGameManager().removeDeadTeams();
                        }

                        /*
                        try {
                            game.getPlayers().remove(villagerConnectedToPlayer.getUniqueId());
                        } catch (Exception ignored) {
                            //Bukkit.broadcastMessage("§f§l[DEBUG] Was not able to remove a player from the players list. " +
                            //villagerConnectedToPlayer.getName() + "; " + villagerConnectedToPlayer.getUniqueId());
                        }
                         */
                    }
                    game.getGameManager().checkWinner();
                }
            }
        } catch (Exception ignored) {

        }
    }

    @EventHandler
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(event.getDamager() instanceof Player) {
                Player attacker = (Player) event.getDamager();

                if(game.getSpectators().contains(player)) event.setCancelled(true);
                if(game.getSpectators().contains(attacker)) event.setCancelled(true);

                if(!(game.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
                    if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
                        if(!game.isArenaEnabled()) {
                            if(!game.getArenaPlayers().contains(player.getUniqueId()) &&
                                    !game.getArenaPlayers().contains(attacker.getUniqueId())) {
                                event.setCancelled(true);
                            }
                        }
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    if(game.isInGrace()) {
                        event.setCancelled(true);
                    }

                    if(game.getPlayers().contains(player.getUniqueId())) {
                        if(game.getPlayers().contains(attacker.getUniqueId())) {
                            for (Player allPlayers : game.getSpectators()) {
                                if(allPlayers.hasPermission("uhc.host")) {
                                    if(game.getReceivePvpAlerts().contains(allPlayers.getUniqueId())) {

                                        TextComponent textComponent = new TextComponent();

                                        textComponent.setText(prefix + mColor + attacker.getName() + sColor + " has attacked " + mColor + player.getName() + sColor + "!");

                                        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                new ComponentBuilder(sColor + "Click to teleport to " + mColor + attacker.getName() + sColor + "!").create()));
                                        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + attacker.getName()));

                                        allPlayers.spigot().sendMessage(textComponent);
                                    }
                                }
                            }
                        }
                    }

                    if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow && ((Arrow)event.getDamager()).getShooter() instanceof Player) {
                        if (((Player)event.getEntity()).getHealth() - event.getFinalDamage() > 0.0D) {
                            ((Player)((Arrow)event.getDamager()).getShooter()).sendMessage(game.getPrefix() + game.getmColor() + player.getName() + "'s" + game.getsColor() +
                                    " health is at " + ChatColor.RED + Math.round(player.getHealth() - 1) + "❤.");
                        }
                    }
                }
            } else {
                if(!(game.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
                    if(!game.getArenaPlayers().contains(player.getUniqueId())) {
                        event.setCancelled(true);
                    }
                }

                /*
                if(game.isInGrace()) {
                    if(!game.getArenaPlayers().contains(player.getUniqueId())) {
                        event.setCancelled(true);
                    }
                }
                 */
            }
        } else {
            if(event.getDamager() instanceof Player || event.getDamager() instanceof Projectile) {
                Player attacker = (Player) event.getDamager();

                if(game.getSpectators().contains(attacker)) event.setCancelled(true);
                if(!(game.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void handlePlayerPracticeDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if(game.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
            if(game.getArenaPlayers().contains(player.getUniqueId())) {
                if(player.getWorld().getName().equalsIgnoreCase("uhc_practice")) {
                    event.setKeepInventory(true);
                    event.setDeathMessage("");

                    if(player.getKiller() != null) {
                        player.getKiller().sendMessage(prefix + ChatColor.GREEN + "You have slain " + player.getName() + "!");
                        player.sendMessage(prefix + ChatColor.RED + "You have been slain by " + player.getKiller().getName() + "!");

                        player.getKiller().getInventory().addItem(new ItemHandler(Material.GOLDEN_APPLE).
                                setDisplayName(ChatColor.GOLD + "Golden Head").build());
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.spigot().respawn();

                            game.getInventoryHandler().handlePracticeInventory(player);
                            game.getGameManager().scatterPlayer(player, Bukkit.getWorld("uhc_practice"), 49);
                        }
                    }.runTaskLater(game, 20);
                }
            }
        }
    }

    @EventHandler
    public void handlePlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if(game.getGameStateManager().getCurrentGameState() instanceof IngameState) {
            game.registerPlayerDeath(player);

            event.setKeepInventory(false);

            if(player.getKiller() != null) {
                game.getPlayerKills().put(player.getKiller().getUniqueId(), game.getPlayerKills().get(player.getKiller().getUniqueId()) + 1);

                try {
                    if(game.getGameManager().isTeamGame()) {
                        if(game.getTeamNumber().get(player.getKiller().getUniqueId()) != -1) {
                            int teamKills = game.getTeamManager().getTeams().get(game.getTeamNumber().get(player.getKiller().getUniqueId())).getKills();

                            game.getTeamManager().getTeams().get(game.getTeamNumber().get(player.getKiller().getUniqueId())).setKills(teamKills + 1);
                        }
                    }
                } catch (Exception ignored) {}

                PlayerData.getByName(player.getKiller().getName()).setKills(PlayerData.getByName(player.getKiller().getName()).getKills() + 1);


                Player killer = player.getKiller();
                event.setDeathMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + "[" +
                        game.getPlayerKills().get(player.getUniqueId()) + "] " + ChatColor.YELLOW + "was" +
                        " slain by " + ChatColor.RED + killer.getName() + ChatColor.GRAY + "[" + game.getPlayerKills().get(killer.getUniqueId()) + "].");
            } else {
                event.setDeathMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + "[" +
                        game.getPlayerKills().get(player.getUniqueId()) + "] " + ChatColor.YELLOW + "has" +
                        " died mysteriously.");
            }

            if(game.getGameManager().isTeamGame()) {
                if(game.getTeamNumber().get(player.getUniqueId()) != -1) {
                    game.getTeamManager().removePlayerFromTeam(game.getTeamNumber().get(player.getUniqueId()), player.getUniqueId());
                    game.getTeamNumber().put(player.getUniqueId(), -1);
                }
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.spigot().respawn();

                    game.getGameManager().setPlayerState(player, PlayerState.SPECTATOR);

                    game.getWhitelisted().remove(player.getUniqueId());
                    game.getLoggedPlayers().remove(player.getUniqueId());

                    game.getDeadPlayersByUUID().add(player.getUniqueId());

                    if(player.hasPermission("uhc.host")) {
                        game.getInventoryHandler().handleStaffInventory(player);
                    } else {
                        player.getInventory().addItem(new ItemStack(Material.COMPASS)); //Create spec inv handler
                    }

                    PlayerData.getByName(player.getName()).setDeaths(PlayerData.getByName(player.getName()).getDeaths() + 1);

                    game.getGameManager().checkWinner();

                    /*
                    if(!player.hasPermission("uhc.host")) {
                        player.sendMessage(prefix + ChatColor.RED + "You will get kicked in 30 seconds!");

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.kickPlayer(ChatColor.RED + "You are dead! Thank's for playing!");
                            }
                        }.runTaskLater(game, 20 * 30);
                    }
                     */
                }
            }.runTaskLater(game, 20);
        }
    }


    @EventHandler
    public void handleFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();

        if(game.getSpectators().contains(player))event.setCancelled(true);

        if(!(game.getGameStateManager().getCurrentGameState() instanceof IngameState)) {
            event.setCancelled(true);
        } else {
            if (event.getFoodLevel() < player.getFoodLevel() && (new Random()).nextInt(100) > 4)
                event.setCancelled(true);
        }
    }

    /*
    @EventHandler
    public void handleProjectileHitEvent(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(player.getKiller() != null) {
                Player killer = player.getKiller();

                killer.sendMessage(prefix + mColor + player.getName() + "'s " + sColor + "health is at "
                        + ChatColor.DARK_RED + player.getHealthScale() + " ❤" + sColor + ".");
            }
        }
    }
     */



}
