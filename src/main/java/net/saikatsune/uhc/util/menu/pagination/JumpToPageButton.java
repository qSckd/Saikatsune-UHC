package net.saikatsune.uhc.util.menu.pagination;

import net.saikatsune.uhc.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class JumpToPageButton extends Button
{
    private int page;
    private PaginatedMenu menu;
    
    @Override
    public String getName(final Player player) {
        return "&ePage " + this.page;
    }
    
    @Override
    public List<String> getDescription(final Player player) {
        return null;
    }
    
    @Override
    public Material getMaterial(final Player player) {
        return Material.BOOK;
    }
    
    @Override
    public int getAmount(final Player player) {
        return this.page;
    }
    
    @Override
    public byte getDamageValue(final Player player) {
        return 0;
    }
    
    @Override
    public void clicked(final Player player, final int i, final ClickType clickType) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        Button.playNeutral(player);
    }
    
    public JumpToPageButton(final int page, final PaginatedMenu menu) {
        this.page = page;
        this.menu = menu;
    }
}
