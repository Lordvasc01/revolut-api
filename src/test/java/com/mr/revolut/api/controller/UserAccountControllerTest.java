package com.mr.revolut.api.controller;

import com.google.gson.Gson;
import com.mr.revolut.api.dto.UserAccountDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class UserAccountControllerTest {
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/api");
        server.setHandler(context);
        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                UserAccountController.class.getCanonicalName() + "," + UserTransactionController.class.getCanonicalName());

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RestAssured.baseURI = "http://localhost/api/userAccount";
        RestAssured.port = 8080;
    }

    @AfterAll
    static void afterAll() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllUserAccountsStatusTest() {
        RestAssured.get("/all")
                .then().statusCode(200);
    }

    @Test
    public void findUserAccountStatusTest() {
        RestAssured.get("/1")
                .then().statusCode(200);
    }

    @Test
    public void findNonexistingUserAccountStatusTest() {
        RestAssured.get("/99")
                .then().statusCode(404);
    }

    @Test
    public void findUserAccountBalanceStatusTest() {
        RestAssured.get("/1/balance")
                .then().statusCode(200);
    }

    @Test
    public void findNonexistingUserAccountBalanceStatusTest() {
        RestAssured.get("/99/balance")
                .then().statusCode(404);
    }

    @Test
    public void createNewUserAccountStatusTest() {
        RestAssured.given().contentType(ContentType.JSON).body(new Gson().toJson(new UserAccountDTO("400.00"))).post("/create")
                .then().statusCode(200);
    }

    @Test
    public void withdrawStatusTest() {
        RestAssured.put("/1/withdraw/100")
                .then().statusCode(200);
    }

    @Test
    public void withdrawInsufficientFundsStatusTest() {
        RestAssured.put("/1/withdraw/9999")
                .then().statusCode(400);
    }

    @Test
    public void withdrawWrongUserAccountStatusTest() {
        RestAssured.put("/99/withdraw/200")
                .then().statusCode(400);
    }

    @Test
    public void withdrawNegativeValueStatusTest() {
        RestAssured.put("/1/withdraw/-200")
                .then().statusCode(400);
    }

    @Test
    public void depositStatusTest() {
        RestAssured.put("/1/deposit/100")
                .then().statusCode(200);
    }

    @Test
    public void depositWrongUserAccountStatusTest() {
        RestAssured.put("/99/withdraw/200")
                .then().statusCode(400);
    }

    @Test
    public void depositNegativeValueStatusTest() {
        RestAssured.put("/1/withdraw/-200")
                .then().statusCode(400);
    }


    @Test
    public void deleteUserAccountStatusTest() {
        RestAssured.delete("/3").then().statusCode(200);
    }

    @Test
    public void deleteWrongUserAccountStatusTest() {
        RestAssured.delete("/99").then().statusCode(400);
    }
}