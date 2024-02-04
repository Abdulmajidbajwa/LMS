var loggedInUserId = readCookie("LoggedInUserId");

var selectedKycQuestionIdCookieStatus = readCookie("selectedKycQuestionId");
var selectedKycQuestionCookieStatus = readCookie("selectedKycQuestion");



if((selectedKycQuestionIdCookieStatus!==null && selectedKycQuestionIdCookieStatus!=="")
		&& (selectedKycQuestionCookieStatus!==null && selectedKycQuestionCookieStatus!=="")){
	
}else{
	document.getElementById('possibleAnswerLabel').style.visibility = 'hidden';
	document.getElementById("possibleAnswerLabel").setAttribute("type", "hidden");
	document.getElementById('possibleAnswer').style.visibility = 'hidden';
	document.getElementById("possibleAnswer").setAttribute("type", "hidden");
}

loadEditKycQuestionData();


function loadEditKycQuestionData(){

	if((selectedKycQuestionIdCookieStatus!==null && selectedKycQuestionIdCookieStatus!=="")
			&& (selectedKycQuestionCookieStatus!==null && selectedKycQuestionCookieStatus!=="")){
		
		var data = null;
		var editKycQuestionDataInfo=null;

		var xhr = new XMLHttpRequest();
		xhr.withCredentials = true;

		xhr.addEventListener("readystatechange", function () {
		 			
			if (this.readyState == 4 && this.status == 200) {
				var editKycQuestionDataInfo=JSON.parse(this.responseText);
				console.log(editKycQuestionDataInfo);
				
				//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
				var requestedAction=editKycQuestionDataInfo["Requested_Action"];
				
				
				if(requestedAction.toString().toLowerCase()=="true"){
//					this.editKycQuestionDataInfo=JSON.parse(this.responseText);
					  console.log(editKycQuestionDataInfo);
					  
					  var tableHeader = editKycQuestionDataInfo.Table_Headers;
					  
					  console.log("Header:");
					  for(i=0;i<tableHeader.length;i++){
						  console.log(tableHeader[i]);
					  }
					  
					  
					  var tableData = editKycQuestionDataInfo.Table_Data;
					  var selectedKycQuestionRecordIndex=null;
					  
					  for(i=0;i<tableData.length;i++){
						  console.log("Record No:",i);
						  for(j=0;j<tableHeader.length;j++){
							console.log(tableData[i][j]);
							
							console.log("Comparing");
							console.log("Cookie Role Id: "+selectedKycQuestionIdCookieStatus);
							console.log("Comparing Role Id: "+tableData[i][0]);
							console.log("selectedRoleIdCookieStatus==tableData[i][1]---Status: "+(selectedKycQuestionIdCookieStatus==tableData[i][0]));
							
							if((selectedKycQuestionIdCookieStatus==tableData[i][0]) && selectedKycQuestionRecordIndex==null){
								selectedKycQuestionRecordIndex=i;
								console.log("Record No(Selected RoleId): ",i);
							}
						  }
					  }
					  
					  
					  console.log("Selected Role Id Details");
					  for(i=0;i<tableHeader.length;i++){
						  console.log(tableHeader[i]+ ": "+tableData[selectedKycQuestionRecordIndex][i]);
					  }
					  
					  
					  console.log("KYC Question Category: ",tableData[selectedKycQuestionRecordIndex][3].toLowerCase());
					  
					  document.getElementById("kycQuestion").value = tableData[selectedKycQuestionRecordIndex][1];
					  document.getElementById("answerType").value = tableData[selectedKycQuestionRecordIndex][2].toLowerCase();
					  document.getElementById("possibleAnswer").value = tableData[selectedKycQuestionRecordIndex][3].toLowerCase();
					  
					  if(tableData[selectedKycQuestionRecordIndex][2].toLowerCase()=="enum"){
							
							document.getElementById('possibleAnswerLabel').style.visibility = 'visible';
							document.getElementById("possibleAnswerLabel").setAttribute("type", "text");
							document.getElementById('possibleAnswer').style.visibility = 'visible';
							document.getElementById("possibleAnswer").setAttribute("type", "text");
						}else{
							document.getElementById('possibleAnswerLabel').style.visibility = 'hidden';
							document.getElementById("possibleAnswerLabel").setAttribute("type", "hidden");
							document.getElementById('possibleAnswer').style.visibility = 'hidden';
							document.getElementById("possibleAnswer").setAttribute("type", "hidden");
						}
					  
					  
					  document.getElementById("category").value = tableData[selectedKycQuestionRecordIndex][4].trim().toLowerCase();
					  
					  if(tableData[selectedKycQuestionRecordIndex][5]=="false"){
						  document.getElementById("mandatoryStatus").value = "optional";
					  }else{
						  if(tableData[selectedKycQuestionRecordIndex][5]=="true"){
							  document.getElementById("mandatoryStatus").value = "required";
						  }
					  }
					  
					  document.getElementById("expiryTime").value = tableData[selectedKycQuestionRecordIndex][6];
					  
					  handleMandatoryQuestion();
					  
					//Hide Submit Button
					  document.getElementById('submit').style.visibility = 'hidden';
					  document.getElementById('update').style.visibility = 'visible';
				}else{
					
					var requestedAction=editKycQuestionDataInfo["Message"];
					alert(requestedAction);
				}
		}	
			
		});

		xhr.open("POST", "/LMSServer/GetKYCQuestions");
		xhr.setRequestHeader("authString", readCookie("Token"));
		
		xhr.send(data);
		
		
	}else{
//		alert("Cookie with Name:selectedUserId Not Found");
//		alert("Cookie with Name:selectedUserUsername Not Found");
//		document.getElementById("employeeUsername").disabled=false;
		
		//Hide Submit Button
		  document.getElementById('submit').style.visibility = 'visible';
		  document.getElementById('update').style.visibility = 'hidden';
	}
}


