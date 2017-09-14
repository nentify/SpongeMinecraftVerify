package com.tekkify.minecraftverify;

import com.google.inject.Inject;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.util.UUID;

@Plugin(id = MinecraftVerify.ID, name = MinecraftVerify.NAME)
public class MinecraftVerify {

    public static final String ID = "minecraftverify";
    public static final String NAME = "MinecraftVerify";

    @Inject
    private Game game;

    @Inject
    private Logger logger;

    private CloseableHttpClient httpClient;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        CommandSpec verifyCommand = CommandSpec.builder()
                .description(Text.of("Verify your Minecraft account"))
                .permission("")
                .arguments(GenericArguments.string(Text.of("code")))
                .executor(new VerifyCommand())
                .build(); // permission?

        game.getCommandManager().register(this, verifyCommand, "verify");

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

    public void verify(UUID playerUuid, String code) {
        Task.builder()
                .async()
                .name("MinecraftVerify - Sending account verification code [" + code + "]")
                .execute(new VerifyTask(this, httpClient, playerUuid, code))
                .submit(this);
    }
}
