# Claire Share

Java web application to share text messages between connected clients in a home scenario. Claire should be installed on your home server.

Clients are connected by websockets, sent messages are forwarded to all connected clients. New clients get last sent message. If the webapp is SSL protected, received messages can be copied to the system clipboard.

The web app is security protected, mostly to indentify the clients. E.g. my Tomcat has users called thinkpad, mobile and tablet. Users must have role claire.

Browsers close websockets after some time, to reconnect use the button.

# Supported Application Servers

Should run on all Webapp 4.0 servers, e.g. Tomcat 9. Needs Java 11 or later.

# License

The application is under the Apache License 2.0

Copyright 2023 by Jürgen Weber

# Screenshot

![curlWebManager](doc/claire.png?raw=true)



