//Execute Load Roles DropDown Function
//window.onload = function() {
//	diableRadioButtons();
	var loggedInUserId = readCookie("LoggedInUserId");
	loadEditRoleData();
	
//};
function diableRadioButtons(){
	var inputs = document.getElementsByTagName('input');

	for(var i = 0; i < inputs.length; i++) {
	    if(inputs[i].type.toLowerCase() == 'radio') {
	    	inputs[i].disabled = true;
	    }
	}
}

function loadEditRoleData(){
	var selectedRoleIdCookieStatus = readCookie("selectedRoleId");
	var selectedRoleCookieStatus = readCookie("selectedRole");

	if((selectedRoleIdCookieStatus!==null && selectedRoleIdCookieStatus!=="")
			&& (selectedRoleCookieStatus!==null && selectedRoleCookieStatus!=="")){
//		alert("Cookie with Name:selectedRoleId"+" Having Value: "+selectedRoleIdCookieStatus);
//		alert("Cookie with Name:selectedRole"+" Having Value: "+selectedRoleCookieStatus);
		
		
		var data = null;
		var editRoleDataInfo=null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		 /* if (this.readyState === 4) {
			  
			  this.editRoleDataInfo=JSON.parse(this.responseText);
			  
			  
			  var tableHeader = this.editRoleDataInfo.Table_Headers;
			  
			  console.log("Header:");
			  for(i=0;i<tableHeader.length;i++){
				  console.log(tableHeader[i]);
			  }
			  
			  
			  var tableData = this.editRoleDataInfo.Table_Data;
			  var selectedRoleRecordIndex=null;
			  
			  for(i=0;i<tableData.length;i++){
				  console.log("Record No:",i);
				  for(j=0;j<tableHeader.length;j++){
					console.log(tableData[i][j]);
					
					console.log("Comparing");
					console.log("Cookie Role Id: "+selectedRoleIdCookieStatus);
					console.log("Comparing Role Id: "+tableData[i][1]);
					console.log("selectedRoleIdCookieStatus==tableData[i][1]---Status: "+(selectedRoleIdCookieStatus==tableData[i][1]));
					
					if((selectedRoleIdCookieStatus==tableData[i][1]) && selectedRoleRecordIndex==null){
						selectedRoleRecordIndex=i;
						console.log("Record No(Selected RoleId): ",i);
					}
				 }
			  }
			  
			  
			  console.log("Selected Role Id Details");
			  for(i=0;i<tableHeader.length;i++){
				  console.log(tableHeader[i]+ ": "+tableData[selectedRoleRecordIndex][i]);
			  }
			  
			  document.getElementById("roleName").value = tableData[selectedRoleRecordIndex][2];
			  document.getElementById("roleName").disabled=true;
			  
			  document.getElementById("roleDiscription").value = tableData[selectedRoleRecordIndex][3];
			  
			  
			  console.log("Permissions: "+tableData[selectedRoleRecordIndex][4]);
			  if(tableData[selectedRoleRecordIndex][4].startsWith(",")){
				  var permissionsCommaString=checkAndGetCorrectString(tableData[selectedRoleRecordIndex][4]);
		       	
				  
				  var permissionsArray=permissionsCommaString.split(",");
				  
				  for(var i=0;i<permissionsArray.length;i++)
					{
					  console.log("Permission No: "+i);
					  console.log("Permission: "+permissionsArray[i]);
					  
					  if(permissionsArray[i]=="USER_MANAGEMENT_R"){
						  document.getElementById("userMgmt").checked = true;
						  document.getElementById("readOptionForUserMgmt").checked = true;
					  }else if(permissionsArray[i]=="USER_MANAGEMENT_RW"){
						  document.getElementById("readOptionForUserMgmt").checked = true;
						  document.getElementById("readAndWriteOptionForUserMgmt").checked = true;
						  
					  }else if(permissionsArray[i]=="CUSTOMER_MANAGEMENT_R"){
						  document.getElementById("customerManagement").checked = true;
						  document.getElementById("readOptionForCustomerManagement").checked = true;
					  }else if(permissionsArray[i]=="CUSTOMER_MANAGEMENT_RW"){
						  document.getElementById("customerManagement").checked = true;
						  document.getElementById("readAndWriteOptionForCustomerManagement").checked = true;
						  
					  }else if(permissionsArray[i]=="PRODUCT_MANAGEMENT_R"){
						  document.getElementById("productManagement").checked = true;
						  document.getElementById("readOptionForProductManagement").checked = true;
					  }else if(permissionsArray[i]=="PRODUCT_MANAGEMENT_RW"){
						  document.getElementById("productManagement").checked = true;
						  document.getElementById("readAndWriteOptionForProductManagement").checked = true;
						  
					  }else if(permissionsArray[i]=="ASSUMPTION_MANAGEMENT_R"){
						  document.getElementById("assumptionManagement").checked = true;
						  document.getElementById("readOptionForAssumptionManagement").checked = true;
					  }else if(permissionsArray[i]=="ASSUMPTION_MANAGEMENT_RW"){
						  document.getElementById("assumptionManagement").checked = true;
						  document.getElementById("readAndWriteOptionForAssumptionManagement").checked = true;
						  
					  }else if(permissionsArray[i]=="KYC_MANAGEMENT_R"){
						  document.getElementById("kycManagement").checked = true;
						  document.getElementById("readOptionForKYCManagement").checked = true;
					  }else if(permissionsArray[i]=="KYC_MANAGEMENT_RW"){
						  document.getElementById("kycManagement").checked = true;
						  document.getElementById("readAndWriteOptionForKYCManagement").checked = true;
						  
					  }else if(permissionsArray[i]=="ROLE_MANAGEMENT_R"){
						  document.getElementById("roleManagement").checked = true;
						  document.getElementById("readOptionForRoleManagement").checked = true;
					  }else if(permissionsArray[i]=="ROLE_MANAGEMENT_RW"){
						  document.getElementById("roleManagement").checked = true;
						  document.getElementById("readAndWriteOptionForRoleManagement").checked = true;
						  
					  }else if(permissionsArray[i]=="PRIV_MANAGEMENT_R"){
						  document.getElementById("privilegeManagement").checked = true;
						  document.getElementById("readOptionForPrivilegeManagement").checked = true;
					  }else if(permissionsArray[i]=="PRIV_MANAGEMENT_RW"){
						  document.getElementById("privilegeManagement").checked = true;
						  document.getElementById("readAndWriteOptionForPrivilegeManagement").checked = true;
						  
					  }else if(permissionsArray[i]=="GENERAL_LEDGER_MANAGEMENT_R"){
						  document.getElementById("generalLedgerManagement").checked = true;
						  document.getElementById("readOptionForGeneralLedgerManagement").checked = true;
					  }else if(permissionsArray[i]=="GENERAL_LEDGER_MANAGEMENT_RW"){
						  document.getElementById("generalLedgerManagement").checked = true;
						  document.getElementById("readAndWriteOptionForGeneralLedgerManagement").checked = true;
						  
					  }else if(permissionsArray[i]=="LOAN_MANAGEMENT_R"){
						  document.getElementById("loanManagement").checked = true;
						  document.getElementById("readOptionForLoanManagement").checked = true;
					  }else{
						  if(permissionsArray[i]=="LOAN_MANAGEMENT_RW"){
							  document.getElementById("loanManagement").checked = true;
							  document.getElementById("readAndWriteOptionForLoanManagement").checked = true;
						  }
					  }
					  
					  
					}
			  
			  }
			  
			  //Hide Submit Button
			  document.getElementById('submit').style.visibility = 'hidden';
			  document.getElementById('submit').type = 'hidden';
			  document.getElementById('update').style.visibility = 'visible';
			  document.getElementById('update').type = 'button';
		  
		  }*/
		  
		  
		  if (this.readyState == 4 && this.status == 200) {
				var editRoleDataInfo=JSON.parse(this.responseText);
				console.log(editRoleDataInfo);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=editRoleDataInfo["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
					
//					this.editRoleDataInfo=JSON.parse(this.responseText);
				  
				  
				  var tableHeader = editRoleDataInfo.Table_Headers;
				  
				  console.log("Header:");
				  for(i=0;i<tableHeader.length;i++){
					  console.log(tableHeader[i]);
				  }
				  
				  
				  var tableData = editRoleDataInfo.Table_Data;
				  var selectedRoleRecordIndex=null;
				  
				  for(i=0;i<tableData.length;i++){
					  console.log("Record No:",i);
					  for(j=0;j<tableHeader.length;j++){
						console.log(tableData[i][j]);
						
						console.log("Comparing");
						console.log("Cookie Role Id: "+selectedRoleIdCookieStatus);
						console.log("Comparing Role Id: "+tableData[i][1]);
						console.log("selectedRoleIdCookieStatus==tableData[i][1]---Status: "+(selectedRoleIdCookieStatus==tableData[i][1]));
						
						if((selectedRoleIdCookieStatus==tableData[i][1]) && selectedRoleRecordIndex==null){
							selectedRoleRecordIndex=i;
							console.log("Record No(Selected RoleId): ",i);
						}
					 }
				  }
				  
				  
				  console.log("Selected Role Id Details");
				  for(i=0;i<tableHeader.length;i++){
					  console.log(tableHeader[i]+ ": "+tableData[selectedRoleRecordIndex][i]);
				  }
				  
				  document.getElementById("roleName").value = tableData[selectedRoleRecordIndex][2];
				  document.getElementById("roleName").disabled=true;
				  
				  document.getElementById("roleDiscription").value = tableData[selectedRoleRecordIndex][3];
				  
				  
				  console.log("Permissions: "+tableData[selectedRoleRecordIndex][4]);
				  if(tableData[selectedRoleRecordIndex][4].startsWith(",")){
					  var permissionsCommaString=checkAndGetCorrectString(tableData[selectedRoleRecordIndex][4]);
			       	
					  
					  var permissionsArray=permissionsCommaString.split(",");
					  
					  for(var i=0;i<permissionsArray.length;i++)
						{
						  console.log("Permission No: "+i);
						  console.log("Permission: "+permissionsArray[i]);
						  
						  if(permissionsArray[i]=="USER_MANAGEMENT_R"){
							  document.getElementById("userMgmt").checked = true;
							  document.getElementById("readOptionForUserMgmt").checked = true;
						  }else if(permissionsArray[i]=="USER_MANAGEMENT_RW"){
							  document.getElementById("readOptionForUserMgmt").checked = true;
							  document.getElementById("readAndWriteOptionForUserMgmt").checked = true;
							  
						  }else if(permissionsArray[i]=="CUSTOMER_MANAGEMENT_R"){
							  document.getElementById("customerManagement").checked = true;
							  document.getElementById("readOptionForCustomerManagement").checked = true;
						  }else if(permissionsArray[i]=="CUSTOMER_MANAGEMENT_RW"){
							  document.getElementById("customerManagement").checked = true;
							  document.getElementById("readAndWriteOptionForCustomerManagement").checked = true;
							  
						  }else if(permissionsArray[i]=="PRODUCT_MANAGEMENT_R"){
							  document.getElementById("productManagement").checked = true;
							  document.getElementById("readOptionForProductManagement").checked = true;
						  }else if(permissionsArray[i]=="PRODUCT_MANAGEMENT_RW"){
							  document.getElementById("productManagement").checked = true;
							  document.getElementById("readAndWriteOptionForProductManagement").checked = true;
							  
						  }else if(permissionsArray[i]=="ASSUMPTION_MANAGEMENT_R"){
							  document.getElementById("assumptionManagement").checked = true;
							  document.getElementById("readOptionForAssumptionManagement").checked = true;
						  }else if(permissionsArray[i]=="ASSUMPTION_MANAGEMENT_RW"){
							  document.getElementById("assumptionManagement").checked = true;
							  document.getElementById("readAndWriteOptionForAssumptionManagement").checked = true;
							  
						  }else if(permissionsArray[i]=="KYC_MANAGEMENT_R"){
							  document.getElementById("kycManagement").checked = true;
							  document.getElementById("readOptionForKYCManagement").checked = true;
						  }else if(permissionsArray[i]=="KYC_MANAGEMENT_RW"){
							  document.getElementById("kycManagement").checked = true;
							  document.getElementById("readAndWriteOptionForKYCManagement").checked = true;
							  
						  }else if(permissionsArray[i]=="ROLE_MANAGEMENT_R"){
							  document.getElementById("roleManagement").checked = true;
							  document.getElementById("readOptionForRoleManagement").checked = true;
						  }else if(permissionsArray[i]=="ROLE_MANAGEMENT_RW"){
							  document.getElementById("roleManagement").checked = true;
							  document.getElementById("readAndWriteOptionForRoleManagement").checked = true;
							  
						  }else if(permissionsArray[i]=="PRIV_MANAGEMENT_R"){
							  document.getElementById("privilegeManagement").checked = true;
							  document.getElementById("readOptionForPrivilegeManagement").checked = true;
						  }else if(permissionsArray[i]=="PRIV_MANAGEMENT_RW"){
							  document.getElementById("privilegeManagement").checked = true;
							  document.getElementById("readAndWriteOptionForPrivilegeManagement").checked = true;
							  
						  }else if(permissionsArray[i]=="GENERAL_LEDGER_MANAGEMENT_R"){
							  document.getElementById("generalLedgerManagement").checked = true;
							  document.getElementById("readOptionForGeneralLedgerManagement").checked = true;
						  }else if(permissionsArray[i]=="GENERAL_LEDGER_MANAGEMENT_RW"){
							  document.getElementById("generalLedgerManagement").checked = true;
							  document.getElementById("readAndWriteOptionForGeneralLedgerManagement").checked = true;
							  
						  }else if(permissionsArray[i]=="LOAN_MANAGEMENT_R"){
							  document.getElementById("loanManagement").checked = true;
							  document.getElementById("readOptionForLoanManagement").checked = true;
						  }else{
							  if(permissionsArray[i]=="LOAN_MANAGEMENT_RW"){
								  document.getElementById("loanManagement").checked = true;
								  document.getElementById("readAndWriteOptionForLoanManagement").checked = true;
							  }
						  }
						  
						  
						}
				  
				  }
				  
				  //Hide Submit Button
				  document.getElementById('submit').style.visibility = 'hidden';
				  document.getElementById('submit').type = 'hidden';
				  document.getElementById('update').style.visibility = 'visible';
				  document.getElementById('update').type = 'button';
				  }else{
					var requestedAction=editRoleDataInfo["Message"];
					alert(requestedAction);
				}
	  	}
		  
		  
		  
		});

		xhr.open("POST", "/LMSServer/GetAllRoles");
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
	}else{
		
		diableRadioButtons();
//		document.getElementById("employeeUsername").disabled=false;
		
		//Hide Submit Button
		  document.getElementById('submit').style.visibility = 'visible';
		  document.getElementById('submit').type = 'button';
		  document.getElementById('update').style.visibility = 'hidden';
		  document.getElementById('update').type = 'hidden';
	}
	
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

