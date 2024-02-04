//*********************************************************************GetAllUsers Api
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
			generateTable(table, tableData,tableHeader.indexOf("Count"));
			
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
					generateTable(table, tableData,tableHeader.indexOf("Count"));	
			}else{
				var requestedAction=userInfoJsonData["Message"];
				alert(requestedAction);
			}
	}
	  
	  
	  
	});

	xhr.open("POST", "/LMSServer/GetAllUsers");
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);


	

function generateTableHead(table, data) {
  
  let thead = table.createTHead();
  
  let row = thead.insertRow();
  row.setAttribute('class', 'row100 head');
  for (let key of data) {
	  if(key!=="Count"){//Skipping First Array Element(Count)
			  
		    let th = document.createElement("th");
		    
		    let text = document.createTextNode(key);
		    th.appendChild(text);
		    row.appendChild(th);
	  }
  }
  
  let th = document.createElement("th");
  let text = document.createTextNode("Action");
  th.appendChild(text);
  row.appendChild(th);
  
}


function generateTable(table, data, columnIndexToSkip) {
	
	
	for (let element of data) {
		
    let row = table.insertRow();
    row.setAttribute('class', 'row100 body');
    for (key in element) {
    	if(key!==columnIndexToSkip.toString()){ //Skipping First Array Element(Count)
    		if(element[key].startsWith(",")){
       		 let cell = row.insertCell();
   			 createListInCell(cell,checkAndGetCorrectString(element[key]));
       	}else{
       		let cell = row.insertCell();
       	    let text = document.createTextNode(checkAndGetCorrectString(element[key]));
       	    cell.appendChild(text);
       	}
      }
    }
    
//Add Edit Button at the end of each line
    let cell = row.insertCell();
	let button = document.createElement("button");
//	button.setAttribute("onClick","editClicked()");
	button.setAttribute("type","button");
	button.setAttribute("name","editButton");
	button.setAttribute("id",element[1]); //user id
	button.setAttribute("value",element[2]); // user Username
	button.setAttribute("onClick","clickMe(this)");
	
	
	let text = document.createTextNode("Edit");
 	button.appendChild(text);
 	cell.appendChild(button);
  }
}

function clickMe(e){
	 
	writeCookie("selectedUserId",e.id, "1");
	writeCookie("selectedUserUsername",e.value, "1");
//    alert(readCookie("selectedUserId"));
//    alert(readCookie("selectedUserUsername"));
    
//    location.replace("CreateNewUser.html");
    $('#content').load('CreateNewUser.html');
 
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


function createListInCell(cell,text){
	
	console.log("Received Parameter");
	console.log(text);
	
	var listView=document.createElement('ul');
	var permissions=text.split(",");
	
	for(var i=0;i<permissions.length;i++)
	{
	    var listViewItem=document.createElement('li');
	    listViewItem.appendChild(document.createTextNode(permissions[i]));
	    listView.appendChild(listViewItem);
	    
	    
	    
	    cell.appendChild(listView);
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

function addUser(){
//	location.replace("CreateNewUser.html");
	deleteCookie("selectedUserId");
	deleteCookie("selectedUserUsername");
	
	$('#content').load('CreateNewUser.html');
}

/*function editClicked(){
//	alert("Edit Clicked");
//	$(document).ready(function(){
//		$('table thead tr td').click(function (){
//			alert($(this).text());
//		})
//	}); 
	    var table = document.getElementById('display-table');
	    var cells = table.getElementsByTagName('td');

	    for (var i = 0; i < cells.length; i++) {
	        // Take each cell
	        var cell = cells[i];
	        // do something on onclick event for cell
	        cell.onclick = function () {
	            // Get the row id where the cell exists
	            var rowId = this.parentNode.rowIndex;

	            var rowsNotSelected = table.getElementsByTagName('tr');
	            for (var row = 0; row < rowsNotSelected.length; row++) {
	                rowsNotSelected[row].style.backgroundColor = "";
	                rowsNotSelected[row].classList.remove('selected');
	            }
	            var rowSelected = table.getElementsByTagName('tr')[rowId];
	            rowSelected.style.backgroundColor = "yellow";
	            rowSelected.className += " selected";

	            msg = 'The ID of the company is: ' + rowSelected.cells[0].innerHTML;
	            msg += '\nThe cell value is: ' + this.innerHTML;
	            alert(msg);
	        }
	    }

}*/



function productInfo(){
	location.replace("ProductInfo.html");
}

function customersInfo(){
	location.replace("CustomersInfo.html");
}

function kycInfo(){
	location.replace("KycQuestionInfo.html");
}
function rolesInfo(){
	location.replace("RolesInfo.html");
}
function productSpecificationAssumptionsInfo(){
	location.replace("ProductSpecificationAssumptionsInfo.html");
}


