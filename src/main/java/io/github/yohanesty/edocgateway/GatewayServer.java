package io.github.yohanesty.edocgateway;

import io.github.yohanesty.edocgateway.config.PartnerProfile;
import io.github.yohanesty.edocgateway.pipeline.BusinessHandler;
import io.github.yohanesty.edocgateway.pipeline.LengthPrefixedFrameDecoder;
import io.github.yohanesty.edocgateway.pipeline.ValidationHandler;
import io.github.yohanesty.edocgateway.protocol.DummyMessageCodec;
import io.github.yohanesty.edocgateway.repository.ErrorRecordRepository;
import io.github.yohanesty.edocgateway.service.AsyncRegistrationService;
import io.github.yohanesty.edocgateway.service.MessageValidator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class GatewayServer {
    public static void main(String[] args) throws Exception {
        final String host = "127.0.0.1";
        final int port = 19090;
        final PartnerProfile profile = PartnerProfile.demo();
        final DummyMessageCodec codec = new DummyMessageCodec();
        final ErrorRecordRepository errorRepository = new ErrorRecordRepository();
        final AsyncRegistrationService registrationService = new AsyncRegistrationService();
        final MessageValidator validator = new MessageValidator(profile, codec);

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("frameDecoder", new LengthPrefixedFrameDecoder(profile.getMaxPayloadBytes()));
                            ch.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast("validation", new ValidationHandler(profile, codec, validator, errorRepository));
                            ch.pipeline().addLast("business", new BusinessHandler(registrationService));
                        }
                    });

            System.out.println("Portfolio gateway listening on " + host + ":" + port);
            bootstrap.bind(host, port).sync().channel().closeFuture().sync();
        } finally {
            registrationService.shutdown();
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
