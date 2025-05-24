package net.saikatsune.uhc.util.menu.buttons;

import net.saikatsune.uhc.util.menu.Button;
import net.saikatsune.uhc.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class BackButton extends Button
{
    private Menu back;
    
    @Override
    public Material getMaterial(final Player player) {
        return Material.BED;
    }
    
    @Override
    public byte getDamageValue(final Player player) {
        return 0;
    }
    
    @Override
    public String getName(final Player player) {
        return "&cGo back";
    }
    
    @Override
    public List<String> getDescription(final Player player) {
        return new ArrayList<String>();
    }
    
    @Override
    public void clicked(final Player player, final int i, final ClickType clickType) {
        Button.playNeutral(player);
        this.back.openMenu(player);
    }
    
    public BackButton(final Menu back) {
        this.back = back;
    }
}
