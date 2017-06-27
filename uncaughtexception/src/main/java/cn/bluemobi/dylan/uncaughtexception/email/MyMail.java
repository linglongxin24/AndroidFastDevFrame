 package cn.bluemobi.dylan.uncaughtexception.email;
 
 import java.util.Date;
 import java.util.Properties;
 import javax.activation.CommandMap;
 import javax.activation.DataSource;
 import javax.activation.FileDataSource;
 import javax.activation.MailcapCommandMap;
 import javax.mail.Authenticator;
 import javax.mail.BodyPart;
 import javax.mail.Multipart;
 import javax.mail.PasswordAuthentication;
 import javax.mail.Session;
 import javax.mail.internet.InternetAddress;
 import javax.mail.internet.MimeBodyPart;
 import javax.mail.internet.MimeMessage;
 import javax.mail.internet.MimeMultipart;
 
 public class MyMail extends Authenticator
 {
   private String _user;
   private String _pass;
   private String[] _to;
   private String _from;
   private String _port;
   private String _sport;
   private String _host;
   private String _subject;
   private String _body;
   private boolean _auth;
   private boolean _debuggable;
   private Multipart _multipart;
   
   public MyMail()
   {
     this._host = "smtp.qq.com";
     this._port = "465";
     this._sport = "465";
     this._user = "";
     this._pass = "";
     this._from = "";
     this._subject = "";
     this._body = "";
     this._debuggable = false;
     this._auth = true;
     this._multipart = new MimeMultipart();
     MailcapCommandMap mc = (MailcapCommandMap)CommandMap.getDefaultCommandMap();
     mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
     mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
     mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
     mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
     mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
     CommandMap.setDefaultCommandMap(mc);
   }
   
   public String get_user() {
     return this._user;
   }
   
   public void set_user(String _user) { this._user = _user; }
   
   public String get_pass() {
     return this._pass;
   }
   
   public void set_pass(String _pass) { this._pass = _pass; }
   
   public String[] get_to() {
     return this._to;
   }
   
   public void set_to(String[] _to) { this._to = _to; }
   
   public String get_from() {
     return this._from;
   }
   
   public void set_from(String _from) { this._from = _from; }
   
   public String get_port() {
     return this._port;
   }
   
   public void set_port(String _port) { this._port = _port; }
   
   public String get_sport() {
     return this._sport;
   }
   
   public void set_sport(String _sport) { this._sport = _sport; }
   
   public String get_host() {
     return this._host;
   }
   
   public void set_host(String _host) { this._host = _host; }
   
   public String get_subject() {
     return this._subject;
   }
   
   public void set_subject(String _subject) { this._subject = _subject; }
   
   public String get_body() {
     return this._body;
   }
   
   public void set_body(String _body) { this._body = _body; }
   
   public boolean is_auth() {
     return this._auth;
   }
   
   public void set_auth(boolean _auth) { this._auth = _auth; }
   
   public boolean is_debuggable() {
     return this._debuggable;
   }
   
   public void set_debuggable(boolean _debuggable) { this._debuggable = _debuggable; }
   
   public Multipart get_multipart() {
     return this._multipart;
   }
   
   public void set_multipart(Multipart _multipart) { this._multipart = _multipart; }
   
   public MyMail(String user, String pass) {
     this();
     this._user = user;
     this._pass = pass;
   }
   
   public boolean send() throws Exception { Properties props = _setProperties();
     if ((!this._user.equals("")) && (!this._pass.equals("")) && (this._to.length > 0) && (!this._from.equals("")) && (!this._subject.equals("")) && (!this._body.equals(""))) {
       Session session = Session.getInstance(props, this);
       MimeMessage msg = new MimeMessage(session);
       msg.setFrom(new InternetAddress(this._from));
       InternetAddress[] addressTo = new InternetAddress[this._to.length];
       for (int i = 0; i < this._to.length; i++) {
         addressTo[i] = new InternetAddress(this._to[i]);
       }
       msg.setRecipients(javax.mail.internet.MimeMessage.RecipientType.TO, addressTo);
       msg.setSubject(this._subject);
       msg.setSentDate(new Date());
       BodyPart messageBodyPart = new MimeBodyPart();
       messageBodyPart.setText(this._body);
       this._multipart.addBodyPart(messageBodyPart);
       msg.setContent(this._multipart);
       javax.mail.Transport.send(msg);
       return true;
     }
     return false;
   }
   
   public void addAttachment(String filename) throws Exception {
     BodyPart messageBodyPart = new MimeBodyPart();
     DataSource source = new FileDataSource(filename);
     messageBodyPart.setDataHandler(new javax.activation.DataHandler(source));
     
 
     String[] zhong = filename.split("/");
     String my_filename = zhong[(zhong.length - 1)];
     messageBodyPart.setFileName(my_filename);
     this._multipart.addBodyPart(messageBodyPart);
   }
   
 
 
 
   public PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication(this._user, this._pass); }
   
   private Properties _setProperties() {
     Properties props = new Properties();
     props.put("mail.smtp.host", this._host);
     if (this._debuggable)
       props.put("mail.debug", "true");
     if (this._auth)
       props.put("mail.smtp.auth", "true");
     props.put("mail.smtp.port", this._port);
     props.put("mail.smtp.socketFactory.port", this._sport);
     props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
     props.put("mail.smtp.socketFactory.fallback", "false");
     return props; }
   
   public String getBody() { return this._body; }
   
   public void setBody(String _body) {
     this._body = _body;
   }
 }

