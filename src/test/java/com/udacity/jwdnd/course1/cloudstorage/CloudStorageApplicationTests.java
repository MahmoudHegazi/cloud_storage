package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.controller.SignupController;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
	private static String nextUrl = "";
	private int createdNoteId = -1;
	@LocalServerPort
	private int port;

	private WebDriver driver;


	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;
	private ResultPage resultPage;


	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();



	}

	@BeforeEach
	public void beforeEach() {

		createdNoteId = 0;
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--test-type");
		// start in incognito
		options.addArguments("--incognito");
		options.addArguments("--disable-popup-bloacking");
		DesiredCapabilities chrome = DesiredCapabilities.chrome();
		chrome.setJavascriptEnabled(true);
		options.setCapability(ChromeOptions.CAPABILITY, options);
		//Create driver object for Chrome
		this.driver = new ChromeDriver(options);
		WebDriverWait wait = new WebDriverWait(driver, 3);
		WebDriverManager.chromedriver().setup();

		if (!nextUrl.equals("")) {
			driver.get(nextUrl);
		}
	}

	@AfterEach
	public void afterEach() {

		if (this.driver != null) {
			driver.quit();
		}

	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
		nextUrl = "http://localhost:" + this.port + "/home";
	}

	/** @// TODO: 10/28/2021 add 5 home Access/ Logins and signup Main Tests
	 * 1- Write a Selenium test that verifies that the home page is not accessible without logging in.
	 * 2- add New user and validated the success message (signup)
	 * 3- add invalid user and make sure no redirect and validate error message (signup)
	 * 4- login success add new user then validate success message then click on redirect link from success message and log with that user (login)
	 * 5- login failed try to log with invalid credentials and check error message
	 * **/

	// Write a Selenium test that verifies that the home page is not accessible without logging in.
	@Test
	public void getHomePageWithoutLogin() {
		if (!driver.getCurrentUrl().contains("home")){
			driver.get("http://localhost:" + this.port + "/home");
		}
		Assertions.assertEquals("Login", driver.getTitle());
		Assertions.assertNotEquals("Home", driver.getTitle());
		nextUrl = "http://localhost:" + this.port + "/signup";
	}


	/* Signup and Login Test Start **/

	// (Create New User)
	@Test
	public void addValidUser() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")){
			driver.get("http://localhost:" + port + "/signup");
		}

		int randomNum = (int)(Math.random() * 990000);
		String userName = "user" + randomNum + "_" + (int)(Math.random() * 990000);
		SignupPage signupPage = new SignupPage(driver);

		signupPage.createUser("Python", "king", userName, "myStrongPass");
		Thread.sleep(1000);
		System.out.println(driver.getCurrentUrl());
		assertTrue(driver.getCurrentUrl().contains("login"));
		nextUrl = "http://localhost:" + this.port + "/signup";
	}

	// test add invalid user (duplicated username) add new user then try to add another user with same username
	@Test
	public void addInvalidUser() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")){
			driver.get("http://localhost:" + port + "/signup");
		}

		int randomNum = (int)(Math.random() * 990000);
		String userName = "user" + randomNum + "_" + (int)(Math.random() * 990000);

		SignupPage signupPage = new SignupPage(driver);
		// test add invalid user (first add valid user with username and then check if it accepts same username);

		signupPage.createUser("Python" + randomNum, "king" + randomNum, userName, "apassword");
		//validate the user created
		LoginPage loginPage = new LoginPage(driver);
		assertEquals("You successfully signed up!",loginPage.getSuccessSignupMessage());
		Thread.sleep(2000);
		driver.get("http://localhost:" + port + "/signup");
		// add new user with same username and validate the error message displayed
		signupPage.createUser("Python" + randomNum, "king" + randomNum, userName, "strongpasss");
		assertEquals("The username already exists.",signupPage.getErrorMessage());
		nextUrl = "http://localhost:" + this.port + "/login";
	}


	// (Test Login and redirect to home)
	@Test
	public void loginSuccess() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")){
			driver.get("http://localhost:" + port + "/signup");
		}
		//get random username and pass
		int randomNum = (int)(Math.random() * 990000);
		String userName = "user" + randomNum + "_" + (int)(Math.random() * 990000);
		String password = "password_" + randomNum + "_" + (int)(Math.random() * 990000);


		SignupPage signupPage = new SignupPage(driver);

		signupPage.createUser("Python" + randomNum, "king" + randomNum, userName, password);
		//validate the user created using one time message local
		LoginPage loginPage = new LoginPage(driver);
		assertEquals("You successfully signed up!",loginPage.getSuccessSignupMessage());

		String beforeLogUrl = driver.getCurrentUrl();
		// create new user
		loginPage.logIn(userName, password);
		String afterLogUrl = driver.getCurrentUrl();

		// check url before login and after login
		assertNotEquals(beforeLogUrl, afterLogUrl);

		// make sure after log it redirect to home
		Assertions.assertEquals("Home", driver.getTitle());

	}


	// (Test Login failed and error message and not redirected)
	@Test
	public void loginFailed() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("login")){
			driver.get("http://localhost:" + port + "/login");
		}
		//get random username and pass
		int randomNum = (int)(Math.random() * 990000);
		String userName = "user" + randomNum + "_" + (int)(Math.random() * 990000);
		String password = "password_" + randomNum + "_" + (int)(Math.random() * 990000);
		Thread.sleep(1000);

		LoginPage loginPage = new LoginPage(driver);
		String beforeLogUrl = driver.getCurrentUrl();
		// create new user
		loginPage.logIn(userName, password);
		String afterLogUrl = driver.getCurrentUrl();


		// make sure no redirection happened and the error query parameter added
		assertEquals(beforeLogUrl+"?error", afterLogUrl);

		// check error message
		Assertions.assertEquals("Invalid username or password", loginPage.getErrorMsg());
		nextUrl = "http://localhost:" + this.port + "/signup";

	}


	@Test
	public void loginAndSignout() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")){
			driver.get("http://localhost:" + port + "/signup");
		}
		SignupPage signupPage = new SignupPage(driver);

		int randomNum = (int)(Math.random() * 990000);
		String userName = "user" + randomNum + "_" + (int)(Math.random() * 990000);
		String password = "password_" + randomNum + "_" + (int)(Math.random() * 990000);

		signupPage.createUser("user", "man", userName, password);
		LoginPage loginPage = new LoginPage(driver);
		assertEquals("You successfully signed up!",loginPage.getSuccessSignupMessage());
		String beforeLoginUrl = driver.getCurrentUrl();
		loginPage.logIn(userName, password);
		HomePage homePage = new HomePage(driver);
		homePage.logout();
		String afterLogoutUrl = driver.getCurrentUrl();
		assertNotEquals(beforeLoginUrl, afterLogoutUrl, "Yes We are not at home page after logout.");
		nextUrl = "http://localhost:" + this.port + "/home";
	}
	/* Signup and Login Test End **/


	// test to create new Note after signup and login then confirm the note is created and no error message


	// Function To create 3 notes or more  notes and confirm the title appears
	@Test
	public void loginAndCreateNoteTest() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")) {
			driver.get("http://localhost:" + port + "/signup");
		}
		SignupPage signupPage = new SignupPage(driver);
		String uname = "user" + (int)(Math.random() * 60000);
		String pass = "pass" + (int)(Math.random() * 60000);
		signupPage.createUser("Mr",uname, uname, pass);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.logIn(uname, pass);
		Thread.sleep(1000);
		HomePage homePage = new HomePage(driver);
		homePage.gotToTab("notes");
		// add new random note and msg
		Thread.sleep(1000);
		for (int i=0; i<3; i++){
			Thread.sleep(500);
			NotePage notePage = new NotePage(driver);
			String neNoteTitle = notePage.createRandomNote();
			createdNoteId += 1;

			driver.get("http://localhost:" + port + "/home");
			homePage.gotToTab("notes");
			Thread.sleep(1000);
			assertTrue(notePage.getAllNotesText(driver).contains(neNoteTitle));
		}
	}

	// create 3 Notes and delete them and confirm they deleted
	@Test
	public void deleteNotes() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")) {
			driver.get("http://localhost:" + port + "/signup");
		}
		SignupPage signupPage = new SignupPage(driver);

		String uname = "user" + (int)(Math.random() * 60000);
		String pass = "pass" + (int)(Math.random() * 60000);
		signupPage.createUser("Mr",uname, uname, pass);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.logIn(uname, pass);
		Thread.sleep(1000);
		HomePage homePage = new HomePage(driver);
		homePage.gotToTab("notes");
		// add new random note and msg
		Thread.sleep(1000);
		for (int i=0; i<3; i++){
			Thread.sleep(500);
			NotePage notePage = new NotePage(driver);
			String neNoteTitle = notePage.createRandomNote();
			createdNoteId = 0;
			driver.get("http://localhost:" + port + "/home");
			homePage.gotToTab("notes");
			Thread.sleep(1000);
			assertTrue(notePage.getAllNotesText(driver).contains(neNoteTitle));
			Thread.sleep(1000);
			notePage.deleteNoteById(driver, "note-delete-" + createdNoteId);
			driver.get("http://localhost:" + port + "/home");
			homePage.gotToTab("notes");
			Thread.sleep(1000);
			assertNull(notePage.getElementById(driver, "note-"+createdNoteId));
		}


	}


	// create 1 Notes and update the title and description and confirm that
	@Test
	public void editNote() throws InterruptedException {
		if (!driver.getCurrentUrl().contains("signup")) {
			driver.get("http://localhost:" + port + "/signup");
		}
		SignupPage signupPage = new SignupPage(driver);

		String uname = "user" + (int)(Math.random() * 60000);
		String pass = "pass" + (int)(Math.random() * 60000);
		signupPage.createUser("Mr",uname, uname, pass);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.logIn(uname, pass);
		Thread.sleep(1000);
		HomePage homePage = new HomePage(driver);
		homePage.gotToTab("notes");
		// add new random note and msg
		Thread.sleep(500);
		NotePage notePage = new NotePage(driver);
		String neNoteTitle = notePage.createRandomNote();

		driver.get("http://localhost:" + port + "/home");
		homePage.gotToTab("notes");
		Thread.sleep(500);
		assertTrue(notePage.getAllNotesText(driver).contains(neNoteTitle));
		//notePage.deleteNoteById(driver, "note-delete-" + createdNoteId);
		notePage.openEditNoteModelById(driver, "open-note-edit-" + createdNoteId);
		Thread.sleep(500);

		String newTitle = "hello world new" + (int)(Math.random() * 60000);
		String newDesc = "hello world from Java professional selenium" + (int)(Math.random() * 60000);
		notePage.fillNoteEditTitle(newTitle);
		notePage.fillNoteEditDesc(newDesc);
		notePage.submitEditNote();
		Thread.sleep(1000);
		driver.get("http://localhost:" + port + "/home");
		homePage.gotToTab("notes");
		Thread.sleep(1000);
		assertTrue(notePage.getNotePartList(driver, "title").contains(newTitle));
		assertTrue(notePage.getNotePartList(driver, "description").contains(newDesc));
		assertTrue(notePage.getAllDescriptionText(driver).contains(newDesc));
		}


		/** Note Credentials is Test Driven Part Tests Made and some run first  **/
		// test for add new credentials
	    // make sure also the encryption work and the password not appears
	    @Test
		public void addCredentials() throws InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
			if (!driver.getCurrentUrl().contains("signup")) {
				driver.get("http://localhost:" + port + "/signup");
			}
			SignupPage signupPage = new SignupPage(driver);

			String uname = "user" + (int)(Math.random() * 10000);
			String pass = "MyPassword:_is:" + (int)(Math.random() * 999999999);

			signupPage.createUser("Mr",uname, uname, pass);
			LoginPage loginPage = new LoginPage(driver);
			loginPage.logIn(uname, pass);
			Thread.sleep(1000);
			HomePage homePage = new HomePage(driver);
			homePage.gotToTab("credentials");
			String url = "www.udacity.com";
			CredentialsPage credentialsPage = new CredentialsPage(driver);
			String encodedSalt = credentialsPage.generateNewSecureSalt();
			Thread.sleep(1000);
			credentialsPage.openAddCredentialsModal();
			Thread.sleep(1000);
			credentialsPage.fillAddCredentialsForm(url, uname, pass);
			Thread.sleep(1000);
			if (!driver.getCurrentUrl().contains("home")) {
				driver.get("http://localhost:" + port + "/home");
			}
			Thread.sleep(1000);
			homePage.gotToTab("credentials");
			Thread.sleep(1000);
			assertEquals(uname, credentialsPage.getAllUserNamesStrings().get(0));

		}


	@Test
	public void deleteCredentials() throws InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
		if (!driver.getCurrentUrl().contains("signup")) {
			driver.get("http://localhost:" + port + "/signup");
		}
		SignupPage signupPage = new SignupPage(driver);

		String uname = "user" + (int)(Math.random() * 10000);
		String pass = "MyPassword:_is:" + (int)(Math.random() * 999999999);

		signupPage.createUser("Mr",uname, uname, pass);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.logIn(uname, pass);
		Thread.sleep(1000);
		HomePage homePage = new HomePage(driver);
		homePage.gotToTab("credentials");
		String url = "www.udacity.com";
		CredentialsPage credentialsPage = new CredentialsPage(driver);
		String encodedSalt = credentialsPage.generateNewSecureSalt();
		Thread.sleep(1000);
		credentialsPage.openAddCredentialsModal();
		Thread.sleep(1000);
		credentialsPage.fillAddCredentialsForm(url, uname, pass);
		Thread.sleep(1000);
		if (!driver.getCurrentUrl().contains("home")) {
			driver.get("http://localhost:" + port + "/home");
		}
		homePage.gotToTab("credentials");
		Thread.sleep(1000);
		credentialsPage.deleteElementByIndex(0);
		if (!driver.getCurrentUrl().contains("home")) {
			driver.get("http://localhost:" + port + "/home");
		}
		homePage.gotToTab("credentials");
		Thread.sleep(1000);
		// we added only 1 credentials so if delete success it will no cred so the list of password will be0
		assertEquals(0, credentialsPage.getAllUserPasswordsStrings().size());

	}



	@Test
	public void editCredentials() throws InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
		if (!driver.getCurrentUrl().contains("signup")) {
			driver.get("http://localhost:" + port + "/signup");
		}
		SignupPage signupPage = new SignupPage(driver);


		String uname = "user" + (int)(Math.random() * 10000);
		String pass = "MyPassword:_is:" + (int)(Math.random() * 999999999);

		signupPage.createUser("Mr",uname, uname, pass);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.logIn(uname, pass);
		Thread.sleep(1000);
		HomePage homePage = new HomePage(driver);
		homePage.gotToTab("credentials");
		String url = "www.udacity.com";
		CredentialsPage credentialsPage = new CredentialsPage(driver);
		credentialsPage.setEdit_credentials(driver.findElements(By.className("edit_credentials")));
		String encodedSalt = credentialsPage.generateNewSecureSalt();
		Thread.sleep(1000);
		credentialsPage.openAddCredentialsModal();
		Thread.sleep(1000);
		String username = "user";
		String oldpass = "password";
		credentialsPage.fillAddCredentialsForm(url, username, oldpass);

		// make sure password not appears unencrypted
		assertFalse(credentialsPage.getAllUserPasswordsStrings().contains(oldpass));
		Thread.sleep(10000);
		if (!driver.getCurrentUrl().contains("home")) {
		  driver.get("http://localhost:" + port + "/home");
		}
		Thread.sleep(1000);
		homePage.gotToTab("credentials");
		Thread.sleep(1000);
		String uname1 = "user1";
		String pass1 = "new_password" + (int)(Math.random() * 999999999);
		Thread.sleep(3000);
		credentialsPage.setEdit_credentials(driver.findElements(By.className("edit_credentials")));

		credentialsPage.openEditCredentialsModal(0);
		Thread.sleep(1000);
		credentialsPage.fillEditCredentialsForm(null, uname1, pass1);
		if (!driver.getCurrentUrl().contains("home")) {
			driver.get("http://localhost:" + port + "/home");

		}
		Thread.sleep(1000);
		homePage.gotToTab("credentials");
		Thread.sleep(1000);
		assertFalse(credentialsPage.getAllUserPasswordsStrings().contains(pass1));

	}



	}



