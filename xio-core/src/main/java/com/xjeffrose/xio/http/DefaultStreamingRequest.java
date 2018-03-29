package com.xjeffrose.xio.http;

import brave.Span;
import com.google.auto.value.AutoValue;
import com.xjeffrose.xio.core.internal.UnstableApi;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.ToString;

/** Value class for representing a streaming outgoing HTTP1/2 Request, for use in a client. */
@UnstableApi
@AutoValue
@ToString
public abstract class DefaultStreamingRequest implements StreamingRequest, Traceable {

  @Override
  public boolean startOfStream() {
    return true;
  }

  public abstract HttpMethod method();

  public abstract String path();

  public abstract Headers headers();

  public abstract int streamId();

  @Nullable
  public abstract Span traceSpan();

  /** Not intended to be called. */
  @Override
  public String version() {
    return "";
  }

  @Override
  public boolean keepAlive() {
    return false;
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder method(HttpMethod method);

    public abstract Builder path(String path);

    public abstract Builder headers(Headers headers);

    public abstract Builder streamId(int streamId);

    public abstract Builder traceSpan(Span span);

    public abstract DefaultStreamingRequest build();

    abstract Optional<Headers> headers();

    public Builder host(String host) {
      if (!headers().isPresent()) {
        headers(new DefaultHeaders());
      }

      headers().get().set(HttpHeaderNames.HOST, host);
      return this;
    }
  }

  public static Builder builder() {
    return new AutoValue_DefaultStreamingRequest.Builder().streamId(-1);
  }
}