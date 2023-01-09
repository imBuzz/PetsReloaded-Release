package me.imbuzz.dev.petsreloaded.core;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import com.google.common.collect.Maps;
import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import me.imbuzz.dev.petsreloaded.api.IPetsAPI;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommands;
import me.imbuzz.dev.petsreloaded.core.data.DataLoader;
import me.imbuzz.dev.petsreloaded.core.files.FileType;
import me.imbuzz.dev.petsreloaded.core.files.SettingsFile;
import me.imbuzz.dev.petsreloaded.core.hook.ExternalPluginHook;
import me.imbuzz.dev.petsreloaded.core.hook.ImplementedHookType;
import me.imbuzz.dev.petsreloaded.core.listeners.WorldListener;
import me.imbuzz.dev.petsreloaded.core.managers.PetsManager;
import me.imbuzz.dev.petsreloaded.core.nms.INMSHandler;
import me.imbuzz.dev.petsreloaded.core.nms.ServerProtocols;
import me.imbuzz.dev.petsreloaded.core.utils.VariablesContainer;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class PetsReloaded extends JavaPlugin implements IPetsAPI {

    public static final int SPIGOT_CODE = 98113;
    private final Map<ImplementedHookType, ExternalPluginHook> hooks = new HashMap<>();

    private PetsManager petsManager;
    private InventoryManager inventoryManager;

    private PetsCommands petsCommands;
    private VariablesContainer variablesContainer;

    private Metrics metrics;
    private UpdateChecker checker;

    private static PetsReloaded instance;

    private INMSHandler nmsHandler;
    private DataLoader dataLoader;

    @Override
    public void onDisable() {
        if (petsManager != null) petsManager.stop();
    }

    private final Map<FileType, SettingsManager> configurationFiles = Maps.newEnumMap(FileType.class);

    public static PetsReloaded get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        setupFiles();

        if (!setupLoader()) {
            getLogger().severe("Disabled due to an error on the loader type");
            setEnabled(false);
            return;
        }

        nmsHandler = ServerProtocols.getNmsHandler(this);
        if (nmsHandler == null) {
            setEnabled(false);
            return;
        }

        nmsHandler.getEntityHandler().registerEntities();
        getLogger().info("Loaded NMS for Minecraft Server " + ServerProtocols.getServerVersion());

        checkForHooks();

        variablesContainer = new VariablesContainer(this);

        petsManager = new PetsManager(this);
        new WorldListener();

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        petsCommands = new PetsCommands();
        metrics = new Metrics(this, 13336);

        new UpdateChecker(this, UpdateCheckSource.SPIGET, String.valueOf(SPIGOT_CODE))
                .checkEveryXHours(12)
                .setNotifyByPermissionOnJoin("petsreloaded.update")
                .setUserAgent(new UserAgentBuilder().addPluginNameAndVersion())
                .setDownloadLink("https://www.spigotmc.org/resources/petsreloaded-create-your-own-custom-pets-eula-compliant-1-8-x-1-18-x.98113/")
                .checkNow();
    }

    private void checkForHooks() {
        for (ImplementedHookType value : ImplementedHookType.values()) {
            if (value.isEnabled()) {
                ExternalPluginHook hook = value.getSupplier().get();
                hook.init();
                hooks.put(value, hook);
            }
        }

        getLogger().info("Loaded " + hooks.size() + " hooks ");
    }

    public boolean isHookEnabled(ImplementedHookType type) {
        return hooks.containsKey(type);
    }

    public <T extends ExternalPluginHook> T getHook(ImplementedHookType hook) {
        return (T) hooks.get(hook);
    }

    public SettingsManager getSettings() {
        return configurationFiles.get(FileType.CONFIG);
    }

    public SettingsManager getLanguage() {
        return configurationFiles.get(FileType.LANGUAGE);
    }

    public void reload() {
        for (SettingsManager value : configurationFiles.values())
            value.reload();

        variablesContainer = new VariablesContainer(this);
        petsManager.reload();
    }

    private void setupFiles() {
        for (FileType value : FileType.values()) {
            configurationFiles.put(value, SettingsManagerBuilder
                    .withYamlFile(new File(getDataFolder(), value.getFileName()))
                    .configurationData(value.getClazz())
                    .useDefaultMigrationService()
                    .create());
        }
    }

    private boolean setupLoader() {
        try {
            dataLoader = getSettings().getProperty(SettingsFile.LOADER_TYPE).getLoader().get();
            return dataLoader.init();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
