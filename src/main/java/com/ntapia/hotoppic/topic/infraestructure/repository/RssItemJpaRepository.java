package com.ntapia.hotoppic.topic.infraestructure.repository;

import com.ntapia.hotoppic.topic.domain.RssItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RssItemJpaRepository extends JpaRepository<RssItem, Long> {

}
