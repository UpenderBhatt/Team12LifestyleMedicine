
package uk.ac.cf.cm6213.team12project.goalroadmap.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.cf.cm6213.team12project.goalroadmap.service.ReflectionInst;
import java.util.Optional;

@Repository
public interface ReflectionRepo extends JpaRepository<ReflectionInst, Long> {
    Optional<ReflectionInst> findByTaskId(Long taskId);
}