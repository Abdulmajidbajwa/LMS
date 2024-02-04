var loggedInUserId = readCookie("LoggedInUserId");
loadEditAssumptionsData();

function loadEditAssumptionsData(){
	var selectedApplicationIdCookieStatus = readCookie("selectedApplicationId");
	
	
	var loanApplicationDataInfo=null;
		
		var data = null;
		var editAssumptionDataInfo=null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		 /* if (this.readyState === 4) {
			  

			this.loanApplicationDataInfo = JSON.parse(this.responseText);
			console.log(this.loanApplicationDataInfo);

			console.log("Application Cookie is: "+ selectedApplicationIdCookieStatus);
			console.log("Record to Compare is: "+ this.loanApplicationDataInfo.id)

			generateFields(this.loanApplicationDataInfo);
		  }*/
		  
		  
		  if (this.readyState == 4 && this.status == 200) {
				var responseJsonData=JSON.parse(this.responseText);
				console.log(responseJsonData);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=responseJsonData["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
					this.loanApplicationDataInfo = JSON.parse(this.responseText);
					console.log(this.loanApplicationDataInfo);

					console.log("Application Cookie is: "+ selectedApplicationIdCookieStatus);
					console.log("Record to Compare is: "+ this.loanApplicationDataInfo.id)

					generateFields(this.loanApplicationDataInfo);		
				}else{
					var requestedAction=responseJsonData["Message"];
					alert(requestedAction);
				}
	  	}
		  
		  
		});

		xhr.open("POST", "LMSServer/getLoanApplicationById?loanApplicationId="+selectedApplicationIdCookieStatus);
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
	
	
}


