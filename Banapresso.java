package com.koreait;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Banapresso {

	public static void main(String[] args) {
		String DRIVER_ID = "webdriver.chrome.driver"; // 이름 정해진거다
		String DRIVER_PATH = "D:/smithrowe/jsp/chromedriver.exe";
		
		System.setProperty(DRIVER_ID, DRIVER_PATH);
		WebDriver driver = new ChromeDriver();
		
		String base_url = "https://www.banapresso.com/store";
		ArrayList<String> nicknameList = new ArrayList<>();
		ArrayList<String> storeList = new ArrayList<>();
		ArrayList<String> addressList = new ArrayList<>();
		try {
			driver.get(base_url);
			List<WebElement> list1 = driver.findElements(By.cssSelector("span.store_name_map > b"));
			List<WebElement> list2 = driver.findElements(By.cssSelector("span.store_name_map > i"));
			List<WebElement> list3 = driver.findElements(By.cssSelector("span.store_name_map > span:nth-child(3)"));
			for(WebElement el : list1) {
				nicknameList.add(String.valueOf(el.getText()));
			}
			for(WebElement el : list2) {
				storeList.add(String.valueOf(el.getText()));
			}
			for(WebElement el : list3) {
				addressList.add(String.valueOf(el.getText()));
			}
			int i = 2;
			while(true) {
				try {
					WebElement nextPage;
					if(i != 1) {
						nextPage = driver.findElement(By.cssSelector("div.pagination > ul > li:nth-child(" + i + ")"));
					} else {
						nextPage = driver.findElement(By.cssSelector("div.pagination > span.btn_page_next"));
					}
					nextPage.click();
					Thread.sleep(500);
					list1 = driver.findElements(By.cssSelector("span.store_name_map > b"));
					list2 = driver.findElements(By.cssSelector("span.store_name_map > i"));
					list3 = driver.findElements(By.cssSelector("span.store_name_map > span:nth-child(3)"));
					for(WebElement el : list1) {
						nicknameList.add(String.valueOf(el.getText()));
					}
					for(WebElement el : list2) {
						storeList.add(String.valueOf(el.getText()));
					}
					for(WebElement el : list3) {
						addressList.add(String.valueOf(el.getText()));
					}
					if(i == 5) {
						i = 1;
					} else {
						i++;
					}
				} catch(Exception e) {
					System.out.println("End");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Dbconn db;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "";
		for (int j=0; j<nicknameList.size(); j++) {
			try {
				conn = Dbconn.getConnection();
				if(conn != null){
					sql = "insert into tb_bana(nickname, store, address) values(?, ?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, nicknameList.get(j));
					pstmt.setString(2, storeList.get(j));
					pstmt.setString(3, addressList.get(j));
					pstmt.executeUpdate();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("success");
	}

}
