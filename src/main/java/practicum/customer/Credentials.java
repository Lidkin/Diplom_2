package practicum.customer;

import org.apache.commons.lang3.RandomStringUtils;


public class Credentials {

    public Credentials() {}

    public String getEmail() {
        return RandomStringUtils.randomAlphanumeric(1, 10) + "@"
                + RandomStringUtils.randomAlphabetic(3, 6) + ".com";
    }

    public String getPassword() {
        return RandomStringUtils.randomAlphanumeric(6, 10);
    }

    public String getName() {
        return RandomStringUtils.randomAlphabetic(1, 10);
    }

 }


