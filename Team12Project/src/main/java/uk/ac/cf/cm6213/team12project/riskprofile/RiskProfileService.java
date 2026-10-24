package uk.ac.cf.cm6213.team12project.riskprofile;

public interface RiskProfileService {
    RiskProfile getProfile();
    RiskProfile getProfileForUser(Long userId);
}
