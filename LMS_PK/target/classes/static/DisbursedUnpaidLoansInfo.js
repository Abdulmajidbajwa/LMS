//*********************************************************************GetKycQuestion Api
	var data = null;
	var userInfoJsonData=null;

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
				 
				  var keysAsHeaders;
				  var valueArray;
				  var valuesStrOfcurrentRecord;
				  
				  currentRecordKeysAsHeaders = Object.keys(applicationInfoJsonData.data[0]);
				  console.log("Application Id Key:",currentRecordKeysAsHeaders);
				  
				  console.log("Table Header:",currentRecordKeysAsHeaders);
				  console.log("Table Values:",valuesStrOfcurrentRecord);
				  
				  let table = document.querySelector("table");
				  generateTableHead(table, currentRecordKeysAsHeaders);
				  generateTable(table, applicationInfoJsonData);
				  
				  console.log("Loop End:");
			}else{
				
				var requestedAction=applicationInfoJsonData["Message"];
				alert(requestedAction);
			}
  	}
	  
	});

	xhr.open("POST", "/LMSServer/getPaidOrUnpaidDisbursedLoans?isPaidRequired=false");
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);



function generateTableHead(table, data) {

  let thead = table.createTHead();
  let row = thead.insertRow();
  row.setAttribute('class', 'row100 head');
  
    let th1 = document.createElement("th");
    let text1 = document.createTextNode("Cell No");
    th1.appendChild(text1);
    row.appendChild(th1);

    let th2 = document.createElement("th");
    let text2 = document.createTextNode("Loan Id");
    th2.appendChild(text2);
    row.appendChild(th2);
    
    let th3 = document.createElement("th");
    let text3 = document.createTextNode("Loan Status");
    th3.appendChild(text3);
    row.appendChild(th3);
    
    let headerCustomerCellNo = document.createElement("th");
    let textCustomerCellNo = document.createTextNode("Due Date");
    headerCustomerCellNo.appendChild(textCustomerCellNo);
    row.appendChild(headerCustomerCellNo);
    
    let th4 = document.createElement("th");
    let text4 = document.createTextNode("Outstanding Balance");
    th4.appendChild(text4);
    row.appendChild(th4);
    
    
    let th6 = document.createElement("th");
    let text6 = document.createTextNode("Created At");
    th6.appendChild(text6);
    row.appendChild(th6);
    
    let th7 = document.createElement("th");
    let text7 = document.createTextNode("Created By");
    th7.appendChild(text7);
    row.appendChild(th7);
    
    let th8 = document.createElement("th");
    let text8 = document.createTextNode("Updated At");
    th8.appendChild(text8);
    row.appendChild(th8);
    
    let th9 = document.createElement("th");
    let text9 = document.createTextNode("Updated By");
    th9.appendChild(text9);
    row.appendChild(th9);
    
  
  let th10 = document.createElement("th");
  let text10 = document.createTextNode("Action");
  th10.appendChild(text10);
  row.appendChild(th10);
}


