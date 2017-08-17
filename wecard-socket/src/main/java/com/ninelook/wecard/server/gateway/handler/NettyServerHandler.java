package com.ninelook.wecard.server.gateway.handler;

import com.ninelook.wecard.protocol.Communication;
import com.ninelook.wecard.server.NMidConstant;
import com.ninelook.wecard.server.NServer;
import com.ninelook.wecard.server.gateway.auth.Session;
import com.ninelook.wecard.server.gateway.connection.ClientManager;
import com.ninelook.wecard.server.gateway.protobuf.ProtobufDecoder;
import com.ninelook.wecard.server.scene.dispatcher.MainRoute;
import com.ninelook.wecard.server.scene.message.NMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Netty Server Handler
 *
 * @author Simon
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    static Logger logger = LogManager.getLogger(NettyServerHandler.class.getName());

    private WebSocketServerHandshaker handshaker;

    private ProtobufDecoder protobufDecoder = new ProtobufDecoder(Communication.HeshReqMessage.getDefaultInstance());

    private static final String WEBSOCKET_PATH = "/websocket";


    /**
     * 消息派发器
     */
    static MainRoute dispatcher = new MainRoute();

    /**
     * 心跳机制
     * @param ctx
     * @param evt
     * @throws Exception
     */
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;

            String msg = "";
            if (e.state() == IdleState.READER_IDLE) {
                logger.debug("READER_IDLE[" + msg + "]");
                ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
            } else if (e.state() == IdleState.WRITER_IDLE) {
                logger.debug("WRITER_IDLE[" + msg + "]");
                ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
            }
        }
    }

    /**
     * 连接被关闭
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        AttributeKey<Session> attrKeySession = AttributeKey.valueOf("Session");
        Attribute<Session> attrSession = ctx.attr(attrKeySession);
        Session session = attrSession.get();

        logger.warn("client diconnect!");
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.warn("client connect!");

        super.channelActive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * html/webSocket 路由
     * @param ctx
     * @param msg
     */
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof BinaryWebSocketFrame) {
            try {
                Object obj = protobufDecoder.decode(((BinaryWebSocketFrame) msg).content());
                onWebSocketHandle(ctx, (Communication.HeshReqMessage)obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接受http信息
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // Handle a bad request.
        if (!req.getDecoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // Handshake
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(req), null, true);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    /**
     * 返回http信息
     * @param ctx
     * @param req
     * @param res
     */
    private static void sendHttpResponse(
            ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 客户端传入数据
     *
     * @param ctx
     */
    public void onWebSocketHandle(ChannelHandlerContext ctx, Communication.HeshReqMessage heshMsg) {

        try {
            //Session
            AttributeKey<Session> attrKeySession = AttributeKey.valueOf("Session");
            Attribute<Session> attrSession = ctx.attr(attrKeySession);
            Session session = attrSession.get();


            //user id
            long uid = Long.valueOf(heshMsg.getUid());


            //login请求
            if (heshMsg.getMid() == NMidConstant.MESSAGE_ID_SCENE_SYSTEM_LOGIN
                    && session == null ) {
                logger.info("client login... uid:" + uid);

                String sessionKey = "@@";
                session = new Session();
                session.setUid(uid);
                session.setSessionKey(sessionKey);

                //验证当前用户
                if (session.authValid() == true) {

                    //添加用户到client管理器
                    ClientManager.getInstance().facadeRegisterClient(uid, ctx.channel());

                    //当前通道关联session
                    attrSession.set(session);
                } else {
                    String eMsg = "session not exists!";

                    logger.error(eMsg);
                    throw new Exception("session valid failure!");
                }
            }

            //message id
            int mid = heshMsg.getMid();

            //判断是否验证通过
            if ((mid != NMidConstant.MESSAGE_ID_SCENE_SYSTEM_TEST_RESET) && (session == null || !session.isValid())) {
                String eMsg = "session not exists!";

                logger.error(eMsg);
                throw new Exception(eMsg);
            }


            NMessage message = new NMessage(uid, mid, heshMsg, ctx.channel());

            dispatcher.route(message);
        } catch (Exception e) {
            logger.error("NettyMainHandlerchannelRead.channelRead ... " + e.toString());
        }


    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location =  req.headers().get(HOST) + WEBSOCKET_PATH;
        if (NServer.SSL) {
            return "wss://" + location;
        } else {
            return "ws://" + location;
        }
    }

}

