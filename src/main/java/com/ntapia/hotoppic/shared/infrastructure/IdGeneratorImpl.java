package com.ntapia.hotoppic.shared.infrastructure;

import com.ntapia.hotoppic.shared.domain.IdGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorImpl implements IdGenerator {

  @Override
  public String generate() {
    return UUID.randomUUID().toString();
  }
}
