//Execute Load Roles DropDown Function
//window.onload = function() {
//	console.log("CreateNewUser Page is Populated and JS is Running on Load");
//	console.log("Calling disableRadioButtons Function");
//	disableRadioButtons();
	var loggedInUserId = readCookie("LoggedInUserId");
	loadRolesDropDown();
	loadEditUserData();
	
//};

//function disableRadioButtons(){
//	var inputs = document.getElementsByTagName('input');
//
//	for(var i = 0; i < inputs.length; i++) {
//	    if(inputs[i].type.toLowerCase() == 'radio') {
//	    	inputs[i].disabled = true;
//	    }
//	}
//}

function loadEditUserData(){
	var selectUserIdCookieStatus = readCookie("selectedUserId");
	var selectUserUsernameCookieStatus = readCookie("selectedUserUsername");

	if((selectUserIdCookieStatus!==null && selectUserIdCookieStatus!=="")
			&& (selectUserUsernameCookieStatus!==null && selectUserUsernameCookieStatus!=="")){
//		alert("Cookie with Name:selectedUserId"+" Having Value: "+selectUserIdCookieStatus);
//		alert("Cookie with Name:selectedUsername"+" Having Value: "+selectUserUsernameCookieStatus);
		
		
		var data = null;
		var editUserDataInfo=null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		 /* if (this.readyState === 4) {
			  
			  this.editUserDataInfo=JSON.parse(this.responseText);
			  console.log(editUserDataInfo);
			  
			  document.getElementById("employeeName").value = this.editUserDataInfo.displayName;
			  document.getElementById("employeeUsername").value = this.editUserDataInfo.username;
			  document.getElementById("employeeUsername").disabled=true;
			  
//			  
			  
			  
			  if(this.editUserDataInfo.category!==null){
				  console.log(this.editUserDataInfo.category.toLowerCase());
				  document.getElementById("employeeCategory").value = this.editUserDataInfo.category.toLowerCase();
			  }else{
				  console.log(this.editUserDataInfo.category);
				  document.getElementById("employeeCategory").value = this.editUserDataInfo.category; 
			  }
			  
			  
			  document.getElementById("role").value = this.editUserDataInfo.role;
			  
			  //Hide Submit Button
			  document.getElementById('submit').style.visibility = 'hidden';
			  document.getElementById('update').style.visibility = 'visible';
		  
		  }*/
		  
		  
		  if (this.readyState == 4 && this.status == 200) {
				var editUserDataInfo=JSON.parse(this.responseText);
				console.log(editUserDataInfo);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=editUserDataInfo["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
//					this.editUserDataInfo=JSON.parse(this.responseText);
					  console.log(editUserDataInfo);
					  
					  document.getElementById("employeeName").value = editUserDataInfo.displayName;
					  document.getElementById("employeeUsername").value = editUserDataInfo.username;
					  document.getElementById("employeeUsername").disabled=true;
					  
//					  
					  
					  
					  if(this.editUserDataInfo.category!==null){
						  console.log(this.editUserDataInfo.category.toLowerCase());
						  document.getElementById("employeeCategory").value = editUserDataInfo.category.toLowerCase();
					  }else{
						  console.log(this.editUserDataInfo.category);
						  document.getElementById("employeeCategory").value = editUserDataInfo.category; 
					  }
					  
					  
					  document.getElementById("role").value = editUserDataInfo.role;
					  
					  //Hide Submit Button
					  document.getElementById('submit').style.visibility = 'hidden';
					  document.getElementById('update').style.visibility = 'visible';		
				}else{
					var requestedAction=editUserDataInfo["Message"];
					alert(requestedAction);
				}
	  	}
		  
		  
		});

		xhr.open("POST", "/LMSServer/GetUserToUpdate?userName="+selectUserUsernameCookieStatus);
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
		
		
	}else{
//		alert("Cookie with Name:selectedUserId Not Found");
//		alert("Cookie with Name:selectedUserUsername Not Found");
		document.getElementById("employeeUsername").disabled=false;
		
		//Hide Submit Button
		  document.getElementById('submit').style.visibility = 'visible';
		  document.getElementById('update').style.visibility = 'hidden';
	}
}

function begin(){

	if(validateForm()){
		processAddUser();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}

function updateBegin(){

	if(validateForm()){
		processUpdateUser();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}

function processAddUser(){
	
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
				alert('User Created Successfully');
				$('#content').load('UserInfoUsingDynamicTable.html');
			}else{
				
				var requestedAction=jsonResponseData["Message"];
				alert(requestedAction);
			}
		}
	});
	
	
	var employeeName = document.getElementById("employeeName").value;
	var employeeUsername = document.getElementById("employeeUsername").value;
	var employeeCategory = document.getElementById("employeeCategory").value;
	var role = document.getElementById("role").value;
	
	
	console.log("Registration Parameters are");
	console.log(employeeName);
	console.log(employeeCategory);
	console.log(employeeUsername);
	console.log(role);
	
		xhr.open("POST", "/LMSServer/AddUser?fullName="+employeeName+"&addusername="+employeeUsername+"&adduserpassword=Lahore@123&adduserconfirmpassword=Lahore@123&newusertype="+employeeCategory+"&newUserRole="+role+"&callerId="+loggedInUserId);

		xhr.setRequestHeader("authString", readCookie("Token"));
		xhr.send(data);
	
	}


