/**
 * 
 */
package com.github.leeyazhou.rpc.proxy;

import java.lang.reflect.Proxy;

import com.github.leeyazhou.rpc.URL;

/**
 * @author leeyazhou
 *
 */
public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaceClass, URL url) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
            new Class[] { interfaceClass },
            new ProxyHandler(interfaceClass, url));
    }
}
