var white_turn=true;
var move_number=0;
var match_finished=false;

function getMove()
{
 	document.getElementById("111").innerHTML="";
	var repeat=true;
	while(repeat)
	{
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() 
		{
			if (xhttp.readyState == 4 && xhttp.status == 200) 
			{	 
				repeat=false;
				myFunction(xhttp);
			}
			else
				repeat=false;
		};
		xhttp.open("GET","MatchHandlerServlet?viewer="+move_number,true);
		xhttp.send();
	}
}

function myFunction(xml) 
{
  var i;
  var start;
  var end;
  var replace;
  var check;
  var mate;
  var side;
  var start_row,start_column
  var loc_row,loc_column;
  var piece;
  
  if(white_turn)
	 side=1;
  else
	  side=-1;
  
  var xmlDoc = xml.responseXML;
  var x = xmlDoc.getElementsByTagName("move");
  for (i = 0; i <x.length; i++) 
  { 
	if(!white_turn)
	{
		start=63-x[i].getElementsByTagName("start")[0].childNodes[0].nodeValue/1;
		end=63-x[i].getElementsByTagName("end")[0].childNodes[0].nodeValue/1;
	}
	else
	{
		start=x[i].getElementsByTagName("start")[0].childNodes[0].nodeValue/1;
		end=x[i].getElementsByTagName("end")[0].childNodes[0].nodeValue/1;
	}
	piece=x[i].getElementsByTagName("replace")[0].childNodes[0].nodeValue/1;
	check=x[i].getElementsByTagName("check")[0].childNodes[0].nodeValue;
	mate=x[i].getElementsByTagName("mate")[0].childNodes[0].nodeValue;
	stalemate=x[i].getElementsByTagName("stalemate")[0].childNodes[0].nodeValue;
  }

  if(piece!=6||start_column-loc_column==1||start_column-loc_column==-1)
  {
	  var temp;
	  temp= document.getElementById(start).innerHTML;
	  document.getElementById(start).innerHTML ="";
	  
	    if(piece==4)
		{
			if(side==1)
				temp="&#9814;";
			else
				temp="&#9820;";
		}		
		if(piece==5)
		{
			
			if(side==1)
			temp="&#9813;";
			else
				temp="&#9819;";
		}
		if(piece==2)
		{
			if(side==1)
			temp="&#9815;"
			else
				temp="&#9821;";
		}
		if(piece==3)
		{
			if(side==1)
			temp="&#9816;"
			else
				temp="&#9822;";
		}
	  document.getElementById(end).innerHTML =temp;
  }
  else
  { 
		if(start_column>loc_column)
		{	
			if(side==-1)
			{
				document.getElementById(end+1).innerHTML=document.getElementById(56).innerHTML;
				document.getElementById(56).innerHTML="";
			}
			else
			{
				document.getElementById(end+1).innerHTML=document.getElementById(0).innerHTML;
				document.getElementById(0).innerHTML="";
			}
		}
		if(start_column<loc_column)
		{	
			if(side==-1)
			{
				document.getElementById(end-1).innerHTML=document.getElementById(63).innerHTML;
				document.getElementById(63).innerHTML="";	
			}
			else
			{
				document.getElementById(end-1).innerHTML=document.getElementById(0).innerHTML;
				document.getElementById(0).innerHTML="";	
			}
		}
		document.getElementById(end).innerHTML=document.getElementById(start).innerHTML;
		document.getElementById(start).innerHTML="";	
  }
 
   
  if(check=="true")
	  document.getElementById("111").innerHTML="you'r under check";
  
  if(mate=="false")
	  match_finished=false;
  else
  {
	  document.getElementById("111").innerHTML="you'r mate";
	  match_finished=true;
  }
  
  if(stalemate=="false")
	   match_finished=false;
  else
  {
	  document.getElementById("111").innerHTML="stalemate";
	  match_finished=true;
  }
  move_number++;
  white_turn=!white_turn;
  
  if(!match_finished)
	  getMove();  
  else
  {
		  var r = confirm("match finished, press ok to get back to home page ");
		  if (r == true) 
		  {
			 window.location='userpage.html';
		  } 
		  else 
		  {
		      x = "You pressed Cancel!";
		  } 
  }
  
}