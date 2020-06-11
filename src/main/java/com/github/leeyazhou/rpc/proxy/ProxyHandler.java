/**
 * 
 */
package com.github.leeyazhou.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.github.leeyazhou.rpc.Invocation;
import com.github.leeyazhou.rpc.NettyClient;
import com.github.leeyazhou.rpc.URL;

/**
 * @author leeyazhou
 *
 */
public class ProxyHandler implements InvocationHandler {

    private Class<?>    interfaceClass;
    private URL         url;
    private NettyClient client;

    public ProxyHandler(Class<?> interfaceClass, URL url) {
        this.interfaceClass = interfaceClass;
        this.url = url;
        this.client = new NettyClient(this.url.getHost(), this.url.getPort());
        this.client.init();
        this.client.startup();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("toString".equals(method.getName()) && method.getParameterCount() == 0) {
            return toString();
        }

        Invocation invocation = new Invocation().setServiceName(interfaceClass.getName());
        invocation.setServiceMethod(method.getName()).setArgs(args);
        if (args != null) {
            String[] argTypes = new String[method.getParameterTypes().length];
            for (int i = 0; i < method.getParameterTypes().length; i++) {
                argTypes[i] = method.getParameterTypes()[i].getName();
            }
            invocation.setArgTypes(argTypes);
        }

        return this.client.invoke(invocation);
    }

}
