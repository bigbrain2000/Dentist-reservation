# Dentist-reservation


---

### **Table of contents**
- [Application requirements](#requirements)
- [Running the application](#running-the-application)
- [Database initialization](#database-initialization)
- [Model layer](#model-layer)

---
### Requirements
###**Java** 

To be able to install and run this project, please make sure you have installed Java 15 or higher, otherwise, the setup will not work! 
To check your java version, please run java -version in the command line.

###**Maven**
It would be good if you also installed Maven to your system.
To check if you have Maven installed run <pre>mvn -version</pre> in the command line.
If you don`t have Maven installed on your system then download it from [**here**](https://maven.apache.org/download.cgi).

A useful tutorial for installing Maven on Windows 10 is [How to install MAVEN on WINDOWS 10 | Step by Step](https://www.youtube.com/watch?v=RfCWg5ay5B0) and if you use Linux [Install Apache Maven On Linux | Maven Tutorial | JavaHome](https://www.youtube.com/watch?v=p-Xt1RkHdZE). 

###**JavaFX**
For this application make sure you install JavaFX SDK on your machine, using the instructions provided in the [Official Documentation](https://www.google.com/search?client=firefox-b-d&q=build). 

Make sure to export the PATH_TO_FX environment variable, or to replace it in every command you will find in this documentation from now on, with the path/to/javafx-sdk-15.0.1/lib.

A useful tutorial for installing Maven is [JavaFX install & setup (IntelliJ) 💡](https://www.youtube.com/watch?v=Ope4icw6bVk&t=72s)

***Note: you can download version 15 of the javafx-sdk, by replacing in the download link for version 16 the 16 with 15.***


###**Nitrite database**
For this project I used Nitrite which is an open source nosql embedded document store written in Java with MongoDB like API. It supports both in-memory and single file based persistent store.
The link for downloading Nitrite can be found [here](https://github.com/nitrite/nitrite-java/releases/download/v3.4.3/nitrite-explorer-3.4.3.jar).

Download the jar and run --module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml. 
You should see a window like this open:
![](https://github.com/bigbrain2000/Dentist-reservation/blob/main/src/main/resources/images/Nitrite.png)

### Running the application
If you have installed all the requirements, you should be able to run the following commands:
* **Build**
<pre>
mvn clean install
</pre>

* **Run**
<pre>
mvn javafx:run
</pre>

---
### Database initialization
* Encrypting passwords

Encrypting the passwords is done via the following 2 Java functions, found in [UserService](https://github.com/bigbrain2000/Dentist-reservation/blob/main/src/main/java/services/UserService.java):
<pre>
    private static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }
</pre>

Nitrite was used in the [UserService](https://github.com/bigbrain2000/Dentist-reservation/blob/main/src/main/java/services/UserService.java) file, where I initialized a database, and a repository of User objects:
<pre>
 public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("users_database.db").toFile())
                .openOrCreate("test", "test");

        userRepository = database.getRepository(User.class);
    }
</pre>

### Model layer
This application uses the following models:
- **[User](https://github.com/bigbrain2000/Dentist-reservation/blob/main/src/main/java/model/User.java)**
    * a user is defined by username, password, first name, second name, phone number, address and role
- **[Service](https://github.com/bigbrain2000/Dentist-reservation/blob/main/src/main/java/model/Service.java)**
    * a service is defined by name and price
- **[MedicalRecord](https://github.com/bigbrain2000/Dentist-reservation/blob/main/src/main/java/model/MedicalRecord.java)**
    * a medical record is defined by client username and 5 predefined questions about his health and previous medical problems
- **[Appointment](https://github.com/bigbrain2000/Dentist-reservation/blob/main/src/main/java/model/Appointment.java)**
    * an appointment is defined by client username, his first name and second name
    * other important information like dentist name, service name, service price, appointment date and a field that indicates the patient has completed the medical record

All of these classes contains the information above along with getters, setters, toString(), hashCode() and equals().

---
### Service layer
Each service contains methods to insert and delete data from databases.
More over I added some methods that check the apparition of unexpected exceptions before managing the data from databases.