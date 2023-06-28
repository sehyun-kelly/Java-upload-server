# Java Upload Server
<img width="1282" alt="Screenshot 2023-06-27 at 5 54 22 PM" src="https://github.com/sehyun-kelly/Java-upload-server/assets/89621420/4ffa3e89-ea60-40f7-85fd-ca9b55c6cc08">

#### COMP 3940 | Assignment 2
Term: Set 3G, Fall 2022 
Members: Chengyang Li, Sehyun Park, Alex Pu, Melanie Ta, Shen Yen

### How to run
- Stay in the root folder uploadserver
- Update IP addresses in the server and console client
- Compile classes using:
```
    javac ConsoleApp/*.java
    javac CustomException/*.java
    javac HttpServlet/*.java
    javac UploadServer/*.java
```
- To start UploadServer, run: `java UploadServer/UploadServer`
- To run ConsoleApp, use: `java ConsoleApp/Activity`


### Description
- Launch an upload form at http://localhost:8888/. Through the web form, users can upload their images of choice. Upon submission, the server will display a list of images on the html page.
- Users can also connect to the server using ConsoleApp. Upon sending an image, the server will return a JSON string of all the current images.

### Project structure
    ├── ConsoleApp              # Java console app to upload images
    ├── CustomException         # Custom exceptions
    ├── HttpServlet             # Servlet, request, and response classes
    ├── images                  # Store all images
    └── UploadServer            # Server

- Server log message can be found in log.txt

### Contribution
- Chengyang Li
  - Develop user input for Console App
  - JSON parsing for the Console app
  - Write images data from the Console app to Server
- Kelly Park
  - Parse HTTP Request for Console App
  - Parse HTTP Request for Web
  - Manage multi-threading flow in UploadServerThread
- Alex Pu:
  - Custom exceptions
  - Manage Exceptions and Logs
  - Video
- Melanie Ta
  - Manage code structure
  - Construct request for Console App
  - Construct the server's response
- Shen Yen
  - Develop user input for Console app
  - Write images data from the Console app to Server
  - Video
