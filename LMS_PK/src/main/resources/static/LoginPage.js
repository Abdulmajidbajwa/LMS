// JavaScript Document
var modal = document.getElementById('id01');

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}




function Login(){
	
//**********************************************************************LoginWithUsername Api	
	var data = null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;
	
	//Will Cleared ALl Cookies While Logout But Deleting All Cookies Right Now Before Setting New Cookies Just In Case
	deleteAllCookies();

	xhr.onreadystatechange = function() {
		
		//All Apis always returned 200 Response incase of Success or Failure
    	if (this.readyState == 4 && this.status == 200) {
//    		console.log(this.responseText);
			var jsonResponseData=JSON.parse(this.responseText);
			console.log(jsonResponseData);
			
			//As All Apis always returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=jsonResponseData["Requested_Action"];
			
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(jsonResponseData["Requested_Action"]);
				console.log(jsonResponseData["Token"]);
				writeCookie('Token', jsonResponseData["Token"], 1);
				writeCookie('LoggedInUserId', jsonResponseData["userId"], 1);
				
				alert("Valid Credentials, Good to go...");
				console.log(readCookie("Token"));
				console.log(readCookie("LoggedInUserId"));
				
				location.replace("NavigationPage.html")
			}else{
				
				var requestedAction=jsonResponseData["Message"];
				alert(requestedAction);
			}
    	}
  	};
	
	var username = document.getElementById("username").value;
	var password = document.getElementById("password").value;

	
	xhr.open("POST", "/LoginWithUsername?username="+username+"&password="+password);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	 
	xhr.send(data);
}


function writeCookie(cname, cvalue, exdays) {
  var d = new Date();
  d.setTime(d.getTime() + (exdays*24*60*60*1000));
  var expires = "expires="+ d.toUTCString();
  document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}


function readCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i <ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}

