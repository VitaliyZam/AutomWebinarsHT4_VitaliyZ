package main.tests;

import main.utils.BaseTest;
import main.utils.Properties;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {

    @Test(dataProvider = "admCredentials")
    public void admLogin(String login, String password){
        actions.login(login, password);
    }

    // TODO implement test for product creation
    @Test(dependsOnMethods = "admLogin")
    public void createNewProduct() {
        actions.createProduct(CreateProductTest.product);
    }

    // TODO implement logic to check product visibility on website
    @Test(dependsOnMethods = "createNewProduct")
    public void checkIfNewProductIsCreated() {
        actions.newProductIsCreated();
    }
}
