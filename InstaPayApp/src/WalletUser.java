public class WalletUser extends User {
    private String walletProvider;

    public WalletUser(String name, String phonenum, String password, String walletProvider) {
        super(name, phonenum, password);
        this.walletProvider = walletProvider;
    }

    public String getWalletProvider() {
        return walletProvider;
    }
}
