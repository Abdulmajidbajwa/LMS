//*********************************************************************GetCustomerList Api
	var data = null;
	var userInfoJsonData=null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
	  /*if (this.readyState === 4) {
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
		  
		    let table = document.querySelector("table");
		  
			generateTableHead(table, tableHeader);
			generateTable(table, tableData,tableHeader.indexOf("OTP INFO"));
	  }*/
	  
	  
	  
	  if (this.readyState == 4 && this.status == 200) {
			var userInfoJsonData=JSON.parse(this.responseText);
			console.log(userInfoJsonData);
			
			//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=userInfoJsonData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(this.responseText);
//				  this.userInfoJsonData=JSON.parse(this.responseText);
				 
				  var tableHeader = userInfoJsonData.Table_Headers;
				  
				  console.log("Header:");
				  for(i=0;i<tableHeader.length;i++){
					  console.log(tableHeader[i]);
				  }
				  
				  var tableData = userInfoJsonData.Table_Data;
				  
				  for(i=0;i<tableData.length;i++){
					  console.log("Record No:",i);
					  for(j=0;j<tableHeader.length;j++){
						console.log(tableData[i][j]);
					  }
				  }
				  
				    let table = document.querySelector("table");
				  
					generateTableHead(table, tableHeader);
					generateTable(table, tableData,tableHeader.indexOf("OTP INFO"));	
			}else{
				var requestedAction=userInfoJsonData["Message"];
				alert(requestedAction);
			}
	}
	  
	  
	});

	xhr.open("POST", "/LMSServer/getCustomerList");
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);



function generateTableHead(table, data) {
//alert("In Table Head");

  let thead = table.createTHead();
  let row = thead.insertRow();
  row.setAttribute('class', 'row100 head');
//  var columnIndexToSkip = data.indexOf("OPT INFO");
  
  
  for (let key of data) {
	  
	if(key!=="OTP INFO"){
		
		
		let th = document.createElement("th");
	    let text = document.createTextNode(key);
	    th.appendChild(text);
	    row.appendChild(th);
	}  
    
  }
  
/*  let actionHeading = document.createElement("th");
  let action = document.createTextNode("Action");
  actionHeading.appendChild(action);
  row.appendChild(actionHeading);*/
 }

function generateTable(table, data,columnIndexToSkip) {

	for (let element of data) {
    let row = table.insertRow();
    row.setAttribute('class', 'row100 body');
    for (key in element) {
	    if(key!==columnIndexToSkip.toString() && (element[4]=="BLOCKED")){
	        let cell = row.insertCell();
	        let text = document.createTextNode(element[key]);
	        cell.appendChild(text);
	    }	
    }
    
//    //Adding Button against Each record in Grid
//    let actionButton = document.createElement("button");
//    let deleteText = document.createTextNode("Delete");
//    
//    actionButton.setAttribute("id","delete");	
//    actionButton.setAttribute("name","delete");	
//    actionButton.setAttribute("onClick","getConfirmation()");	
//    
//    actionButton.appendChild(deleteText);
//    row.appendChild(actionButton);
  }
}

function getConfirmation() {
    var retVal = confirm("Do you want to continue ?");
    if( retVal == true ) {
       alert("User Deleted");
       location.replace("CustomersInfo.html");
    }
    else {
    	alert("No Need to delete User");
    	location.replace("CustomersInfo.html");
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
	deleteCookie("selectedAssumptionId");
	deleteCookie("selectedAssumption");
	
	location.replace("UserInfoUsingDynamicTable.html");
}
