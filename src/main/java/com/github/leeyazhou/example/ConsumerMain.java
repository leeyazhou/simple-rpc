package com.github.leeyazhou.example;

import com.github.leeyazhou.example.service.EchoService;
import com.github.leeyazhou.rpc.URL;
import com.github.leeyazhou.rpc.proxy.ProxyFactory;

/**
 * 
 * @author leeyazhou
 *
 */
public class ConsumerMain {

    public static void main(String[] args) {
        EchoService echoService = ProxyFactory.getProxy(EchoService.class,
            new URL().setHost("127.0.0.1").setPort(25001));
        System.out.println("结果：" + echoService.echo("test echo"));
        System.out.println("结果：" + echoService.hello("Simple RPC"));
    }
}
