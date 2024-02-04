//*********************************************************************GetKycQuestion Api
	var data = null;
	var userInfoJsonData=null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
	 /* if (this.readyState === 4) {
		  console.log(this.responseText);
		  this.applicationInfoJsonData=JSON.parse(this.responseText);
		 
		  var keysAsHeaders;
		  var valueArray;
		  var valuesStrOfcurrentRecord;

		  currentRecordKeysAsHeaders = Object.keys(this.applicationInfoJsonData.data[0]);
		  console.log("Application Id Key:",currentRecordKeysAsHeaders);
		  
		  console.log("Table Header:",currentRecordKeysAsHeaders);
		  console.log("Table Values:",valuesStrOfcurrentRecord);
		  
		  let table = document.querySelector("table");
		  generateTableHead(table, currentRecordKeysAsHeaders);
		  generateTable(table, this.applicationInfoJsonData);
		  
		  console.log("Loop End:");
	  }*/
	  
	  
	  if (this.readyState == 4 && this.status == 200) {
			var applicationInfoJsonData=JSON.parse(this.responseText);
			console.log(applicationInfoJsonData);
			
			//As All Apis Always Returned 200 Response except incase unhandled Exception Occurred. So, we need one Flag Name as Requested_Action, It will only True incase of Success. So, always check this flag
			var requestedAction=applicationInfoJsonData["Requested_Action"];
			
			
			if(requestedAction.toString().toLowerCase()=="true"){
				  console.log(this.responseText);
//				  this.applicationInfoJsonData=JSON.parse(this.responseText);
				 
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

	xhr.open("POST", "/LMSServer/getPendingOrCompletedLoanApplicationsByStatus?requiredPendingRequest=true");
	xhr.setRequestHeader("authString", readCookie("Token"));
	
	xhr.send(data);



function generateTableHead(table, data) {
//alert("In Table Head");

  let thead = table.createTHead();
  let row = thead.insertRow();
  row.setAttribute('class', 'row100 head');
  
//  for (let key of data) {
    let th1 = document.createElement("th");
    let text1 = document.createTextNode("Application Id");
    th1.appendChild(text1);
    row.appendChild(th1);
//  }
    let th2 = document.createElement("th");
    let text2 = document.createTextNode("Production Name");
    th2.appendChild(text2);
    row.appendChild(th2);
    
    let th3 = document.createElement("th");
    let text3 = document.createTextNode("Customer Id");
    th3.appendChild(text3);
    row.appendChild(th3);
    
    let headerCustomerCellNo = document.createElement("th");
    let textCustomerCellNo = document.createTextNode("Customer Cell No");
    headerCustomerCellNo.appendChild(textCustomerCellNo);
    row.appendChild(headerCustomerCellNo);
    
    
    /*let headerQuestionAnswer = document.createElement("th");
    let textQuestionAnswer = document.createTextNode("Question");
    headerQuestionAnswer.appendChild(textQuestionAnswer);
    row.appendChild(headerQuestionAnswer);
    
    let headerAnswer = document.createElement("th");
    let textAnswer = document.createTextNode("Answer");
    headerAnswer.appendChild(textAnswer);
    row.appendChild(headerAnswer);*/
    
    let th4 = document.createElement("th");
    let text4 = document.createTextNode("Requested Amount");
    th4.appendChild(text4);
    row.appendChild(th4);
    
    let th5 = document.createElement("th");
    let text5 = document.createTextNode("Application Status");
    th5.appendChild(text5);
    row.appendChild(th5);
    
    let th6 = document.createElement("th");
    let text6 = document.createTextNode("Creation Date");
    th6.appendChild(text6);
    row.appendChild(th6);
    
    let th7 = document.createElement("th");
    let text7 = document.createTextNode("Created By");
    th7.appendChild(text7);
    row.appendChild(th7);
    
    let th8 = document.createElement("th");
    let text8 = document.createTextNode("Updated Date");
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
	
//	let thead = table.createTHead();
//	  let row = thead.insertRow();
//	  row.setAttribute('class', 'row100 head');
	
	
	for(i=0;i<applicationInfoJsonData.data.length;i++){
		
		if(applicationInfoJsonData.data[i].status=="PENDINGFORAPPROVAL"){
			let thead = table.createTHead();
			  let row = thead.insertRow();
			  row.setAttribute('class', 'row100 head');
			  
			  console.log("Application Number------------------------------------------------------:",i);
			  
			  var applicationId = applicationInfoJsonData.data[i].id;
			  var applicationRequestedAmount = applicationInfoJsonData.data[i].requestedamount;
			  var applicationStatus = applicationInfoJsonData.data[i].status;
			  
			  //["2", "SME", "Individual", ",Channel Fee", ",95", ",KYC Question One?"]
			  
			  valuesStrOfcurrentRecord='["'+applicationId+'"';
			  
			  let cell1 = row.insertCell();
		      let text1 = document.createTextNode(applicationId);
		      cell1.appendChild(text1);
			  
			  console.log("Application Id:",applicationId);
			  console.log("Application Amount:",applicationRequestedAmount);
			  console.log("Application Status:",applicationStatus);
			  
			  var applicationProductObj = applicationInfoJsonData.data[i].product;
			  
			  console.log("Product:",applicationProductObj);
			  console.log("Product Name:",applicationProductObj.productName);
			  console.log("Product Category:",applicationProductObj.productCatagory);
			  
			  valuesStrOfcurrentRecord=valuesStrOfcurrentRecord+',';
			  valuesStrOfcurrentRecord=valuesStrOfcurrentRecord+'"'+applicationProductObj.productName+'"';
			  
			  let cell2 = row.insertCell();
		      let text2 = document.createTextNode(applicationProductObj.productName);
		      cell2.appendChild(text2);
//			  valuesStrofcurrentRecord=valuesStrofcurrentRecord+',';
//			  valuesStrofcurrentRecord=valuesStrofcurrentRecord+'"'+applicationProductObj.productCatagory+'"';
			  
			  console.log("Product Specification:",applicationProductObj.productSpecification);
			  console.log("Product Specification Length:",applicationProductObj.productSpecification.length);
			  
			  for(j=0;j<applicationProductObj.productSpecification.length;j++){
				  console.log("Product Specification Name:",applicationProductObj.productSpecification[j].name);
				  console.log("Product Specification Value:",applicationProductObj.productSpecification[j].value);
			  }	
			  
			  
			  
			  var applicationCustomerObj = applicationInfoJsonData.data[i].customer;
			  
			  console.log("Customer:",applicationCustomerObj);
			  console.log("Customer Id:",applicationCustomerObj.id);
			  console.log("Customer CellNo:",applicationCustomerObj.cellNo);
			  console.log("Customer FullName:",applicationCustomerObj.fullName);
			  
			  valuesStrOfcurrentRecord=valuesStrOfcurrentRecord+',';
			  valuesStrOfcurrentRecord=valuesStrOfcurrentRecord+'"'+applicationCustomerObj.id+'"';
			  
			 	  
			  let cell3 = row.insertCell();
		      let text3 = document.createTextNode(applicationCustomerObj.id);
		      cell3.appendChild(text3);
			  
		      let cellCustomerCellNo = row.insertCell();
		      let textCustomerCellNo = document.createTextNode(applicationCustomerObj.cellNo);
		      cellCustomerCellNo.appendChild(textCustomerCellNo);
		      
		      
		      /*console.log("Questions:",applicationProductObj.questions);
			  
			  let questionCell = row.insertCell();
			  let answerCell = row.insertCell();
		      
			  for(j=0;j<applicationProductObj.questions.length;j++){
				  console.log("Question Name:",applicationProductObj.questions[j].question);
				  console.log("Answer:",applicationProductObj.questions[j].answer);
				  
			      let questionText = document.createTextNode((j+1)+"-"+applicationProductObj.questions[j].question);
			      questionCell.appendChild(questionText);
			      var br = document.createElement("br");
			      questionCell.appendChild(br);
			  
			      let answerText = document.createTextNode((j+1)+"-"+applicationProductObj.questions[j].answer);
			      answerCell.appendChild(answerText);
			      var br = document.createElement("br");
			      answerCell.appendChild(br);
			  
			  }*/
		      
			  
			  valuesStrOfcurrentRecord=valuesStrOfcurrentRecord+',';
			  valuesStrOfcurrentRecord=valuesStrOfcurrentRecord+'"'+applicationRequestedAmount+'"';
			  
			  let cell4 = row.insertCell();
		      let text4 = document.createTextNode(applicationRequestedAmount);
		      cell4.appendChild(text4);
			  
			  valuesStrOfcurrentRecord=valuesStrOfcurrentRecord+',';
			  valuesStrOfcurrentRecord=valuesStrOfcurrentRecord+'"'+applicationStatus+'"]';
			  
			  let cell5 = row.insertCell();
		      let text5 = document.createTextNode(applicationStatus);
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
		  	let button = document.createElement("button");
//		  	button.setAttribute("onClick","editClicked()");
		  	button.setAttribute("type","button");
		  	button.setAttribute("name","editButton");
		  	button.setAttribute("id",applicationId); //user id
		  	button.setAttribute("value",applicationId); // user Username
		  	button.setAttribute("onClick","applicationDetailPage(this)");
		  	
		  	
		  	let text = document.createTextNode("detail");
		   	button.appendChild(text);
		   	cell6.appendChild(button);
		}
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
		  
		  currentRecordKeysAsHeaders = Object.keys(this.applicationInfoJsonData.data[0]);
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

