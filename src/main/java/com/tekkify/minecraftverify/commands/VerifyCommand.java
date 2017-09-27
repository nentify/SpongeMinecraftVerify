package com.tekkify.minecraftverify.commands;

import com.tekkify.minecraftverify.MinecraftVerify;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * /verify (code)
 *
 * Command is ran by the user with a code which is sent to the external service to attempt to verify the user.
 */
public class VerifyCommand implements CommandExecutor {

    private MinecraftVerify plugin;

    /**
     * Create a new instance of the command and store an instance of the main class - used to run the verify method.
     *
     * @param plugin Instance of the main plugin class
     */
    public VerifyCommand(MinecraftVerify plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String code = args.<String>getOne("code").get();

        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(TextColors.RED, "Only players may verify themselves."));
            return CommandResult.empty();
        }

        Player player = (Player) src;

        if (code.length() != plugin.getConfig().getCodeLength()) {
            src.sendMessage(Text.of(TextColors.RED, "Verification code should be 8 characters long"));
            return CommandResult.empty();
        }

        plugin.verify(player, code);

        return CommandResult.success();
    }
}
