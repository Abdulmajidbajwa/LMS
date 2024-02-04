//window.onload = function() {
	loadAssumptions();
//	loadKycQuestions();
//};
	
	/*class assumption = {

			   Properties 
			  assumptionName: "",
			  assumptionValue: "",

			   Getter methods 
			  getAssumptionName: function() {
			    return this.assumptionName;
			  },
			  getAssumptionValue: function() {
			    return this.assumptionValue;
			  },

			   Setter methods 
			  setAssumptionName: function(assumptionName) {
			    this.assumptionName = assumptionName;
			  },
			  setAssumptionValue: function(assumptionValue) {
			    this.assumptionValue = assumptionValue;
			  }
			};*/
	
	
function loadAssumptions(){
	
//**********************************************************************GetProductSpecificationAssumption Api
	data = null;

	xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {

		if (this.readyState == 4 && this.status == 200) {
			var userInfoJsonData=JSON.parse(this.responseText);
			console.log(userInfoJsonData);
			
			//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=userInfoJsonData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(this.responseText);
//				this.userInfoJsonData=JSON.parse(this.responseText);
				 
				var tableData = userInfoJsonData.Table_Data;
				
				for(i=0;i<tableData.length;i++){
					  console.log("Record No:",i);
						console.log(tableData[i][1]);
						
						var checkBox = document.createElement("INPUT");
						checkBox.setAttribute("type", "checkbox");
						checkBox.setAttribute("id", (tableData[i][0]));
						checkBox.setAttribute("value", tableData[i][1]);
						document.getElementById("assumptionList").appendChild(checkBox);
						
						var label = document.createElement("LABEL");
						label.setAttribute("for", tableData[i][1]);
						label.innerHTML=tableData[i][1];
						document.getElementById("assumptionList").appendChild(label);
				  }
				loadKycQuestions();
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


function loadKycQuestions(){
	
//**********************************************************************GetKYCQuestion Api
	data = null;

	xhrz = new XMLHttpRequest();
	xhrz.withCredentials = true;

	xhrz.addEventListener("readystatechange", function () {
	  /*if (this.readyState === 4) {
		console.log(this.responseText);
		this.userInfoJsonData=JSON.parse(this.responseText);
		 
		var tableData = this.userInfoJsonData.Table_Data;
		
		for(i=0;i<tableData.length;i++){
			  console.log("Record No:",i);
				console.log(tableData[i][1]);
				
				var checkBox = document.createElement("INPUT");
				checkBox.setAttribute("type", "checkbox");
				checkBox.setAttribute("id", (tableData[i][0]));
				checkBox.setAttribute("value", tableData[i][(tableData.length-tableData.length)]);
				document.getElementById("kycQuestionsList").appendChild(checkBox);
				
				var label = document.createElement("LABEL");
				
				if(tableData[i][1].endsWith("?")){
					
					checkBox.setAttribute("value", tableData[i][1].substring(0, tableData[i][1].length-1));
			        
			        label.setAttribute("for", tableData[i][1].substring(0, tableData[i][1].length-1));
					label.innerHTML=tableData[i][1].substring(0, tableData[i][1].length-1);
					document.getElementById("kycQuestionsList").appendChild(label);
					
					if(tableData[i][5]=="required"){
						checkBox.checked=true;
						checkBox.disabled=true;
					}
					
			    }else{
			    	
			    	checkBox.setAttribute("value", tableData[i][1]);
			        
			        label.setAttribute("for", tableData[i][1]);
					label.innerHTML=tableData[i][1];
					document.getElementById("kycQuestionsList").appendChild(label);
					
					if(tableData[i][5].toLowerCase()=="required"){
						checkBox.checked=true;
						checkBox.disabled=true;
					}
			    }
				
				
				
		  }
		loadEditProductData();
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
				
				for(i=0;i<tableData.length;i++){
					  console.log("Record No:",i);
						console.log(tableData[i][1]);
						
						var checkBox = document.createElement("INPUT");
						checkBox.setAttribute("type", "checkbox");
						checkBox.setAttribute("id", (tableData[i][0]));
						checkBox.setAttribute("value", tableData[i][(tableData.length-tableData.length)]);
						document.getElementById("kycQuestionsList").appendChild(checkBox);
						
						var label = document.createElement("LABEL");
						
					    	checkBox.setAttribute("value", tableData[i][1]);
					        
					        label.setAttribute("for", tableData[i][1]);
							label.innerHTML=tableData[i][1];
							document.getElementById("kycQuestionsList").appendChild(label);
							
							if(tableData[i][5].toLowerCase()=="required"){
								checkBox.checked=true;
								checkBox.disabled=true;
							}
						
				  }
				loadEditProductData();
			}else{
				var requestedAction=userInfoJsonData["Message"];
				alert(requestedAction);
			}
	}
	});

	xhrz.open("POST", "/LMSServer/GetKYCQuestions");
	xhrz.setRequestHeader("authString", readCookie("Token"));
	
	xhrz.send(data);
}


function loadEditProductData(){
	var selectedProductIdCookieStatus = readCookie("selectedProductId");
	var selectedProductCookieStatus = readCookie("selectedProduct");

	alert("Product ID: "+selectedProductIdCookieStatus);
	alert("Product Name: "+selectedProductCookieStatus);
	
	
	if((selectedProductIdCookieStatus!==null && selectedProductIdCookieStatus!=="")
			&& (selectedProductCookieStatus!==null && selectedProductCookieStatus!=="")){
		
		
		var data = null;
		var editProductDataInfo=null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
			
			if (this.readyState == 4 && this.status == 200) {
				var editProductDataInfo=JSON.parse(this.responseText);
				console.log(editProductDataInfo);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=editProductDataInfo["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
//					this.editProductDataInfo=JSON.parse(this.responseText);
					  
					  
					  var tableHeader = editProductDataInfo.Table_Headers;
					  
					  console.log("Header:");
					  for(i=0;i<tableHeader.length;i++){
						  console.log(tableHeader[i]);
					  }
					  
					  
					  var tableData = editProductDataInfo.Table_Data;
					  var selectedProductRecordIndex=null;
					  
					  for(i=0;i<tableData.length;i++){
						  console.log("Record No:",i);
//						  for(j=0;j<tableHeader.length;j++){
//							console.log(tableData[i][j]);
							
							console.log("Comparing");
							console.log("Cookie Product Id: "+selectedProductIdCookieStatus);
							console.log("Comparing Product Id: "+tableData[i][0]);
							console.log("selectedProductIdCookieStatus==tableData[i][1]---Status: "+(selectedProductIdCookieStatus==tableData[i][0]));
							
							if((selectedProductIdCookieStatus==tableData[i][0]) && selectedProductRecordIndex==null){
								selectedProductRecordIndex=i;
								console.log("Record No(Selected ProductId): ",i);
								break;
							}
//						  }
					  }
					  
					  
					  console.log("Selected Product Id Details");
					  for(i=0;i<tableHeader.length;i++){
						  console.log(tableHeader[i]+ ": "+tableData[selectedProductRecordIndex][i]);
					  }
					  
					  document.getElementById("productName").value = tableData[selectedProductRecordIndex][1];
					  document.getElementById("productType").value = tableData[selectedProductRecordIndex][2];
					  
					  
					  console.log("Selected Assumptions List: "+tableData[selectedProductRecordIndex][3]);
					  if(tableData[selectedProductRecordIndex][3].startsWith(",")){
						  var assumptionsCommaString=checkAndGetCorrectString(tableData[selectedProductRecordIndex][3]);
						  var assumptionsArray=assumptionsCommaString.split(",");
						  
						  for(var i=0;i<assumptionsArray.length;i++)
						  {
							  console.log("Assumption: "+assumptionsArray[i]); 
							  
							  var inputs, index;

							  inputs = document.getElementsByTagName('input');
							  for (index = 0; index < inputs.length; ++index) {
							      // deal with inputs[index] element.
								  console.log("Input Value "+inputs[index].value);
								  console.log("Selected Assumption "+assumptionsArray[i]);
								  
								  
								  if(inputs[index].value==assumptionsArray[i]){
									  inputs[index].checked=true;
								  }
							  }
						  }
					 }
					  
					  /*console.log("Selected Assumptions Values List: "+tableData[selectedProductRecordIndex][4]);
					  if(tableData[selectedProductRecordIndex][4].startsWith(",")){
						  var assumptionCommaString=checkAndGetCorrectString(tableData[selectedProductRecordIndex][4]);
						  var assumptionArray=assumptionCommaString.split(",");
						  
						  console.log("Assumptions Values: "+assumptionCommaString); 
						  writeCookie("selectedProductAssumptionValues: ",assumptionCommaString, "1");
						  
						  for(var i=0;i<assumptionArray.length;i++)
						  {
							  assumption[i]=assumptionArray[i];
						  }
					  }*/
					  
					  console.log("Selected KYC Question List: "+tableData[selectedProductRecordIndex][5]);
					  if(tableData[selectedProductRecordIndex][5].startsWith(",")){
						  var permissionsCommaString=checkAndGetCorrectString(tableData[selectedProductRecordIndex][5]);
						  var permissionsArray=permissionsCommaString.split(",");
						  
						  for(var i=0;i<permissionsArray.length;i++)
						  {
							  console.log("KYC Question: "+permissionsArray[i]); 
							  
							  var inputs, index;

							  inputs = document.getElementsByTagName('input');
							  for (index = 0; index < inputs.length; ++index) {
								  
								  console.log("Input Value "+inputs[index].value);
								  console.log("Selected KYC Question "+permissionsArray[i]);
								  
								  if(permissionsArray[i].endsWith("?")){
									  console.log("KYC Question ends on ?");
									  console.log("KYC Question 1: "+inputs[index].value);
									  console.log("KYC Question 2: "+permissionsArray[i].substring(0,permissionsArray[i].length-1));
									  if(inputs[index].value==permissionsArray[i].substring(0,permissionsArray[i].length-1)){
										  inputs[index].checked=true;
									  }
								  }else{
									  if(inputs[index].value==permissionsArray[i]){
										  inputs[index].checked=true;
									  }  
								  }
								  
							  }
						  }
					  }
				}else{
					var requestedAction=editProductDataInfo["Message"];
					alert(requestedAction);
				}
	  	}
			
			
		});

		 //Hide Submit Button
		  document.getElementById('submit').style.visibility = 'hidden';
		  document.getElementById('update').style.visibility = 'visible';
		
		xhr.open("POST", "/LMSServer/GetAllProducts");
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
		
		
	}else{
//		alert("Cookie with Name:selectedRoleId Not Found");
//		alert("Cookie with Name:selectedRole Not Found");
		
//		diableRadioButtons();
//		document.getElementById("employeeUsername").disabled=false;
		
		//Hide Submit Button
		  document.getElementById('submit').style.visibility = 'visible';
		  document.getElementById('update').style.visibility = 'hidden';
	}
	
}



