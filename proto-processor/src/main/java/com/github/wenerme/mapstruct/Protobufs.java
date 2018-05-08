package com.github.wenerme.mapstruct;

import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/5/8
 */
public interface Protobufs {
  String PROTOBUF_GENERATED_MESSAGE_V3 = "com.google.protobuf.GeneratedMessageV3";
  String PROTOBUF_GENERATED_MESSAGE_V3_BUILDER = "com.google.protobuf.GeneratedMessageV3.Builder";
  String LIST_SUFFIX = "List";
  String PROTOBUF_ABSTRACT_MESSAGE_BUILDER = "com.google.protobuf.AbstractMessage.Builder";

  static boolean isMessageOrBuilder(org.mapstruct.ap.internal.model.common.Type type) {
    return isMessageOrBuilder(type.getTypeElement());
  }

  static boolean isBuilder(org.mapstruct.ap.internal.model.common.Type type) {
    return isBuilder(type.getTypeElement());
  }

  static boolean isBuilder(Element element) {
    return isMessageOrBuilder(element) && element.getSimpleName().contentEquals("Builder");
  }

  static boolean isMessageOrBuilder(Element element) {
    if (element == null) {
      return false;
    }
    Element e = element.getEnclosingElement();
    if (e != null) {
      if (e.getKind() == ElementKind.CLASS) {
        // Builder or subclass of Builder or Message
        return e.toString().equals("com.google.protobuf.GeneratedMessageV3.Builder")
            || ((TypeElement) e)
                .getSuperclass()
                .toString()
                .startsWith(PROTOBUF_GENERATED_MESSAGE_V3);
      } else if (e.getKind() == ElementKind.INTERFACE) {
        // MessageOrBuilder interface
        List<? extends TypeMirror> interfaces = ((TypeElement) e).getInterfaces();
        for (TypeMirror mirror : interfaces) {
          if (mirror.toString().equals("com.google.protobuf.MessageOrBuilder")) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
