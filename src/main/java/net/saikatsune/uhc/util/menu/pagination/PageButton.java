package net.saikatsune.uhc.util.menu.pagination;

import net.saikatsune.uhc.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class PageButton extends Button
{
    private int mod;
    private PaginatedMenu menu;
    
    @Override
    public void clicked(final Player player, final int i, final ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
            Button.playNeutral(player);
        }
        else if (this.hasNext(player)) {
            this.menu.modPage(player, this.mod);
            Button.playNeutral(player);
        }
        else {
            Button.playFail(player);
        }
    }
    
    private boolean hasNext(final Player player) {
        final int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }
    
    @Override
    public String getName(final Player player) {
        if (!this.hasNext(player)) {
            return (this.mod > 0) ? "&7Last page" : "&7First page";
        }
        final String str = "(&e" + (this.menu.getPage() + this.mod) + "/&e" + this.menu.getPages(player) + "&a)";
        return (this.mod > 0) ? "&a\u27f6" : "&c\u27f5";
    }
    
    @Override
    public List<String> getDescription(final Player player) {
        return new ArrayList<String>();
    }
    
    @Override
    public byte getDamageValue(final Player player) {
        return (byte)(this.hasNext(player) ? 11 : 7);
    }
    
    @Override
    public Material getMaterial(final Player player) {
        return Material.CARPET;
    }
    
    public PageButton(final int mod, final PaginatedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }
}
