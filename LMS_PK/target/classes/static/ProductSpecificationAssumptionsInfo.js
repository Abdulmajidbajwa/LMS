//*********************************************************************GetKycQuestion Api
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
			generateTable(table, tableData);
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
					generateTable(table, tableData);		
			}else{
				var requestedAction=userInfoJsonData["Message"];
				alert(requestedAction);
			}
	}
	  
	  
	  
	});

	xhr.open("POST", "/LMSServer/GetProductAssumptions");
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);



function generateTableHead(table, data) {
//alert("In Table Head");
  let thead = table.createTHead();
  let row = thead.insertRow();
  row.setAttribute('class', 'row100 head');
  for (let key of data) {
    let th = document.createElement("th");
    let text = document.createTextNode(key);
    th.appendChild(text);
    row.appendChild(th);
  }
  
  let th = document.createElement("th");
  
  let text = document.createTextNode("Action");
  th.appendChild(text);
  row.appendChild(th);
}
function generateTable(table, data) {
//	alert("In Generate Head");
	for (let element of data) {
    let row = table.insertRow();
    row.setAttribute('class', 'row100 body');
    for (key in element) {
      let cell = row.insertCell();
      let text = document.createTextNode(element[key]);
      cell.appendChild(text);
    }
    
  //Add Edit Button at the end of each line
    let cell = row.insertCell();
	let button = document.createElement("button");
  //button.setAttribute("onClick","editClicked()");
	button.setAttribute("type","button");
	button.setAttribute("name","editButton");
	button.setAttribute("id",element[0]); //user id
	button.setAttribute("value",element[1]); // user Username
	button.setAttribute("onClick","clickMe(this)");
	
	let text = document.createTextNode("Edit");
 	button.appendChild(text);
 	cell.appendChild(button);
  }
}

function clickMe(e){
	 
	writeCookie("selectedAssumptionId",e.id, "1");
	writeCookie("selectedAssumption",e.value, "1");
//    alert(readCookie("selectedAssumptionId"));
//    alert(readCookie("selectedAssumption"));
    
//    location.replace("CreateNewAssumption.html"); // for testing
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


/*function addKycQuestion(){
	location.replace("CreateNewKycQuestion.html");
}*/
function back(){
	location.replace("UserInfoUsingDynamicTable.html");
}
function addNewAssumption(){
	deleteCookie("selectedAssumptionId");
	deleteCookie("selectedAssumption");
	
//	location.replace("CreateNewAssumption.html");
	$('#content').load('CreateNewAssumption.html');
}