package uk.ac.cf.cm6213.team12project.AiImplementation.infoSearchTool;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoPageInfoRepository extends JpaRepository<InfoPageInfo, Long> {
    InfoPageInfo findByUserId(Long userId);
}