function generateFields(selectedLoanApplicationDataInfo) {
	
	console.log("In Table Data**********************************************************");
	
	var loanApplicationForm = document.getElementById("loanApplicationForm");


//		console.log("Application Number------------------------------------------------------:");
		  
		  
		  var headingCustomerDetails = document.createElement("H3")                
		  var customerDetails = document.createTextNode("Customer Details"); 
		  headingCustomerDetails.appendChild(customerDetails); 
		  loanApplicationForm.appendChild(headingCustomerDetails);
		  
		  var applicationCustomerObj = selectedLoanApplicationDataInfo.customer;
		  
		  console.log("Customer:",applicationCustomerObj);
		  console.log("Customer Id:",applicationCustomerObj.id);
		  console.log("Customer CellNo:",applicationCustomerObj.cellNo);
		  console.log("Customer FullName:",applicationCustomerObj.fullName);
		  
		  
		  
		  var customerIdLabel = document.createElement("Label");
		  customerIdLabel.innerHTML="Customer Id";
		  loanApplicationForm.appendChild(customerIdLabel);
		  
		  var customerIdInput = document.createElement("input");
		  customerIdInput.setAttribute("type", "text");
		  customerIdInput.setAttribute("value", applicationCustomerObj.id);
		  customerIdInput.setAttribute("name", applicationCustomerObj.id);
		  loanApplicationForm.appendChild(customerIdInput);
		  
		  
		  var customerCellNoLabel = document.createElement("Label");
		  customerCellNoLabel.innerHTML="Customer Cell No";
		  loanApplicationForm.appendChild(customerCellNoLabel);
		  
		  var customerCellNoInput = document.createElement("input");
		  customerCellNoInput.setAttribute("type", "text");
		  customerCellNoInput.setAttribute("value", applicationCustomerObj.cellNo);
		  customerCellNoInput.setAttribute("name", applicationCustomerObj.cellNo);
		  loanApplicationForm.appendChild(customerCellNoInput);
		  
		  
		  
		  var customerFullNameLabel = document.createElement("Label");
		  customerFullNameLabel.innerHTML="Customer Full Name";
		  loanApplicationForm.appendChild(customerFullNameLabel);
		  
		  var customerFullNameInput = document.createElement("input");
		  customerFullNameInput.setAttribute("type", "text");
		  customerFullNameInput.setAttribute("value", applicationCustomerObj.fullName);
		  customerFullNameInput.setAttribute("name", applicationCustomerObj.fullName);
		  loanApplicationForm.appendChild(customerFullNameInput);
		  
		  
		  
		  
		  var applicationProductObj = selectedLoanApplicationDataInfo.product;
		  console.log("Product:",applicationProductObj);
		  console.log("Product Name:",applicationProductObj.productName);
		  console.log("Product Category:",applicationProductObj.productCatagory);
		  
		  var applicationId = selectedLoanApplicationDataInfo.id;
		  var applicationRequestedAmount = selectedLoanApplicationDataInfo.requestedamount;
		  var applicationStatus = selectedLoanApplicationDataInfo.status;
		  
		  var headingApplicationDetails = document.createElement("H3")                
		  var applicationDetails = document.createTextNode("Application Details"); 
		  headingApplicationDetails.appendChild(applicationDetails); 
		  loanApplicationForm.appendChild(headingApplicationDetails);
		  
		  
		  var applicationIdLabel = document.createElement("Label");
		  applicationIdLabel.innerHTML="Application Id";
		  loanApplicationForm.appendChild(applicationIdLabel);
		  
		  var applicationIdInput = document.createElement("input");
		  applicationIdInput.setAttribute("type", "text");
		  applicationIdInput.setAttribute("value", selectedLoanApplicationDataInfo.id);
		  applicationIdInput.setAttribute("name", selectedLoanApplicationDataInfo.id);
		  loanApplicationForm.appendChild(applicationIdInput);
		  
		  
		  var applicationRequestedAmountLabel = document.createElement("Label");
		  applicationRequestedAmountLabel.innerHTML="Application Requested Amount";
		  loanApplicationForm.appendChild(applicationRequestedAmountLabel);
		  
		  var applicationRequestedAmountInput = document.createElement("input");
		  applicationRequestedAmountInput.setAttribute("type", "text");
		  applicationRequestedAmountInput.setAttribute("value", selectedLoanApplicationDataInfo.requestedamount);
		  applicationRequestedAmountInput.setAttribute("name", selectedLoanApplicationDataInfo.requestedamount);
		  loanApplicationForm.appendChild(applicationRequestedAmountInput);
		  
		  var applicationStatusLabel = document.createElement("Label");
		  applicationStatusLabel.innerHTML="Application Status";
		  loanApplicationForm.appendChild(applicationStatusLabel);
		  
		  var applicationStatusInput = document.createElement("input");
		  applicationStatusInput.setAttribute("type", "text");
		  applicationStatusInput.setAttribute("value", selectedLoanApplicationDataInfo.status);
		  applicationStatusInput.setAttribute("name", selectedLoanApplicationDataInfo.status);
		  loanApplicationForm.appendChild(applicationStatusInput);
		  
		  
		  var headingProductDetails = document.createElement("H3")                
		  var productDetails = document.createTextNode("Product Details"); 
		  headingProductDetails.appendChild(productDetails); 
		  loanApplicationForm.appendChild(headingProductDetails);
		  
		  var productNameLabel = document.createElement("Label");
		  productNameLabel.innerHTML="Product Name";
		  loanApplicationForm.appendChild(productNameLabel);
		  
		  var productNameInput = document.createElement("input");
		  productNameInput.setAttribute("type", "text");
		  productNameInput.setAttribute("value", applicationProductObj.productName);
		  productNameInput.setAttribute("name", applicationProductObj.productName);
		  loanApplicationForm.appendChild(productNameInput);
		  
		  
		  var productCategoryLabel = document.createElement("Label");
		  productCategoryLabel.innerHTML="Product Category";
		  loanApplicationForm.appendChild(productCategoryLabel);
		  
		  var productCatagoryInput = document.createElement("input");
		  productCatagoryInput.setAttribute("type", "text");
		  productCatagoryInput.setAttribute("value", applicationProductObj.productCatagory);
		  productCatagoryInput.setAttribute("name", applicationProductObj.productCatagory);
		  loanApplicationForm.appendChild(productCatagoryInput);
		  
		  
		  if(applicationProductObj.productSpecification.length>0){
			  var headingProductAssumptions= document.createElement("H4")                
			  var productAssumptions = document.createTextNode("Product Assumptions Details"); 
			  headingProductAssumptions.appendChild(productAssumptions); 
			  loanApplicationForm.appendChild(headingProductAssumptions);  
		  }
		  
		  
		  for(j=0;j<applicationProductObj.productSpecification.length;j++){
			  console.log("Product Specification Name:",applicationProductObj.productSpecification[j].name);
			  console.log("Product Specification Value:",applicationProductObj.productSpecification[j].value);
			  
			  var productSpecificationLabel = document.createElement("Label");
			  productSpecificationLabel.innerHTML=applicationProductObj.productSpecification[j].name;
			  loanApplicationForm.appendChild(productSpecificationLabel);
			  
			  var productSpecificationInput = document.createElement("input");
			  productSpecificationInput.setAttribute("type", "text");
			  productSpecificationInput.setAttribute("value", applicationProductObj.productSpecification[j].value);
			  productSpecificationInput.setAttribute("name", applicationProductObj.productSpecification[j].name);
			  loanApplicationForm.appendChild(productSpecificationInput);
			  
		  }	
		  
		  
		  
		  console.log("Questions:",applicationProductObj.questions);
		  
		  if(applicationProductObj.questions.length>0){
			  var headingProductQuestions= document.createElement("H4")                
			  var productQuestions= document.createTextNode("Product Question Details"); 
			  headingProductQuestions.appendChild(productQuestions); 
			  loanApplicationForm.appendChild(headingProductQuestions);  
		  }
		  
		  
		  for(j=0;j<applicationProductObj.questions.length;j++){
			  console.log("Question Name:",applicationProductObj.questions[j].question);
			  console.log("Answer:",applicationProductObj.questions[j].answer);
			  
			  var productQuestionLabel = document.createElement("Label");
			  productQuestionLabel.innerHTML=applicationProductObj.questions[j].question;
			  loanApplicationForm.appendChild(productQuestionLabel);
			  
			  var productQuestionInput = document.createElement("input");
			  productQuestionInput.setAttribute("type", "text");
			  productQuestionInput.setAttribute("value", applicationProductObj.questions[j].answer);
			  productQuestionInput.setAttribute("name", applicationProductObj.questions[j].answer);
			  loanApplicationForm.appendChild(productQuestionInput);
		  }
		  
		  
		  if(selectedLoanApplicationDataInfo.status=="ACCEPTED"){
			  let buttonDisburse = document.createElement("input");
			  buttonDisburse.setAttribute("type","button");
			  buttonDisburse.setAttribute("name","submit");
			  buttonDisburse.setAttribute("id",applicationId); //user id
			  buttonDisburse.setAttribute("value","disburse"); // user Username
			  buttonDisburse.setAttribute("onClick","disburseLoan(this)");
			  let textDisburse = document.createTextNode("Disburse");
			  buttonDisburse.appendChild(textDisburse);
			  loanApplicationForm.appendChild(buttonDisburse);  
		  }
		  
		  if(selectedLoanApplicationDataInfo.status=="PENDINGFORAPPROVAL"){
			    let buttonAccepted = document.createElement("input");
			    buttonAccepted.setAttribute("type","button");
			    buttonAccepted.setAttribute("name","submit");
			    buttonAccepted.setAttribute("id",applicationId); //user id
			    buttonAccepted.setAttribute("value","Approve"); // user Username
			    buttonAccepted.setAttribute("onClick","updateLoanApplicationStatusToAccepted(this)");
			  	let textAccepted = document.createTextNode("Approve");
			  	buttonAccepted.appendChild(textAccepted);
			   	loanApplicationForm.appendChild(buttonAccepted); 
			   	
			   	
			   	let buttonRejected = document.createElement("input");
			   	buttonRejected.setAttribute("type","button");
			   	buttonRejected.setAttribute("name","submit");
			   	buttonRejected.setAttribute("id",applicationId); //user id
			   	buttonRejected.setAttribute("value","Reject"); // user Username
			   	buttonRejected.setAttribute("onClick","updateLoanApplicationStatusToRejected(this)");
			  	let textRejected = document.createTextNode("Reject");
			  	buttonRejected.appendChild(textRejected);
			   	loanApplicationForm.appendChild(buttonRejected);  
			   	
		  }
}