function begin(){
	if(validateForm()){
		processAddKycQuestion();
	}else{
		alert("Unable to Process. Please Provide Full Details");
	}
}

function updateBegin(){

	if(validateForm()){
		processUpdateKycQuestion();
	}else{
		alert("Unable to Process Form. Please Provide Full Details");
	}
}

function validateForm(){ 
	var kycQuestion = document.getElementById("kycQuestion");
	var expiryTime = document.getElementById("expiryTime");  
	var mandatory = document.getElementById("mandatoryStatus").value;
   
//    var alphaSpaceRegex = /^[a-zA-Z ]*$/;
//	var alphabetSpaceWithSpecialRegex = /^[A-Z@~`!@#$%^&*()_=+\\';:"\/?>.<,-]*$/;
    var numericRegex = /^[0-9]*[0-9][0-9]*$/;
    
//    var alphabetSpaceAndUnderscoreRegex=/^[A-Za-z_ ]*$/;
//    var alphaNumeicSpecialRegex=/^[ A-Za-z0-9_@./,#&+?(){}[]-]*$/;
    
    if (kycQuestion.value == ""){ 
        alert("Please Enter KYC Question"); 
        kycQuestion.focus(); 
        return false; 
    }else{
    	/*if(alphaNumeicSpecialRegex.test(kycQuestion.value)==false){
    		alert("Please Enter Correct KYC Question"); 
    		kycQuestion.focus(); 
            return false; 
    	}*/
    } 
    
//    if(man)
    if(mandatory=="required"){
    	//no need to check
    }else{
    	if (expiryTime.value == ""){ 
            alert("Please Enter Expiry Time"); 
            expiryTime.focus(); 
            return false; 
        }else{
        	if(numericRegex.test(expiryTime.value)==false){
        		alert("Please Enter Correct Expiry Time"); 
        		expiryTime.focus(); 
                return false; 
        	}
        }
    }
    

    return true; 
}


function processAddKycQuestion(){
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
				
				deleteCookie("selectedKycQuestionId");
				deleteCookie("selectedKycQuestion");
				
				$('#content').load('KycQuestionInfo.html');
			}else{
				var requestedAction=applicationInfoJsonData["Message"];
				alert(requestedAction);
			}
	}
	  
	  
	});

	var kycQuestion = document.getElementById("kycQuestion").value;
	var answerType = document.getElementById("answerType").value;
	var possibleAnswer = document.getElementById("possibleAnswer").value;
	var category = document.getElementById("category").value;
	var expiryTime = document.getElementById("expiryTime").value;
	var mandatory = document.getElementById("mandatoryStatus").value;
	
	console.log("Kyc Question Parameters are");
	console.log(kycQuestion);
	console.log(answerType);
	console.log(possibleAnswer);
	console.log(category);
	console.log(expiryTime);
	console.log(mandatory);
	console.log(readCookie("Token"));
	
	var mandatoryStatus;
	if(mandatory=="optional"){
		mandatoryStatus=false;
	}else{
		if(mandatory=="required"){
			mandatoryStatus=true;
			expiryTime=0;
		}
	}
	

	
	xhr.open("POST", "/LMSServer/AddKYCQuestion?questionToAsk="+kycQuestion+"&answerType="+answerType+"&listOfPossibleAnswers="+possibleAnswer+"&catagory="+category+"&mandatoryStatus="+mandatoryStatus+"&expiryInDays="+expiryTime);
	xhr.setRequestHeader("authString", readCookie("Token"));
	xhr.send(data);
}


