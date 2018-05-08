package com.github.wenerme.mapstruct;

import static org.junit.Assert.assertEquals;

import java.util.ServiceLoader;
import org.junit.Test;
import org.mapstruct.ap.internal.util.Services;
import org.mapstruct.ap.spi.AccessorNamingStrategy;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/5/8
 */
public class ProtobufAccessorNamingStrategyTest {

  @Test
  public void testService() {

    AccessorNamingStrategy strategy =
        ServiceLoader.load(AccessorNamingStrategy.class, Services.class.getClassLoader())
            .iterator()
            .next();
    assertEquals(
        "com.github.wenerme.mapstruct.ProtobufAccessorNamingStrategy",
        strategy.getClass().getCanonicalName());
  }
}
