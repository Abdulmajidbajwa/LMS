var loggedInUserId = readCookie("LoggedInUserId");

loadLoanRepaymentData();

function loadLoanRepaymentData(){
	var selectedCustomerLoanRequestCookieStatus = readCookie("selectedCustomerLoanRequest");
	 document.getElementById("cellNo").value = selectedCustomerLoanRequestCookieStatus;
}



function begin(){

	if(validateForm()){
		processLoanRepayment();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}

function processLoanRepayment(){
	
	//**********************************************************************AddUser Api
	var data = null;
	
	console.log("In Begin Function");

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
		if (this.readyState == 4 && this.status == 200) {
//			console.log(this.responseText);
			var jsonResponseData=JSON.parse(this.responseText);
			console.log(jsonResponseData);
			
			//As All Apis always returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=jsonResponseData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(jsonResponseData["Requested_Action"]);
				alert('Loan Repayment Successful');
				writeCookie("glIdToGet",1, "1");
				$('#content').load('GeneralLedgerInfo.html');
			}else{
				
				var requestedAction=jsonResponseData["Message"];
				alert(requestedAction);
			}
		}
	});
	
	
	var cellNo = document.getElementById("cellNo").value;
	var loanRepaymentAmount = document.getElementById("loanRepaymentAmount").value;
	
	
	console.log("Loan Repayment Parameters are");
	console.log(cellNo);
	console.log(loanRepaymentAmount);
	
		xhr.open("POST", "/LMSServer/repayLoanUsingTransaction?cellNo="+cellNo+"&repayLoanAmount="+loanRepaymentAmount);

		xhr.setRequestHeader("authString", readCookie("Token"));
		xhr.send(data);
	
	}


/*function validateRadioButton(elementIdToCheck) {
	
	selectedRoleValue=document.querySelector('input[name='+elementIdToCheck+']:checked');
	
	if(selectedRoleValue==null){
		return false;
	}else{
		if(selectedRoleValue.value==false){
			return false;
		}
	}
	
	return true;	

}*/

function validateForm(){ 
    var cellNo = document.forms["form"]["cellNo"];               
    var loanRepaymentAmount = document.forms["form"]["loanRepaymentAmount"];  
   
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
    
    if (loanRepaymentAmount.value == ""){ 
        window.alert("Please Enter Loan Repayment Amount"); 
        loanRepaymentAmount.focus(); 
        return false; 
    }else{
    	if(numberRegex.test(loanRepaymentAmount.value)==false){
    		alert("Please Enter Correct Loan Repayment Amount"); 
    		loanRepaymentAmount.focus(); 
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



