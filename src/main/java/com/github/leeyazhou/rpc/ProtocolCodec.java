/**
 * 
 */
package com.github.leeyazhou.rpc;

import java.util.List;

import com.github.leeyazhou.rpc.serializer.JdkSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 * @author leeyazhou
 *
 */
public class ProtocolCodec extends ByteToMessageCodec<Message> {

	private final byte PROTOCOL_CODE = (byte) 0;
	private final byte TYPE_REQUEST = (byte) 0;
	private final byte TYPE_RESPONSE = (byte) 1;
	private final JdkSerializer serializer = new JdkSerializer();

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		byte[] headerBytes = serializer.encode(msg.getHeaders());

		out.writeByte(PROTOCOL_CODE);
		out.writeByte(msg.getType());
		out.writeByte((byte) 0);// keeped
		out.writeByte((byte) 0);// keeped

		byte[] body;
		if (msg.isRequest()) {
			body = serializer.encode(msg.getInvocation());
		} else {
			body = serializer.encode(msg.getResponse());
		}

		out.writeInt(msg.id());
		out.writeInt(headerBytes.length);
		out.writeInt(body.length);

		out.writeBytes(headerBytes);
		out.writeBytes(body);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();
		if (in.readableBytes() < 6) {
			in.resetReaderIndex();
			return;
		}

		final byte protocolCode = in.readByte();
		if (protocolCode != PROTOCOL_CODE) {
			in.resetReaderIndex();
			throw new IllegalArgumentException("unsupported protocol : " + protocolCode);
		}

		final byte type = in.readByte();
		if (type != TYPE_REQUEST && type != TYPE_RESPONSE) {
			in.resetReaderIndex();
			throw new IllegalArgumentException("unsupporte message type : " + type);
		}

		in.readByte();// keeped
		in.readByte();// keeped
		final int id = in.readInt();
		final int headerLength = in.readInt();
		final int bodyLength = in.readInt();

		if (in.readableBytes() < headerLength + bodyLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] headerBytes = new byte[headerLength];
		in.readBytes(headerBytes);

		byte[] bodyBytes = new byte[bodyLength];
		in.readBytes(bodyBytes);

		Message msg = new Message().id(id).setType(type);
		msg.setHeaders(serializer.decode(headerBytes));

		if (msg.isRequest()) {
			msg.setInvocation(serializer.decode(bodyBytes));
		} else {
			msg.setResponse(serializer.decode(bodyBytes));
		}
		out.add(msg);

	}

}
