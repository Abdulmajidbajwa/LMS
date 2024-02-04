//window.onload = function() {
	loadCompleteAssumptions();
//	loadCompleteKycQuestions();
//};



function loadCompleteAssumptions(){
	
//**********************************************************************GetProductSpecificationAssumption Api
	var data = null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
	 /* if (this.readyState === 4) {
		console.log(this.responseText);
		this.userInfoJsonData=JSON.parse(this.responseText);
		 
		var tableData = this.userInfoJsonData.Table_Data;
		
		var assumptionList=readCookie("AssumptionListSelectedCheckBoxes");
		var assumptionListArr = assumptionList.slice(0, -1).split(',');
		console.log(assumptionListArr);
		
		for(i=0;i<tableData.length;i++){
			  console.log("Record No:",i);
				console.log(tableData[i][1]);
				
				console.log("Selected KYC Questions");
				console.log(assumptionListArr);
				
				console.log(tableData[i][0]);
				
				console.log("Record going to check");
				console.log(assumptionListArr.includes(tableData[i][0]));
				
				
				if(assumptionListArr.includes(tableData[i][0])){
				var label = document.createElement("LABEL");
				label.setAttribute("for", tableData[i][1]);
				label.innerHTML=tableData[i][1];
				document.getElementById("assumptionList").appendChild(label);
				
				var input = document.createElement("INPUT");
				input.setAttribute("type", "text");
				input.setAttribute("id", tableData[i][0]);
				input.setAttribute("name", tableData[i][1]);
				document.getElementById("assumptionList").appendChild(input);
				
				
				}
		  }
		
		var assumtionList=readCookie("AssumptionListSelectedCheckBoxes");
		var assumtionListArr = assumtionList.split(',');
		console.log(assumtionListArr);
		
//		loadEditProductAssumptionsData();
	  }*/
	  
	  
	  if (this.readyState == 4 && this.status == 200) {
			var userInfoJsonData=JSON.parse(this.responseText);
			console.log(userInfoJsonData);
			
			//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=userInfoJsonData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(this.responseText);
//				this.userInfoJsonData=JSON.parse(this.responseText);
				 
				var tableData = userInfoJsonData.Table_Data;
				
				var assumptionList=readCookie("AssumptionListSelectedCheckBoxes");
				var assumptionListArr = assumptionList.slice(0, -1).split(',');
				console.log(assumptionListArr);
				
				for(i=0;i<tableData.length;i++){
					  console.log("Record No:",i);
						console.log(tableData[i][1]);
						
						console.log("Selected KYC Questions");
						console.log(assumptionListArr);
						
						console.log(tableData[i][0]);
						
						console.log("Record going to check");
						console.log(assumptionListArr.includes(tableData[i][0]));
						
						
						if(assumptionListArr.includes(tableData[i][0])){
						var label = document.createElement("LABEL");
						label.setAttribute("for", tableData[i][1]);
						label.innerHTML=tableData[i][1];
						document.getElementById("assumptionList").appendChild(label);
						
						var input = document.createElement("INPUT");
						input.setAttribute("type", "text");
						input.setAttribute("id", tableData[i][0]);
						input.setAttribute("name", tableData[i][1]);
						document.getElementById("assumptionList").appendChild(input);
						
						
						}
				  }
				
				var assumtionList=readCookie("AssumptionListSelectedCheckBoxes");
				var assumtionListArr = assumtionList.split(',');
				console.log(assumtionListArr);
			}else{
				var requestedAction=userInfoJsonData["Message"];
				alert(requestedAction);
			}
	}
	});

	xhr.open("POST", "/LMSServer/GetProductAssumptions");
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);
}



