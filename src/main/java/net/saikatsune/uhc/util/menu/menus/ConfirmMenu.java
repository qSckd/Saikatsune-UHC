package net.saikatsune.uhc.util.menu.menus;

import net.saikatsune.uhc.util.menu.Button;
import net.saikatsune.uhc.util.menu.Callback;
import net.saikatsune.uhc.util.menu.Menu;
import net.saikatsune.uhc.util.menu.buttons.BooleanButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ConfirmMenu extends Menu
{
    private String title;
    private Callback<Boolean> response;
    
    @Override
    public Map<Integer, Button> getButtons(final Player player) {
        final HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        for (int i = 0; i < 9; ++i) {
            if (i == 3) {
                buttons.put(i, new BooleanButton(true, this.response));
            }
            else if (i == 5) {
                buttons.put(i, new BooleanButton(false, this.response));
            }
            else {
                buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte)14, new String[0]));
            }
        }
        return buttons;
    }
    
    @Override
    public String getTitle(final Player player) {
        return this.title;
    }
    
    public ConfirmMenu(final String title, final Callback<Boolean> response) {
        this.title = title;
        this.response = response;
    }
}
