/**
 * 
 */
package com.github.leeyazhou.rpc;

/**
 * @author leeyazhou
 *
 */
public class URL {

    private String host;
    private int    port;

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public URL setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public URL setPort(int port) {
        this.port = port;
        return this;
    }

}