//function loadEditProductAssumptionsData(){
//	var selectedProductAssumptionValuesCookieStatus = readCookie("selectedProductAssumptionValues");
//
//	alert("Product Assumption Values: "+selectedProductAssumptionValuesCookieStatus);
//	
//	
//	
//	var dict = new Object();
//
//	dict["Channel Fee"]=50;
//	dict["Grace Period"]=10;
//
//	writeCookie("AssumptionListCookies", JSON.stringify(dict), 1);
//
//	var getProperty = function (propertyName) {
//	  var assumptionListCookies = JSON.parse(readCookie("assumptionListCookies"));
//	  return assumptionListCookies[propertyName];
//	};
//
//	alert(getProperty("Channel Fee"));
//	alert(getProperty("Grace Period"));
//
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	if(selectedProductAssumptionValuesCookieStatus!==null && selectedProductAssumptionValuesCookieStatus!==""){
//		
//		var data = null;
//		var editProductDataInfo=null;
//
//		var xhr = new XMLHttpRequest();
//		xhr.withCredentials = true;
//
//		xhr.addEventListener("readystatechange", function () {
//		  if (this.readyState === 4) {
//			  
//			//Set Fields Values here
//			  console.log(selectedProductAssumptionValuesCookieStatus);
//			  
//		  
//		  }
//		});
//
//		xhr.open("POST", "/LMSServer/GetAllProducts");
//		xhr.setRequestHeader("authString", readCookie("Token"));
//		
//		xhr.send(data);
//		
//		
//	}else{
//		
//		//Hide Submit Button
//		  document.getElementById('submit').style.visibility = 'visible';
//		  document.getElementById('update').style.visibility = 'hidden';
//	}
//	
//}







function begin(){
	
	//**********************************************************************AddProduct Api
		var data = null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		  /*if (this.readyState === 4) {
			console.log(this.responseText);
			
//			location.replace("ProductInfo.html");
			$('#content').load('ProductInfo.html');			
		  }*/
		  
		  
		  if (this.readyState == 4 && this.status == 200) {
				var responseJsonData=JSON.parse(this.responseText);
				console.log(responseJsonData);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=responseJsonData["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
					console.log(this.responseText);
					
//					location.replace("ProductInfo.html");
					$('#content').load('ProductInfo.html');		
				}else{
					var requestedAction=responseJsonData["Message"];
					alert(requestedAction);
				}
	  	}
		  
		});

		
		
		var productName = readCookie("productName");
		var productType = readCookie("productType");
		var loggedInUserId = readCookie("LoggedInUserId");
		
		var selectedAssumptionsElements = document.getElementById("assumptionList").getElementsByTagName("INPUT");

		console.log("Selected Assumptions Names and Values"); 
		
		for (var j = 0; j < selectedAssumptionsElements.length; j++)
		{
			console.log(selectedAssumptionsElements);
			console.log(selectedAssumptionsElements[j].name);
			console.log(selectedAssumptionsElements[j].value);
		}
		
		var kycQuestionsList=readCookie("KycQuestionListSelectedCheckBoxes");
		var kycQuestionsListArr = kycQuestionsList.slice(0, -1).split(',');
		console.log(kycQuestionsListArr);
		
		var kycQuestionsListStr = kycQuestionsListArr.toString();
		
		var assumptionList=readCookie("AssumptionListSelectedCheckBoxes");
		var assumptionListArr = assumptionList.slice(0, -1).split(',');
		console.log(assumptionListArr);
		
		var assumptionListStr = assumptionListArr.toString();
		
		console.log("Products Parameters are");
		console.log(productName);
		console.log(productType);
		console.log(kycQuestionsListStr);
		
		console.log(assumptionListStr);
		
		
		var selectedAssumptionsElements = document.getElementById("assumptionList").getElementsByTagName("INPUT");

		console.log("Selected Assumptions Names and Values"); 
		
		var selectedAssumptionsValues="";
		for (var j = 0; j < selectedAssumptionsElements.length; j++)
		{
			console.log(selectedAssumptionsElements);
			console.log(selectedAssumptionsElements[j].name);
			console.log(selectedAssumptionsElements[j].value);
			
			if(validateAssumptionValues(selectedAssumptionsElements[j].value)){
				selectedAssumptionsValues=selectedAssumptionsValues+(selectedAssumptionsElements[j].value)+",";
			}else{
				return;
			}
		}
		
		selectedAssumptionsValues=selectedAssumptionsValues.slice(0, -1);
		
		console.log(selectedAssumptionsValues);
		
		console.log("Complete AddProduct API String");
		console.log("/LMSServer/AddProduct?productCatagory="+productType+"&productName="+productName+"&KYCQuestionIds="+kycQuestionsListStr+"&assumptionsIds="+assumptionListStr+"&assumptionsValues="+selectedAssumptionsValues+"&callerId="+loggedInUserId);
		
		xhr.open("POST", "/LMSServer/AddProduct?productCatagory="+productType+"&productName="+productName+"&KYCQuestionIds="+kycQuestionsListStr+"&assumptionsIds="+assumptionListStr+"&assumptionsValues="+selectedAssumptionsValues+"&callerId="+loggedInUserId);
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
	}


