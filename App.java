// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

class User {
    protected String name;
    protected String phonenum;
    protected String password;


    public User (String name, String phonenum, String password ){
        this.name=name;
        this.phonenum=phonenum;
        this.password=password;
    }

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getPhonenum() {
        return phonenum;
    }
}

class WalletUser extends User {
    private String walletProvider;

    public WalletUser(String name, String phonenum, String password, String walletProvider) {
        super(name, phonenum, password);
        this.walletProvider = walletProvider;
    }

    public String getWalletProvider() {
        return walletProvider;
    }
}

class BankAccountUser extends User {
    private String bankaccountNum;

    public BankAccountUser(String name, String phonenum, String password, String bankaccountNum) {
        super(name, phonenum, password);
        this.bankaccountNum = bankaccountNum;
    }

    public String getBankaccountNum() {
        return bankaccountNum;
    }
}

interface SignInStrategy {
    boolean authenticate(String username, String password);
}

class AuthenticationUtils {
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

class SignUpContext {
    private SignupStrategy signupStrategy;
    public void setSignupStrategyStrategy(SignupStrategy signupStrategy) {
        this.signupStrategy = signupStrategy;
    }

    public void createaccount() {
        signupStrategy.signup();
    }
}

interface SignupStrategy {
    void setVerificationFacade(UserVerificationFacade facade);
    User getCurrentUser();
    void signup();
    List<? extends User> getUsers();
}

class BankAccount implements SignupStrategy, SignInStrategy{
    private List<BankAccountUser> bankUsers;
    private UserVerificationFacade facade;
    private BankAccountUser currentUser; // Track the current user
    private Scanner scanner;

    public BankAccount(List<BankAccountUser> bankUsers) {
        this.bankUsers = bankUsers;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void setVerificationFacade(UserVerificationFacade facade) {
        this.facade = facade;
    }

    @Override
    public void signup() {
        boolean isUsernameUnique = false;

        while (!isUsernameUnique) {
            System.out.print("Enter Your User Name: ");
            String userName = scanner.nextLine();

            boolean isDuplicate = bankUsers.stream().anyMatch(u -> u.getName().equals(userName));
            if (isDuplicate) {
                System.out.println("Username already exists. Please choose a different one.");
            } else {
                isUsernameUnique = true; // Break the loop when a unique username is provided

                System.out.print("Enter Your Phone Number: ");
                String phoneNumber = scanner.nextLine();

                System.out.print("Enter Your Password: ");
                String password = scanner.nextLine();

                System.out.print("Enter Your Account Number: ");
                String bankAccountNum = scanner.nextLine();

                if (facade != null) {
                    boolean isVerified = facade.verifyUser(bankAccountNum, phoneNumber, "", true);

                    if (!isVerified) {
                        System.out.println("Verification failed. Bank Account User not created.");
                        return; // Exit the signup process if verification fails
                    }
                } else {
                    System.out.println("Facade is not available to verify user.");
                    return; // Exit the signup process if verification cannot be performed
                }

                // If verification passes, proceed with user creation
                BankAccountUser newUser = new BankAccountUser(userName, phoneNumber, password, bankAccountNum);
                bankUsers.add(newUser);

                // Set the current user to the newly signed-up user
                setCurrentUser(newUser);

                System.out.println("Bank Account User signed up and verified successfully!");
            }
        }
    }



    @Override
    public boolean authenticate(String username, String password) {
        int attempts = 0;
        while (attempts < 3) { // Allow only 3 attempts
            BankAccountUser authenticatedUser = authenticateUser(username, password);
            if (authenticatedUser != null) {
                setCurrentUser(authenticatedUser);
                System.out.println("Logged in as: " + authenticatedUser.getName());
                return true; // Exit the method after successful authentication
            }
            System.out.println("Invalid username or password");
            attempts++;
        }
        System.out.println("Too many login attempts. Please try again later.");
        return false;
    }



    // Method to authenticate the user based on username and password
    private BankAccountUser authenticateUser(String username, String password) {
        for (BankAccountUser user : bankUsers) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return user; // Return the authenticated user
            }
        }
        System.out.println("Invalid username or password");
        return null; // Return null if authentication fails
    }

    public void setCurrentUser(BankAccountUser user) {
        this.currentUser = user;
    }

