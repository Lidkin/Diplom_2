 package practicum.customer;


public class CustomerBody {

    private String email;
    private String password;
    private String name;


    public CustomerBody(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public CustomerBody setEmail(String email){
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CustomerBody setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public CustomerBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
