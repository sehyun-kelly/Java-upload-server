# COMP-3940 Assignment 2

#### Group 3
Term: Set 3G, Fall 2022 \
Members: Chengyang Li, Sehyun Park, Alex Pu, Melanie Ta, Shen Yen

### Description
- Launch a upload form in http://localhost:8080/
- From the URL, users can select an image file from a local folder and upload it to the multithread server along with additional form data such as the date and caption of the image.
- Server should print appropriate debug (log) messages on the console at runtime to clearly present the order in which classes and methods are being called on the server side. Any caught exception should be logged into a file.
- After the request to upload has been handled, the server shall return an alphabetically sorted listing of the images folder as a response.
- The server will distinguish between a request coming from a browser vs a desktop/native app. If the request comes from the desktop/native app, the server returns the listing as a JSON string as opposed to an HTML page.
