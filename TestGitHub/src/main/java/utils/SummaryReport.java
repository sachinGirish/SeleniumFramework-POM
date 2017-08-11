package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;

public class SummaryReport {

	public static String from;
	public static String fromPass;
	public static String[] to;
	public static String dir;
	public static DateFormat dateFormat;
	public static Date date;
	public static String subject;
	public static String bodyText;
	public static String zipName;
	
	static{
		dir = System.getProperty("user.dir");
		dateFormat = new SimpleDateFormat("dd-MM-yy HH-mm-ss");
		date = new Date();
		try {
			from = JavaUtils.getPropValue("fromEmail");
			fromPass = JavaUtils.getPropValue("fromPass");
			to = JavaUtils.getPropValue("toEmails").split(",");
			subject = JavaUtils.getPropValue("emailSubject")+dateFormat.format(date);
			bodyText = JavaUtils.getPropValue("emailBody");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException{
		Properties properties = System.getProperties();  
		properties.setProperty("mail.transport.protocol", "smtp");  
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");  
		properties.put("mail.smtp.auth", "true");  
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

		Session session = Session.getDefaultInstance(properties,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(from,fromPass);  
			}  
		});

		try{  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(from)); 
			for(String str : to)
			{
				message.addRecipient(Message.RecipientType.TO,new InternetAddress(str));
			}

			message.setSubject(subject); 
			Multipart multipart = new MimeMultipart();  
			MimeBodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(bodyText);
			multipart.addBodyPart(messageBodyPart1);  

			//attachment
			
			zipName = createZip();

			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			DataSource source = new FileDataSource(zipName);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName(zipName);
			multipart.addBodyPart(messageBodyPart2);

			message.setContent(multipart);  
			Transport.send(message);  
			System.out.println("Mail sent...");  
		}catch (MessagingException ex) {ex.printStackTrace();}  
		
		moveScreenshots();
		
	}

	@SuppressWarnings("resource")
	public static String createZip() throws IOException{
		FileInputStream fis;
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		// create the ZIP file
		String zipName = dir+"\\results.zip";
		File f = new File(zipName);
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f.getName()));
		ZipEntry e;
		File folder = new File(dir+"/screenshots");
		if(folder.exists()){
			File[] listOfFiles = folder.listFiles();
			// compress the files
			for(File file:listOfFiles){
				if(file.getName().endsWith(".png")){
					fis = new FileInputStream(file);
					// add ZIP entry to output stream
					e = new ZipEntry(file.getName());
					out.putNextEntry(e);
					// transfer bytes from the file to the ZIP file
					int len;
					while((len = fis.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					/*data = fis.toString().getBytes();
					out.write(data, 0, data.length);*/
					// complete the entry
					out.closeEntry();
					fis.close();
				}
			}
		}
		File result = new File(dir+"/target/surefire-reports/emailable-report.html");
		fis = new FileInputStream(result);
		e = new ZipEntry(result.getName());
		out.putNextEntry(e);
		int len;
		while((len = fis.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		/*data = fis.toString().getBytes();
		out.write(data, 0, data.length);*/
		out.closeEntry();

		// complete the ZIP file
		out.close();
		fis.close();
		return f.getName();
	}
	
	public static void deleteZip(){
		File file = new File(dir+zipName);
		if(file.exists())
			file.delete();
	}

	public static void moveScreenshots() throws IOException{
		File mainDest = new File(dir+"/archive");
		if(mainDest.exists())
			mainDest.mkdirs();
		File destinationFolder = new File(mainDest, dateFormat.format(date));
		File sourceFolder = new File(dir+"/screenshots");

		if (!destinationFolder.exists())
			destinationFolder.mkdirs();

		// Check weather source exists and it is folder.
		if (sourceFolder.exists() && sourceFolder.isDirectory())
		{
			// Get list of the files and iterate over them
			File[] listOfFiles = sourceFolder.listFiles();

			if (listOfFiles != null)
			{
				for (File child : listOfFiles )
				{
					// Move files to destination folder
					File srcFile = new File(child.getAbsolutePath());
					File destFile = new File(destinationFolder.getAbsolutePath()+"\\"+child.getName());
					FileUtils.moveFile(srcFile, destFile);
//					FileUtils.forceDelete(srcFile);
				}

				// Add if you want to delete the source folder 
				// sourceFolder.delete();
			}
		}
		else
		{
			System.out.println(sourceFolder + "  Folder does not exists");
		}
	}

}
