package me.imbuzz.dev.petsreloaded.core.files;

import ch.jalu.configme.SettingsHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileType {

    CONFIG("settings.yml", SettingsFile.class),
    LANGUAGE("language.yml", LanguageFile.class);

    private final String fileName;
    private final Class<? extends SettingsHolder> clazz;

}
