package me.imbuzz.dev.petsreloaded.core.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.petsreloaded.core.data.mysql.MySQLLoader;
import me.imbuzz.dev.petsreloaded.core.data.sqlite.SQLiteLoader;

import java.util.function.Supplier;

@RequiredArgsConstructor
public enum LoaderType {

    SQLITE(SQLiteLoader::new),
    MYSQL(MySQLLoader::new);

    @Getter
    private final Supplier<DataLoader> loader;

}
