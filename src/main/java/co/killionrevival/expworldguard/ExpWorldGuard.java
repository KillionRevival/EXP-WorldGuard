package co.killionrevival.expworldguard;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import co.killionrevival.expworldguard.handlers.ReqExpHandler;
import co.killionrevival.killioncommons.KillionUtilities;
import co.killionrevival.killioncommons.util.console.ConsoleUtil;
import lombok.Getter;

import com.sk89q.worldguard.session.SessionManager;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class ExpWorldGuard extends JavaPlugin {
    private final String pluginName = "Exp-WorldGuard";

    @Getter
    private static Plugin plugin;
    @Getter
    private static KillionUtilities killionUtilities;
    @Getter
    private static ConsoleUtil myLogger;

    @Getter
    private static IntegerFlag reqExpLvlFlag;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        plugin = this;
        killionUtilities = new KillionUtilities(this);
        myLogger = killionUtilities.getConsoleUtil();

        SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();
        sessionManager.registerHandler(ReqExpHandler.FACTORY, null);

        myLogger.sendSuccess(this.pluginName + " has been enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        myLogger.sendSuccess(this.pluginName + " has been disabled.");
    }

    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            IntegerFlag flag = new IntegerFlag("req-exp-lvl");

            registry.register(flag);
            reqExpLvlFlag = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("req-exp-lvl");
            if (existing instanceof IntegerFlag) {
                reqExpLvlFlag = (IntegerFlag) existing;
            } else {
                myLogger.sendError("Failed to register flag.", e);
            }
        }
    }
}