package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.Reporter;

public class JavaUtils {

	public static HashMap<String, String> readExcelData(String sheetname, String uniqueValue) throws EncryptedDocumentException, InvalidFormatException, IOException {
		HashMap<String,String> dataMap = null;
		String key, value = null;
		FileInputStream file = new FileInputStream("./test-data/testData.xlsx");
		dataMap = new HashMap<String, String>();
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetname);
		Iterator<Row> it = sheet.rowIterator();

		Row headers = it.next();
		while(it.hasNext()) {

			Row record = it.next();
			String cellValue = record.getCell(0).toString();
			if(cellValue.equals(uniqueValue)) {

				for(int i=0;i<headers.getLastCellNum();i++){
					try{
						if (record.getCell(i).getCellType() == record.getCell(i).CELL_TYPE_NUMERIC) {
							try{
								record.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
								value = record.getCell(i).toString().trim();
							}catch(Exception e){}
							key = headers.getCell(i).toString().trim();

						} else {

							key = headers.getCell(i).toString().trim();
							try {
								value = record.getCell(i).toString().trim();
							} catch (Exception e) {}
						}
					}catch(Exception e){
						continue;
					}

					dataMap.put(key, value);
				}

				break;
			}
		}
		return dataMap;
	}
	
	public static String[] getMinorDate(){
		String[] date = new String[3];
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR,-17);
		date[0] = Integer.toString(c.get(Calendar.DATE));
		date[1] = new DateFormatSymbols().getMonths()[c.get(Calendar.MONTH)];
		date[2] = Integer.toString(c.get(Calendar.YEAR));
		System.out.println("Selected Minor Date: "+date[0]+"-"+date[1]+"-"+date[2]);
		return date;
	}

	public static void writeValueToExcel(String sheetname,String uniqueValue, String columnName, String value) throws EncryptedDocumentException, InvalidFormatException, IOException{
		FileInputStream file = new FileInputStream("./test-data/testData.xlsx");
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetname);
		Iterator<Row> it = sheet.rowIterator();

		Row headers = it.next();
		while (it.hasNext()) {

			Row record = it.next();
			String cellValue = record.getCell(0).toString().trim();
			if (cellValue.equalsIgnoreCase(uniqueValue)) {
				for(int i=0;i<headers.getLastCellNum();i++){
					if(headers.getCell(i).getStringCellValue().equals(columnName)){
						record.createCell(i).setCellValue(columnName);
						break;
					}
				}
				break;
			}
		}
		FileOutputStream fos = new FileOutputStream("./test-data/testData.xlsx");
		wb.write(fos);
		wb.close();
		fos.close();
	}

	public static String generateRandomNumber(int number) {

		Random ran = new Random();
		int x = ran.nextInt(number)+1;
		String randomNo = String.valueOf(x);
		return randomNo;
	}

	public static String[] generateNrandomNumbers(int no, int number){
		Random ran = new Random();
		String[] randomNos = new String[no];
		HashSet<String> randomSet = new HashSet<String>();
		while(randomSet.size()<no){
			int x = ran.nextInt(number)+1;
			randomSet.add(String.valueOf(x));
		}
		int i=0;
		for(String s:randomSet){
			randomNos[i] = s;
			i++;
		}
		return randomNos;
	}

	public static String getTodaysDDxxMMMYYYY(){
		String[] suffixes =
				//    0     1     2     3     4     5     6     7     8     9
			{ "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
					//    10    11    12    13    14    15    16    17    18    19
					"th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
					//    20    21    22    23    24    25    26    27    28    29
					"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
					//    30    31
					"th", "st" };

		Date date = new Date();
		SimpleDateFormat formatDayOfMonth  = new SimpleDateFormat("d");
		int day = Integer.parseInt(formatDayOfMonth.format(date));
		String dayStr = day + suffixes[day];
		
		SimpleDateFormat formatMonthAndYear = new SimpleDateFormat("MMM yyyy");
		String mmyy = formatMonthAndYear.format(date);
		
		return dayStr +" "+ mmyy;
		
	}

	public String getTodaysDateAndTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar cal = Calendar.getInstance();
		Date tdy = cal.getTime();
		String today = df.format(tdy);

		return today;
	}
	
	public static String getTodaysDateDDMMYYYY(){
		DateFormat df = new SimpleDateFormat("dd MMMM yyyy");

		Calendar cal = Calendar.getInstance();
		Date tdy = cal.getTime();
		String today = df.format(tdy);

		return today;
	}

	public String getRequiredDateandTime(int daysToAdd) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar cal1 = Calendar.getInstance();
		cal1.add(Calendar.DATE, daysToAdd);
		Date day = cal1.getTime();
		String reqDate = df.format(day);

		return reqDate;
	}


	public static String getPropValue(String key) throws IOException
	{
		File configFile = new File("./config.properties");
		String host = null;
		try {
			FileReader reader = new FileReader(configFile);
			Properties props = new Properties();
			props.load(reader);
			host = props.getProperty(key);
			reader.close();
		} catch (FileNotFoundException ex) {
			// file does not exist
		} catch (IOException ex) {
			// I/O error
		}

		return host;
	}

	public static void setPropValue(String key,String value){
		File configFile = new File("./config.properties");
		try {
			Properties props = new Properties();
			props.setProperty(key, value);
			FileWriter writer = new FileWriter(configFile);
			props.store(writer, "host settings");
			writer.close();
		} catch (FileNotFoundException ex) {
			// file does not exist
		} catch (IOException ex) {
			// I/O error
		}
	}


}
