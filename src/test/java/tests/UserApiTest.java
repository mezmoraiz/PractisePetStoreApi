
package tests;

import base.BaseTest;
import model.User;
import utils.JsonUtils;
import org.testng.annotations.Test;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.annotations.Listeners;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Listeners(AllureTestNg.class)
public class UserApiTest  extends BaseTest {

    @Test
    public void createUserTest() {

        User user = new User(
                123456,
                "test_user_01",
                "Eduard",
                "Tester",
                "test@example.com",
                "Test12345"
        );

        String json = JsonUtils.toJson(user);

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/user")
                .then()
                .statusCode(200)
                .body("message", equalTo("123456"));
    }

    @Test
    public void loginUserTest() {

        given()
                .queryParam("username", "test_user_01")
                .queryParam("password", "Test12345")
                .when()
                .get("/user/login")
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", org.hamcrest.Matchers.containsString("logged in user session:"));
    }
    @Test
    public void logoutUserTest() {

        given()
                .when()
                .get("/user/logout")
                .then()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", equalTo("ok"));
    }

    @Test(dependsOnMethods = "createUserTest")
    public void getUserTest() {

        given()
                .when()
                .get("/user/test_user_01")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Eduard"))
                .body("username", equalTo("test_user_01"));
    }

    @Test(dependsOnMethods = "getUserTest")
    public void updateUserTest() {

        given()
                .contentType("application/json")
                .body("""
                        {
                            "id": 123456,
                            "username": "test_user_01",
                            "firstName": "Alex",
                            "lastName": "Tester",
                            "email": "test@example.com",
                            "password": "Test12345"
                        }
                        """)
                .when()
                .put("/user/test_user_01")
                .then()
                .statusCode(200)
                .body("message", equalTo("123456"));
    }

    @Test(dependsOnMethods = "updateUserTest")
    public void verifyUpdateUserTest() {

        given()
                .when()
                .get("/user/test_user_01")
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Alex"));
    }

    @Test(dependsOnMethods = "verifyUpdateUserTest")
    public void deleteUserTest() {

        given()
                .when()
                .delete("/user/test_user_01")
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteUserTest")
    public void getDeletedUserTest() {

        given()
                .when()
                .get("/user/test_user_01")
                .then()
                .statusCode(404)
                .body("message", equalTo("User not found"));
    }
}
