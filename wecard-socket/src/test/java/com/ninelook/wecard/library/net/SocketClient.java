//package com.ninelook.moko.library.net;
//
//import com.ninelook.wecard.library.common.PushInfoReceiver;
//import com.ninelook.wecard.protocol.Response;
//import io.netty.channel.Channel;
//
//import java.net.InetSocketAddress;
//import java.util.concurrent.TimeUnit;
//
//
//public class SocketClient {
//
//    private long uid = 0;
//    private final String host;
//    private final int port;
//
//    private Channel channel;
//
//    private ClientBootstrap bootstrap;
//
//    private MyPipelineFactory myPipelineFactory;
//
//    public SocketClient(String host, int port) {
//        this.host = host;
//        this.port = port;
//    }
//
//    public SocketClient(String host, int port, long uid) {
//        this.host = host;
//        this.port = port;
//        this.uid = uid;
//    }
//
//    public void run() {
//        try {
//
//            //单个循环池线程数量默认为CORE * 2
//            ClientBootstrap bootstrap = new ClientBootstrap(
//                                                               new NioClientSocketChannelFactory()
//            );
//
//            //tcp no delay
//            bootstrap.setOption("child.tcpNoDelay", true);
//
//            //设置已经接受后的通道的选项(ServerChannel)
//            bootstrap.setOption("child.keepAlive", true);
//
//            this.myPipelineFactory = new MyPipelineFactory();
//
//            bootstrap.setPipelineFactory(myPipelineFactory);
//
//            ChannelFuture f = bootstrap.connect(new InetSocketAddress(host, port));
//
//
//
//            this.bootstrap = bootstrap;
//            this.channel = f.getChannel();
//
////            // Wait until the connection is closed or the connection attempt fails.
////            f.getChannel().getCloseFuture().awaitUninterruptibly();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }
//
//    public ChannelFuture write(Object message) {
//        return channel.write(message);
//    }
//
//    public Response.HeshResMessage read() {
//        Response.HeshResMessage msg = null;
//        try {
//            do{
//                msg = myPipelineFactory.reader.read(5, TimeUnit.SECONDS);
//                if (msg.getPush() == true) {
//                    PushInfoReceiver.getInstance().addPushInfo(this.uid, msg);
//                }
//            } while (msg.getPush() == true);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return msg;
//    }
//
//    public Response.HeshResMessage readFightInfo() {
//        Response.HeshResMessage msg = null;
//        try {
//            do{
//                msg = myPipelineFactory.reader.read(5, TimeUnit.SECONDS);
//            } while (!msg.hasDeadInfo() && !msg.hasFightAttackResultInfo());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return msg;
//    }
//
//    public void close() {
//        this.channel.close();
//        this.bootstrap.releaseExternalResources();
//    }
//
//    public static void main(String[] args) throws Exception {
//        new SocketClient("127.0.0.1", 8000).run();
//    }
//
//    class MyPipelineFactory implements ChannelPipelineFactory {
//        public BlockingReadHandler<Response.HeshResMessage> reader = new BlockingReadHandler<Response.HeshResMessage>();
//
//        public ChannelPipeline getPipeline() throws Exception {
//            ChannelPipeline pipeline = pipeline();
//
//            pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
//            pipeline.addLast("protobufDecoder", new ProtobufDecoder(Response.HeshResMessage.getDefaultInstance()));
//
//            pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
//            pipeline.addLast("protobufEncoder", new ProtobufEncoder());
////            pipeline.addLast("handler", new ClientHandler());
//
//            pipeline.addLast("reader", reader);
//            return pipeline;
//        }
//    }
//
//    class ClientHandler extends SimpleChannelUpstreamHandler {
//
//        @Override
//        public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) {
//            System.err.println(event.getMessage());
//        }
//
//        @Override
//        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
//
//        }
//
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent cause) {
//            cause.getCause().printStackTrace();
//            ctx.getChannel().close();
//        }
//    }
//}
//
//
