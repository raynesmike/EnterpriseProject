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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Login {
	private static Logger logger = LogManager.getLogger();

	public String hash;
	public static String token;

	private static Login instance = null;
	
	public static Login getInstance() {
		if(instance == null)
			instance = new Login();
		return instance;
	}
	
	public boolean login(String user, String password) {
		String hashPw = crypt(password);
		logger.info("hashPw: " + hashPw);
		String link = String.format("http://localhost:8888/login?username=%s&password=%s", user, hashPw); 
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(link);
		
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			
			if(response.getStatusLine().getStatusCode() == 401) {
				return false;
			}
			String result = EntityUtils.toString(response.getEntity());
			String arr[] = result.split(":\"");
//			for (String i : arr) {
//				logger.debug(i);
//			}

			// This is bad, but whatever.
			// Checks if "response" section is "ok", returns false otherwise.
			String resToken = arr[1].substring(0,2);
			logger.debug(resToken);
			
			token = arr[2].substring(0, arr[2].length() - 2);
			
			logger.info("Token:" + token);

			// Store the session token in MainController, since it's a singleton.
			MainController.setSessionToken(token);
			
			response.close();
			httpclient.close();
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return true;
	}
	
	public static boolean report(String token) {
		String url = String.format("http://localhost:8888/reports/bookdetail"); 
		CloseableHttpClient hClients = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		//CloseableHttpResponse response = null;
		httpget.setHeader("Authorization", "Bearer " + token);
		
		try {
			CloseableHttpResponse res = hClients.execute(httpget);
			
			if(res.getStatusLine().getStatusCode() == 401) {
				return false;
			}
			FileChooser fc = new FileChooser();
			fc.setTitle("Save File");
			File file = fc.showSaveDialog(null);
			
		
			if(file != null) {
			  	String value = res.getFirstHeader("Content-Disposition").getValue();
			    String fileName = file.getAbsolutePath();
			    
			    FileOutputStream output = new FileOutputStream(fileName);
			    res.getEntity().writeTo(output);
			    output.close();
			}
			
			res.close();
			hClients.close();
			
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
			BigInteger num = new BigInteger(1, bytes);  
	  
	        StringBuilder hexCrypt = new StringBuilder(num.toString(16));  
	  
	        while (hexCrypt.length() < 64) {  
	            hexCrypt.insert(0, '0');  
	        }  
	  
	        return hexCrypt.toString();  
	        
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}		
	}
//	
//	public String getHash() {
//		return hash;
//	}
//
//	public void setHash(String hash) {
//		this.hash = hash;
//	}
//
//	public String getToken() {
//		return token;
//	}
//
//	public void setToken(String token) {
//		this.token = token;
//	}
}
