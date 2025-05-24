package net.saikatsune.uhc.commands.editor;

import net.saikatsune.uhc.Game;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConfigEditorCommand implements CommandExecutor, Listener {

    private final Game game = Game.getInstance();

    private final String prefix = game.getPrefix();

    private final String mColor = game.getmColor();
    private final String sColor = game.getsColor();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("configeditor")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                game.getInventoryHandler().handleConfigEditorInventory(player);
            }
        }
        return false;
    }

    @EventHandler
    public void handleInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getClickedInventory() != null) {
            if(event.getClickedInventory().getName().equals(mColor + "Config Editor")) {
                if(event.getCurrentItem() != null) {
                    if(event.getCurrentItem().getType().equals(Material.OBSIDIAN)) {
                        if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Nether §7§l➡ §aTRUE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setNether(false);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Nether §7§l➡ §cFALSE")){
                            event.setCancelled(true);
                            game.getConfigManager().setNether(true);
                        } else {
                            return;
                        }
                        game.getInventoryHandler().handleConfigEditorInventory(player);
                    } else if(event.getCurrentItem().getType().equals(Material.SHEARS)) {
                        if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Shears §7§l➡ §aTRUE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setShears(false);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Shears §7§l➡ §cFALSE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setShears(true);
                        } else {
                            return;
                        }
                        game.getInventoryHandler().handleConfigEditorInventory(player);
                    } else if(event.getCurrentItem().getType().equals(Material.SUGAR)) {
                        if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Speed I §7§l➡ §aTRUE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setSpeed1(false);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Speed I §7§l➡ §cFALSE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setSpeed1(true);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Speed II §7§l➡ §aTRUE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setSpeed2(false);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Speed II §7§l➡ §cFALSE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setSpeed2(true);
                        } else {
                            return;
                        }
                        game.getInventoryHandler().handleConfigEditorInventory(player);

                    } else if(event.getCurrentItem().getType().equals(Material.ENDER_PEARL)) {
                        if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Enderpearl Damage §7§l➡ §aTRUE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setEnderpearlDamage(false);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Enderpearl Damage §7§l➡ §cFALSE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setEnderpearlDamage(true);
                        } else {
                            return;
                        }
                        game.getInventoryHandler().handleConfigEditorInventory(player);
                    } else if(event.getCurrentItem().getType().equals(Material.BLAZE_POWDER)) {
                        if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Strength I §7§l➡ §aTRUE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setStrength1(false);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Strength I §7§l➡ §cFALSE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setStrength1(true);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Strength II §7§l➡ §aTRUE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setStrength2(false);
                        } else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(mColor + "Strength II §7§l➡ §cFALSE")) {
                            event.setCancelled(true);
                            game.getConfigManager().setStrength2(true);
                        } else {
                            return;
                        }
                        game.getInventoryHandler().handleConfigEditorInventory(player);
                    } else if(event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.NETHER_STAR) {
                        event.setCancelled(true);
                        game.getInventoryHandler().handleConfigEditorInventory(player);
                    }
                }
            }
        }
    }
}
