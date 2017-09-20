package ie.turfclub.utilities;



import ie.turfclub.accountsReports.model.HuntercertsSales;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class MailUtility {

	private Session emailSession;
	//private Properties popProperties;
	private Properties smtpProperties;

	private String userName;
	private String password;
	private String popUrl;
	private String startFolder;
	private String tableHeaderForDescription;
	private String tableHeaderForDescriptionContains;
	private String tableHeaderForCustomer;
	private String tableHeaderForOrderId;
	private String tableHeaderForAmount;

	private InternetAddress[] ItAndAccountsEmailRecipients;
	private InternetAddress[] vetsEmailRecipients;

	public MailUtility(Environment env) {

		System.out.println("init mail");
		this.userName = env.getProperty("mail.username");
		this.password = env.getProperty("mail.password");
		//this.popUrl = env.getProperty("mail.pop3.host");
		//this.startFolder = env.getProperty("mail.readmail.startfolder");
		this.tableHeaderForDescription = env
				.getProperty("mail.readmail.link.tableHeadingForDescription");
		this.tableHeaderForDescriptionContains = env
				.getProperty("mail.readmail.link.tableHeadingDescriptionContains");
		this.tableHeaderForCustomer = env
				.getProperty("mail.readmail.link.tableHeadingForCustomer");
		this.tableHeaderForOrderId = env
				.getProperty("mail.readmail.link.tableHeadingForOrderId");
		this.tableHeaderForAmount = env
				.getProperty("mail.readmail.link.tableHeadingForAmount");

		String[] itAccountsEmail = env.getProperty("mail.itAndAccount.addresses").split(
				";");
		ItAndAccountsEmailRecipients = new InternetAddress[itAccountsEmail.length];
		int i = 0;
		for (String email : itAccountsEmail) {

			try {
				System.out.println(email);
				ItAndAccountsEmailRecipients[i] = new InternetAddress(email);
			} catch (AddressException e) {

				e.printStackTrace();
			}
			i++;

		}
		vetsEmailRecipients = new InternetAddress[3];
		try {
			vetsEmailRecipients[0] = new InternetAddress("it@turfclub.ie");
			vetsEmailRecipients[1] = new InternetAddress("richard.hayden@turfclub.ie");
			vetsEmailRecipients[2] = new InternetAddress("ray.bergin@turfclub.ie");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//popProperties = new Properties();
		//popProperties.put("mail.pop3.host", env.getProperty("mail.pop3.host"));
		//popProperties.put("mail.pop3.port", env.getProperty("mail.pop3.port"));
		//popProperties.put("mail.pop3.starttls.enable", env.getProperty("mail.pop3.starttls.enable"));

		smtpProperties = new Properties();
		smtpProperties.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
		smtpProperties.put("mail.smtp.starttls.enable",
				env.getProperty("mail.pop3.starttls.enable") );
		smtpProperties.put("mail.smtp.host", env.getProperty("mail.smtp.host"));
		smtpProperties.put("mail.smtp.port", env.getProperty("mail.smtp.port"));

	}

	// function for sending stack exception error messages to IT
	public void sendITError(String subject, String messageText) {

	}

	public void sendAccountsEmail(String subject, String messageText, String attachmentPath){
		 try {
			 emailSession = Session.getInstance(smtpProperties,
						new javax.mail.Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(userName,
										password);
							}
						});
				// Create a default MimeMessage object.
				Message message = new MimeMessage(emailSession);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(userName));

	         // Set To: header field of the header.
	         for(InternetAddress a : ItAndAccountsEmailRecipients){
					System.out.println(a.toString());
				}

				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(this.ItAndAccountsEmailRecipients[0].toString()));

	         // Set Subject: header field
	         message.setSubject(subject);

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText(messageText);

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         if(attachmentPath != null && attachmentPath.length() > 0) {
		         String filename = attachmentPath;
		         DataSource source = new FileDataSource(filename);
		         messageBodyPart.setDataHandler(new DataHandler(source));
		         messageBodyPart.setFileName(filename);
	         }
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
		
	}
	
	public void sendEmailWithToRecipients(String subject, String messageText, String attachmentPath, ArrayList<String> recipentEmails){
		 try {
			 emailSession = Session.getInstance(smtpProperties,
						new javax.mail.Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(userName,
										password);
							}
						});
				// Create a default MimeMessage object.
				Message message = new MimeMessage(emailSession);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(userName));

	         // Set To: header field of the header.
	         for(String email : recipentEmails){
				
				if(email.contains("@")){
					//message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
					message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
				}
	         }
	         // Set Subject: header field
	         message.setSubject(subject);

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText(messageText);

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         String filename = attachmentPath;
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
		
	}
	
	
	public void sendVetsEmail(String subject, String messageText, String attachmentPath){
		 try {
			 emailSession = Session.getInstance(smtpProperties,
						new javax.mail.Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(userName,
										password);
							}
						});
				// Create a default MimeMessage object.
				Message message = new MimeMessage(emailSession);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(userName));

	         // Set To: header field of the header.
	         for(InternetAddress a : vetsEmailRecipients){
					System.out.println(a.toString());
				}

				message.addRecipients(Message.RecipientType.TO, this.vetsEmailRecipients);

	         // Set Subject: header field
	         message.setSubject(subject);

	         // Create the message part
	         BodyPart messageBodyPart = new MimeBodyPart();

	         // Now set the actual message
	         messageBodyPart.setText(messageText);

	         // Create a multipar message
	         Multipart multipart = new MimeMultipart();

	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);

	         // Part two is attachment
	         messageBodyPart = new MimeBodyPart();
	         String filename = attachmentPath;
	         System.out.println("Sending Attachment: " + filename );
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);

	         // Send the complete message parts
	         message.setContent(multipart);

	         // Send message
	         Transport.send(message);

	         System.out.println("Sent message successfully....");
	  
	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      }
		
	}
	
	
	
	// function for sending error messages to IT and Accounts
	public void sendITAndAccountsError(String subject, String messageText) {
		try {

			emailSession = Session.getInstance(smtpProperties,
					new javax.mail.Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(userName,
									password);
						}
					});
			// Create a default MimeMessage object.
			Message message = new MimeMessage(emailSession);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(userName));


			for(InternetAddress a : ItAndAccountsEmailRecipients){
				System.out.println(a.toString());
			}

			message.addRecipients(Message.RecipientType.TO, this.ItAndAccountsEmailRecipients);
			// Set Subject: header field
			message.setSubject(subject);



	         // Now set the actual message
	         message.setText(messageText);

			// Send message
			Transport.send(message);

			// System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			System.out.println("ERROR");
			throw new RuntimeException(e);
		}
		
		
		
	}

	// function to send messages with attachments
	public void send(String subject, String messageText,
			ArrayList<String> recipients, String attachmentFileName, String filename) {
		try {

			emailSession = Session.getInstance(smtpProperties,
					new javax.mail.Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(userName,
									password);
						}
					});
			// Create a default MimeMessage object.
			Message message = new MimeMessage(emailSession);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(userName));

			/*InternetAddress[] mailAddress_TO = new InternetAddress[recipients
					.size()];*/
			/*int i = 0;
			for (String email : recipients) {

				mailAddress_TO[i] = new InternetAddress(email);
				i++;

			}*/
			
			for(String email : recipients){
				
				if(email.contains("@")){
					//message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
					message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
				}
	         }

			//message.addRecipients(Message.RecipientType.TO, mailAddress_TO);
			// Set Subject: header field
			message.setSubject(subject);

			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(messageText);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();

			DataSource source = new FileDataSource(attachmentFileName);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			// Send message
			Transport.send(message);

			// System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			System.out.println("ERROR");
			throw new RuntimeException(e);
		}
	}

	public ArrayList<HuntercertsSales> readEmailsAccounts() {
		ArrayList<HuntercertsSales> items = new ArrayList<>();
		try {

			System.out.println("CHECK MAIL");
			//emailSession = Session.getDefaultInstance(popProperties);
			String customerName, orderId, amount;
			boolean readEmailDetails = false;

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("imaps");
			store.connect(popUrl, userName, password);

			// create the folder object and open it

			Folder emailFolder = store.getFolder(startFolder);
			emailFolder.open(Folder.READ_WRITE);

			// retrieve the messages from the folder in an array and print it
			// System.out.println(emailFolder.getUnreadMessageCount());

			Message[] messages = emailFolder.search(new FlagTerm(new Flags(
					Flag.SEEN), false));
			System.out.println("messages.length---" + messages.length);

			Flags flags = new Flags(Flag.SEEN);
			for (int i = 0, n = messages.length; i < n; i++) {
				Message message = messages[i];

				String html = message.getContent().toString();
				// html = Jsoup.clean(html, Whitelist.basic());
				System.out.println(html);
				Document doc = Jsoup.parse(html);
				Elements links = doc.getElementsByTag("td");
				customerName = "";
				orderId = "";
				amount = "";
				readEmailDetails = false;
				for (Element link : links) {
					if (link.text().contains(tableHeaderForDescription)) {
						Element description = link.nextElementSibling();
						System.out.println(description.text()
								+ tableHeaderForDescriptionContains);
						if (description.text().contains(
								tableHeaderForDescriptionContains)) {
							readEmailDetails = true;
						}
					}

					if (link.text().contains(tableHeaderForCustomer)) {

						Element customer = link.nextElementSibling();
						customerName = customer.text();
						System.out.println(customer.text());
					}
					if (link.text().contains(tableHeaderForOrderId)) {
						Element order = link.nextElementSibling();
						orderId = order.text();
					}
					if (link.text().contains(tableHeaderForAmount)) {
						Element amountEl = link.nextElementSibling();
						amount = amountEl.text().replaceAll("EUR", "");
					}
					// check that information is present and if not email IT

				}
				if (readEmailDetails) {
					HuntercertsSales obj = new HuntercertsSales();
					obj.setSalesAmount(amount);
					obj.setSalesPayee(customerName);
					obj.setSalesOrderId(orderId);
					items.add(obj);

					System.out.println("adding " + orderId + " " + customerName
							+ amount);
				}

				message.setFlags(flags, true);
			}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			System.out.println("ERROR");
			e.printStackTrace();
		} catch (MessagingException e) {
			System.out.println("ERROR");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}

		return items;
	}

	public void sendAIRTableRecordEmail(String subject, String body,
			String filePath, ArrayList<String> emails) {
		
		this.send(subject, body, emails, filePath, "AIR_FILE.csv");
	}

	public void sendSMSReminderRecordEmail(String subject, String body,
			String filePath, ArrayList<String> emails) {
		
		this.send(subject, body, emails, filePath, "SMSReminder.txt");
	}

}
