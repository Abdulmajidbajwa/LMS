//*********************************************************************GetKycQuestion Api
	var data = null;
	var generalLedgerInfoJsonData=null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;
	
	var glIdToGetCookieStatus = readCookie("glIdToGet");

	xhr.addEventListener("readystatechange", function () {
	  
	  if (this.readyState == 4 && this.status == 200) {
			var generalLedgerInfoJsonData=JSON.parse(this.responseText);
			console.log(generalLedgerInfoJsonData);
			
			//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=generalLedgerInfoJsonData["Requested_Action"];
			
			if(requestedAction.toString().toLowerCase()=="true"){
			  
			  console.log(this.responseText);
//			  this.generalLedgerInfoJsonData=JSON.parse(this.responseText);
				 
			  var keysAsHeaders;
			  var valueArray;
			  var valuesStrOfcurrentRecord;
			  
			  currentRecordKeysAsHeaders = Object.keys(generalLedgerInfoJsonData.data[0]);
			  console.log("GL Id Key:",currentRecordKeysAsHeaders);
			  
			  console.log("Table Header:",currentRecordKeysAsHeaders);
			  console.log("Table Values:",valuesStrOfcurrentRecord);
			  
			  let table = document.querySelector("table");
			  generateTableHead(table, currentRecordKeysAsHeaders);
			  generateTable(table, generalLedgerInfoJsonData);
			  
			  console.log("Loop End:");
			  
			  }else{
				var requestedAction=generalLedgerInfoJsonData["Message"];
				alert(requestedAction);
			}
	}
	  
	  
	  
	});

	xhr.open("POST", "/LMSServer/GetGeneralLedger?glId="+glIdToGetCookieStatus);
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);



function generateTableHead(table, data) {
//alert("In Table Head");

  let thead = table.createTHead();
  let row = thead.insertRow();
  row.setAttribute('class', 'row100 head');
  
//  for (let key of data) {
    let th1 = document.createElement("th");
    let text1 = document.createTextNode("Id");
    th1.appendChild(text1);
    row.appendChild(th1);
//  }
    let th2 = document.createElement("th");
    let text2 = document.createTextNode("Description");
    th2.appendChild(text2);
    row.appendChild(th2);
    
    let th3 = document.createElement("th");
    let text3 = document.createTextNode("Loan Application Id");
    th3.appendChild(text3);
    row.appendChild(th3);
    
    let th10 = document.createElement("th");
    let text10 = document.createTextNode("Loan Id");
    th10.appendChild(text10);
    row.appendChild(th10);
    
    /*let headerCustomerCellNo = document.createElement("th");
    let textCustomerCellNo = document.createTextNode("Transaction Id");
    headerCustomerCellNo.appendChild(textCustomerCellNo);
    row.appendChild(headerCustomerCellNo);*/
    
    
    /*let headerQuestionAnswer = document.createElement("th");
    let textQuestionAnswer = document.createTextNode("Question");
    headerQuestionAnswer.appendChild(textQuestionAnswer);
    row.appendChild(headerQuestionAnswer);
    
    let headerAnswer = document.createElement("th");
    let textAnswer = document.createTextNode("Answer");
    headerAnswer.appendChild(textAnswer);
    row.appendChild(headerAnswer);*/
    
    let th4 = document.createElement("th");
    let text4 = document.createTextNode("Debit Amount");
    th4.appendChild(text4);
    row.appendChild(th4);
    
    let th5 = document.createElement("th");
    let text5 = document.createTextNode("Credit Amount");
    th5.appendChild(text5);
    row.appendChild(th5);
    
    let th6 = document.createElement("th");
    let text6 = document.createTextNode("Created At");
    th6.appendChild(text6);
    row.appendChild(th6);
    
    let th7 = document.createElement("th");
    let text7 = document.createTextNode("Created By");
    th7.appendChild(text7);
    row.appendChild(th7);
    
    /*let th8 = document.createElement("th");
    let text8 = document.createTextNode("Updated At");
    th8.appendChild(text8);
    row.appendChild(th8);
    
    let th9 = document.createElement("th");
    let text9 = document.createTextNode("Updated By");
    th9.appendChild(text9);
    row.appendChild(th9);*/
  
  /*let th6 = document.createElement("th");
  let text6 = document.createTextNode("Action");
  th6.appendChild(text6);
  row.appendChild(th6);*/
}


