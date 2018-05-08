package com.github.wenerme.mapstruct;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import lombok.Data;
import lombok.experimental.Accessors;
import org.mapstruct.ap.internal.model.common.Type;
import org.mapstruct.ap.internal.model.source.SourceMethod;
import org.mapstruct.ap.internal.model.source.SourceMethod.Builder;
import org.mapstruct.ap.internal.processor.ModelElementProcessor;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2018/5/8
 */
public class ProtobufMethodCreationProcessor
    implements ModelElementProcessor<List<SourceMethod>, List<SourceMethod>> {

  @Override
  public List<SourceMethod> process(
      ProcessorContext context, TypeElement mapperTypeElement, List<SourceMethod> methods) {
    if (!context.getTypeFactory().isTypeAvailable(Protobufs.PROTOBUF_GENERATED_MESSAGE_V3)) {
      return methods;
    }

    Map<String, MessageContext> messages = new HashMap<>();

    for (SourceMethod method : methods) {
      if (method.isUpdateMethod()) {
        continue;
      }

      Type builderType = method.getReturnType();
      if (Protobufs.isMessageOrBuilder(builderType)) {
        if (Protobufs.isBuilder(builderType)) {

          MessageContext msg = new MessageContext();
          msg.setBuilderType(builderType);

          Type messageType =
              context
                  .getTypeFactory()
                  .getType((TypeElement) builderType.getTypeElement().getEnclosingElement());
          msg.setMessageType(messageType);

          messages.put(msg.getKey(), msg);
        }
      }
    }

    for (MessageContext msg : messages.values()) {
      Type messageType = msg.getMessageType();
      Type builderType = msg.getBuilderType();
      for (Element element : messageType.getTypeElement().getEnclosedElements()) {
        if (element.getSimpleName().contentEquals("newBuilder")) {
          // add newBuilder as ObjectFactory

          ExecutableElement executableElement = (ExecutableElement) element;
          if (executableElement.getParameters().isEmpty()) {
            Builder builder = new SourceMethod.Builder();

            builder
                .setExecutable(executableElement)
                .setReturnType(msg.getBuilderType())
                .setDefininingType(msg.getMessageType())
                .setDeclaringMapper(context.getTypeFactory().getType(mapperTypeElement))
                .setParameters(Collections.emptyList())
                .setExceptionTypes(Collections.emptyList())
                .setTypeFactory(context.getTypeFactory())
                .setMessager(context.getMessager())
                .setTypeUtils(context.getTypeUtils());
            methods.add(builder.build());
          }
          //          else {
          //            Builder builder = new SourceMethod.Builder();
          //
          //            builder
          //                .setExecutable(executableElement)
          //                .setReturnType(msg.getBuilderType())
          //                .setDefininingType(msg.getMessageType())
          //                .setDeclaringMapper(msg.getMessageType())
          //                .setParameters(
          //                    Collections.singletonList(new Parameter("msg",
          // msg.getMessageType())))
          //                .setExceptionTypes(Collections.emptyList())
          //                .setTypeFactory(context.getTypeFactory())
          //                .setMessager(context.getMessager())
          //                .setTypeUtils(context.getTypeUtils());
          //            methods.add(builder.build());
          //          }
        }
        //        else if (element.getSimpleName().contentEquals("toBuilder")) {
        // add builder conversion
        //          ExecutableElement executableElement = (ExecutableElement) element;
        //
        //          Builder builder = new SourceMethod.Builder();
        //
        //          builder
        //              .setExecutable(executableElement)
        //              .setReturnType(msg.getBuilderType())
        //              .setDefininingType(msg.getMessageType())
        ////              .setDeclaringMapper(context.getTypeFactory().getType(mapperTypeElement))
        //              .setDeclaringMapper(msg.getMessageType())
        //              .setParameters(
        //                  Collections.singletonList(new Parameter("message",
        // msg.getMessageType())))
        //              .setExceptionTypes(Collections.emptyList())
        //
        //              .setTypeFactory(context.getTypeFactory())
        //              .setMessager(context.getMessager())
        //              .setTypeUtils(context.getTypeUtils());
        //
        //          SourceMethod sourceMethod = builder.build();
        //          methods.add(sourceMethod);
        //        }
      }

      //      for (Element element : msg.getBuilderType().getTypeElement().getEnclosedElements()) {
      //        if (element.getSimpleName().contentEquals("build")) {
      //          ExecutableElement executableElement = (ExecutableElement) element;
      //
      //
      //          Builder builder = new SourceMethod.Builder();
      //
      //          builder
      //            .setExecutable(new DelegateExecutableElement() {
      //              @Override
      //              public ExecutableElement getExecutableElement() {
      //                return executableElement;
      //              }
      //
      //              @Override
      //              public Name getSimpleName() {
      //                return context.getElementUtils().getName("MyNuilderXX.BB");
      //              }
      //            })
      //            .setReturnType(msg.getMessageType())
      //            .setDefininingType(msg.getBuilderType())
      //            .setDeclaringMapper(context.getTypeFactory().getType(mapperTypeElement))
      //            .setParameters(
      //              Collections.singletonList(new Parameter("b", msg.getBuilderType())))
      //            .setExceptionTypes(Collections.emptyList())
      //            .setTypeFactory(context.getTypeFactory())
      //            .setMessager(context.getMessager())
      //            .setTypeUtils(context.getTypeUtils());
      //
      //          methods.add(builder.build());
      //        }
      //      }
    }

    return methods;
  }

  @Override
  public int getPriority() {
    return 900;
  }

  @Data
  @Accessors(chain = true)
  public static class MessageContext {
    private Type builderType;
    private Type messageType;

    public String getKey() {
      return messageType.getFullyQualifiedName();
    }
  }
}
