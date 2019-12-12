package javafx.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javafx.controller.MainController;
import javafx.stage.FileChooser;

public class Login {

	public String hash;
	public static String token;

	private static Login instance = null;
	
	public static Login getInstance() {
		if(instance == null)
			instance = new Login();
		return instance;
	}
	
	public boolean login(String user, String password) {
		String crypt = crypt(password);
		System.out.println("Crypt = " + crypt);
		String link = String.format("http://localhost:8888/login?username=%s&password=%s", user, crypt); 
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(link);
		
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			
			if(response.getStatusLine().getStatusCode() == 401) {
				return false;
			}
			
			String result = EntityUtils.toString(response.getEntity());
			
			String arr[] = result.split(":\"");
			
			token = arr[2].substring(0, arr[2].length() - 2);
			
			System.out.println("token = " + token);
			
			
			response.close();
			httpclient.close();
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return true;
	}
	
	public boolean report(String token) {
		String link = String.format("http://localhost:8888/reports/bookdetail"); 
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(link);
		//CloseableHttpResponse response = null;
		httpget.setHeader("Authorization", "Bearer " + token);
		
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			
			if(response.getStatusLine().getStatusCode() == 401) {
				return false;
			}
			
			FileChooser fc = new FileChooser();
			fc.setTitle("Save File");
			File file = fc.showSaveDialog(null);
			
		
			if(file != null) {
			  	String value = response.getFirstHeader("Content-Disposition").getValue();
			    String fileName = file.getAbsolutePath();
			    
			    FileOutputStream output = new FileOutputStream(fileName);
			    response.getEntity().writeTo(output);
			    output.close();
			}
			
			response.close();
			httpclient.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public String crypt(String password) {
		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			
			byte[] bytes = sha.digest(password.getBytes(StandardCharsets.UTF_8));
			
			// Convert byte array into signum representation  
	        BigInteger number = new BigInteger(1, bytes);  
	  
	        // Convert message digest into hex value  
	        StringBuilder hexString = new StringBuilder(number.toString(16));  
	  
	        // Pad with leading zeros 
	        while (hexString.length() < 64)  
	        {  
	            hexString.insert(0, '0');  
	        }  
	  
	        return hexString.toString();  
	        
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}		
	}
	
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