function begin(){

	if(validateForm()){
		processAddProduct();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}

function updateBegin(){

	if(validateForm()){
		processUpdateProduct();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}

/*function markCheckbox(assumptionsArrayInput){
	
	//**********************************************************************GetProductSpecificationAssumption Api
		data = null;

		xhr = new XMLHttpRequest();
		xhr.withCredentials = true;
//		var assumptionsArrayInput=assumptionsArray;

		xhr.addEventListener("readystatechange", function () {
		  if (this.readyState === 4) {
			console.log(this.responseText);
			this.userInfoJsonData=JSON.parse(this.responseText);
			 
			var tableData = this.userInfoJsonData.Table_Data;
			var assumptionListDiv=null;
			var selectedAssumptionIds = [];
			
			for(i=0;i<tableData.length;i++){
				    console.log("Record No:",i);
					console.log(tableData[i][1]);
					
					for(j=0;j<assumptionsArrayInput.length;j++){
						if(tableData[i][1]==assumptionsArrayInput[j]){
							
							assumptionListDiv = document.getElementById("assumptionList");
							alert('"'+(tableData[i][0])+'"');
							selectedAssumptionIds.push((tableData[i][0]));
						}
				   }
			  }
			
			
			for(m=0;m<assumptionListDiv.children.length;m++){
			    
		    	if(assumptionListDiv.children[m].localName=="input")
		    	alert(assumptionListDiv.children[m].id+" | "+assumptionListDiv.children[m].value);
		    	
		    	for(n=0;n<selectedAssumptionIds.children.length;n++){
		    		if(assumptionListDiv.children[n].id==){
		    			
		    		}
		    	}
		    	
		    }
		  }
		});

		xhr.open("POST", "/LMSServer/GetProductAssumptions");
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
	}*/



function validateForm(){ 
    var productName = document.getElementById("productName");  
    
//    var nameRegex = /^[a-zA-Z ]*$/;
//    alphabetSpaceWithSpecialRegex
//    var alphabetSpaceWithSpecialRegex = /^[A-Z@~`!@#$%^&*()_=+\\';:"\/?>.<,-]*$/;
    var alphabetSpaceAndUnderscoreRegex=/^[A-Za-z_ ]*$/;
    
    if (productName.value == ""){ 
        alert("Please Enter Product Name"); 
        productName.focus(); 
        return false; 
    }else{
    	if(alphabetSpaceAndUnderscoreRegex.test(productName.value)==false){
    		alert("Please Enter Correct Product Name"); 
    		productName.focus(); 
            return false; 
    	}
    }
    
    var noCheckBoxSelected=true;
    var assumptionListDivElements=document.getElementById("assumptionList").getElementsByTagName("input");
    for (var i = 0; i < assumptionListDivElements.length; i++){
        if (assumptionListDivElements[i].type == "checkbox"){
        	if (assumptionListDivElements[i].checked){
        		noCheckBoxSelected=false;
        	}
        }
    }
    if(noCheckBoxSelected){
    	alert("Please Select Atleast One Assumption");
    	return false;
    }
    

    return true; 
}


function processAddProduct(){
	
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
	
	var assumptionListSelectedCheckBoxes="";
	var kycQuestionListSelectedCheckBoxes="";
	
	var assumptionList = document.getElementById("assumptionList").childNodes; 
	 for (var j = 0; j < assumptionList.length; j++)
	 {
		if(assumptionList[j].nodeName =='INPUT'){
			if(assumptionList[j].checked){
//				 alert(assumptionList[j].value);
				 assumptionListSelectedCheckBoxes=assumptionListSelectedCheckBoxes+assumptionList[j].id+",";
			 }
		}
	 }
	 
	 var kycQuestionsList = document.getElementById("kycQuestionsList").childNodes; 
	 for (var j = 0; j < kycQuestionsList.length; j++)
	 {
		if(kycQuestionsList[j].nodeName =='INPUT'){
			if(kycQuestionsList[j].checked){
				 kycQuestionListSelectedCheckBoxes=kycQuestionListSelectedCheckBoxes+kycQuestionsList[j].id+",";
			 }
		}
	 }

	console.log("Saving Following Info in Cookies"); 
	console.log(productName);
	console.log(productType);
	console.log(assumptionListSelectedCheckBoxes);
	console.log(kycQuestionListSelectedCheckBoxes);
	
	
//	writeCookie('productId', readCookie("selectedProductId"), 1);
	writeCookie('productName', productName, 1);
	writeCookie('productType', productTypeStr, 1);
	writeCookie('AssumptionListSelectedCheckBoxes', assumptionListSelectedCheckBoxes, 1);
	writeCookie('KycQuestionListSelectedCheckBoxes', kycQuestionListSelectedCheckBoxes, 1);

//	location.replace("CreateNewProductScreenTwo.html");
	$('#content').load('CreateNewProductScreenTwo.html');
}


function processUpdateProduct(){
	
	var productName = document.getElementById("productName").value;
	
	var productTypeStr = document.getElementById("productType").value;
	
	var assumptionListSelectedCheckBoxes="";
	var kycQuestionListSelectedCheckBoxes="";
	
	var assumptionList = document.getElementById("assumptionList").childNodes; 
	 for (var j = 0; j < assumptionList.length; j++)
	 {
		if(assumptionList[j].nodeName =='INPUT'){
			if(assumptionList[j].checked){
//				 alert(assumptionList[j].value);
				 assumptionListSelectedCheckBoxes=assumptionListSelectedCheckBoxes+assumptionList[j].id+",";
			 }
		}
	 }
	 
	 var kycQuestionsList = document.getElementById("kycQuestionsList").childNodes; 
	 for (var j = 0; j < kycQuestionsList.length; j++)
	 {
		if(kycQuestionsList[j].nodeName =='INPUT'){
			if(kycQuestionsList[j].checked){
				 kycQuestionListSelectedCheckBoxes=kycQuestionListSelectedCheckBoxes+kycQuestionsList[j].id+",";
			 }
		}
	 }

	console.log("Saving Following Info in Cookies"); 
	console.log(productName);
	console.log(productType);
	console.log(assumptionListSelectedCheckBoxes);
	console.log(kycQuestionListSelectedCheckBoxes);
	
	
	writeCookie('productId', readCookie("selectedProductId"), 1);
	writeCookie('productName', productName, 1);
	writeCookie('productType', productTypeStr, 1);
	writeCookie('AssumptionListSelectedCheckBoxes', assumptionListSelectedCheckBoxes, 1);
	
	deleteCookie('KycQuestionListSelectedCheckBoxes');
	writeCookie('KycQuestionListSelectedCheckBoxes', kycQuestionListSelectedCheckBoxes, 1);

//	location.replace("CreateNewProductScreenTwo.html");
	$('#content').load('CreateNewProductScreenTwo.html');
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
	
	
	function back(){
//		location.replace("ProductInfo.html");
		$('#content').load('CreateNewProductScreenOne.html');
		}
	
	
	function checkAndGetCorrectString(cellInputString){
		console.log("CheckAndGetCorrectString--Start");
		console.log("Received String: "+cellInputString);
		
		var returnedString;
		
		if(cellInputString.startsWith(",")){
			returnedString=cellInputString.substring(1, cellInputString.length);
		}else{
			returnedString=cellInputString;
		}
		
		console.log("Returned String: "+returnedString);
		console.log("CheckAndGetCorrectString--End");
		return returnedString;
	}