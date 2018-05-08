package com.github.wenerme.mapstruct;

import java.beans.Introspector;
import javax.lang.model.element.ExecutableElement;
import org.mapstruct.ap.internal.util.Nouns;
import org.mapstruct.ap.spi.DefaultAccessorNamingStrategy;
import org.mapstruct.ap.spi.MethodType;

/**
 * Protobuf adapter for mapstruct
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 17/11/2017
 */
public class ProtobufAccessorNamingStrategy extends DefaultAccessorNamingStrategy {

  @Override
  public MethodType getMethodType(ExecutableElement element) {
    MethodType type = super.getMethodType(element);
    if (type != MethodType.OTHER && Protobufs.isMessageOrBuilder(element)) {
      String name = super.getPropertyName(element);
      String simpleName = element.getSimpleName().toString();
      // TODO PRESENCE_CHECKER

      switch (name) {
        case "allFields":
        case "unknownFields":
          return MethodType.OTHER;
      }
      // Skip OrBuilder interface process
      if (name.endsWith("OrBuilder")) {
        return MethodType.OTHER;
      } else if (name.endsWith("OrBuilderList")) {
        return MethodType.OTHER;
      } else if (name.endsWith("BuilderList")) {
        return MethodType.OTHER;
      } else if (name.endsWith("ValueList")) {
        // Skip ENUM list
        return MethodType.OTHER;
      } else if (name.endsWith("Bytes")) {
        // Skip string bytes property
        return MethodType.OTHER;
      } else if (name.endsWith("Value")) {
        // Skip enum Value
        return MethodType.OTHER;
      } else if (simpleName.startsWith("addAll")) {
        // Correct to detect the field type
        return MethodType.SETTER;
      }
    }
    return type;
  }

  @Override
  public String getPropertyName(ExecutableElement element) {
    String name = super.getPropertyName(element);
    if (Protobufs.isMessageOrBuilder(element)) {
      String simpleName = element.getSimpleName().toString();
      // No more OrBuilder
      if (name.endsWith("List")) {
        name = name.substring(0, name.length() - "List".length());
      } else if (name.endsWith("Builder")) {
        name = name.substring(0, name.length() - "Builder".length());
      } else if (simpleName.startsWith("addAll")) {
        name = name.substring("all".length());
        name = Introspector.decapitalize(name);
      }
    }
    return name;
  }

  @Override
  public String getElementName(ExecutableElement element) {
    // https://github.com/mapstruct/mapstruct/issues/1336
    String name = super.getElementName(element);
    if (Protobufs.isMessageOrBuilder(element)) {
      name = Nouns.singularize(name);
    }
    return name;
  }
}
