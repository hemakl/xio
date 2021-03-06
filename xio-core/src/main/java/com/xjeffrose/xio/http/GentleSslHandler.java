package com.xjeffrose.xio.http;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ssl.OptionalSslHandler;
import io.netty.handler.ssl.SslContext;
import lombok.extern.slf4j.Slf4j;

/**
 * GentleSslHandler will attach an SSL handler to the pipeline if the incoming data is not clear
 * text. Otherwise it will attach a clear text handler. It is expected that the clear text handler
 * will inform the peer to try to communicate again over SSL, then close the connection.
 */
@Slf4j
public class GentleSslHandler extends OptionalSslHandler {

  private final ChannelHandler cleartextHandler;

  /**
   * @param sslContext SslContext context used to build an SslHandler
   * @param cleartextHandler ChannelHandlerAdapter handler attached to the pipeline when a cleartext
   *     connection is received; must be Sharable
   */
  public GentleSslHandler(SslContext sslContext, ChannelHandlerAdapter cleartextHandler) {
    super(sslContext);
    Preconditions.checkArgument(cleartextHandler.isSharable(), "cleartextHandler must be Sharable");
    this.cleartextHandler = cleartextHandler;
  }

  @Override
  protected String newSslHandlerName() {
    return "ssl handler";
  }

  /*
  @Override
  protected SslHandler newSslHandler(ChannelHandlerContext context, SslContext sslContext) {
    return sslContext.newHandler(context.alloc());
  }
  */

  @Override
  protected String newNonSslHandlerName() {
    return "cleartext upgrade handler";
  }

  @Override
  protected ChannelHandler newNonSslHandler(ChannelHandlerContext context) {
    return cleartextHandler;
  }
}
