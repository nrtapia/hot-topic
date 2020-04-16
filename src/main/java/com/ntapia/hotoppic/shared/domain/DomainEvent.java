package com.ntapia.hotoppic.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
@EqualsAndHashCode(of = "data", callSuper = false)
public class DomainEvent<T> extends ApplicationEvent {

  private T data;

  public DomainEvent(Object source, T data) {
    super(source);
    this.data = data;
  }
}