    public List<BankAccountUser> getUsers() {
        return bankUsers;
    }
    @Override
    public BankAccountUser getCurrentUser() {
        return currentUser;
    }

    public UserVerificationFacade getVerificationFacade() {
        return facade;
    }

    public void printBankUsers() {//testing storage purpose
        for (BankAccountUser user : bankUsers) {
            System.out.println("Username: " + user.getName() + ", Phone Number: " + user.getPhonenum() +
                    ", Account Number: " + user.getBankaccountNum());
        }
    }

    // Close the scanner in a controlled manner
    public void closeScanner() {
        scanner.close();
    }

}

class WalletAccount implements SignupStrategy, SignInStrategy{
    private List<WalletUser> walletUsers;
    private WalletUser currentUser; // Track the current user
    private UserVerificationFacade facade;
    private Scanner scanner;

    public WalletAccount(List<WalletUser> walletUsers) {
        this.walletUsers = walletUsers;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void setVerificationFacade(UserVerificationFacade facade) {
        this.facade = facade;
    }

    @Override
    public void signup() {
        boolean isUsernameUnique = false;

        while (!isUsernameUnique) {
            System.out.print("Enter Your User Name: ");
            String userName = scanner.nextLine();

            boolean isDuplicate = walletUsers.stream().anyMatch(u -> u.getName().equals(userName));
            if (isDuplicate) {
                System.out.println("Username already exists. Please choose a different one.");
            } else {
                isUsernameUnique = true; // Break the loop when a unique username is provided

                System.out.print("Enter Your Phone Number: ");
                String phoneNumber = scanner.nextLine();

                System.out.print("Enter Your Password: ");
                String password = scanner.nextLine();

                System.out.println("Choose WalletProvider:");
                System.out.println("CHOOSE [1] FOR VODCASH");
                System.out.println("CHOOSE [2] FOR ETISALATCASH");
                System.out.println("CHOOSE [3] FOR ORANGECASH");
                System.out.println("CHOOSE [4] FOR WEPAY");

                int providerChoice = scanner.nextInt();

                WalletProvider selectedProvider = getWalletProviderByChoice(providerChoice);
                if (selectedProvider == null) {
                    System.out.println("Invalid choice for WalletProvider.");
                    return ;
                }

                if (facade != null) {
                    boolean isVerified = facade.verifyUser("", phoneNumber, selectedProvider.getName(), false);

                    if (!isVerified) {
                        System.out.println("Verification failed. Wallet Account User not created.");
                        return; // Exit the signup process if verification fails
                    }
                } else {
                    System.out.println("Facade is not available to verify user.");
                    return; // Exit the signup process if verification cannot be performed
                }

                // If verification passes, proceed with user creation
                WalletUser newUser = new WalletUser(userName, phoneNumber, password, selectedProvider.getName());
                walletUsers.add(newUser);

                // Set the current user to the newly signed-up user
                setCurrentUser(newUser);

                System.out.println("Wallet Account User signed up and verified successfully!");
            }
        }
    }


    private WalletProvider getWalletProviderByChoice(int choice) {
        switch (choice) {
            case 1:
                return WalletProvider.VODCASH;
            case 2:
                return WalletProvider.ETISALATCASH;
            case 3:
                return WalletProvider.ORANGECASH;
            case 4:
                return WalletProvider.WEPAY;
            default:
                return null;
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        int attempts = 0;
        while (attempts < 3) { // Allow only 3 attempts
            WalletUser authenticatedUser = authenticateUser(username, password);
            if (authenticatedUser != null) {
                setCurrentUser(authenticatedUser);
                System.out.println("Logged in as: " + authenticatedUser.getName());
                return true; // Exit the method after successful authentication
            }
            System.out.println("Invalid username or password");
            attempts++;
        }
        System.out.println("Too many login attempts. Please try again later.");
        return false;
    }
    // Method to authenticate the user based on username and password
    private WalletUser authenticateUser(String username, String password) {
        for (WalletUser user : walletUsers) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return user; // Return the authenticated user
            }
        }
        System.out.println("Invalid username or password");
        return null; // Return null if authentication fails
    }


    public void setCurrentUser(WalletUser user) { // Update to WalletUser
        this.currentUser = user;
    }
    public List<WalletUser> getUsers() {
        return walletUsers;
    }