function disburseLoan(e){
	 
	console.log("Application Loan Id: "+e.id);
	console.log("Application Loan Value: "+e.value);
	
	var data = null;
	var userInfoJsonData=null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
		if (this.readyState == 4 && this.status == 200) {
		console.log(this.responseText);
		  
		  var jsonResponseData=JSON.parse(this.responseText);
			console.log(jsonResponseData);
			
			//As All Apis always returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=jsonResponseData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(jsonResponseData["Requested_Action"]);
				$('#content').load('ApplicationInfoDispursed.html');
				alert("Successfully Disbursed");
			}else{
				var requestedAction=jsonResponseData["Message"];
				alert(requestedAction);
				$('#content').load('ApplicationInfoPending.html');
			}
			
	  }
	});

	xhr.open("POST", "/LMSServer/disburseApprovedLoanUsingTransaction?applicationId="+e.id);
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);
 
}

function updateLoanApplicationStatusToAccepted(e){
	 
	console.log("Application Loan Id: "+e.id);
	console.log("Application Loan Value: "+e.value);
	
	var data = null;
	var userInfoJsonData=null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
		if (this.readyState == 4 && this.status == 200) {
		console.log(this.responseText);
		  
		  var jsonResponseData=JSON.parse(this.responseText);
			console.log(jsonResponseData);
			
			var requestedAction=jsonResponseData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(jsonResponseData["Requested_Action"]);
				$('#content').load('ApplicationInfoDispursed.html');
				alert("Successfully Accepted");
			}else{
				var requestedAction=jsonResponseData["Message"];
				alert(requestedAction);
				$('#content').load('ApplicationInfoPending.html');
			}
			
	  }
	});

	xhr.open("POST", "/LMSServer/updateLoanApplicationStatus?loanApplicationId="+e.id+"&loanApplicationStatus="+"Approved");
	
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);
 
}

