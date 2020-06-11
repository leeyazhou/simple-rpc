/**
 * 
 */
package com.github.leeyazhou.example;

import com.github.leeyazhou.example.service.EchoService;
import com.github.leeyazhou.example.service.EchoServiceImpl;
import com.github.leeyazhou.rpc.NettyServer;

/**
 * @author leeyazhou
 *
 */
public class ProviderMain {

    public static void main(String[] args) {
        NettyServer server = new NettyServer("127.0.0.1", 25001);

        server.registerProssor(EchoService.class.getName(), new EchoServiceImpl());
        server.startup();
    }
}
