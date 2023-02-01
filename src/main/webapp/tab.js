    "use strict";

	const req = new XMLHttpRequest();
const url='http://julio:8080/claire/WSServlet';
req.open("GET", url);
req.send();

req.onreadystatechange = (e) => {
  console.log(req.responseText)

var jmsg = JSON.parse(req.responseText);  
  
  console.log(jmsg);
  
}