function updateLoanApplicationStatusToRejected(e){
	 
	console.log("Application Loan Id: "+e.id);
	console.log("Application Loan Value: "+e.value);
	
	var data = null;
	var userInfoJsonData=null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
		if (this.readyState == 4 && this.status == 200) {
		console.log(this.responseText);
		  
		  var jsonResponseData=JSON.parse(this.responseText);
			console.log(jsonResponseData);
			
			var requestedAction=jsonResponseData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(jsonResponseData["Requested_Action"]);
				$('#content').load('ApplicationInfoDispursed.html');
				alert("Successfully Rejected");
			}else{
				var requestedAction=jsonResponseData["Message"];
				alert(requestedAction);
				$('#content').load('ApplicationInfoPending.html');
			}
			
	  }
	});

	xhr.open("POST", "/LMSServer/updateLoanApplicationStatus?loanApplicationId="+e.id+"&loanApplicationStatus="+"Rejected");
	
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);
 
}




function updateBegin(){

	if(validateForm()){
		processUpdateAssumption();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}

function begin(){

	if(validateForm()){
		processAddAssumption();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}


function validateForm(){ 
    var assumptionName = document.getElementById("assumptionName");   

//    var nameRegex = /^[a-zA-Z ]*$/;
//    var alphabetWithSpecialRegex = /^[A-Z@~`!@#$%^&*()_=+\\';:"\/?>.<,-]*$/;
    
    var alphabetSpaceAndUnderscoreRegex=/^[A-Za-z_ ]*$/;
    
    if (assumptionName.value == ""){ 
        alert("Please Enter AssumptionName Name"); 
        assumptionName.focus(); 
        return false; 
    }else{
    	if(alphabetSpaceAndUnderscoreRegex.test(assumptionName.value)==false){
    		alert("Please Enter Correct AssumptionName Name"); 
    		assumptionName.focus(); 
            return false; 
    	}
    } 

    return true; 
}






function processAddAssumption(){
	
//**********************************************************************AddUser Api
	var data = null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
	  /*if (this.readyState === 4) {
		console.log(this.responseText);
		
//		location.replace("ProductSpecificationAssumptionsInfo.html");
		alert("Successfully Added Assumption");
		$('#content').load('ProductSpecificationAssumptionsInfo.html');
		
	  }*/
	  
	  if (this.readyState == 4 && this.status == 200) {
			console.log(this.responseText);
			  
			  var jsonResponseData=JSON.parse(this.responseText);
				console.log(jsonResponseData);
				
				//As All Apis always returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=jsonResponseData["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
					console.log(this.responseText);
					
					alert("Successfully Added Assumption");
					$('#content').load('ProductSpecificationAssumptionsInfo.html');
				}else{
					var requestedAction=jsonResponseData["Message"];
					alert(requestedAction);
					$('#content').load('ApplicationInfoPending.html');
				}
		  }
	});

	var assumptionName = document.getElementById("assumptionName").value;
	
	var assumptionType = document.getElementById("assumptionType").value;
	
	console.log("Assumption Parameters are");
	console.log(assumptionName);
	console.log(assumptionType);

	xhr.open("POST", "/LMSServer/AddProductSpecificationAssumption?assumptionName="+assumptionName+"&assumptionDataType="+assumptionType+"&callerId="+callerId);
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);
}

function processUpdateAssumption(){
	
	//**********************************************************************UpdateAssumption Api
		var data = null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		  if (this.readyState === 4) {
			console.log(this.responseText);

			alert("Successfully Updated Assumption");
			
			deleteCookie("selectedAssumptionId");
			deleteCookie("selectedAssumption");
			
			$('#content').load('ProductSpecificationAssumptionsInfo.html');
		  }
		});

		
		var assumptionId=readCookie("selectedAssumptionId");
		var assumptionName = document.getElementById("assumptionName").value;
		var assumptionType = document.getElementById("assumptionType").value;
		
		console.log("Assumption Parameters are");
		console.log(assumptionId);
		console.log(assumptionName);
		console.log(assumptionType);

		xhr.open("POST", "/LMSServer/UpdateAssumption?assumptionId="+assumptionId+"&assumption="+assumptionName+"&assumptionType="+assumptionType+"&callerId="+callerId);
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
	}


/*$(window).unload(function() {
	//  $.cookies.del('name_of_your_cookie');
		alert("Unloading Current Window, Deleting Cookies");
		deleteCookie("selectedKycQuestionId");
		deleteCookie("selectedKycQuestion");
	});	
*/

function back(){
	
	deleteCookie("selectedAssumptionId");
	deleteCookie("selectedAssumption");
	
//	location.replace("ProductSpecificationAssumptionsInfo.html");
	$('#content').load('CreateNewAssumption.html');
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

function deleteCookie(name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
};