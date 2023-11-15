
public enum WalletProvider {
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
