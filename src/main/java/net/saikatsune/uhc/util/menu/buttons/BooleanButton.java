package net.saikatsune.uhc.util.menu.buttons;

import net.saikatsune.uhc.util.menu.Button;
import net.saikatsune.uhc.util.menu.Callback;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class BooleanButton extends Button
{
    private boolean confirm;
    private Callback<Boolean> callback;
    
    @Override
    public void clicked(final Player player, final int i, final ClickType clickType) {
        if (this.confirm) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20.0f, 0.1f);
        }
        else {
            player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 20.0f, 0.1f);
        }
        player.closeInventory();
        this.callback.callback(this.confirm);
    }
    
    @Override
    public String getName(final Player player) {
        return this.confirm ? "&aConfirm" : "&cCancel";
    }
    
    @Override
    public List<String> getDescription(final Player player) {
        return new ArrayList<String>();
    }
    
    @Override
    public byte getDamageValue(final Player player) {
        return (byte)(this.confirm ? 5 : 14);
    }
    
    @Override
    public Material getMaterial(final Player player) {
        return Material.WOOL;
    }
    
    public BooleanButton(final boolean confirm, final Callback<Boolean> callback) {
        this.confirm = confirm;
        this.callback = callback;
    }
}
