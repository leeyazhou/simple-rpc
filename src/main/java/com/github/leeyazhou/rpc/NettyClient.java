/**
 * 
 */
package com.github.leeyazhou.rpc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author leeyazhou
 *
 */
public class NettyClient {
    private static final Logger                            logger      = LoggerFactory.getLogger(NettyClient.class);
    private static final NioEventLoopGroup                 worker      = new NioEventLoopGroup(0);
    private final String                                   host;
    private final int                                      port;
    private Channel                                        channel;
    private static final ConcurrentMap<Integer, RpcResult> resultCache = new ConcurrentHashMap<>();
    private static final AtomicInteger                     idCreater   = new AtomicInteger();
    private final AtomicBoolean                            init        = new AtomicBoolean();
    private Bootstrap                                      bootstrap;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() {
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class).group(worker);
        bootstrap.option(ChannelOption.SO_REUSEADDR, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocolCodec());
                ch.pipeline().addLast(new ClientHandler(NettyClient.this));
            }
        });
    }

    public void startup() {
        if (init.compareAndSet(false, true)) {
            try {
                ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
                this.channel = channelFuture.channel();
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
    }


    public Object invoke(Invocation invocation) {
        Message msg = new Message().setInvocation(invocation);

        msg.id(idCreater.incrementAndGet());
        this.channel.writeAndFlush(msg);

        RpcResult result = new RpcResult();
        resultCache.put(msg.id(), result);
        return result.getResult();

    }

    public void response(Message msg) {
        RpcResult result = resultCache.get(msg.id());
        if (result != null) {
            result.setResult(msg.getResponse());
        }
    }

    static class RpcResult {
        private Object               result;

        private final CountDownLatch latch = new CountDownLatch(1);

        public Object getResult() {
            try {
                latch.await(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }

        public void setResult(Object result) {
            this.result = result;
            latch.countDown();
        }
    }
}
