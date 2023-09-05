package com.springboot.spingboottexting.redisConfig;

public class RedisConfig {
    private String host;
    private int port;

    public RedisConfig() {
        // Default configuration
        this.host = "localhost"; // Redis server host
        this.port = 6379; // Redis server port
    }

    public RedisConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
