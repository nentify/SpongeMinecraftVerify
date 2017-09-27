package com.tekkify.minecraftverify.config;

import ninja.leaping.configurate.objectmapping.Setting;

public class Config {

    @Setting("The command a player must use to enter the verification code.")
    private String command = "verify";

    @Setting("The token is a secret shared between the verification endpoint and this plugin. It is sent with the verification POST request to prove the request came from this plugin.")
    private String token = "";

    @Setting("The URI settings are used to build the request to the verification endpoint.")
    private Uri uri = new Uri();

    public String getCommand() {
        return command;
    }

    public String getToken() {
        return token;
    }

    public Uri getUri() {
        return uri;
    }

    public static class Uri {

        @Setting("If the web server receiving the verification request uses https, set this to true.")
        private boolean https = false;

        @Setting("The host of the verification endpoint.")
        private String host = "auth.tekkify.com";

        @Setting("The path of the endpoint to receive the verification POST request.")
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
