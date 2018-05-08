package com.github.wenerme.mapstruct.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 17/11/2017
 */
@Data
@Accessors(chain = true)
public class Address {

  private String zip;
  private String street;
  private List<Contact> contacts;
}