    public UserVerificationFacade getVerificationFacade() {
        return facade;
    }
    @Override
    public WalletUser getCurrentUser() {
        return currentUser;
    }
    public void printWalletUsers() {
        for (WalletUser user : walletUsers) {
            System.out.println("Username: " + user.getName() + ", Phone Number: " + user.getPhonenum() +
                    ", Wallet Provider: " + user.getWalletProvider());
        }
    }

    public void closeScanner() {
        scanner.close();
    }

}

// UserVerificationFacade (Facade)
class UserVerificationFacade {
    private BankAccountVerification bankAccountVerification;
    private WalletProviderVerification walletProviderVerification;
    private OTPService otpService;
    private OTPGenerator otpGenerator;


    public UserVerificationFacade(
            BankAccountVerification bankAccountVerification,
            WalletProviderVerification walletProviderVerification,
            OTPService otpService,
            OTPGenerator otpGenerator
    ) {
        this.bankAccountVerification = bankAccountVerification;
        this.walletProviderVerification = walletProviderVerification;
        this.otpService = otpService;
        this.otpGenerator = otpGenerator;
    }

    public boolean verifyUser(String accountNumber, String mobileNumber, String walletProvider, boolean isBankUser) {
        // Perform user verification based on the type of user (bank or wallet)
        boolean isVerified = false;
        Scanner scanner = new Scanner(System.in);

        if (isBankUser) {
            isVerified = bankAccountVerification.verifyBankAccount(accountNumber, mobileNumber);
        } else {//if wallet user

            isVerified = walletProviderVerification.verifyWallet(mobileNumber, walletProvider);

        }

        // if both user's is verified, send OTP for mobile number verification
        if (isVerified) {

            String generatedOTP = otpGenerator.generateOTP();
            otpService.sendOTP(mobileNumber, generatedOTP);

            // Wait for user input

            System.out.print("Enter the correct OTP: ");

            String enteredOTP = scanner.nextLine();
            boolean isOTPValid = otpService.verifyOTP(mobileNumber, enteredOTP, generatedOTP);

            if (isOTPValid) {
                System.out.println("User verification successful, and OTP verification successful.");

                return true;

            } else {
                System.out.println("User verification successful, but OTP verification failed.");
            }

        }
        return false;
    }
}

// BankAccountVerification (Strategy)
interface BankAccountVerification {
    boolean verifyBankAccount(String accountNumber, String mobileNumber);//for example calling for API
}

//actual dummy API which returns True
class BankAPI implements BankAccountVerification {
    @Override
    public boolean verifyBankAccount(String accountNumber, String mobileNumber) {
        //returning true for simplicity
        return true;
    }
}

// WalletProviderVerification (Strategy)
interface WalletProviderVerification {
    boolean verifyWallet(String mobileNumber, String walletProvider);//for example calling for API
}

class WalletAPI implements WalletProviderVerification {
    //walletAPI is checking that the number is right and is suitable for the selected once
    private String phoneNumber;

    @Override
    public boolean verifyWallet(String mobileNumber, String walletProvider) {
        //set the instance variable before verification
        this.phoneNumber = mobileNumber;

        if (isValidPhoneNumber()) {
            if (isValidWalletProvider(walletProvider)) {
                System.out.println("Verification successful");
                return true;
            } else {
                System.out.println("Invalid WalletProvider");
            }
        } else {
            System.out.println("Invalid phone number");
        }
        return false;
    }

    private boolean isValidPhoneNumber() {
        return phoneNumber.length() == 11;
    }

    private boolean isValidWalletProvider(String walletProvider) {
        switch (walletProvider.toLowerCase()) {
            case "vodcash":
                return phoneNumber.startsWith("010");
            case "etisalatcash":
                return phoneNumber.startsWith("011");
            case "orangecash":
                return phoneNumber.startsWith("012");
            case "wepay":
                return phoneNumber.startsWith("015");
            default:
                return false;
        }
    }
}

class OTPService {
    public void sendOTP(String phoneNumber, String otp) {
        //appears to the user wait for his input then scan
        System.out.println("OTP Sent to " + phoneNumber + ": " + otp);
    }

