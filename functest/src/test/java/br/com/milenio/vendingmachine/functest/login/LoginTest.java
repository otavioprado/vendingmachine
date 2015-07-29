package br.com.milenio.vendingmachine.functest.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

@Test
public class LoginTest {
	
	private static final String BTN_LOGIN = "btnLogin";
	private static final String SENHA = "senha";
	private static final String USUARIO = "usuario";
	private static final String HTTP_LOCALHOST_8080_VENDINGMACHINE = "http://localhost:8080/vendingmachine";
	private WebElement usuario;
	private WebElement senha;
	private WebElement btnLogin;
	private WebDriver driver;
	private WebElement messages;
	
	@Test
	public void deveriaRealizarLoginComSucessoAtravesDeUsuarioSenhaValidos() {
		driver = new FirefoxDriver();
		driver.get(HTTP_LOCALHOST_8080_VENDINGMACHINE);
		
		usuario = driver.findElement(By.name(USUARIO));
		senha = driver.findElement(By.name(SENHA));
		btnLogin = driver.findElement(By.name(BTN_LOGIN));
		
		usuario.sendKeys("otavio");
		senha.sendKeys("gudiao");
        btnLogin.click();
        
        // Check the title of the page
        AssertJUnit.assertEquals("Sistema de Gestão de Vending Machines", driver.getTitle());
        
        driver.quit();
    }
	
	@Test
	public void deveriaValidarUsuarioInexistente() {
		driver = new FirefoxDriver();
		driver.get(HTTP_LOCALHOST_8080_VENDINGMACHINE);
		
		usuario = driver.findElement(By.name(USUARIO));
		senha = driver.findElement(By.name(SENHA));
		btnLogin = driver.findElement(By.name(BTN_LOGIN));
		
		usuario.sendKeys("ZEH");
		senha.sendKeys("gudiao");
        btnLogin.click();
        messages = driver.findElement(By.id("messages"));
        String mensagem = messages.getText();
        
        Assert.assertEquals(mensagem, "Usuário ZEH não existe cadastrado no sistema.");
        
        driver.quit();
    }
	
	@Test
	public void deveriaValidarUsuarioBloqueado() {
		driver = new FirefoxDriver();
		driver.get(HTTP_LOCALHOST_8080_VENDINGMACHINE);
		
		usuario = driver.findElement(By.name(USUARIO));
		senha = driver.findElement(By.name(SENHA));
		btnLogin = driver.findElement(By.name(BTN_LOGIN));
		
		usuario.sendKeys("bruna");
		senha.sendKeys("gudiao");
        btnLogin.click();
        messages = driver.findElement(By.id("messages"));
        String mensagem = messages.getText();
        
        Assert.assertEquals(mensagem, "Este usuário está bloqueado no sistema. Motivo: Afastamento");
        
        driver.quit();
    }
	
	@Test
	public void deveriaValidarUsuarioSemPermissaoDeLogin() {
		driver = new FirefoxDriver();
		driver.get(HTTP_LOCALHOST_8080_VENDINGMACHINE);
		
		usuario = driver.findElement(By.name(USUARIO));
		senha = driver.findElement(By.name(SENHA));
		btnLogin = driver.findElement(By.name(BTN_LOGIN));
		
		usuario.sendKeys("pedro");
		senha.sendKeys("gudiao");
        btnLogin.click();
        String mensagem;
    	messages = driver.findElement(By.id("accessDenied"));
    	mensagem = messages.getText();

        Assert.assertEquals(mensagem, "Erro: Acesso negado");
        
        driver.quit();
    }
}
