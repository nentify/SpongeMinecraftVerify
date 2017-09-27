# MinecraftVerify

MinecraftVerify is a small plugin created for the Sponge API to allow Minecraft players to verify themselves with an external service.

## Usage

The external service is expected to generate a code for the player. The player can then run the verification command (default: `/verify <code>`) which sends the code to the external service to complete the verification. When the external service responds, a message is sent to the player and an event is emitted, both depending on if the verification was successful or not.

## Configuation

This plugin is highly configurable through the config file. You must specify a shared, secret token which only the Minecraft server and external service know. This is used to prove that the request came from the Minecraft server. You must also configure the endpoint URI to send the request to.

Optionally, you can change the length of the code the user can send in the command, as well as the command alias itself.

## Events

### Verification Event

A `MinecraftAccountVerifiedEvent` event is emitted when an account verification is completed. This event contains the player's UUID and the code they enterred.

#### Successful Verification Event

If the verification was successful, a `MinecraftAccountVerifiedEvent.Success` event is emitted, also containg the player's account name on the external service (this is returned by the external service, so can be anything).

#### Failed Verification Event

If the verification failed, a `MinecraftAccountVerifiedEvent.Fail` event is emitted. This event contains no further information.