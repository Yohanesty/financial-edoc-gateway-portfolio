package io.github.yohanesty.edocgateway.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class LengthPrefixedFrameDecoder extends ByteToMessageDecoder {
    private final int maxFrameLength;

    public LengthPrefixedFrameDecoder(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) return;
        in.markReaderIndex();
        int length = in.readInt();
        if (length <= 0 || length > maxFrameLength) {
            ctx.close();
            return;
        }
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        out.add(in.readRetainedSlice(length));
    }
}
