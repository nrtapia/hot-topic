package com.ntapia.hotoppic.topic.infraestructure.repository;

import com.ntapia.hotoppic.topic.domain.RssItem;
import com.ntapia.hotoppic.topic.domain.TopicAnalyse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicAnalyseJpaRepository extends JpaRepository<TopicAnalyse, String> {

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Rss r SET r.rssItems = :items WHERE r.id = :rssId")
  void updateRssItems(
      @Param("rssId") Long rssId,
      @Param("items") List<RssItem> rssItems
  );
}
