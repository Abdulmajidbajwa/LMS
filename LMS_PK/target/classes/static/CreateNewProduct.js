var loggedInUserId = readCookie("LoggedInUserId");

function begin(){
	
//**********************************************************************AddUser Api
	var data = null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
	  if (this.readyState === 4) {
		console.log(this.responseText);
		
		location.replace("ProductInfo.html");
	  }
	});

	var productName = document.getElementById("productName").value;
	
	var productTypeStr = document.getElementById("productType").value;
	/*var productType;
	if(productTypeStr.toUpperCase()=="PERSONAL"){
		productType=100;
	}else if(productTypeStr.toUpperCase()=="INDIVIDUAL"){
		productType=101;
	}else{
		if(productTypeStr.toUpperCase()=="PARTNER"){
			productType=102;
		}
	}*/
	
	
	var minLoanAmount = document.getElementById("minLoanAmount").value;
	var maxLoanAmount = document.getElementById("maxLoanAmount").value;
	
	var minServiceCharge = document.getElementById("minServiceCharge").value;
	var maxServiceCharge = document.getElementById("maxServiceCharge").value;
	
	var minCreditScore = document.getElementById("minCreditScore").value;
	
	var channelFee = document.getElementById("channelFee").value;
	var gracePeriodDays = document.getElementById("gracePeriodDays").value;
	var latePaymentFees = document.getElementById("latePaymentFees").value;
	var kyc = document.getElementById("kyc").value;
	
	
	
	console.log("Products Parameters are");
	console.log(productName);
	console.log(productTypeStr);
//	console.log(productType);
	console.log(minLoanAmount);
	console.log(maxLoanAmount);
	console.log(minServiceCharge);
	console.log(maxServiceCharge);
	console.log(minCreditScore);
	console.log(channelFee);
	console.log(gracePeriodDays);
	console.log(latePaymentFees);
	console.log(kyc);

	xhr.open("POST", "/LMSServer/AddProduct?productCatagory="+productTypeStr+"&productName="+productName+"&callerId="+loggedInUserId);
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);
}


function back(){
	
	deleteCookie("selectedAssumptionId");
	deleteCookie("selectedAssumption");
	
	location.replace("ProductInfo.html");
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