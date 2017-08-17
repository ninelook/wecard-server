package com.ninelook.wecard.server;

import com.ninelook.wecard.common.config.ConfigHandler;
import com.ninelook.wecard.common.config.SpringConfig;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.gateway.handler.NettyServerInitializer;
import com.ninelook.wecard.server.scene.async.AsyncProcessor;
import com.ninelook.wecard.server.scene.home.HomeManager;
import com.ninelook.wecard.server.scene.player.PlayerManager;
import com.ninelook.wecard.server.scene.ticker.TickerService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;


/**
 * 服务启动类
 *
 * User: Simon
 */
public class NServer {
    private static Logger logger = Logger.getLogger(NServer.class);

    public static final boolean SSL = System.getProperty("ssl") != null;

    public static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8087"));


    public void start() {
        //初始服务
        init();

        //启动服务
        run();
    }

    public void init() {
        //启动并设置Spring
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        ctx.registerShutdownHook();
        NContext.setApplicationContext(ctx);

        //初始化房间管理器
        HomeManager homeManager = HomeManager.getInstance();
        homeManager.init();

        //初始化用户管理器
        PlayerManager playerManager = PlayerManager.getInstance();
        playerManager.init();

        //初始化Gateway
        ClientManager clientManager = ClientManager.getInstance();
        clientManager.init();

        //启动异步处理器
        AsyncProcessor asyncProcessor = AsyncProcessor.getInstance();
        asyncProcessor.start();

        //启动滴答服务
        TickerService tickerService = TickerService.getInstance();
        tickerService.start();
    }

    public void run() {
        try {
            // Configure the server.
            logger.warn("server network init...");

            //SSL
            final SslContext sslCtx;
            if (SSL) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

            //通过配置读取端口
            int port = ConfigHandler.getInstance().getSocketServerPort();

            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new NettyServerInitializer(sslCtx));

                //tcp no delay
                bootstrap.option(ChannelOption.TCP_NODELAY, true);

                //设置已经接受后的通道的选项(ServerChannel)
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

                logger.warn("port:" + port);
                logger.warn("server started.");


                bootstrap.bind(port).sync().channel().closeFuture().sync();
            }finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new NServer().start();
    }
}
