package use_case.adopt_user_preview;
import entities.Organizations;

public interface AdoptUserPreviewDataAccessInterface {
    boolean existsByName(String identifier);
    void save(Organizations organizations);

    Organizations get(String username);
    Organizations accessApi(String orgID);
}
