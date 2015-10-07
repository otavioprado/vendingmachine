package br.com.mileniodistribuidora.vendingmachine;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest {
	// Declarando um objeto do tipo WebDriver, utilizado pelo Selenium WebDriver. 
	private static WebDriver driver; // M�todo que inicia tudo que for necess�rio para o teste 
	
	// Cria uma inst�ncia do navegador e abre a p�gina inicial da DevMedia. 
	@BeforeClass 
	public static void setUpTest() {
		driver = new FirefoxDriver();
		driver.get("http://localhost:8080/vendingmachine/"); 
	}
	
	// M�todo que finaliza o teste, fechando a inst�ncia do WebDriver.
	@AfterClass
	public static void tearDownTest() {
		driver.quit(); 
	}
	
	// M�todo que testa o login
	@Test
	public void testaLogin() {
		// Instancia um novo objeto do tipo "WebElement", e passa como par�metro
		// um elemento da tela cujo valor do atributo "name" seja igual a "usuario".
		WebElement element = driver.findElement(By.name("usuario"));
		
		// Insere dados no elemento "usuario".
		element.sendKeys("otavio");
		
		// Atribui ao objeto �element� o elemento de atributo "name" igual a "senha".
		element = driver.findElement(By.name("senha"));
		
		// Insere dados no elemento "senha".
		element.sendKeys("gudiao");
		
		// Clica no bot�o "OK" e submete os dados para concluir o login.
		driver.findElement(By.id("btnLogin")).click();
	}
}
