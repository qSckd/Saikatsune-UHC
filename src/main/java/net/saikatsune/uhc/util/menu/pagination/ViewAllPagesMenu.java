package net.saikatsune.uhc.util.menu.pagination;

import lombok.NonNull;
import net.saikatsune.uhc.util.menu.Button;
import net.saikatsune.uhc.util.menu.Menu;
import net.saikatsune.uhc.util.menu.buttons.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ViewAllPagesMenu extends Menu
{
    @NonNull
    PaginatedMenu menu;
    
    @Override
    public String getTitle(final Player player) {
        return "Jump to page";
    }
    
    @Override
    public Map<Integer, Button> getButtons(final Player player) {
        final HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        buttons.put(0, new BackButton(this.menu));
        int index = 10;
        for (int i = 1; i <= this.menu.getPages(player); ++i) {
            buttons.put(index++, new JumpToPageButton(i, this.menu));
            if ((index - 8) % 9 == 0) {
                index += 2;
            }
        }
        return buttons;
    }
    
    @Override
    public boolean isAutoUpdate() {
        return true;
    }
    
    public ViewAllPagesMenu(@NonNull final PaginatedMenu menu) {
        if (menu == null) {
            throw new NullPointerException("menu");
        }
        this.menu = menu;
    }
    
    @NonNull
    public PaginatedMenu getMenu() {
        return this.menu;
    }
}
