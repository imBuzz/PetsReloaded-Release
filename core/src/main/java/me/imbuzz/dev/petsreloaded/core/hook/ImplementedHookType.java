package me.imbuzz.dev.petsreloaded.core.hook;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.petsreloaded.core.hook.hooks.ModelEngineHook;
import org.bukkit.Bukkit;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter
public enum ImplementedHookType {

    MODEL_ENGINE("ModelEngine", ModelEngineHook::new);

    private final String pluginName;
    private final Supplier<ExternalPluginHook> supplier;

    public boolean isEnabled() {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null && Bukkit.getPluginManager().getPlugin(pluginName).isEnabled();
    }

}
