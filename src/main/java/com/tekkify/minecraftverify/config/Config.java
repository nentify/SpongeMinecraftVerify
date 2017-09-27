package com.tekkify.minecraftverify.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

public class Config {

    @Setting(comment = "The command a player must use to enter the verification code.")
    private String command = "verify";

    @Setting(comment = "The token is a secret shared between the verification endpoint and this plugin. It is sent with the verification POST request to prove the request came from this plugin.")
    private String token = "";

    @Setting(value = "code-length", comment = "The length of the code the user is given to verify themselves.")
    private int codeLength = 10;

    @Setting(comment = "The URI settings are used to build the request to the verification endpoint.")
    private Uri uri = new Uri();

    public String getCommand() {
        return command;
    }

    public String getToken() {
        return token;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public Uri getUri() {
        return uri;
    }

    @ConfigSerializable
    public static class Uri {

        @Setting(comment = "If the web server receiving the verification request uses https, set this to true.")
        private boolean https = false;

        @Setting(comment = "The host of the verification endpoint.")
        private String host = "auth.tekkify.com";

        @Setting(comment = "The path of the endpoint to receive the verification POST request.")
        private String path = "/api/verify";

        public String getScheme() {
            return https ? "http" : "https";
        }

        public String getHost() {
            return host;
        }

        public String getPath() {
            return path;
        }
    }
}
