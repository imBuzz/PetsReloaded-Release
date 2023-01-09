package me.imbuzz.dev.petsreloaded.core.files;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import com.google.common.collect.Lists;
import me.imbuzz.dev.petsreloaded.core.data.LoaderType;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.*;

public class SettingsFile implements SettingsHolder {

    @Comment({"Disable this option if you are using this plugin in multiple servers all connected to the same database", "because when a player logged out from server A in location B, and log in server C", "pet will be spawned in location B on the server C that probability is not the same location of A"})
    public static final Property<Boolean> SPAWN_ON_LAST_LOCATION = newProperty("options.spawn-to-last-pet-location", true);

    public static final Property<Boolean> CAN_RIDE = newProperty("options.can-ride", true);
    public static final Property<Boolean> ENABLE_REMOVE_NAME_WHEN_RIDING = newProperty("options.enable-riding-name", false);
    public static final Property<Boolean> ENABLE_INTERACT = newProperty("options.enable-pet-interact", true);
    public static final Property<List<String>> DISABLED_WORLDS = newListProperty("options.blacklisted-worlds", Lists.newArrayList());

    public static final Property<Integer> MAX_CHARS_FOR_RENAME = newProperty("options.rename.max-name-length", 20);
    public static final Property<List<String>> BLACKLISTED_WORDS_FROM_RENAME = newListProperty("options.rename.blacklisted-words", Lists.newArrayList());

    @Comment("Choose between SQLITE or MYSQL")
    public static final Property<LoaderType> LOADER_TYPE = newBeanProperty(LoaderType.class, "database.type", LoaderType.SQLITE);

    public static final Property<String> MSQL_HOSTNAME = newProperty("database.mysql.hostname", "localhost");
    public static final Property<Integer> MSQL_PORT = newProperty("database.mysql.port", 3306);
    public static final Property<String> MSQL_DATABASE_NAME = newProperty("database.mysql.databaseName", "petsreloaded");

    @Comment("Do not change this while the plugin is loaded")
    public static final Property<String> MSQL_TABLE = newProperty("database.mysql.tableName", "pets_data");

    public static final Property<String> MSQL_USERNAME = newProperty("database.mysql.username", "root");
    public static final Property<String> MSQL_PASSWORD = newProperty("database.mysql.password", "root");

}
