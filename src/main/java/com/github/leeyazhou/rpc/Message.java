/**
 * 
 */
package com.github.leeyazhou.rpc;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author leeyazhou
 *
 */
public class Message implements Serializable {
    private static final long             serialVersionUID = 1L;
    private int                           id;
    private ConcurrentMap<String, String> headers          = new ConcurrentHashMap<>();
    private byte                          type             = 0;
    private Invocation                    invocation;
    private Object                        response;

    public int id() {
        return id;
    }

    public Message id(int id) {
        this.id = id;
        return this;
    }

    public ConcurrentMap<String, String> getHeaders() {
        return headers;
    }

    public Message setHeaders(ConcurrentMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Message setType(byte type) {
        this.type = type;
        return this;
    }

    public boolean isRequest() {
        return getType() == (byte) 0;
    }

    /**
     * 0- request<br/>
     * 1-response
     * 
     * @return
     */
    public byte getType() {
        return type;
    }

    /**
     * @param response the response to set
     */
    public Message setResponse(Object response) {
        this.response = response;
        return this;
    }

    /**
     * @param invocation the invocation to set
     */
    public Message setInvocation(Invocation invocation) {
        this.invocation = invocation;
        return this;
    }

    /**
     * @return the invocation
     */
    public Invocation getInvocation() {
        return invocation;
    }

    /**
     * 0 - request<br/>
     * 1 - response
     * @return the response
     */
    public Object getResponse() {
        return response;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Message [id=");
        builder.append(id);
        builder.append(", headers=");
        builder.append(headers);
        builder.append("]");
        return builder.toString();
    }

}
