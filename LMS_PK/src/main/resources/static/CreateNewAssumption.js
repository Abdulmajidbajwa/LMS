
var loggedInUserId = readCookie("LoggedInUserId");
loadEditAssumptionsData();

function loadEditAssumptionsData(){
	var selectedAssumptionIdCookieStatus = readCookie("selectedAssumptionId");
	var selectedAssumptionCookieStatus = readCookie("selectedAssumption");

	if((selectedAssumptionIdCookieStatus!==null && selectedAssumptionIdCookieStatus!=="")
			&& (selectedAssumptionCookieStatus!==null && selectedAssumptionCookieStatus!=="")){
//		alert("Cookie with Name:selectedAssumptionId"+" Having Value: "+selectedAssumptionIdCookieStatus);
//		alert("Cookie with Name:selectedAssumption"+" Having Value: "+selectedAssumptionCookieStatus);
		
		
		var data = null;
		var editAssumptionDataInfo=null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		 /* if (this.readyState === 4) {
			  
			  this.editAssumptionDataInfo=JSON.parse(this.responseText);
			  console.log(editAssumptionDataInfo);
			  
			  
			  var tableHeader = this.editAssumptionDataInfo.Table_Headers;
			  
			  console.log("Header:");
			  for(i=0;i<tableHeader.length;i++){
				  console.log(tableHeader[i]);
			  }
			  
			  
			  var tableData = this.editAssumptionDataInfo.Table_Data;
			  var selectedAssumptionIndex=null;
			  
			  for(i=0;i<tableData.length;i++){
				  console.log("Record No:",i);
				  for(j=0;j<tableHeader.length;j++){
					console.log(tableData[i][j]);
					
					console.log("Comparing");
					console.log("Cookie Assumption Id: "+selectedAssumptionIdCookieStatus);
					console.log("Comparing Assumption Id: "+tableData[i][0]);
					console.log("selectedAssumptionIdCookieStatus==tableData[i][1]---Status: "+(selectedAssumptionIdCookieStatus==tableData[i][0]));
					
					if((selectedAssumptionIdCookieStatus==tableData[i][0]) && selectedAssumptionIndex==null){
						selectedAssumptionIndex=i;
						console.log("Record No(Selected AssumptionId): ",i);
					}
				  }
			  }
			  
			  
			  console.log("Selected Assumption Id Details");
			  for(i=0;i<tableHeader.length;i++){
				  console.log(tableHeader[i]+ ": "+tableData[selectedAssumptionIndex][i]);
			  }
			  
			  
			  document.getElementById("assumptionName").value = tableData[selectedAssumptionIndex][1];
			  document.getElementById("assumptionType").value = tableData[selectedAssumptionIndex][2].toLowerCase();
			  
			//Hide Submit Button
			  document.getElementById('submit').style.visibility = 'hidden';
			  document.getElementById('update').style.visibility = 'visible';
		  
		  }*/
		  
		  
		  if (this.readyState == 4 && this.status == 200) {
				var editAssumptionDataInfo=JSON.parse(this.responseText);
				console.log(editAssumptionDataInfo);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=editAssumptionDataInfo["Requested_Action"];
				
				
				if(requestedAction.toString().toLowerCase()=="true"){
//					this.editAssumptionDataInfo=JSON.parse(this.responseText);
					  console.log(editAssumptionDataInfo);
					  
					  var tableHeader = editAssumptionDataInfo.Table_Headers;
					  
					  console.log("Header:");
					  for(i=0;i<tableHeader.length;i++){
						  console.log(tableHeader[i]);
					  }
					  
					  
					  var tableData = editAssumptionDataInfo.Table_Data;
					  var selectedAssumptionIndex=null;
					  
					  for(i=0;i<tableData.length;i++){
						  console.log("Record No:",i);
						  for(j=0;j<tableHeader.length;j++){
							console.log(tableData[i][j]);
							
							console.log("Comparing");
							console.log("Cookie Assumption Id: "+selectedAssumptionIdCookieStatus);
							console.log("Comparing Assumption Id: "+tableData[i][0]);
							console.log("selectedAssumptionIdCookieStatus==tableData[i][1]---Status: "+(selectedAssumptionIdCookieStatus==tableData[i][0]));
							
							if((selectedAssumptionIdCookieStatus==tableData[i][0]) && selectedAssumptionIndex==null){
								selectedAssumptionIndex=i;
								console.log("Record No(Selected AssumptionId): ",i);
							}
						  }
					  }
					  
					  
					  console.log("Selected Assumption Id Details");
					  for(i=0;i<tableHeader.length;i++){
						  console.log(tableHeader[i]+ ": "+tableData[selectedAssumptionIndex][i]);
					  }
					  
					  
					  document.getElementById("assumptionName").value = tableData[selectedAssumptionIndex][1];
					  document.getElementById("assumptionType").value = tableData[selectedAssumptionIndex][2].toLowerCase();
					  
					//Hide Submit Button
					  document.getElementById('submit').style.visibility = 'hidden';
					  document.getElementById('update').style.visibility = 'visible';
				}else{
					
					var requestedAction=editAssumptionDataInfo["Message"];
					alert(requestedAction);
				}
		}
		});

		xhr.open("POST", "/LMSServer/GetProductAssumptions");
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
		
		
	}else{
//		alert("Cookie with Name:selectedAssumptionId Not Found");
//		alert("Cookie with Name:selectedAssumptionName Not Found");
		
		//Hide Submit Button
		  document.getElementById('submit').style.visibility = 'visible';
		  document.getElementById('update').style.visibility = 'hidden';
	}
	
	
	
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
	
		  if (this.readyState == 4 && this.status == 200) {
				var applicationInfoJsonData=JSON.parse(this.responseText);
				console.log(applicationInfoJsonData);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=applicationInfoJsonData["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
					console.log(this.responseText);
					alert("Successfully Added Assumption");
					$('#content').load('ProductSpecificationAssumptionsInfo.html');
				}else{
					var requestedAction=applicationInfoJsonData["Message"];
					alert(requestedAction);
				}
	  	}
		
	});

	var assumptionName = document.getElementById("assumptionName").value;
	
	var assumptionType = document.getElementById("assumptionType").value;
	
	console.log("Assumption Parameters are");
	console.log(assumptionName);
	console.log(assumptionType);

	xhr.open("POST", "/LMSServer/AddProductSpecificationAssumption?assumptionName="+assumptionName+"&assumptionDataType="+assumptionType+"&callerId="+loggedInUserId);
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);
}

