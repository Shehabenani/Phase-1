import java.util.List;

public interface SignupStrategy {
    void setVerificationFacade(UserVerificationFacade facade);
    User getCurrentUser();
    void signup();
    List<? extends User> getUsers();
}
