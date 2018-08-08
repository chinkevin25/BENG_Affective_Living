package ExtraUtils.DataStructure;

/**
 * Created by LinZh on 3/10/2016.
 */
public class UserAccount {
    private String mName;
    private String mEmail;
    private String mCompany;

    public UserAccount(String name, String email, String compay)
    {
        this.mName = name;
        this.mEmail = email;
        this.mCompany = compay;
    }

    public String getName()
    {
        return this.mName;
    }
    public String getEmail()
    {
        return this.mEmail;
    }
    public String getCompany()
    {
        return this.mCompany;
    }
}
