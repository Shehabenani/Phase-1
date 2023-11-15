// TransferFactory (Factory)
public class TransferFactory {
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
