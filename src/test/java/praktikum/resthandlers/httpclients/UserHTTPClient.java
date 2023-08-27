package praktikum.resthandlers.httpclients;

import io.restassured.response.Response;
import praktikum.ApiUrls;
import praktikum.requestEntities.User;

public class UserHTTPClient extends BaseHTTPClient{
    public Response createUser(User user) {
        return doPostRequest(
                ApiUrls.HOST + ApiUrls.CREATE_USER,
                user,
                "application/json"
        );
    }
    public Response deleteUser(String token) {
        return doDeleteRequest(
                ApiUrls.HOST + ApiUrls.USER,
                token
        );
    }
    public Response loginUser(User user) {
        return doPostRequest(
                ApiUrls.HOST + ApiUrls.LOGIN_USER,
                user,
                "application/json"
        );
    }
    public Response updateUser(User user, String token) {
        return doPatchRequest(
                ApiUrls.HOST + ApiUrls.USER,
                user,
                "application/json",
                token
        );
    }
}
