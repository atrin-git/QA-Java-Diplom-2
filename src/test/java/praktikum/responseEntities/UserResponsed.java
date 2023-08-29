package praktikum.responseEntities;

import lombok.Getter;
import lombok.Setter;
import praktikum.requestEntities.User;

public class UserResponsed {
    @Getter @Setter
    private String success;
    @Getter @Setter
    private User user;
    @Getter @Setter
    private String accessToken;
    @Getter @Setter
    private String refreshToken;
    @Getter @Setter
    private String message;

    public UserResponsed() { }

    public UserResponsed(String success, User user, String accessToken, String refreshToken) {
        this.success = success;
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public UserResponsed(String success, String message) {
        this.success = success;
        this.message = message;
    }
}
