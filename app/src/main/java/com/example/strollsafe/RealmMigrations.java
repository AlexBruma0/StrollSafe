package com.example.strollsafe;

import io.realm.DynamicRealm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class RealmMigrations implements RealmMigration {
    private static final int SCHEMA_V_PREV = 1;
    private static final int SCHEMA_V_NOW = 2;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            RealmObjectSchema saveMessage = schema.get("SaveMessage");
            if(saveMessage == null) {
                saveMessage = schema.create("SaveMessage");
            }
            saveMessage.addField("name", Caregiver.class);
            oldVersion++;
        }
    }

    public static RealmConfiguration getDefaultConfig() {
        return new RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_V_NOW)
                .migration(new RealmMigrations())
                .build();
    }
}