function processUpdateUser(){
	
	//**********************************************************************AddUser Api
	var data = null;
	
	console.log("In Begin Function");

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
		if (this.readyState == 4 && this.status == 200) {
//			console.log(this.responseText);
			var jsonResponseData=JSON.parse(this.responseText);
			console.log("Update User API Response: "+jsonResponseData);
			
			//As All Apis always returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=jsonResponseData["Requested_Action"];
			
			console.log("Update User API Response Requested Action: "+requestedAction);
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(jsonResponseData["Requested_Action"]);
				alert('User Updated Successfully');

				deleteCookie("selectedUserId");
				deleteCookie("selectedUserUsername");
				
				$('#content').load('UserInfoUsingDynamicTable.html');
				
			}else{
				var requestedAction=jsonResponseData["Message"];
				alert(requestedAction);
			}
		}
	});
	
	
	var employeeName = document.getElementById("employeeName").value;
	var employeeCategory = document.getElementById("employeeCategory").value;
	var employeeUsername = document.getElementById("employeeUsername").value;
	var role = document.getElementById("role").value;
	
	
	console.log("Updation Parameters are");
	console.log(employeeName);
	console.log(employeeCategory);
	console.log(employeeUsername);
	console.log(role);
	

		xhr.open("POST", "/LMSServer/UpdateUser?username="+employeeUsername+"&displayName="+employeeName+"&category="+employeeCategory+"&role="+role+"&callerId="+loggedInUserId);

		xhr.setRequestHeader("authString", readCookie("Token"));
		xhr.send(data);
	}

function validateRadioButton(elementIdToCheck) {
	
	selectedRoleValue=document.querySelector('input[name='+elementIdToCheck+']:checked');
	
	if(selectedRoleValue==null){
		return false;
	}else{
		if(selectedRoleValue.value==false){
			return false;
		}
	}
	
	return true;	

}

function validateForm(){ 
    var employeeName = document.forms["form"]["employeeName"];               
    var employeeCategory = document.forms["form"]["employeeCategory"];  
    var employeeUsername = document.forms["form"]["employeeUsername"];   
   
//    var nameRegex=/^[A-Z]+$/i;
    var nameRegex = /^[a-zA-Z ]*$/;
//    var alphabetSpaceWithSpecialRegex = /^[a-zA-Z @~`!@#$%^&*()_=+\\';:"\/?>.<,-]*$/;
    var alphabetSpaceWithSpecialAndPostStringRegex = /^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@monamitech.com$/;
    
//    employeeUsername
    if (employeeName.value == ""){ 
        alert("Please Enter Employee Name"); 
        employeeName.focus(); 
        return false; 
    }else{
    	if(nameRegex.test(employeeName.value)==false){
    		alert("Please Enter Correct Employee Name"); 
            employeeName.focus(); 
            return false; 
    	}
    } 
    
    if (employeeUsername.value == ""){ 
        window.alert("Please Select Employee Username"); 
        employeeUsername.focus(); 
        return false; 
    }else{
    	if(alphabetSpaceWithSpecialAndPostStringRegex.test(employeeUsername.value)==false){
    		alert("Please Enter Correct Employee Username"); 
    		employeeUsername.focus(); 
            return false; 
    	}
    }  
   
    if (employeeCategory.value == ""){ 
        window.alert("Please Select Employee Category"); 
        employeeCategory.focus(); 
        return false; 
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



function loadRolesDropDown(){
	
	//**********************************************************************AddUser Api
		var data = null;
		console.log("In loadRolesDropDown Function");

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		  if (this.readyState === 4 ) {
			console.log(this.responseText);
			
			this.userInfoJsonData=JSON.parse(this.responseText);
			 
			var tableHeader = this.userInfoJsonData.Table_Headers;
			  
			  console.log("Header:");
			  for(i=0;i<tableHeader.length;i++){
				  console.log(tableHeader[i]);
			  }
			  
			  var tableData = this.userInfoJsonData.Table_Data;
			  
			  for(i=0;i<tableData.length;i++){
				  console.log("Record No:",i);
				  for(j=0;j<tableHeader.length;j++){
					console.log(tableData[i][j]);
				  }
			  }
			  
			  
			  var roles = document.getElementById("role");
			    
			    //Add the Options to the DropDownList.
			    for (var i = 0; i < tableData.length; i++) {
			        var option = document.createElement("OPTION");

			        //Set Customer Name in Text part.
			        option.innerHTML = tableData[i][2];

			        //Set CustomerId in Value part.
			        option.value = tableData[i][2];

			        //Add the Option element to DropDownList.
			        roles.options.add(option);
			    }
		  }
		});

		xhr.open("POST", "/LMSServer/GetAllRoles");
		xhr.setRequestHeader("authString", readCookie("Token"));
		xhr.send(data);

	}
