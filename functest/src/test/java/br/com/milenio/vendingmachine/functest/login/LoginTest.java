package br.com.milenio.vendingmachine.functest.login;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class LoginTest {
	@Test
	public void testDeveriaRealizarLoginComSucessoAtravesDeUsuarioSenhaValidos() {
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:8080/vendingmachine");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("usuario"));
        element.sendKeys("otavio");
        element = driver.findElement(By.name("senha"));
        element.sendKeys("gudiao");
        element = driver.findElement(By.name("login"));
        
        element = driver.findElement(By.name("btn-login"));
        element.click();
        
        // Check the title of the page
        Assert.assertEquals("Sistema de Gestão de Vending Machines", driver.getTitle());
        
        //Close the browser
        driver.quit();
    }
}
