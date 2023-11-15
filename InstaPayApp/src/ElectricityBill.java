import java.util.Scanner;

public class ElectricityBill implements UtilityBill {
    private double amount = 500;
    private String ePaymentCode;
    private Scanner scanner;

    public ElectricityBill() {
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void createBill() {
        takeEPaymentCode();
        confirmPayment();
    }

    private void takeEPaymentCode() {
        System.out.println("Please enter e-payment code: ");
        ePaymentCode = scanner.nextLine();
    }

    private void confirmPayment() {
        System.out.println("Electricity bill created for amount: $" + amount);
        System.out.println("(1) Confirm (2) Cancel");

        int choice;
        while (true) {
            try {
                choice = scanner.nextInt();
                if (choice == 1) {
                    deductFromAccount();
                    break;
                } else if (choice == 2) {
                    System.out.println("Bill payment cancelled.");
                    return;
                } else {
                    System.out.println("Invalid choice. Please enter 1 to confirm or 2 to cancel.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
                scanner.nextLine();
            }
        }
    }
    @Override
    public void deductFromAccount() {
        System.out.println("Deducting $" + amount + " for electricity bill with e-payment code: " + ePaymentCode + " from your account");
    }
}