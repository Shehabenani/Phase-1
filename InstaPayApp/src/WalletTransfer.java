import java.util.Scanner;

// WalletTransfer (Concrete Transfer)
public class WalletTransfer implements Transfer {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter receiver phone number:");
        String receiver = scanner.next();

        System.out.println("Enter amount to transfer:");
        double amount = scanner.nextDouble();
        // Implement transfer logic to another wallet
        System.out.println("Transferring $" + amount + " to wallet " + receiver);
        // Additional logic specific to transferring to a wallet

    }
}
