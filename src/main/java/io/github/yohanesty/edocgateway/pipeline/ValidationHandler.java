package io.github.yohanesty.edocgateway.pipeline;

import io.github.yohanesty.edocgateway.config.PartnerProfile;
import io.github.yohanesty.edocgateway.model.DummyMessage;
import io.github.yohanesty.edocgateway.model.ErrorRecord;
import io.github.yohanesty.edocgateway.protocol.DummyMessageCodec;
import io.github.yohanesty.edocgateway.repository.ErrorRecordRepository;
import io.github.yohanesty.edocgateway.service.MessageValidator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ValidationHandler extends SimpleChannelInboundHandler<String> {
    private final PartnerProfile profile;
    private final DummyMessageCodec codec;
    private final MessageValidator validator;
    private final ErrorRecordRepository errorRepository;

    public ValidationHandler(PartnerProfile profile,
                             DummyMessageCodec codec,
                             MessageValidator validator,
                             ErrorRecordRepository errorRepository) {
        this.profile = profile;
        this.codec = codec;
        this.validator = validator;
        this.errorRepository = errorRepository;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String raw) {
        try {
            DummyMessage message = codec.parse(raw);
            validator.validate(message);
            ctx.fireChannelRead(message);
        } catch (Exception e) {
            ErrorRecord record = errorRepository.save(
                    new ErrorRecord(profile.getPartnerId(), raw, e.getMessage()));
            ctx.writeAndFlush("FAIL|" + record.getId() + "\n");
        }
    }
}