function handleRadioButton(clicked_id) {

//	alert(clicked_id);
	if(clicked_id.toUpperCase()=="USERMGMT"){
		
		// is userMgmt checked?
	    var userMgmtCheckBoxStatus = document.getElementById('userMgmt').checked; // true or false

	    // get list of radio buttons with name 'readAndWriteOptionForUserMgmt'
	    var radios = document.getElementsByName('readAndWriteOptionForUserMgmt');

	    // loop through list of radio buttons
	    for (var i=0, len=radios.length; i<len; i++) {
	        var currentRadioButton = radios[i]; // current radio button
	        if ( userMgmtCheckBoxStatus ) { // userMgmt checkbox checked
	            currentRadioButton.disabled=false;
//	            console.log(currentRadioButton.value);
	        } else { // kycValidation not checked
	        	currentRadioButton.disabled = true; // no radios disabled
	        	currentRadioButton.checked = false;
	        }
	    }
	}else if(clicked_id.toUpperCase()=="CUSTOMERMANAGEMENT"){
		// is regCustomerList checked?
	    var customerMgmtCheckBoxStatus = document.getElementById('customerManagement').checked; // true or false

	    // get list of radio buttons with name 'readAndWriteOptionForRegCustomerList'
	    var radios = document.getElementsByName('readAndWriteOptionForCustomerManagement');

	    // loop through list of radio buttons
	    for (var i=0, len=radios.length; i<len; i++) {
	        var currentRadioButton = radios[i]; // current radio button

	        if ( customerMgmtCheckBoxStatus ) { // regCustomerList checkbox checked
	            currentRadioButton.disabled=false;
	        } else { // kycValidation not checked
	        	currentRadioButton.disabled = true; // no radios disabled
	        	currentRadioButton.checked = false;
	        }
	    }
	}else if(clicked_id.toUpperCase()=="PRODUCTMANAGEMENT"){

		var productMgmtCheckBoxStatus = document.getElementById('productManagement').checked; // true or false

	    var radios = document.getElementsByName('readAndWriteOptionForProductManagement');

	    // loop through list of radio buttons
	    for (var i=0, len=radios.length; i<len; i++) {
	        var currentRadioButton = radios[i]; // current radio button

	        if ( productMgmtCheckBoxStatus ) { 
	            currentRadioButton.disabled=false;
	        } else { // notificationMenu not checked
	        	currentRadioButton.disabled = true; // no radios disabled
	        	currentRadioButton.checked = false;
	        }

	    }
	}else if(clicked_id.toUpperCase()=="ASSUMPTIONMANAGEMENT"){
		// is kycValidation checked?
	    var assumptionMgmtCheckBoxStatus = document.getElementById('assumptionManagement').checked; // true or false

	    // get list of radio buttons with name 'readAndWriteOptionForKYCValidation'
	    var radios = document.getElementsByName('readAndWriteOptionForAssumptionManagement');

	    // loop through list of radio buttons
	    for (var i=0, len=radios.length; i<len; i++) {
	        var currentRadioButton = radios[i]; // current radio button

	        if ( assumptionMgmtCheckBoxStatus ) { // kycValidation checkbox checked
	            currentRadioButton.disabled=false;
	        } else { // kycValidation not checked
	        	currentRadioButton.disabled = true; // no radios disabled
	        	currentRadioButton.checked = false;
	        }

	    }
	}else if(clicked_id.toUpperCase()=="KYCMANAGEMENT"){
		// is kycValidation checked?
	    var kycMgmtCheckBoxStatus = document.getElementById('kycManagement').checked; // true or false

	    // get list of radio buttons with name 'readAndWriteOptionForKYCValidation'
	    var radios = document.getElementsByName('readAndWriteOptionForKYCManagement');

	    // loop through list of radio buttons
	    for (var i=0, len=radios.length; i<len; i++) {
	        var currentRadioButton = radios[i]; // current radio button

	        if ( kycMgmtCheckBoxStatus ) { // kycValidation checkbox checked
	            currentRadioButton.disabled=false;
	        } else { // kycValidation not checked
	        	currentRadioButton.disabled = true; // no radios disabled
	        	currentRadioButton.checked = false;
	        }

	    }
	}else if(clicked_id.toUpperCase()=="ROLEMANAGEMENT"){
		// is kycValidation checked?
	    var roleMgmtCheckBoxStatus = document.getElementById('roleManagement').checked; // true or false

	    // get list of radio buttons with name 'readAndWriteOptionForKYCValidation'
	    var radios = document.getElementsByName('readAndWriteOptionForRoleManagement');

	    // loop through list of radio buttons
	    for (var i=0, len=radios.length; i<len; i++) {
	        var currentRadioButton = radios[i]; // current radio button

	        if ( roleMgmtCheckBoxStatus ) { // kycValidation checkbox checked
	            currentRadioButton.disabled=false;
	        } else { // kycValidation not checked
	        	currentRadioButton.disabled = true; // no radios disabled
	        	currentRadioButton.checked = false;
	        }

	    }
	}else if(clicked_id.toUpperCase()=="PRIVILEGEMANAGEMENT"){
		// is kycValidation checked?
	    var privMgmtCheckBoxStatus = document.getElementById('privilegeManagement').checked; // true or false

	    // get list of radio buttons with name 'readAndWriteOptionForKYCValidation'
	    var radios = document.getElementsByName('readAndWriteOptionForPrivilegeManagement');

	    // loop through list of radio buttons
	    for (var i=0, len=radios.length; i<len; i++) {
	        var currentRadioButton = radios[i]; // current radio button

	        if ( privMgmtCheckBoxStatus ) { // kycValidation checkbox checked
	            currentRadioButton.disabled=false;
	        } else { // kycValidation not checked
	        	currentRadioButton.disabled = true; // no radios disabled
	        	currentRadioButton.checked = false;
	        }

	    }
	}else if(clicked_id.toUpperCase()=="GENERALLEDGERMANAGEMENT"){
		// is kycValidation checked?
	    var generalLedgerMgmtCheckBoxStatus = document.getElementById('generalLedgerManagement').checked; // true or false

	    // get list of radio buttons with name 'readAndWriteOptionForKYCValidation'
	    var radios = document.getElementsByName('readAndWriteOptionForGeneralLedgerManagement');

	    // loop through list of radio buttons
	    for (var i=0, len=radios.length; i<len; i++) {
	        var currentRadioButton = radios[i]; // current radio button

	        if ( generalLedgerMgmtCheckBoxStatus ) { // kycValidation checkbox checked
	            currentRadioButton.disabled=false;
	        } else { // kycValidation not checked
	        	currentRadioButton.disabled = true; // no radios disabled
	        	currentRadioButton.checked = false;
	        }

	    }
	}else{
		if(clicked_id.toUpperCase()=="LOANMANAGEMENT"){
			// is loanDispersal checked?
		    var loanMgmtCheckBoxStatus = document.getElementById('loanManagement').checked; // true or false

		    // get list of radio buttons with name 'readAndWriteOptionForLoanDispersal'
		    var radios = document.getElementsByName('readAndWriteOptionForLoanManagement');

		    // loop through list of radio buttons
		    for (var i=0, len=radios.length; i<len; i++) {
		        var currentRadioButton = radios[i]; // current radio button

		        if ( loanMgmtCheckBoxStatus ) { // loanDispersal checkbox checked
		            currentRadioButton.disabled=false;
		        } else { // kycValidation not checked
		        	currentRadioButton.disabled = true; // no radios disabled
		        	currentRadioButton.checked = false;
		        }
		    }
		}
	}
}

