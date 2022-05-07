package praktikim;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private final String email;
    private final String password;
    private final String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        name = null;
    }

    @Step("Создать случайные параметры для пользователя")
    public static User getRandomUser() {
        String userEmail = RandomStringUtils.randomAlphabetic(10) + "@mail.ru";
        String userPassword = RandomStringUtils.randomAlphabetic(10);
        String userName = RandomStringUtils.randomAlphabetic(10);

        Allure.addAttachment("email: ", userEmail);
        Allure.addAttachment("Пароль: ", userPassword);
        Allure.addAttachment("Имя: ", userName);

        return new User(userEmail, userPassword, userName);
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
