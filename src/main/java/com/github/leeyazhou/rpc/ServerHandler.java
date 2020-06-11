/**
 * 
 */
package com.github.leeyazhou.rpc;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author leeyazhou
 *
 */
public class ServerHandler extends SimpleChannelInboundHandler<Message> {
    private static final Logger                    logger       = LoggerFactory.getLogger(ServerHandler.class);
    private volatile ConcurrentMap<String, Object> prossorCache = new ConcurrentHashMap<>();

    public ServerHandler(ConcurrentMap<String, Object> prossorCache) {
        this.prossorCache = prossorCache;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        logger.info("服务器收到请求：" + msg);
        Message message = new Message().id(msg.id()).setType((byte) 1);
        try {
            Object response = doProcess(msg);
            message.setResponse(response);
        } catch (Exception e) {
            message
                .setResponse(e);
            logger.error("", e);
        } finally {
            ctx.channel().writeAndFlush(message);
        }
    }

    private Object doProcess(Message msg) throws Exception {
        Object prossor = prossorCache.get(msg.getInvocation().getServiceName());
        if (prossor == null) {
            throw new IllegalAccessError("no prossor [" + msg.getInvocation().getServiceName() + "] found");
        }

        Class<?>[] argTypes = new Class<?>[msg.getInvocation().getArgTypes().length];
        for (int i = 0; i < msg.getInvocation().getArgTypes().length; i++) {
            argTypes[i] = Class.forName(msg.getInvocation().getArgTypes()[i]);
        }

        Method method = prossor.getClass().getMethod(msg.getInvocation().getServiceMethod(), argTypes);
        Object result = method.invoke(prossor, msg.getInvocation().getArgs());
        logger.info("服务器端处理结果：{}", result);
        return result;
    }

}
