package com.ntapia.hotoppic.shared.domain;

import lombok.Value;
import org.springframework.context.ApplicationEvent;

@Value
public class DomainEvent<T> extends ApplicationEvent {

  private T data;

  public DomainEvent(Object source, T data) {
    super(source);
    this.data = data;
  }
}
