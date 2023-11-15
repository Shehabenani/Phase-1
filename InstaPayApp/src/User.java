public class User {
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