    public boolean verifyOTP(String phoneNumber, String enteredOTP, String generatedOTP) {
        System.out.println("Entered OTP: " + enteredOTP);
        System.out.println("Correct OTP: " + generatedOTP);
        return generatedOTP.equals(enteredOTP);
    }
}

//class to generate a random OTP
class OTPGenerator {
    private final int OTP_LENGTH = 4;
    //generates and returns a random four digits
    public String generateOTP() {
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otpBuilder.append(random.nextInt(10));
        }

        return otpBuilder.toString();
    }
}

// TransferType Enum
enum TransferType {
    WALLET,
    INSTAPAY,
    BANK
}

// BillType Enum
enum BillType {
    GAS,         // Gas utility bill
    ELECTRICITY, // Electricity utility bill
    WATER        // Water utility bill
}
enum WalletProvider {
    VODCASH("vodcash", "010"),
    ETISALATCASH("etisalatcash", "011"),
    ORANGECASH("orangecash", "012"),
    WEPAY("wepay", "015");

    private final String name;
    private final String prefix;

    WalletProvider(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }
}

// UtilityBill Interface
interface UtilityBill {
    void createBill();
    void deductFromAccount();
}

// Transfer Interface
interface Transfer {
    void execute();
}

// TransferFactory (Factory)
class TransferFactory {
    public Transfer createTransferToBankUsers(TransferType type, BankAccount bankAccount ) {
        switch (type) {
            case WALLET:
                return new WalletTransfer();
            case INSTAPAY:
                return new InstapayTransfer();
            case BANK:
                // Pass the BankAccount instance to BankTransfer
                return new BankTransfer(bankAccount);//hereeeee
            default:
                throw new IllegalArgumentException("Invalid transfer type");
        }
    }
    public Transfer createTransferToWalletUsers(TransferType type, WalletAccount walletAccount ) {
        switch (type) {
            case WALLET:
                return new WalletTransfer();
            case INSTAPAY:
                return new InstapayTransfer();
            default:
                throw new IllegalArgumentException("Invalid transfer type");
        }
    }
    public UtilityBill createBill(BillType type) {
        switch (type) {
            case GAS:
                return new GasBill();
            case ELECTRICITY:
                return new ElectricityBill();
            case WATER:
                return new WaterBill();
            default:
                throw new IllegalArgumentException("Invalid bill type");
        }
    }
}

// WalletTransfer (Concrete Transfer)
class WalletTransfer implements Transfer {
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

// InstapayTransfer (Concrete Transfer)
class InstapayTransfer implements Transfer {
    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter receiver account:");
        String receiver = scanner.next();

        System.out.println("Enter amount to transfer:");
        double amount = scanner.nextDouble();
        // Implement transfer logic to another instapay account
        System.out.println("Transferring $" + amount  + " to account " + receiver);
        // Additional logic specific to transferring to an account

    }
}

class BankTransfer implements Transfer {
    private final BankAccount bankAccount;

    public BankTransfer(BankAccount bankAccount) {
        this.bankAccount = App.authenticatedBankAccount;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        BankAccountUser currentUser = bankAccount.getCurrentUser();

        if (currentUser != null) {
            System.out.print("Enter the recipient's bank account number: ");
            String recipientAccountNum = scanner.nextLine();

            // Check if the recipient is a valid bank user
            if (isValidBankUser(recipientAccountNum, bankAccount.getUsers())) {
                System.out.print("Enter the amount to transfer: ");
                double amount = scanner.nextDouble();

                //transfer logic
                System.out.println("Transfering " + amount + "\nDone");

            } else {
                System.out.println("Invalid recipient.");
            }
        } else {
            System.out.println("User not authenticated. Please sign in first.");
        }
    }

    // Check if the recipient is a valid bank user
    private boolean isValidBankUser(String recipientAccountNum, List<BankAccountUser> users) {
        for (BankAccountUser user : users) {
            if (user.getBankaccountNum().equals(recipientAccountNum)) {
                return true;
            }
        }
        return false;
    }

}

class GasBill implements UtilityBill {
    private double amount = 40;
    private String ePaymentCode;
    private double currentReading;
    private Scanner scanner;

