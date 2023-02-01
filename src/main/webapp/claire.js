    "use strict";

document.addEventListener("visibilitychange", () => {
  if (document.visibilityState === 'visible') {
    console.log('visibilitychange, visible');
  } else {
    console.log('visibilitychange, not visible');
  }
});

addEventListener('pageshow', (event) => {
	console.log('pageshow');
 });

        var Claire = {};

        Claire.socket = null;

		Claire.target = '/claire/share/';

		Claire.user = "initial";

        Claire.initialize = function() {
			console.log('initialize');
			
			var url = window.location.host + Claire.target + Claire.user;

			console.log('url: ' + url);
			
            if (window.location.protocol == 'http:') {
                Claire.connect('ws://' + url);
            } else {
                Claire.connect('wss://' + url);
            }
        };

        Claire.connect = (function(host) {
            if ('WebSocket' in window) {
				try {
                Claire.socket = new WebSocket(host);
                 }
		  catch (error) {
		    console.error(error.message);
		  }
            } else {
                console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            Claire.socket.onopen = function () {
				
				document.getElementById('status').textContent = 'open';
				document.getElementById("status").style.color = "green";
				
                console.log('Info: WebSocket connection opened.');
                
                document.getElementById('reconnectBT').onclick = function(event) {
                    
                        Claire.initialize();
                    
                };

                document.getElementById('shareBT').onclick = function(event) {
                    
                        Claire.sendMessage();
                    
                };
                
                document.getElementById('copyBT').onclick = function(event) {
                    
                        Claire.clipCopy();
                    
                };
                
                document.getElementById("shareBT").disabled = false;
                document.getElementById("reconnectBT").disabled = true;


           /*     document.getElementById('clipShareBT').onclick = function(event) {
                    
                        Claire.clipShare();
                    
                };
             */   
                
                
            };

            Claire.socket.onclose = function (event) {
                
                document.getElementById('status').textContent = 'closed';
                document.getElementById("status").style.color = "red";
                
                console.log('Info: WebSocket closed, reason: ' + event.reason);
                
                document.getElementById("shareBT").disabled = true;
                document.getElementById("reconnectBT").disabled = false;
                 
                
                document.getElementById('shareBT').onclick = null;
                document.getElementById('copyBT').onclick = null;
            };

			Claire.socket.onerror = function(err) {
    			console.error('Socket encountered error: ', err, 'Closing socket');
    			Claire.socket.close();
  			};

            Claire.socket.onmessage = function (event) {
		
				Claire.receiveMessage(event.data);		
            };
        });


        Claire.receiveMessage = (function(message) {

			console.log(message);
			
			var jmsg = JSON.parse(message);
				
			if (jmsg.hasOwnProperty('user'))
			{
				console.log("message.user: " + jmsg.user);
				Claire.user = jmsg.user;
				document.getElementById('userID').textContent = Claire.user; 
			}
			if (jmsg.hasOwnProperty('text'))
			{
				console.log("message.text: " + jmsg.text);
				document.getElementById('originUserID').textContent = jmsg.origin;
				document.getElementById('remotemsg').value = jmsg.text;
			}
        });


        Claire.sendMessage = (function() {
            var localmsg = document.getElementById('localmsg').value;
            if (localmsg != '') {
				
				 const jsonmsg = {
    				origin: Claire.user,
    				text: localmsg,
				    date: Date.now()
  				};
				
				var message = JSON.stringify(jsonmsg);
				
				console.log("sendmessage: " + message);
				
                Claire.socket.send(message);
                document.getElementById('localmsg').value = '';
            }
        });

        Claire.clipCopy = (function() {
			
            var remotemsg = document.getElementById('remotemsg').value;
            if (remotemsg != '') {
				console.log("clipCopy: " + remotemsg);	
				
				navigator.clipboard.writeText(remotemsg).then(() => {
	  				console.log('Content copied to clipboard');
					},() => {
	  				console.error('Failed to copy');
				});
			}
        });

		Claire.clipShare = (async function() {
			let text = '';
			console.log('clipShare');
		  try {
		    const permission = await navigator.permissions.query({ name: 'clipboard-read' });
		    if (permission.state === 'denied') {
		      throw new Error('Not allowed to read clipboard.');
		    }
		    text = await navigator.clipboard.readText();
		  }
		  catch (error) {
		    console.error(error.message);
		  }
		  console.log('clip: ' + text);
		  return text;
		});


        Claire.initialize();