function processUpdateKycQuestion(){
	//**********************************************************************AddKYCQuestion Api
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
				
				deleteCookie("selectedKycQuestionId");
				deleteCookie("selectedKycQuestion");
				
//				location.replace("KycQuestionInfo.html");
				$('#content').load('KycQuestionInfo.html');
			}else{
				var requestedAction=applicationInfoJsonData["Message"];
				alert(requestedAction);
			}
	}
	  
	  
	});
	
	var kycQuestionId = readCookie("selectedKycQuestionId");
	var kycQuestion = document.getElementById("kycQuestion").value;
	var answerType = document.getElementById("answerType").value;
	var possibleAnswer = document.getElementById("possibleAnswer").value;
	var category = document.getElementById("category").value;
	var mandatory = document.getElementById("mandatoryStatus").value;
	var expiryTime = document.getElementById("expiryTime").value;
	
	
	console.log("Kyc Question Parameters are");
	console.log(kycQuestionId);
	console.log(kycQuestion);
	console.log(answerType);
	console.log(possibleAnswer);
	console.log(category);
	console.log(mandatory);
	console.log(expiryTime);
	
	var mandatoryStatus;
	if(mandatory=="optional"){
		mandatory=false;
	}else{
		if(mandatory=="required"){
			mandatory=true;
		}
	}

	xhr.open("POST", "/LMSServer/UpdateKycQuestion?kycQuestionId="+kycQuestionId+"&questionToAsk="+kycQuestion+"&answerType="+answerType+"&listOfPossibleAnswers="+possibleAnswer+"&catagory="+category+"&mandatoryStatus="+mandatory+"&expiryInDays="+expiryTime);
	xhr.setRequestHeader("authString", readCookie("Token"));
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
	
	function handleMandatoryQuestion() {
		if(document.getElementById("mandatoryStatus").value=="optional"){
			document.getElementById("expiryTime").disabled=false;
			document.getElementById("expiryTime").setAttribute("type", "text");
			document.getElementById('expiryTimeLabel').style.visibility = 'visible';
		}else{
			if(document.getElementById("mandatoryStatus").value=="required"){
				document.getElementById("expiryTime").disabled=true;
				document.getElementById("expiryTime").setAttribute("type", "hidden");
				document.getElementById('expiryTimeLabel').style.visibility = 'hidden';
			}
		}
	}
	
	function handleAnswerTypeDropDown() {
		
		var answerType= document.getElementById("answerType").value;
		
		if(answerType=="enum"){
			
			document.getElementById('possibleAnswerLabel').style.visibility = 'visible';
			document.getElementById("possibleAnswerLabel").setAttribute("type", "text");
			document.getElementById('possibleAnswer').style.visibility = 'visible';
			document.getElementById("possibleAnswer").setAttribute("type", "text");
		}else{
			document.getElementById('possibleAnswerLabel').style.visibility = 'hidden';
			document.getElementById("possibleAnswerLabel").setAttribute("type", "hidden");
			document.getElementById('possibleAnswer').style.visibility = 'hidden';
			document.getElementById("possibleAnswer").setAttribute("type", "hidden");
		}
		
	}
	
	
	function back(){
		deleteCookie("selectedKycQuestionId");
		deleteCookie("selectedKycQuestion");

		$('#content').load('KycQuestionInfo.html');
	}	
	
	function deleteCookie(name) {
	    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	};
	
/*	$(document).ready(function() {
		var max_fields      = 10; //maximum input boxes allowed
		var wrapper   		= $(".input_fields_wrap"); //Fields wrapper
		var add_button      = $(".add_field_button"); //Add button ID
		
		var x = 1; //initlal text box count
		$(add_button).click(function(e){ //on add input button click
			e.preventDefault();
			if(x < max_fields){ //max input box allowed
				x++; //text box increment
				$(wrapper).append('<div><input type="text" name="mytext[]"/><a href="#" class="remove_field">Remove</a></div>'); //add input box
			}
		});
		
		$(wrapper).on("click",".remove_field", function(e){ //user click on remove text
			e.preventDefault(); $(this).parent('div').remove(); x--;
		})
	});*/
	
	