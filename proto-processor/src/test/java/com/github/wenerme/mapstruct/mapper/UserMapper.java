package com.github.wenerme.mapstruct.mapper;

import com.github.wenerme.mapstruct.UserProtos.AddressDTO;
import com.github.wenerme.mapstruct.UserProtos.ContactDTO;
import com.github.wenerme.mapstruct.UserProtos.PermissionDTO;
import com.github.wenerme.mapstruct.UserProtos.UserDTO;
import com.github.wenerme.mapstruct.dto.Address;
import com.github.wenerme.mapstruct.dto.Contact;
import com.github.wenerme.mapstruct.dto.Permission;
import com.github.wenerme.mapstruct.dto.User;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;

@Mapper(
  //  uses = {BuilderFactory.class, ProtobufMapper.class},
  collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserDTO.Builder mapBuilder(User user);

  // TODO auto conversion
  default UserDTO map(UserDTO.Builder s) {
    return s.build();
  }

  User map(UserDTO userDTO);

  AddressDTO.Builder mapBuilder(Address address);

  Address map(AddressDTO address);

  ContactDTO.Builder mapBuilder(Contact s);

  default ContactDTO map(ContactDTO.Builder s) {
    return s.build();
  }

  default AddressDTO map(AddressDTO.Builder s) {
    return s.build();
  }

  @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
  Permission map(PermissionDTO permissionDTO);

  PermissionDTO map(Permission perm);

  interface ProtobufMapper {
    // ProtobufMethodCreationProcessor will add ObjectFactory for Builder
    //    @SuppressWarnings("unchecked")
    //    static <T extends com.google.protobuf.GeneratedMessageV3.Builder<T>> T newBuilder(
    //        @TargetType Class<?> type) {
    //      try {
    //        return (T) type.getDeclaringClass().getMethod("newBuilder").invoke(null);
    //      } catch (Exception e) {
    //        throw new RuntimeException(e);
    //      }
    //    }
  }
  //
  class BuilderFactory {}
}
