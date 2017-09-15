package com.tekkify.minecraftverify;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.scheduler.Task;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Sends a HTTP POST request to the external service to attempt to verify the user.
 * The result is passed on to the FinishVerifyTask to complete the process.
 * This process should be run async (on a separate thread).
 */
public class VerifyTask implements Runnable {

    private MinecraftVerify plugin;
    private HttpClient httpClient;
    private User user;
    private String code;

    /**
     * Create a new instance of this class to use in a Task.
     *
     * @param plugin An instance of the plugin, used to submit a new sync task.
     * @param httpClient A HttpClient to execute the request on (should be a shared instance).
     * @param user The Minecraft user being verified.
     * @param code The verification code being sent to the external service.
     */
    public VerifyTask(MinecraftVerify plugin, HttpClient httpClient, User user, String code) {
        this.plugin = plugin;
        this.httpClient = httpClient;
        this.user = user;
        this.code = code;
    }

    @Override
    public void run() {
        String accountName = null;
        boolean success = false;

        try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("auth.tekkify.com")
                    .setPath("/api/verify")
                    .setParameter("token", "1234")
                    .setParameter("code", code)
                    .setParameter("mc_uuid", user.getUniqueId().toString())
                    .setParameter("mc_username", user.getName())
                    .build();

            HttpPost httpPost = new HttpPost(uri);

            ResponseHandler<String> responseHandler = new VerifyResponseHandler();

            accountName = httpClient.execute(httpPost, responseHandler);
            success = true;

        } catch (ClientProtocolException e) {
            // Failed to verify the user (probably invalid code, finishes in the finally block)
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // Shouldn't occur
        } finally {
            submitFinishTask(accountName, success);
        }
    }

    /**
     * Submit the FinishVerifyTask to be run in sync (on the main thread) to finish up the verification.
     *
     * @param accountName The account name that was verified (if successful). Will be null if the verification failed.
     * @param success If the verification was successful or not.
     */
    private void submitFinishTask(@Nullable String accountName, boolean success) {
        Task.builder()
                .name("MinecraftVerify - Finishing verification [" + code + "]")
                .execute(new FinishVerifyTask(user.getUniqueId(), code, accountName, success))
                .submit(plugin);
    }
}
