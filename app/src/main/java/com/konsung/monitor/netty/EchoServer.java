package com.konsung.monitor.netty;

import android.os.Handler;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by liangkun on 2017/10/12 0012.
 * netty框架服务类，监听端口连接
 */

public class EchoServer {
    // 端口号
    private final int port;
    private Handler handler;
    // 单例模式
    private static EchoServer echoServerInstance;

    /**
     * 构造器，带端口号和handler数据处理
     * @param port 端口号
     * @param handler handler数据处理
     */
    private EchoServer(int port, Handler handler) {
        this.port = port;
        this.handler = handler;
    }

    /**
     * 单例模式
     * @param port 端口号
     * @param handler 数据处理
     * @return EchoServer类实例
     */
    public static EchoServer getEchoServerInstance(int port, Handler handler) {
        if (echoServerInstance == null) {
            echoServerInstance = new EchoServer(port, handler);
        }
        return echoServerInstance;
    }

    /**
     * 启动方法
     * @throws Exception 异常
     */
    public void start() throws Exception {
        //处理NIO操作的多线程事件循环器，netty默认2个线程池，处理channel消息
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
//            引导程序，作为管理Channel的一个辅助类,创建Channel并发起请求
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    // 当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 1024).localAddress(new
                    InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            // 进行数据分包处理,这里的参数是根据具体协议来指定的
                            channel.pipeline().addLast("decoder", new
                                    LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer
                                    .MAX_VALUE, 1, 2, -3, 0, false));
                            channel.pipeline().addLast(new EchoServerDecoder(handler));
                            channel.pipeline().addLast("encoder", new EchoServerEncoder());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
        } finally {

            bossGroup.shutdownGracefully().sync();
            workerGroup.shutdownGracefully().sync();
        }
    }
}
