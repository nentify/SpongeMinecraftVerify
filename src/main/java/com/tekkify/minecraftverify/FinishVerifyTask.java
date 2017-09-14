package com.tekkify.minecraftverify;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Finishes the verification process.
 * Must run sync (on the main thread).
 */
public class FinishVerifyTask implements Runnable {

    private UUID playerUuid;
    private String code;
    private String accountName;
    private boolean success;

    /**
     * Create a new instance to to run as a task.
     *
     * @param playerUuid The player's Minecraft UUID
     * @param code The verification code used
     * @param accountName The account name for the service the plugin is verifying with, null if the verification failed.
     * @param success True if the verification was successful.
     */
    public FinishVerifyTask(UUID playerUuid, String code, @Nullable String accountName, boolean success) {
        this.playerUuid = playerUuid;
        this.code = code;
        this.accountName = accountName;
        this.success = success;
    }

    @Override
    public void run() {
        Text message;
        MinecraftAccountVerifiedEvent event;

        if (success) {
            message = Text.of(TextColors.GREEN, "Successfully verified your Minecraft account with the Tekkify account named " + accountName);
            event = new MinecraftAccountVerifiedEvent.Success(playerUuid, code, accountName);
        } else {
            message = Text.of(TextColors.RED, "Failed to verify your account");
            event = new MinecraftAccountVerifiedEvent.Fail(playerUuid, code);
        }

        Sponge.getServer().getPlayer(playerUuid).ifPresent(player -> player.sendMessage(message));
        Sponge.getEventManager().post(event);
    }
}
