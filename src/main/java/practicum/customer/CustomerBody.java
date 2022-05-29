 package practicum.customer;


public class CustomerBody {

    String email;
    String password;
    String name;

    public CustomerBody(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Object setEmail(String email){
        this.email = email;
        return this;
    }

    public CustomerBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
