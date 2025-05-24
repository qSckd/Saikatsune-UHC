package net.saikatsune.uhc.tasks;

import net.saikatsune.uhc.Game;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class AnimationUtil {

    public static String loading;

    public static void init() {
        List<String> loadings = Arrays.asList(
                "&2&lMINEWORLD",
                "&a&lM&2&lINEWORLD",
                "&2&lM&a&lI&2&lNEWORLD",
                "&2&lMI&a&lN&2&lEWORLD",
                "&2&lMIN&a&lEWORLD",
                "&2&lMINE&a&lW&2&lORLD",
                "&2&lMINEW&a&lO&2&lRLD",
                "&2&lMINEWO&a&lR&2&lLD",
                "&2&lMINEWOR&a&lL&2&lD",
                "&2&lMINEWORL&a&lD",
                "&a&lMINEWORLD"
        );
        int[] b = {0};
        runTimer(() -> {
            if (b[0] == loadings.size()) b[0] = 0;
            loading = loadings.get(b[0]++);
        }, 0L, (long) 2 * 20L);
    }

    public static String get() {
        return loading;
    }

    public static void run(Runnable runnable) {
        Game.getInstance().getServer().getScheduler().runTask(Game.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Game.getInstance().getServer().getScheduler().runTaskTimer(Game.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Game.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        Game.getInstance().getServer().getScheduler().runTaskLater(Game.getInstance(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Game.getInstance(), runnable);
    }

    public static void runTimerAsync(Runnable runnable, long delay, long timer) {
        Game.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Game.getInstance(), runnable, delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        Game.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Game.getInstance(), runnable, delay, timer);
    }
}