function begin(){

	if(validateForm()){
		processAddRole();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}

function beginUpdate(){

	if(validateForm()){
		processUpdateRole();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}


function validateForm(){ 
    var roleName = document.getElementById("roleName");  
    var roleDiscription = document.getElementById("roleDiscription");   
    
//    var nameRegex = /^[a-zA-Z ]*$/;
//    var alphabetSpaceWithSpecialRegex = /^[A-Z@~`!@#$%^&*()_=+\\';:"\/?>.<,-]*$/;
    
    var alphabetSpaceAndUnderscoreRegex=/^[A-Za-z_ ]*$/;
    
    if (roleName.value == ""){ 
        alert("Please Enter Role Name"); 
        roleName.focus(); 
        return false; 
    }else{
    	if(alphabetSpaceAndUnderscoreRegex.test(roleName.value)==false){
    		alert("Please Enter Correct Role Name"); 
    		roleName.focus(); 
            return false; 
    	}
    }
    
    if (roleDiscription.value == ""){ 
        alert("Please Enter Role Discription Name"); 
        roleDiscription.focus(); 
        return false; 
    }else{
    	if(alphabetSpaceAndUnderscoreRegex.test(roleDiscription.value)==false){
    		alert("Please Enter Correct Role Discription"); 
    		roleDiscription.focus(); 
            return false; 
    	}
    } 

    return true; 
}

function processUpdateRole(){
	
	//**********************************************************************UpdateRole Api
		var data = null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		  /*if (this.readyState === 4) {
			console.log(this.responseText);
			
//			location.replace("RolesInfo.html");
			
			deleteCookie("selectedRoleId");
			deleteCookie("selectedRole");
			
			$('#content').load('RolesInfo.html');
		  }*/
		  
		  
		  if (this.readyState == 4 && this.status == 200) {
				var responseJsonData=JSON.parse(this.responseText);
				console.log(responseJsonData);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=responseJsonData["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
					console.log(this.responseText);
					
					deleteCookie("selectedRoleId");
					deleteCookie("selectedRole");
					
					$('#content').load('RolesInfo.html');		
				}else{
					var requestedAction=responseJsonData["Message"];
					alert(requestedAction);
				}
	  	}
		  
		  
		  
		});

		
		var roleName = document.getElementById("roleName").value;
		var roleDiscription = document.getElementById("roleDiscription").value;
//		var loggedInUserId = readCookie("LoggedInUserId");
		
		console.log("Role Parameters are");
		console.log(roleName);
		console.log(roleDiscription);
		
		
		var selectedRoleValue="";
		var listOfSelectedPermissions="";
		var processRequest=true;
		
		if (userMgmt.checked) {
		    console.log(userMgmt.value + ' is checked!');
			
			if(validateRadioButton("readAndWriteOptionForUserMgmt")){

				selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForUserMgmt"]:checked').value;
				
				console.log(selectedRoleValue);
				listOfSelectedPermissions=listOfSelectedPermissions+","+selectedRoleValue;
				console.log(listOfSelectedPermissions);
			}else{
				processRequest=false;
			}
			
		}
		
		if (customerManagement.checked) {
		    console.log(customerManagement.value + ' is checked!');
			
			if(validateRadioButton("readAndWriteOptionForCustomerManagement")){
				
				selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForCustomerManagement"]:checked').value;
				
				console.log(selectedRoleValue);
				listOfSelectedPermissions=listOfSelectedPermissions+","+selectedRoleValue;
				console.log(listOfSelectedPermissions);
			}else{
				processRequest=false;
			}
		}
		
		if (productManagement.checked) {
		    console.log(productManagement.value + ' is checked!');
		    
		    if(validateRadioButton("readAndWriteOptionForProductManagement")){
				
				selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForProductManagement"]:checked').value;
				
				console.log(selectedRoleValue);
				listOfSelectedPermissions=listOfSelectedPermissions+","+selectedRoleValue;
				console.log(listOfSelectedPermissions);
			}else{
				processRequest=false;
			}
			
		}
		
		if (assumptionManagement.checked) {
		    console.log(assumptionManagement.value + ' is checked!');

		    if(validateRadioButton("readAndWriteOptionForAssumptionManagement")){
				
				selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForAssumptionManagement"]:checked').value;
				
				console.log(selectedRoleValue);
				listOfSelectedPermissions=listOfSelectedPermissions+","+selectedRoleValue;
				console.log(listOfSelectedPermissions);
			}else{
				processRequest=false;
			}
		}
		
		if (kycManagement.checked) {
		    console.log(kycManagement.value + ' is checked!');

		    if(validateRadioButton("readAndWriteOptionForKYCManagement")){
				
				selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForKYCManagement"]:checked').value;
				
				console.log(selectedRoleValue);
				listOfSelectedPermissions=listOfSelectedPermissions+","+selectedRoleValue;
				console.log(listOfSelectedPermissions);
			}else{
				processRequest=false;
			}
		}
		
		if (roleManagement.checked) {
		    console.log(roleManagement.value + ' is checked!');

		    if(validateRadioButton("readAndWriteOptionForRoleManagement")){
				
				selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForRoleManagement"]:checked').value;
				
				console.log(selectedRoleValue);
				listOfSelectedPermissions=listOfSelectedPermissions+","+selectedRoleValue;
				console.log(listOfSelectedPermissions);
			}else{
				processRequest=false;
			}
		}
		
		if (privilegeManagement.checked) {
		    console.log(privilegeManagement.value + ' is checked!');

		    if(validateRadioButton("readAndWriteOptionForPrivilegeManagement")){
				
				selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForPrivilegeManagement"]:checked').value;
				
				console.log(selectedRoleValue);
				listOfSelectedPermissions=listOfSelectedPermissions+","+selectedRoleValue;
				console.log(listOfSelectedPermissions);
			}else{
				processRequest=false;
			}
		}
		
		if (generalLedgerManagement.checked) {
		    console.log(generalLedgerManagement.value + ' is checked!');

		    if(validateRadioButton("readAndWriteOptionForGeneralLedgerManagement")){
				
				selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForGeneralLedgerManagement"]:checked').value;
				
				console.log(selectedRoleValue);
				listOfSelectedPermissions=listOfSelectedPermissions+","+selectedRoleValue;
				console.log(listOfSelectedPermissions);
			}else{
				processRequest=false;
			}
		}
		
		
		
		if (loanManagement.checked) {
		    console.log(loanManagement.value + ' is checked!');
		    
		    if(validateRadioButton("readAndWriteOptionForLoanManagement")){
				
				selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForLoanManagement"]:checked').value;
				
				console.log(selectedRoleValue);
				listOfSelectedPermissions=listOfSelectedPermissions+","+selectedRoleValue;
				console.log(listOfSelectedPermissions);
			}else{
				processRequest=false;
			}
		    
		}
		
		console.log("Final Priviliges List...");
		console.log(listOfSelectedPermissions);

		if(processRequest){
			
			console.log("/LMSServer/UpdateRolePermissions?roleName="+roleName+"&roleDescription="+roleDiscription+"&privilegeNames="+listOfSelectedPermissions+"&callerId="+loggedInUserId);

			xhr.open("POST", "/LMSServer/UpdateRolePermissions?roleName="+roleName+"&roleDescription="+roleDiscription+"&privilegeNames="+listOfSelectedPermissions+"&callerId="+loggedInUserId);
			xhr.setRequestHeader("authString", readCookie("Token"));
			
			xhr.send(data);
		}else{
			alert("Please choose Allowed Roles Option: ReadOnly/ReadAndWrite");
//			alert("Unable to Process Request");
		}
}






