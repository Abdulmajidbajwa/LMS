//*********************************************************************GetKycQuestion Api
	var data = null;
	var userInfoJsonData=null;

	var xhr = new XMLHttpRequest();
	xhr.withCredentials = true;

	xhr.addEventListener("readystatechange", function () {
	  if (this.readyState === 4) {
		console.log(this.responseText);
		  this.applicationInfoJsonData=JSON.parse(this.responseText);
		 
		  
		  
		  for(i=0;i<this.applicationInfoJsonData.obj.length;i++){
			  
			  console.log("Application Number------------------------------------------------------:",i);
			  
			  var applicationId = this.applicationInfoJsonData.obj[i].id;
			  var applicationRequestedAmount = this.applicationInfoJsonData.obj[i].requestedamount;
			  var applicationStatus = this.applicationInfoJsonData.obj[i].status;
			  
			  console.log("Application Id:",applicationId);
			  console.log("Application Amount:",applicationRequestedAmount);
			  console.log("Application Status:",applicationStatus);
			  
			  var applicationProductObj = this.applicationInfoJsonData.obj[i].product;
			  
			  console.log("Product:",applicationProductObj);
			  console.log("Product Name:",applicationProductObj.productName);
			  console.log("Product Category:",applicationProductObj.productCatagory);
			  
			  console.log("Product Specification:",applicationProductObj.productSpecification);
			  console.log("Product Specification Length:",applicationProductObj.productSpecification.length);
			  
			  for(j=0;j<applicationProductObj.productSpecification.length;j++){
				  console.log("Product Specification Name:",applicationProductObj.productSpecification[j].name);
				  console.log("Product Specification Value:",applicationProductObj.productSpecification[j].value);
			  }	
			  
			  console.log("Questions:",applicationProductObj.questions);
			  for(j=0;j<applicationProductObj.questions.length;j++){
				  console.log("Question Name:",applicationProductObj.questions[j].name);
				  console.log("Question Value:",applicationProductObj.questions[j].value);
			  }
			  
			  var applicationCustomerObj = this.applicationInfoJsonData.obj[i].customer;
			  
			  console.log("Customer:",applicationCustomerObj);
			  console.log("Customer Name:",applicationCustomerObj.id);
			  console.log("Customer CellNo:",applicationCustomerObj.cellNo);
			  console.log("Customer FullName:",applicationCustomerObj.fullName);
			  
			  
		  }
		  
		  console.log("Loop End:");
			
	  }
	});

	xhr.open("POST", "/LMSServer/getLoanApplications");
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
    	
    	if(element[key].startsWith(",")){
    		 let cell = row.insertCell();
			 createListInCell(cell,checkAndGetCorrectString(element[key]));
    	}else{
    		let cell = row.insertCell();
    	      let text = document.createTextNode(checkAndGetCorrectString(element[key]));
    	      cell.appendChild(text);
    	}
    	
    }
    
    
    
  //Add Edit Button at the end of each line
    let cell = row.insertCell();
	let button = document.createElement("button");
//	button.setAttribute("onClick","editClicked()");
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
	 
	writeCookie("selectedProductId",e.id, "1");
	writeCookie("selectedProduct",e.value, "1");
    
//    location.replace("CreateNewProductScreenOne.html"); // for testing
    $('#content').load('CreateNewProductScreenOne.html');
 
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
	    
	    if(splittedString[i].endsWith("?")){
	    	listViewItem.appendChild(document.createTextNode(splittedString[i].substring(0, splittedString[i].length-1)));
		    listView.appendChild(listViewItem);
		    
		    cell.appendChild(listView);
	    }else{
	    	listViewItem.appendChild(document.createTextNode(splittedString[i]));
		    listView.appendChild(listViewItem);
		    
		    cell.appendChild(listView);
	    }
	    
	    
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