function generateTable(table, generalLedgerInfoJsonData) {
	
	console.log("In Table Data**********************************************************");
	
	
	for(i=0;i<generalLedgerInfoJsonData.data.length;i++){
		
		let thead = table.createTHead();
		  let row = thead.insertRow();
		  row.setAttribute('class', 'row100 head');
		  
		  console.log("GL Record Number------------------------------------------------------:",i);
		  
		  var glRecordId = generalLedgerInfoJsonData.data[i].idGl;
		  var glDescription = generalLedgerInfoJsonData.data[i].glDescription;
		  var loanId = generalLedgerInfoJsonData.data[i].disbursedLoanDTO.id;
		  var loanApplicationId = generalLedgerInfoJsonData.data[i].disbursedLoanDTO.application.id;
		  var txnId = generalLedgerInfoJsonData.data[i].txnId;
		  var amount = generalLedgerInfoJsonData.data[i].amount;
		  var debitTransaction = generalLedgerInfoJsonData.data[i].debitTransaction;
		  
		  /*var datCreation = generalLedgerInfoJsonData.data[i].datCreation;
		  var createdBy = generalLedgerInfoJsonData.data[i].createdBy;
		  var datLastUpdated = generalLedgerInfoJsonData.data[i].datLastUpdated;
		  var updatedBy = generalLedgerInfoJsonData.data[i].updatedBy;*/
		  
		  var Requested_Action = generalLedgerInfoJsonData.data[i].Requested_Action;
		  
		  console.log("Record Id: "+glRecordId);
		  console.log("Description: "+glDescription);
		  console.log("Loan Id: "+loanId);
		  console.log("Loan Application Id: "+loanApplicationId);
		  console.log("Txn Id: "+txnId);
		  console.log("Amount: "+amount);
		  console.log("Is Debit Txn: "+debitTransaction);
		  
		 /* console.log("Creation Date: "+datCreation);
		  console.log("Created By: "+createdBy);
		  console.log("Updated Date: "+datLastUpdated);
		  console.log("Updated By: "+updatedBy);*/

		  console.log("Requested Action: "+Requested_Action);
		  
		  
		  let CellGlRecordId = row.insertCell();
	      let textGlRecordId = document.createTextNode(glRecordId);
	      CellGlRecordId.appendChild(textGlRecordId);
	      
	      let CellGlDescription = row.insertCell();
	      let textGlDescription = document.createTextNode(glDescription);
	      CellGlDescription.appendChild(textGlDescription);
	      
	      let CellGlLoanApplicationId = row.insertCell();
	      let textGlLoanApplicationId = document.createTextNode(loanApplicationId);
	      CellGlLoanApplicationId.appendChild(textGlLoanApplicationId);
	      
	      let CellGlLoanId = row.insertCell();
	      let textGlLoanId = document.createTextNode(loanId);
	      CellGlLoanId.appendChild(textGlLoanId);
	      
	      /*let CellGlTxnId = row.insertCell();
	      let textGlTxnId = document.createTextNode(txnId);
	      CellGlTxnId.appendChild(textGlTxnId);*/
	      
	      if(debitTransaction==true){
	    	  
	    	  let CellGlDebitAmount = row.insertCell();
		      let textGlDebitAmount = document.createTextNode(amount);
		      CellGlDebitAmount.appendChild(textGlDebitAmount);
		      
		      let CellGlCreditAmount = row.insertCell();
		      let textGlCreditAmount = document.createTextNode("");
		      CellGlCreditAmount.appendChild(textGlCreditAmount);
	    	  
	      }else{
	    	  
	    	  let CellGlDebitAmount = row.insertCell();
		      let textGlDebitAmount = document.createTextNode(" ");
		      CellGlDebitAmount.appendChild(textGlDebitAmount);
		      
		      let CellGlCreditAmount = row.insertCell();
		      let textGlCreditAmount = document.createTextNode(amount);
		      CellGlCreditAmount.appendChild(textGlCreditAmount);
	      }
	      
	      
	      var createdAt = generalLedgerInfoJsonData.data[i].createdAt;
	      var updatedAt = generalLedgerInfoJsonData.data[i].updatedAt;
	      var createdBy = null;
	      var updatedBy = null;
		  
	      if(generalLedgerInfoJsonData.data[i].createdBy!==null){
			  console.log("Creatd By Display name: "+generalLedgerInfoJsonData.data[i].createdBy.displayName);
			  createdBy=generalLedgerInfoJsonData.data[i].createdBy.displayName;
		  }else{
			  console.log("Created by is null");
		  }
			
		 /* if(generalLedgerInfoJsonData.obj[i].updatedBy!==null){
			  console.log("Updated By Display name: "+generalLedgerInfoJsonData.obj[i].updatedBy.displayName);
			  updatedBy=generalLedgerInfoJsonData.obj[i].updatedBy.displayName;
		  }else{
			  console.log("Updated by is null");
		  }*/
		  
		  console.log("Created At: "+createdAt);
		  console.log("Created By: "+createdBy);
//		  console.log("Updated At: "+updatedAt);
//		  console.log("Updated By: "+updatedBy);
	      
	      let CellCreatedAt = row.insertCell();
	      let textCreatedAt = document.createTextNode(createdAt);
	      CellCreatedAt.appendChild(textCreatedAt);
	      
	      let CellCreatedBy= row.insertCell();
	      let textCreatedBy = document.createTextNode(createdBy);
	      CellCreatedBy.appendChild(textCreatedBy);
	      
//	      let CellUpdatedAt = row.insertCell();
//	      let textUpdatedAt = document.createTextNode(updatedAt);
//	      CellUpdatedAt.appendChild(textUpdatedAt);
//	      
//	      let CellUpdatedBy = row.insertCell();
//	      let textUpdatedBy = document.createTextNode(updatedBy);
//	      CellUpdatedBy.appendChild(textUpdatedBy);
	      
	    /*  let CellDatCreation = row.insertCell();
	      let textDateCreation = document.createTextNode(datCreation);
	      CellDatCreation.appendChild(textDateCreation);
	      
	      let CellCreatedBy= row.insertCell();
	      let textCreatedBy = document.createTextNode(createdBy);
	      CellCreatedBy.appendChild(textCreatedBy);
	      
	      let CellDatUpdated = row.insertCell();
	      let textDatUpdated = document.createTextNode(datLastUpdated);
	      CellDatUpdated.appendChild(textDatUpdated);
	      
	      let CellUpdatedBy = row.insertCell();
	      let textUpdatedBy = document.createTextNode(updatedBy);
	      CellUpdatedBy.appendChild(textUpdatedBy);*/
	      
	      
	   /* //Add Edit Button at the end of each line
	    let cell6 = row.insertCell();
	  	let button = document.createElement("button");
//	  	button.setAttribute("onClick","editClicked()");
	  	button.setAttribute("type","button");
	  	button.setAttribute("name","editButton");
	  	button.setAttribute("id",applicationId); //user id
	  	button.setAttribute("value",applicationId); // user Username
	  	button.setAttribute("onClick","applicationDetailPage(this)");
	  	
	  	
	  	let text = document.createTextNode("detail");
	   	button.appendChild(text);
	   	cell6.appendChild(button);*/
		  
	  }
	
	
}

