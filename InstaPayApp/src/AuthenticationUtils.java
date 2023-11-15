import java.util.*;

public class AuthenticationUtils {
    static void authenticateUser(List<? extends User> users, String accountType, SignupStrategy strategy) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter Your User Name: ");
            String userName = scanner.nextLine();

            System.out.print("Enter Your Password: ");
            String password = scanner.nextLine();

            for (User user : users) {
                if (user.getName().equals(userName) && user.getPassword().equals(password)) {
                    System.out.println(accountType + " Account Sign-in successful");
                    if (strategy != null) {
                        if (user instanceof BankAccountUser && strategy instanceof BankAccount) {
                            ((BankAccount) strategy).authenticate(userName, password);
                        } else if (user instanceof WalletUser && strategy instanceof WalletAccount) {
                            ((WalletAccount) strategy).authenticate(userName, password);
                        }
                    }
                    return;
                }
            }

            System.out.println("Invalid username or password for " + accountType + " Account");
        }
    }

}