function processUpdateAssumption(){
	
	//**********************************************************************UpdateAssumption Api
		var data = null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		  /*if (this.readyState === 4) {
			console.log(this.responseText);

			alert("Successfully Updated Assumption");
			
			deleteCookie("selectedAssumptionId");
			deleteCookie("selectedAssumption");
			
			$('#content').load('ProductSpecificationAssumptionsInfo.html');
		  }*/
		  
		  if (this.readyState == 4 && this.status == 200) {
				var applicationInfoJsonData=JSON.parse(this.responseText);
				console.log(applicationInfoJsonData);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=applicationInfoJsonData["Requested_Action"];
				
				if(requestedAction.toString().toLowerCase()=="true"){
					console.log(this.responseText);

					alert("Successfully Updated Assumption");
					
					deleteCookie("selectedAssumptionId");
					deleteCookie("selectedAssumption");
					
					$('#content').load('ProductSpecificationAssumptionsInfo.html');
				}else{
					var requestedAction=applicationInfoJsonData["Message"];
					alert(requestedAction);
				}
	  	}
		});

		
		var assumptionId=readCookie("selectedAssumptionId");
		var assumptionName = document.getElementById("assumptionName").value;
		var assumptionType = document.getElementById("assumptionType").value;
		
		console.log("Assumption Parameters are");
		console.log(assumptionId);
		console.log(assumptionName);
		console.log(assumptionType);

		xhr.open("POST", "/LMSServer/UpdateAssumption?assumptionId="+assumptionId+"&assumption="+assumptionName+"&assumptionType="+assumptionType+"&callerId="+loggedInUserId);
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