import java.util.Scanner;

// UserVerificationFacade (Facade)
public class UserVerificationFacade {
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