function generateTable(table, applicationInfoJsonData) {
	
	console.log("In Table Data**********************************************************");
	
	
	for(i=0;i<applicationInfoJsonData.data.length;i++){
		
			let thead = table.createTHead();
			  let row = thead.insertRow();
			  row.setAttribute('class', 'row100 head');
			  
			  console.log("Application Number------------------------------------------------------:",i);
			  
			  var cellNo = applicationInfoJsonData.data[i].application.customer.cellNo;
			  var loanId = applicationInfoJsonData.data[i].id;
			  var loanStatus = applicationInfoJsonData.data[i].loanStatus;
			  var dueDate = applicationInfoJsonData.data[i].dueDate;
			  var outstandingAmount = applicationInfoJsonData.data[i].outstandingAmount;
			  
			  console.log("Cell No:",cellNo);
			  console.log("Loan Id:",loanId);
			  console.log("Loan Status:",loanStatus);
			  console.log("Due Date:",dueDate);
			  console.log("OutStanding Amount:",outstandingAmount);
			  
			  let cell1 = row.insertCell();
		      let text1 = document.createTextNode(cellNo);
		      cell1.appendChild(text1);
			  
			  let cell2 = row.insertCell();
		      let text2 = document.createTextNode(loanId);
		      cell2.appendChild(text2);
			 	  
			  let cell3 = row.insertCell();
		      let text3 = document.createTextNode(loanStatus);
		      cell3.appendChild(text3);
			  
			  let cell4 = row.insertCell();
		      let text4 = document.createTextNode(dueDate);
		      cell4.appendChild(text4);
			  
			  let cell5 = row.insertCell();
		      let text5 = document.createTextNode(outstandingAmount);
		      cell5.appendChild(text5);
		      
		      
		      var createdAt = applicationInfoJsonData.data[i].createdAt;
		      var updatedAt = applicationInfoJsonData.data[i].updatedAt;
		      var createdBy = null;
		      var updatedBy = null;
			  
		      if(applicationInfoJsonData.data[i].createdBy!==null){
				  console.log("Creatd By Display name: "+applicationInfoJsonData.data[i].createdBy.displayName);
				  createdBy=applicationInfoJsonData.data[i].createdBy.displayName;
			  }else{
				  console.log("Created by is null");
			  }
				
			  if(applicationInfoJsonData.data[i].updatedBy!==null){
				  console.log("Updated By Display name: "+applicationInfoJsonData.data[i].updatedBy.displayName);
				  updatedBy=applicationInfoJsonData.data[i].updatedBy.displayName;
			  }else{
				  console.log("Updated by is null");
			  }
			  
			  console.log("Created At: "+createdAt);
			  console.log("Created By: "+createdBy);
			  console.log("Updated At: "+updatedAt);
			  console.log("Updated By: "+updatedBy);
		      
		      let CellCreatedAt = row.insertCell();
		      let textCreatedAt = document.createTextNode(createdAt);
		      CellCreatedAt.appendChild(textCreatedAt);
		      
		      let CellCreatedBy= row.insertCell();
		      let textCreatedBy = document.createTextNode(createdBy);
		      CellCreatedBy.appendChild(textCreatedBy);
		      
		      let CellUpdatedAt = row.insertCell();
		      let textUpdatedAt = document.createTextNode(updatedAt);
		      CellUpdatedAt.appendChild(textUpdatedAt);
		      
		      let CellUpdatedBy = row.insertCell();
		      let textUpdatedBy = document.createTextNode(updatedBy);
		      CellUpdatedBy.appendChild(textUpdatedBy);
		      
		      
		      
		      
		    //Add Edit Button at the end of each line
		    let cell6 = row.insertCell();
		  	let button = document.createElement("input");
		  	button.setAttribute("type","button");
		  	button.setAttribute("name","editButton");
		  	button.setAttribute("id",cellNo); //user id
		  	button.setAttribute("value","Record Payment"); // user Username
		  	button.setAttribute("onClick","applicationDetailPage(this)");
		  	
		  	
		  	let text = document.createTextNode("Record Payment");
		   	button.appendChild(text);
		   	cell6.appendChild(button);
		   	
		   	if(loanStatus=="OVER_DUE"){
		   		let button2 = document.createElement("input");
			  	button2.setAttribute("type","button");
			  	button2.setAttribute("name","editButton");
			  	button2.setAttribute("id",loanId); //user id
			  	button2.setAttribute("value","Write Off"); // user Username
			  	button2.setAttribute("onClick","updateDisbursedLoanToWriteOff(this)");
			  	
			  	
			  	let textWriteOff = document.createTextNode("Write Off");
			   	button2.appendChild(textWriteOff);
			   	cell6.appendChild(button2);
		   	}
		  
	  }
	
	
}


function applicationDetailPage(e){
	
	writeCookie("selectedCustomerLoanRequest",e.id, "1");
	$('#content').load('LoanRepayment.html');
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


function updateDisbursedLoanToWriteOff(e){
	
	//**********************************************************************AddUser Api
	var data = null;
	
	console.log("In Begin Function");

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
		if (this.readyState == 4 && this.status == 200) {
			var jsonResponseData=JSON.parse(this.responseText);
			console.log(jsonResponseData);
			
			var requestedAction=jsonResponseData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
				console.log(jsonResponseData["Requested_Action"]);
				alert(jsonResponseData["Message"]);
				$('#content').load('DisbursedUnpaidLoansInfo.html');
			}else{
				var requestedAction=jsonResponseData["Message"];
				alert(requestedAction);
			}
		}
	});
	
	
	console.log("Loan Repayment Parameters are");
	console.log("Loan Id: "+e.id);
	
		xhr.open("POST", "/LMSServer/updateDisbursedLoanStatusToWriteOff?loanId="+e.id);

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

function addProduct(){
//	location.replace("CreateNewProductScreenOne.html");
	$('#content').load('CreateNewProductScreenOne.html');
}

function back(){
//	location.replace("UserInfoUsingDynamicTable.html");
	$('#content').load('UserInfoUsingDynamicTable.html');
}

