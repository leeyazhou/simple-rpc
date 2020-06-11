/**
 * 
 */
package com.github.leeyazhou.rpc;

import java.io.Serializable;

/**
 * @author leeyazhou
 *
 */
public class Invocation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String            serviceName;
    private String            serviceMethod;
    private String[]          argTypes;
    private Object[]          args;

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public Invocation setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    /**
     * @param serviceMetho the serviceMetho to set
     */
    public Invocation setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
        return this;
    }

    /**
     * @return the argTypes
     */
    public String[] getArgTypes() {
        return argTypes;
    }

    /**
     * @param argTypes the argTypes to set
     */
    public Invocation setArgTypes(String[] argTypes) {
        this.argTypes = argTypes;
        return this;
    }

    /**
     * @return the args
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * @param args the args to set
     */
    public Invocation setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