    public GasBill() {
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void createBill() {
        takeEPaymentCode();
        takeCurrentReading();
        confirmPayment();
    }

    private void takeEPaymentCode() {
        System.out.println("Please enter e-payment code: ");
        ePaymentCode = scanner.nextLine();
    }

    private void takeCurrentReading() {
        System.out.println("Please enter current reading: ");
        while (true) {
            try {
                currentReading = scanner.nextDouble();
                break;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }
    }

    private void confirmPayment() {
        System.out.println("Gas bill created for amount: $" + amount);
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
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }
    }
    @Override
    public void deductFromAccount() {
        System.out.println("Deducting $" + amount + " for gas bill with e-payment code: " + ePaymentCode + " from your account");
    }
}

class ElectricityBill implements UtilityBill {
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

class WaterBill implements UtilityBill {
    private double amount = 249;
    private String ePaymentCode;
    private Scanner scanner;

    public WaterBill() {
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
        System.out.println("Water bill created for amount: $" + amount);
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
        System.out.println("Deducting $" + amount + " for water bill with e-payment code: " + ePaymentCode + " from your account");
    }
}

// BalanceInquiry
class BalanceInquiry {
    public void checkBalance(User user) {
        System.out.println("Balance inquiry for user: " + user.getName() + " 500$");
        //logic to retrieve and display the balance should be here
    }
}

public class App {

    private static List<BankAccountUser> bankUsers = new ArrayList<>(); //store multiple instances
    private static List<WalletUser> walletUsers = new ArrayList<>();
    private static BankAccount bankAccount;
    private static WalletAccount walletAccount;
    public static BankAccount authenticatedBankAccount = null;
    public static WalletAccount authenticatedWalletAccount = null;



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean exitProgram = false;

