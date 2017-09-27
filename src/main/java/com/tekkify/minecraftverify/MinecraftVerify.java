package com.tekkify.minecraftverify;

import com.google.inject.Inject;
import com.tekkify.minecraftverify.commands.VerifyCommand;
import com.tekkify.minecraftverify.config.Config;
import com.tekkify.minecraftverify.tasks.VerifyTask;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.io.IOException;

@Plugin(id = MinecraftVerify.ID, name = MinecraftVerify.NAME)
public class MinecraftVerify {

    public static final String ID = "minecraftverify";
    public static final String NAME = "MinecraftVerify";

    @Inject
    private Game game;

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    private ObjectMapper<Config>.BoundInstance configMapper;

    private Config config;
    private CloseableHttpClient httpClient;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        // Prepare config first
        try {
            configMapper = ObjectMapper.forClass(Config.class).bindToNew();
            reloadConfig();
        } catch (ObjectMappingException e) {
            logger.error("Failed to initialise config", e);
        }

        // Register command, command alias comes from config
        CommandSpec verifyCommand = CommandSpec.builder()
                .description(Text.of("Verify your Minecraft account"))
                .permission("")
                .arguments(GenericArguments.string(Text.of("code")))
                .executor(new VerifyCommand(this))
                .build(); // permission?

        game.getCommandManager().register(this, verifyCommand, config.getCommand());

        // Create new HTTP client to be used to send verification requests
        httpClient = HttpClients.createDefault();
    }

    @Listener
    public void onStopping(GameStoppingEvent event) {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        reloadConfig();
    }

    public void verify(User user, String code) {
        Task.builder()
                .async()
                .name("MinecraftVerify - Sending account verification code [" + code + "]")
                .execute(new VerifyTask(this, httpClient, user, code))
                .submit(this);
    }

    public Config getConfig() {
        return config;
    }

    public void reloadConfig() {
        try {
            CommentedConfigurationNode rootConfigNode = configLoader.load(ConfigurationOptions.defaults());
            config = configMapper.populate(rootConfigNode);
        } catch (IOException | ObjectMappingException e) {
            logger.error("Failed to load config", e);
        }
    }
}