function processAddRole(){
	
//**********************************************************************AddUser Api
	var data = null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
	  /*if (this.readyState === 4) {
		console.log(this.responseText);
		
//		location.replace("RolesInfo.html");
		$('#content').load('RolesInfo.html');
	  }*/
	  
	  if (this.readyState == 4 && this.status == 200) {
			var responseJsonData=JSON.parse(this.responseText);
			console.log(responseJsonData);
			
			//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=responseJsonData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(this.responseText);
				$('#content').load('RolesInfo.html');		
			}else{
				var requestedAction=responseJsonData["Message"];
				alert(requestedAction);
			}
	}
	  
	  
	});

	var roleName = document.getElementById("roleName").value;
	var roleDiscription = document.getElementById("roleDiscription").value;
	
	console.log("Role Parameters are");
	console.log(roleName);
	console.log(roleDiscription);
	
	var selectedRoleValue="";
	var listOfSelectedRoles="";
	var processRequest=true;
	
	if (userMgmt.checked) {
	    console.log(userMgmt.value + ' is checked!');
		
		if(validateRadioButton("readAndWriteOptionForUserMgmt")){

			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForUserMgmt"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
		
	}
	
	/*if (regCustomerList.checked) {
	    console.log(regCustomerList.value + ' is checked!');
		
		if(validateRadioButton("readAndWriteOptionForRegCustomerList")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForRegCustomerList"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	}
	
	if (notificationMenu.checked) {
	    console.log(notificationMenu.value + ' is checked!');
	    
	    if(validateRadioButton("readAndWriteOptionForNotificationMenu")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForNotificationMenu"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
		
	}
	
	if (kycValidation.checked) {
	    console.log(kycValidation.value + ' is checked!');

	    if(validateRadioButton("readAndWriteOptionForKYCValidation")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForKYCValidation"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	}
	
	if (loanDispersal.checked) {
	    console.log(loanDispersal.value + ' is checked!');
	    
	    if(validateRadioButton("readAndWriteOptionForLoanDispersal")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForLoanDispersal"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	    
	}*/
	
	if (customerManagement.checked) {
	    console.log(customerManagement.value + ' is checked!');
		
		if(validateRadioButton("readAndWriteOptionForCustomerManagement")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForCustomerManagement"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	}
	
	if (productManagement.checked) {
	    console.log(productManagement.value + ' is checked!');
	    
	    if(validateRadioButton("readAndWriteOptionForProductManagement")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForProductManagement"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
		
	}
	
	if (assumptionManagement.checked) {
	    console.log(assumptionManagement.value + ' is checked!');

	    if(validateRadioButton("readAndWriteOptionForAssumptionManagement")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForAssumptionManagement"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	}
	
	if (kycManagement.checked) {
	    console.log(kycManagement.value + ' is checked!');

	    if(validateRadioButton("readAndWriteOptionForKYCManagement")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForKYCManagement"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	}
	
	if (roleManagement.checked) {
	    console.log(roleManagement.value + ' is checked!');

	    if(validateRadioButton("readAndWriteOptionForRoleManagement")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForRoleManagement"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	}
	
	if (privilegeManagement.checked) {
	    console.log(privilegeManagement.value + ' is checked!');

	    if(validateRadioButton("readAndWriteOptionForPrivilegeManagement")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForPrivilegeManagement"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	}
	
	if (generalLedgerManagement.checked) {
	    console.log(generalLedgerManagement.value + ' is checked!');

	    if(validateRadioButton("readAndWriteOptionForGeneralLedgerManagement")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForGeneralLedgerManagement"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	}
	
	
	
	if (loanManagement.checked) {
	    console.log(loanManagement.value + ' is checked!');
	    
	    if(validateRadioButton("readAndWriteOptionForLoanManagement")){
			
			selectedRoleValue=document.querySelector('input[name="readAndWriteOptionForLoanManagement"]:checked').value;
			
			console.log(selectedRoleValue);
			listOfSelectedRoles=listOfSelectedRoles+","+selectedRoleValue;
			console.log(listOfSelectedRoles);
		}else{
			processRequest=false;
		}
	    
	}
	
	console.log("Final Priviliges List...");
	console.log(listOfSelectedRoles);

	if(processRequest){
		
		console.log("/LMSServer/AddRole?roleName="+roleName+"&roleDescription="+roleDiscription+"&privilegeNames="+listOfSelectedRoles+"&callerId="+loggedInUserId);

		xhr.open("POST", "/LMSServer/AddRole?roleName="+roleName+"&roleDescription="+roleDiscription+"&privilegeNames="+listOfSelectedRoles+"&callerId="+loggedInUserId);
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
	}else{
		alert("Please choose Allowed Roles Option: ReadOnly/ReadAndWrite");
	}








}

function validateRadioButton(elementIdToCheck) {
	
//	var test=document.querySelector('input[name='+elementIdToCheck+']:checked');
//	var element = document.getElementById(elementIdToCheck);

//	var test=document.querySelector('input[name='+elementIdToCheck+']:checked');
	selectedRoleValue=document.querySelector('input[name='+elementIdToCheck+']:checked');
	
	if(selectedRoleValue==null){
		return false;
	}else{
		if(selectedRoleValue.value==false){
			return false;
		}
	}
	
	return true;	

	/*if (element.checked == false) {
		alert("Please choose Allowed Roles Option: ReadOnly/ReadAndWrite");
		return false;
	}
	return true;*/
}


/*$(window).unload(function() {
//  $.cookies.del('name_of_your_cookie');
	alert("Unloading Current Window, Deleting Cookies");
	deleteCookie("selectedRoleId");
	deleteCookie("selectedRole");
});*/


function back(){
	deleteCookie("selectedRoleId");
	deleteCookie("selectedRole");
	
//	location.replace("RolesInfo.html");
	$('#content').load('CreateNewRole.html');
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