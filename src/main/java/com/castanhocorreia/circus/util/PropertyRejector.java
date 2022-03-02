package com.castanhocorreia.circus.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class PropertyRejector {
  public static String[] rejectEmptyValues(Object source) {
    final BeanWrapper target = new BeanWrapperImpl(source);
    PropertyDescriptor[] descriptors = target.getPropertyDescriptors();
    Set<String> rejectedValues = new HashSet<>();
    for (PropertyDescriptor descriptor : descriptors) {
      Object propertyValue = target.getPropertyValue(descriptor.getName());
      if (propertyValue == null || propertyValue == "") rejectedValues.add(descriptor.getName());
    }
    String[] result = new String[rejectedValues.size()];
    return rejectedValues.toArray(result);
  }
}
