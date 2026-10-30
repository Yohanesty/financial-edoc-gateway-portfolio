package io.github.yohanesty.edocgateway.pipeline;

import io.github.yohanesty.edocgateway.model.DummyMessage;
import io.github.yohanesty.edocgateway.service.AsyncRegistrationService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class BusinessHandler extends SimpleChannelInboundHandler<DummyMessage> {
    private final AsyncRegistrationService registrationService;

    public BusinessHandler(AsyncRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DummyMessage message) {
        registrationService.register(message);
        ctx.writeAndFlush("OK|" + message.getRequestId() + "\n");
    }
}
