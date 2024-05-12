package org.wksh.core.config;

import github.scarsz.configuralize.DynamicConfig;
import github.scarsz.configuralize.Language;
import org.wksh.core.AnarchyCore;

import java.io.File;
import java.io.IOException;

public class ConfigManager
{
    private final DynamicConfig config;

    public ConfigManager()
    {
        if (!AnarchyCore.plugin().getDataFolder().exists())
        {
            if (!AnarchyCore.plugin().getDataFolder().mkdirs())
            {
                AnarchyCore.logger().warn("Failed to create data folder!");
            }
        }
        config = new DynamicConfig();
        config.addSource(AnarchyCore.class, "config", new File(AnarchyCore.plugin().getDataFolder(), "config.yml"));

        load();
    }

    private void load()
    {
        String languageCode = System.getProperty("user.language").toUpperCase();

        try
        {
            config.setLanguage(Language.valueOf(languageCode));
        } catch (IllegalArgumentException ignored) {}

        if (!config.isLanguageAvailable(config.getLanguage()))
        {
            String lang = languageCode.toUpperCase();
            AnarchyCore.logger().info("Unknown user language " + lang + ".");
            AnarchyCore.logger().info("If you fluently speak " + lang + " as well as English, see the GitHub repo to translate it!");
            config.setLanguage(Language.EN);
        }

        try
        {
            config.saveAllDefaults(false);
        } catch (IOException e)
        {
            throw new RuntimeException("Failed to save default config files", e);
        }

        try
        {
            config.loadAll();
        } catch (Exception e)
        {
            throw new RuntimeException("Failed to load config: ", e);
        }
    }

    public DynamicConfig config()
    {
        return config;
    }
}
