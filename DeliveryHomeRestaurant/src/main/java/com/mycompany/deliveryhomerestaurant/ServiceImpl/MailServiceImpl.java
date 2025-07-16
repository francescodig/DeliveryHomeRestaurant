/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant.ServiceImpl;

import com.mycompany.deliveryhomerestaurant.Service.MailService;
import com.mycompany.deliveryhomerestaurant.util.Constants;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import java.util.Properties;

/**
 *
 * @author franc
 */
public class MailServiceImpl implements MailService {
    
    
    private final String username;
    private final String password;
    private final String smtpHost;
    private final int smtpPort;
    
    public MailServiceImpl() {
        this.username = Constants.email;
        this.password = Constants.passwordApp;
        this.smtpHost = Constants.smtpHost;
        this.smtpPort = Constants.port;
    }


    @Override
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // usa STARTTLS
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));

        
        //Crea una sessione SMTP 
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(text);

        Transport.send(message);
    }
        
}
    
