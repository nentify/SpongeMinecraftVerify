package com.tekkify.minecraftverify;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

import java.util.UUID;

/**
 * Emitted when an account was successfully or unsuccessfully verified.
 */
public class MinecraftAccountVerifiedEvent extends AbstractEvent {

    private UUID playerUuid;
    private String code;

    /**
     * Create a new event for a player with the verification code used.
     *
     * @param playerUuid The player's Minecraft UUID.
     * @param code The verification code.
     */
    public MinecraftAccountVerifiedEvent(UUID playerUuid, String code) {
        this.playerUuid = playerUuid;
        this.code = code;
    }

    @Override
    public Cause getCause() {
        return Sponge.getCauseStackManager().getCurrentCause();
    }

    /**
     * Get the player's Minecraft UUID.
     *
     * @return Minecraft player UUID.
     */
    public UUID getPlayerUuid() {
        return playerUuid;
    }

    /**
     * Get the verification used by the player.
     *
     * @return The verification code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Emitted when a user is successfully verified.
     */
    public static class Success extends MinecraftAccountVerifiedEvent {

        private String accountName;

        /**
         * Create a new event.
         *
         * @param playerUuid The player that was verified.
         * @param code The verification code used.
         * @param accountName The account name of the user on the service the plugin verified with.
         */
        public Success(UUID playerUuid, String code, String accountName) {
            super(playerUuid, code);

            this.accountName = accountName;
        }

        /**
         * Get the account name the user verified with on the external service.
         *
         * @return The account name.
         */
        public String getAccountName() {
            return accountName;
        }
    }

    /**
     * Emitted when a user unsuccessfully attempted to verify.
     * Most likely occurs if the code entered was incorrect.
     */
    public static class Fail extends MinecraftAccountVerifiedEvent {

        /**
         * Create a new event.
         *
         * @param playerUuid The player that attempted to verify.
         * @param code The verification code used.
         */
        public Fail(UUID playerUuid, String code) {
            super(playerUuid, code);
        }
    }
}
