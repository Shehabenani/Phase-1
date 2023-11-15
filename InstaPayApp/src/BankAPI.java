
//actual dummy API which returns True
public class BankAPI implements BankAccountVerification {
    @Override
    public boolean verifyBankAccount(String accountNumber, String mobileNumber) {
        //returning true for simplicity 
        return true;
    }
}