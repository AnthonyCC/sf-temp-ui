function setFilter(ecForm,newForm)
{
	var ecElements=ecForm.elements;
	for(var i=0;i<ecElements.length;i++)
	{
		var element=ecElements[i];
		if(element.name.indexOf("ec_f_")!=-1)
		{
			if(element.value.length>0)
			add_input(newForm,"hidden",element.name,element.value);
		}
	}	
	if(newForm.ec_f_a==null)add_input(newForm,"hidden","ec_f_a","fa");
	else newForm.ec_f_a.value="fa";
}

function add_input(form,type,name,value)
{
	var newInput=document.createElement("input");
	newInput.setAttribute("type", type);
	newInput.setAttribute("name", name);
	newInput.setAttribute("value", value);							
	form.appendChild(newInput);						
	
}	

function getFilterValue(ecForm,isEscape)
{
	var result="";
	var ecElements=ecForm.elements;
	for(var i=0;i<ecElements.length;i++)
	{
		var element=ecElements[i];
		if(element.name.indexOf("ec_f_") !=-1 && element.name != 'ec_f_a')
		{		
			if(element.value.length>0)
			result+=element.name+"="+element.value+"&";			
		}
	}	
	result+="ec_f_a=fa";
	if(isEscape) result=escape(result)
	return result;
}

    function getParameter( name )
	  {
		  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
		  var regexS = "[\\?&]"+name+"=([^&#]*)";
		  var regex = new RegExp( regexS );
		  var results = regex.exec( window.location.href );
		  if( results == null )
		    return "";
		  else
		    return results[1];
	  }