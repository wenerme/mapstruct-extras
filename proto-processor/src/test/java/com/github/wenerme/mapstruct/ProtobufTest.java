package com.github.wenerme.mapstruct;

import com.github.wenerme.mapstruct.UserProtos.UserDTO;
import com.github.wenerme.mapstruct.dto.Address;
import com.github.wenerme.mapstruct.dto.Permission;
import com.github.wenerme.mapstruct.dto.User;
import com.github.wenerme.mapstruct.mapper.UserMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class ProtobufTest {

  @Test
  public void test() throws InvalidProtocolBufferException {
    User user = new User();
    user.setId("");
    user.setEmail("test");
    user.getPermissions().add(Permission.ADMIN);

    UserDTO.Builder dto = UserMapper.INSTANCE.mapBuilder(user);
    UserDTO deserialized = UserDTO.parseFrom(dto.build().toByteArray());
    User back = UserMapper.INSTANCE.map(deserialized);

    Assert.assertEquals("", back.getId());
    Assert.assertEquals("test", back.getEmail());
    Assert.assertTrue(back.getPermissions().contains(Permission.ADMIN));
  }

  @Test
  public void testNested() throws InvalidProtocolBufferException {
    User user = new User();
    user.setId("");
    user.setEmail("test");
    user.setAddresses(
        Arrays.asList(
            new Address().setZip("001").setStreet("st.1"),
            new Address().setZip("002").setStreet("st.2")));

    UserDTO.Builder dto = UserMapper.INSTANCE.mapBuilder(user);
    UserDTO deserialized = UserDTO.parseFrom(dto.build().toByteArray());
    User back = UserMapper.INSTANCE.map(deserialized);

    Assert.assertEquals(2, back.getAddresses().size());
    Assert.assertEquals("001", back.getAddresses().get(0).getZip());
    Assert.assertEquals("st.2", back.getAddresses().get(1).getStreet());
  }

  @Test
  public void testNulls() throws InvalidProtocolBufferException {
    User user = new User();
    // if id is null we should get the default empty string
    user.setEmail("test");
    user.getPermissions().add(Permission.ADMIN);

    UserDTO.Builder dto = UserMapper.INSTANCE.mapBuilder(user);
    UserDTO deserialized = UserDTO.parseFrom(dto.build().toByteArray());
    User back = UserMapper.INSTANCE.map(deserialized);

    Assert.assertEquals("", back.getId());
    Assert.assertEquals("test", back.getEmail());
    Assert.assertTrue(back.getPermissions().contains(Permission.ADMIN));
  }
}