        while (!exitProgram) {
            System.out.println("Welcome to Instapay! Let's get started.");
            System.out.println("Enter 1 for sign-in");
            System.out.println("Enter 2 for sign-up");
            System.out.println("Enter 3 to exit");

            int choice = scanner.nextInt();


            switch (choice) {
                case 1:
                    signInProcess(scanner);
                    break;
                case 2:
                    signUpProcess(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the program");
                    exitProgram = true;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
        scanner.close();
    }


    private static void signInProcess(Scanner scanner) {
        boolean showMainMenu = false;

        while (!showMainMenu) {
            System.out.println("Enter your choice for sign-in:");
            System.out.println("1. Sign in to Bank Account");
            System.out.println("2. Sign in to Wallet Account");
            System.out.println("3. Go back");

            int signInChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (signInChoice) {
                case 1:
                    signInToBankAccount(scanner);
                    // showMainMenu(scanner, authenticatedBankAccount, null);
                    break;
                case 2:
                    signInToWalletAccount(scanner);
                    // showMainMenu(scanner, null, authenticatedWalletAccount);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid sign-in choice");
            }
        }
    }

    private static void signInToBankAccount(Scanner scanner) {
        System.out.print("Enter Bank Account Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Bank Account Password: ");
        String password = scanner.nextLine();

        // Compare username and password with the bank account data
        for (BankAccountUser bankUser : bankUsers) {
            if (bankUser.getName().equals(username) && bankUser.getPassword().equals(password)) {
                // Assuming 'bankAccount' is the existing instance of the authenticated bank account
                authenticatedBankAccount = bankAccount;
                System.out.println("Logged in to Bank Account as: " + username);
                showMainMenu(scanner, authenticatedBankAccount, null); // Show bank menu
                return; // Exit the method after successful authentication
            }
        }
        System.out.println("Invalid username or password for Bank Account");
    }


    private static void signInToWalletAccount(Scanner scanner) {
        System.out.print("Enter Wallet Account Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Wallet Account Password: ");
        String password = scanner.nextLine();

        // Compare username and password with the wallet account data
        for (WalletUser walletUser : walletUsers) {
            if (walletUser.getName().equals(username) && walletUser.getPassword().equals(password)) {

                authenticatedWalletAccount = walletAccount;
                System.out.println("Logged in to Wallet Account as: " + username);
                showMainMenu(scanner, null, authenticatedWalletAccount); // Show wallet menu
                return; // Exit the method after successful authentication
            }
        }
        System.out.println("Invalid username or password for Wallet Account");
    }


    private static void signUpProcess(Scanner scanner) {
        System.out.println("Select an option:");
        System.out.println("1. Create Bank Account");
        System.out.println("2. Create Wallet Account");

        int signUpChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character after reading the integer input


        switch (signUpChoice) {
            case 1:
                bankAccount = new BankAccount(bankUsers);
                bankAccount.setVerificationFacade(new UserVerificationFacade(
                        new BankAPI(),
                        new WalletAPI(),
                        new OTPService(),
                        new OTPGenerator()
                ));
                bankAccount.signup();
                // bankAccounts.add(bankAccount); // Store the created instance
                // bankAccount.printBankUsers();
                break;
            case 2:
                walletAccount = new WalletAccount(walletUsers);
                walletAccount.setVerificationFacade(new UserVerificationFacade(
                        new BankAPI(),
                        new WalletAPI(),
                        new OTPService(),
                        new OTPGenerator()
                ));
                walletAccount.signup();
                // walletAccounts.add(walletAccount); // Store the created instance
                break;
            default:
                System.out.println("Invalid choice");
        }
    }


    private static void showMainMenu(Scanner scanner, BankAccount bankAccount, WalletAccount walletAccount) {
        boolean showMenu = true;

        while (showMenu) {
            System.out.println("Choose an option:");
            System.out.println("1. Transfer to Wallet using the mobile number");
            System.out.println("2. Transfer to Bank account");
            System.out.println("3. Transfer to Another Instapay account");
            System.out.println("4. Inquire about your balance");
            System.out.println("5. Pay bills");
            System.out.println("6. Exit profile");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input from the scanner
                continue; // Restart the loop
            }

            switch (choice) {
                case 1:
                    if (walletAccount != null) {
                        Transfer walletTransfer = new TransferFactory().createTransferToWalletUsers(TransferType.WALLET, walletAccount);
                        walletTransfer.execute();
                    } else if (bankAccount != null) {
                        Transfer walletTransfer = new TransferFactory().createTransferToBankUsers(TransferType.WALLET, bankAccount);
                        walletTransfer.execute();
                    } else {
                        System.out.println("Wallet transfer not available.");
                    }
                    break;
                case 2:
                    if (bankAccount != null) {
                        Transfer bankTransfer = new TransferFactory().createTransferToBankUsers(TransferType.BANK, bankAccount);
                        bankTransfer.execute();
                    } else {
                        System.out.println("Bank transfer not available.");
                    }
                    break;
                case 3:
                    if (bankAccount != null) {
                        Transfer instapayTransfer = new TransferFactory().createTransferToBankUsers(TransferType.INSTAPAY, bankAccount);
                        instapayTransfer.execute();
                    } else if (walletAccount != null) {
                        Transfer instapayTransfer = new TransferFactory().createTransferToWalletUsers(TransferType.INSTAPAY, walletAccount);
                        instapayTransfer.execute();
                    } else {
                        System.out.println("Instapay transfer not available.");
                    }
                    break;
                case 4:
                    if (bankAccount != null) {
                        BalanceInquiry balanceInquiry = new BalanceInquiry();
                        balanceInquiry.checkBalance(bankAccount.getCurrentUser());
                    } else if (walletAccount != null) {
                        BalanceInquiry balanceInquiry = new BalanceInquiry();
                        balanceInquiry.checkBalance(walletAccount.getCurrentUser());
                    } else {
                        System.out.println("Balance inquiry not available.");
                    }
                    break;
                case 5:
                    payBills(scanner);
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }


    private static void payBills(Scanner scanner) {
        boolean showBillMenu = true;

        while (showBillMenu) {
            System.out.println("Choose a bill to pay:");
            System.out.println("1. Gas bill");
            System.out.println("2. Electricity bill");
            System.out.println("3. Water bill");
            System.out.println("4. Exit bill payment");

            int billChoice = scanner.nextInt();
            UtilityBill bill = null;

            switch (billChoice) {
                case 1:
                    bill = new TransferFactory().createBill(BillType.GAS);
                    break;
                case 2:
                    bill = new TransferFactory().createBill(BillType.ELECTRICITY);
                    break;
                case 3:
                    bill = new TransferFactory().createBill(BillType.WATER);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }

            if (bill != null) {
                bill.createBill();
            }
        }
    }
}
