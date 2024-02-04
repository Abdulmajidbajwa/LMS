var loggedInUserId = readCookie("LoggedInUserId");

function begin(){

	if(validateForm()){
		checkAndMarkLatePaymentFeeAgainstCustomer();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}

function checkAndMarkLatePaymentFeeAgainstCustomer(){
	
	//**********************************************************************AddUser Api
	var data = null;
	
	console.log("In Begin Function");

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
		if (this.readyState == 4 && this.status == 200) {
			var jsonResponseData=JSON.parse(this.responseText);
			console.log(jsonResponseData);
			
			var requestedAction=jsonResponseData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(jsonResponseData["Requested_Action"]);
				alert(jsonResponseData["Message"]);
			}else{
				var requestedAction=jsonResponseData["Message"];
				alert(requestedAction);
			}
		}
	});
	
	
	var cellNo = document.getElementById("cellNo").value;
	
	
	console.log("Loan Repayment Parameters are");
	console.log(cellNo);
	
		xhr.open("POST", "/LMSServer/checkAndMarkOverdueEntriesAgainstCustomerUsingCellNo?cellNo="+cellNo+"&callerId="+loggedInUserId);

		xhr.setRequestHeader("authString", readCookie("Token"));
		xhr.send(data);
	
	}

function validateForm(){ 
    var cellNo = document.forms["form"]["cellNo"];               
   
    var numberRegexWithPrefix=/^(03)([0-9]{9})$/;
    var numberRegex=/^\d+$/;

    if (cellNo.value == ""){ 
        alert("Please Enter Cell No"); 
        cellNo.focus(); 
        return false; 
    }else{
    	if(numberRegexWithPrefix.test(cellNo.value)==false){
    		alert("Please Enter Correct Cell No"); 
    		cellNo.focus(); 
            return false; 
    	}
    } 
    
    return true; 
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



function back(){
	deleteCookie("selectedUserId");
	deleteCookie("selectedUserUsername");
	
	$('#content').load('CreateNewUser.html');
}

function deleteCookie(name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};