/*function disburseLoan(e){
	 
//	writeCookie("selectedProductId",e.id, "1");
//	writeCookie("selectedProduct",e.value, "1");
	
	console.log("Application Loan Id: "+e.id);
	console.log("Application Loan Value: "+e.value);
    
//    location.replace("CreateNewProductScreenOne.html"); // for testing
//    $('#content').load('CreateNewProductScreenOne.html');
	
	var data = null;
	var userInfoJsonData=null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
	  if (this.readyState === 4) {
		console.log(this.responseText);
		  this.applicationInfoJsonData=JSON.parse(this.responseText);
		 
		  var keysAsHeaders;
		  var valueArray;
		  var valuesStrOfcurrentRecord;
		  
		  currentRecordKeysAsHeaders = Object.keys(this.applicationInfoJsonData.obj[0]);
		  console.log("Application Id Key:",currentRecordKeysAsHeaders);
		  
		  console.log("Table Header:",currentRecordKeysAsHeaders);
		  console.log("Table Values:",valuesStrOfcurrentRecord);
		  
		  let table = document.querySelector("table");
		  generateTableHead(table, currentRecordKeysAsHeaders);
		  generateTable(table, this.applicationInfoJsonData);
		  
		  console.log("Loop End:");
		  
		  alert("Successfully Dispursed");
			
	  }
	});

	xhr.open("POST", "/LMSServer/approveAndDisperseALoan?applicationId="+e.id);
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);
 
}*/

function applicationDetailPage(e){
	
	writeCookie("selectedApplicationId",e.id, "1");
//	writeCookie("selectedApplication",e.value, "1");
	
//	location.replace("CreateNewProductScreenOne.html");
	$('#content').load('LoanApplicationDetail.html');
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
	var splittedString=text.split(",");
	
	for(var i=0;i<splittedString.length;i++)
	{
	    var listViewItem=document.createElement('li');
	    
	    /*if(splittedString[i].endsWith("?")){
	    	listViewItem.appendChild(document.createTextNode(splittedString[i].substring(0, splittedString[i].length-1)));
		    listView.appendChild(listViewItem);
		    
		    cell.appendChild(listView);
	    }else{*/
	    	listViewItem.appendChild(document.createTextNode(splittedString[i]));
		    listView.appendChild(listViewItem);
		    
		    cell.appendChild(listView);
	    /*}*/
	    
	    
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

function addProduct(){
//	location.replace("CreateNewProductScreenOne.html");
	$('#content').load('CreateNewProductScreenOne.html');
}

function back(){
//	location.replace("UserInfoUsingDynamicTable.html");
	$('#content').load('UserInfoUsingDynamicTable.html');
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