function beginUpdate(){
	
	//**********************************************************************AddProduct Api
		var data = null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		 /* if (this.readyState === 4) {
			console.log(this.responseText);
			
			deleteCookie("selectedProductId");
			deleteCookie("selectedProduct");
			
			
//			location.replace("ProductInfo.html");
			$('#content').load('ProductInfo.html');			
		  }*/
		  
		  if (this.readyState == 4 && this.status == 200) {
				var responseJsonData=JSON.parse(this.responseText);
				console.log(responseJsonData);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=responseJsonData["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
					console.log(this.responseText);
					
					deleteCookie("selectedProductId");
					deleteCookie("selectedProduct");
					
					$('#content').load('ProductInfo.html');			
				}else{
					var requestedAction=responseJsonData["Message"];
					alert(requestedAction);
				}
	  	}
		  
		  
		});

		
		
		var productId = readCookie("productId");
		var productName = readCookie("productName");
		var productType = readCookie("productType");
		
		var selectedAssumptionsElements = document.getElementById("assumptionList").getElementsByTagName("INPUT");

		console.log("Selected Assumptions Names and Values"); 
		
		for (var j = 0; j < selectedAssumptionsElements.length; j++)
		{
			console.log(selectedAssumptionsElements);
			console.log(selectedAssumptionsElements[j].name);
			console.log(selectedAssumptionsElements[j].value);
		}
		
		var kycQuestionsList=readCookie("KycQuestionListSelectedCheckBoxes");
		var kycQuestionsListArr = kycQuestionsList.slice(0, -1).split(',');
		console.log(kycQuestionsListArr);
		
		var kycQuestionsListStr = kycQuestionsListArr.toString();
		
		var assumptionList=readCookie("AssumptionListSelectedCheckBoxes");
		var assumptionListArr = assumptionList.slice(0, -1).split(',');
		console.log(assumptionListArr);
		
		var assumptionListStr = assumptionListArr.toString();
		
		console.log("Products Parameters are");
		console.log(productId);
		console.log(productName);
		console.log(productType);
		console.log(kycQuestionsListStr);
		
		console.log(assumptionListStr);
		
		
		var selectedAssumptionsElements = document.getElementById("assumptionList").getElementsByTagName("INPUT");

		console.log("Selected Assumptions Names and Values"); 
		
		var selectedAssumptionsValues="";
		for (var j = 0; j < selectedAssumptionsElements.length; j++)
		{
			console.log(selectedAssumptionsElements);
			console.log(selectedAssumptionsElements[j].name);
			console.log(selectedAssumptionsElements[j].value);
			
			if(validateAssumptionValues(selectedAssumptionsElements[j].value)){
				selectedAssumptionsValues=selectedAssumptionsValues+(selectedAssumptionsElements[j].value)+",";
			}else{
				return;
			}
		}
		
		selectedAssumptionsValues=selectedAssumptionsValues.slice(0, -1);
		
		console.log(selectedAssumptionsValues);
		
		console.log("Complete AddProduct API String");
		console.log("/LMSServer/UpdateProduct?productId="+productId+"&productCatagory="+productType+"&productName="+productName+"&KYCQuestionIds="+kycQuestionsListStr+"&assumptionsIds="+assumptionListStr+"&assumptionsValues="+selectedAssumptionsValues+"&callerId="+loggedInUserId);
		
		
		xhr.open("POST", "/LMSServer/UpdateProduct?productId="+productId+"&productCatagory="+productType+"&productName="+productName+"&KYCQuestionIds="+kycQuestionsListStr+"&assumptionsIds="+assumptionListStr+"&assumptionsValues="+selectedAssumptionsValues+"&callerId="+loggedInUserId);
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
	}



function validateAssumptionValues(valueNeedsToCheck){
	 var numericRegex = /^[0-9]*[1-9][0-9]*$/;
	
	 console.log("Received Parameters");
	 console.log("Value Needs to Check: "+valueNeedsToCheck);
	 
	if (valueNeedsToCheck == ""){ 
        alert("Please Enter Missing Data"); 
        return false; 
    }else{
    	if(numericRegex.test(valueNeedsToCheck)==false){
    		alert("Please Enter Correct Data"); 
            return false; 
    	}else{
    		return true;
    	}
    }
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
//		location.replace("ProductInfo.html");
		$('#content').load('CreateNewProductScreenOne.html');
	
	}