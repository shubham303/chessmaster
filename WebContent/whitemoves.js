var side="white";              // side=1 means you are white else black;
var board=new Array(8);
var possible_moves=new Array(64);
var kings_loc;
var our_turn;
var check;
var mate;
var stalemate=false;
var select =true;
if(side=="white")
{
    board[0]=[4,3,2,6,5,2,3,4];
    board[1]=[1,1,1,1,1,1,1,1];
    for (var i = 2; i <6 ; i++)
    {
		board[i]=[0,0,0,0,0,0,0,0];
    }
    board[6]=[-1,-1,-1,-1,-1,-1,-1,-1];
   	board[7]=[-4,-3,-2,-6,-5,-2,-3,-4];
	kings_loc=3;
	our_turn=true;
}
else
{
    board[0]=[4,3,2,5,6,2,3,4];
    board[1]=[1,1,1,1,1,1,1,1];
    for (var i = 2; i <6 ; i++)
    {
		board[i]=[0,0,0,0,0,0,0,0];
    }
    board[7]=[-4,-3,-2,-5,-6,-2,-3,-4];
    board[6]=[-1,-1,-1,-1,-1,-1,-1,-1];
	kings_loc=4;  
	our_turn=false;
}

 
var king_moved=false,left_rook_moved=false,right_rook_moved=false;					//for castling purpose                 
var loc;																			//location at which mouse clicked
var loc_row,loc_column;
var piece;																			//value of element at specified location
var make_move=false;
var start,start_row,start_column;	//  location at which previous mouse click occured

function show()
{
    var favDialog = document.getElementById('favDialog');
    favDialog.showModal();
}

function makeMove(event)
{	
	
    var row,column;

    loc=event.target.id/1;
    

	if(loc!=64&&our_turn)
	{
		loc_row=Math.floor(loc/8);
		loc_column=loc%8;
		
		if(make_move==false)
		{
			start_row=loc_row;
			start_column=loc_column;
			start=loc;
			for (var i = 0; i <64; i++)
			{
				possible_moves[i]=false;
			}
			piece=board[loc_row][loc_column];
			if(piece>0)
			{
				//document.get	
				getPossibleMoves();
			 
				for(var i=0;i<possible_moves.length;i++)
				{	
					if(possible_moves[i]==true)
					{
						document.getElementById(i).style.borderColor="#008000";
					}
				}
				make_move=true;
			}
		}
		else
		{
				for(var i=0;i<64;i++)
				{
					if(possible_moves[i]==true)
					{
						document.getElementById(i).style.borderColor=document.getElementById(i).style.backgroundColor;
					}
				}

				if(possible_moves[loc]==true)                                                      //set elementn at desired position
				{
					our_turn=false;
					if(piece==4)
					{
						if(start_row==0&&start_column==7)
							left_rook_moved=true;
						if(start_row==0&&start_column==0)
							right_rook_moved=true;
					}
					if(piece!=6||start_column-loc_column==1||start_column-loc_column==-1)
					{
						board[loc_row][loc_column]=piece;
						board[start_row][start_column]=0;
						document.getElementById(loc).innerHTML=document.getElementById(start).innerHTML;
						document.getElementById(start).innerHTML="";
					}
					else
					{

						board[loc_row][loc_column]=piece;
						board[start_row][start_column]=0;
						if(start_column>loc_column)
						{	
							board[0][0]=0;
							board[start_row][start_column-1]=4;
							document.getElementById(loc+1).innerHTML=document.getElementById(0).innerHTML;
							document.getElementById(0).innerHTML="";
						
						}
						if(start_column<loc_column)
						{	
							board[0][7]=0;
							board[start_row][start_column+1]=4;
							document.getElementById(loc-1).innerHTML=document.getElementById(7).innerHTML;
							document.getElementById(7).innerHTML="";	
						}
						document.getElementById(loc).innerHTML=document.getElementById(start).innerHTML;
						document.getElementById(start).innerHTML="";
						
					}
					if(piece==6)
					{
						kings_loc=loc;
						king_moved=true;
					}
					if(piece==1&&loc_row==7)
					{	
						select=false;
						show();
					}
					if(checkIfEnemyKingUnderAttack(enemy_kings_loc))
					{
						check=true;
						if(isCheckMate())
						{
							mate=true;
						}
						else
							mate=false;
					}
					else
					{
						check=false;
						if(isCheckMate())
						{
							stalemate=true;
						}
					}
					if(select)
					{
						sendMove();
						if(mate!="true"&&stalemate!="true")
						getMove();
					}
				}
				make_move=false;
		}
	}
}
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
		xhttp.open("GET","MatchHandlerServlet",true);
		xhttp.send();
	}
}
function sendMove()
{
	var repeat=true;
	var  i=0;
	while(repeat)
	{
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() 
		{
			repeat=false;
		};
    	var param="start="+start+"&end="+loc+"&replace="+board[loc_row][loc_column]+"&check="+check+"&mate="+mate+"&stalemate="+stalemate;
    	xhttp.open("POST", "MatchHandlerServlet",true);
    	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    	xhttp.send(param);
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
  
  var start_row,start_column
  var loc_row,loc_column;
  var piece;
  
  var xmlDoc = xml.responseXML;
  var x = xmlDoc.getElementsByTagName("move");
  for (i = 0; i <x.length; i++) 
  { 
     start=63-x[i].getElementsByTagName("start")[0].childNodes[0].nodeValue/1;
     end=63-x[i].getElementsByTagName("end")[0].childNodes[0].nodeValue/1;
     piece=x[i].getElementsByTagName("replace")[0].childNodes[0].nodeValue/1;
     check=x[i].getElementsByTagName("check")[0].childNodes[0].nodeValue;
     mate=x[i].getElementsByTagName("mate")[0].childNodes[0].nodeValue;
     stalemate=x[i].getElementsByTagName("stalemate")[0].childNodes[0].nodeValue;
  }
  start_row=Math.floor(start/8);
  start_column=start%8;
  loc_row=Math.floor(end/8);
  loc_column=end%8;
  
  if(piece!=6||start_column-loc_column==1||start_column-loc_column==-1)
  {
	  var temp;
	  temp= document.getElementById(start).innerHTML;
	  document.getElementById(start).innerHTML ="";
	  
	  if(start==56)
		  enemy_right_rook_moved=true;
	  if(start==63)
		  enemy_left_rook_moved=true;
	  
	  if(piece==4)
		  temp="&#9820;";
	  if(piece==5)
		  temp="&#9819;";
	  if(piece==2)
		  temp="&#9821;"
	  if(piece==3)
		temp="&#9822;";
	  
	  document.getElementById(end).innerHTML=temp;
	 
	  board[loc_row][loc_column]=-piece;
	  board[start_row][start_column]=0;
  }
  else
  {
	    board[loc_row][loc_column]=-piece;
		board[start_row][start_column]=0;
		if(start_column>loc_column)
		{	
			board[7][0]=0;
			board[start_row][start_column-1]=-4;
			document.getElementById(end+1).innerHTML=document.getElementById(56).innerHTML;
			document.getElementById(56).innerHTML="";
		}
		if(start_column<loc_column)
		{	
			board[7][7]=0;
			board[start_row][start_column+1]=-4;
			document.getElementById(end-1).innerHTML=document.getElementById(63).innerHTML;
			document.getElementById(63).innerHTML="";	
		}
		document.getElementById(end).innerHTML=document.getElementById(start).innerHTML;
		document.getElementById(start).innerHTML="";
		
		
  }
  if(piece==6)
  {
    enemy_kings_loc=loc;
	enemy_king_moved=true;  
  }
  if(check=="true")
	  document.getElementById("111").innerHTML="you'r under check";
  if(mate=="false")
   our_turn=true;
  
  else
  {
	  var r = confirm("u r mate, press ok to get back to home page ");
	  if (r == true) 
	  {
		 window.location='userpage.html';
	  } 
	  else 
	  {
	      x = "You pressed Cancel!";
	  } 
  }
  if(stalemate=="false")
	   our_turn=true;
  else
  {
	  var r = confirm("its a stalemate, press ok to get back to home page ");
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
function selectPiece()
{
	var x = document.getElementById("mySelect").value;
	var val,i;
	if(x=="rook")
	{
		val=4;
		if(side=="white")
			i="&#9814;";
		
		else
			i="&#9820;";
	}		
	if(x=="queen")
	{
		val=5;
		if(side=="white")
		i="&#9813;";
		else
			i="&#9819;";
	}
	if(x=="bishop")
	{
		val=2;
		if(side=="white")
		i="&#9815;"
		else
			i="&#9821;";
	}
	if(x=="knight")
	{
		val=3;
		if(side=="white")
		i="&#9816;"
		else
			i="&#9822;";

	}
	document.getElementById(loc).innerHTML=i;
	board[loc_row][loc_column]=val;
	select=true;
	
	sendMove();
	if(mate!="true"&&stalemate!="true")
	getMove();
}
function getPossibleMoves()
{
	
	if(piece==3)
	{
		getPossibleMovesForKnight();
	}
	if(piece==1)
	{
		getPossibleMovesForPawn();
	}
	if(piece==4)
	{
		getPossibleMovesForRook();
	}
	if(piece==2)
	{
		getPossibleMovesForBishop();
	}
	if(piece==5)
	{
		getPossibleMovesForQueen();
	}
	if(piece==6)
	{
		
		getPossibleMovesForKing();
	}

}
function getPossibleMovesForKing()
{
	
	var row=loc_row,column;
	var i=0;
	while(i<8)
	{
		if(i==0)
		{
			column=loc_column+1;
		}
		if(i==1)
		{
			column=loc_column-1;
			
		}
		if(i==2)
		{
			row=loc_row+1;
			column=loc_column;
		}
		if(i==3)
		{
			column=loc_column+1;
			
		}
		if(i==4)
		{
			column=loc_column-1;	
		}
		if(i==5)
		{
			row=loc_row-1;
			column=loc_column;
		}
		if(i==6)
		{
			column=loc_column+1;
			
		}
		if(i==7)
		{
			column=loc_column-1;	
		}
		i++;
	
		if(!(row>7||column>7||row<0||column<0))
			checkMove(row,column,row*8+column);
		
	}
	checkCastling();
}
function checkCastling()
{
	if(!king_moved)
	{
		
		if(!left_rook_moved)
		{
			
			if(castlingAllowed(1))
			{
				possible_moves[kings_loc+2]=true;	
			}
		}
		if(!right_rook_moved)
		{
			if(castlingAllowed(-1))
			{
				possible_moves[kings_loc-2]=true;
			}
		}
	}
	
}
function castlingAllowed(side)                                         //side indicate to which side we want to do castling    1 means left and -1 means right
{
	
	var row=Math.floor(kings_loc/8);
	
	var column=(kings_loc%8)+side;
	

	
	while(column>=1&&column<=6)
	{
		
		if(board[row][column]!=0)
			return false;
		
		column+=side;
		
	}
	
	if(checkIfKingUnderAttack(kings_loc+side))
		return false;
	
	if(checkIfKingUnderAttack(kings_loc+side+side))
		return false;

	return true;
	
}
function getPossibleMovesForBishop()
{
	var row=loc_row+1,column=loc_column+1;
	while(row<=7&&column<=7)                                              //move towards left corner
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;

		row++;
		column++;
	}
	
	row=loc_row+1;
	column=loc_column-1;
	while(row<=7&&column>=0)                                               //move towards right corner
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;

		row++;
		column--;
	}

	row=loc_row-1;
	column=loc_column+1;
	while(row>=0&&column<=7)                                                    //move towards lower left corner 
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;

		row--;
		column++;
	}
	
	row=loc_row-1;
	column=loc_column-1;
	while(row>=0&&column>=0)                                                    //move towards lower right corner 
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;
		row--;
		column--;
	}

}
function getPossibleMovesForQueen()
{
	
	var row=loc_row+1,column=loc_column;
	while(row<=7)
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;

		row++;
	}
	row=loc_row-1;
	while(row>=0)
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;
		row--;
	}
	row=loc_row;
	column=loc_column+1;
	while(column<=7)
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;
		column++;
	}
	column=loc_column-1;
	while(column>=0)
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;
		column--;
	}
	
	row=loc_row+1,column=loc_column+1;
	while(row<=7&&column<=7)                                              //move towards left corner
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;

		row++;
		column++;
	}
	
	row=loc_row+1;
	column=loc_column-1;
	while(row<=7&&column>=0)                                               //move towards right corner
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;

		row++;
		column--;
	}

	row=loc_row-1;
	column=loc_column+1;
	while(row>=0&&column<=7)                                                    //move towards lower left corner 
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;

		row--;
		column++;
	}
	
	row=loc_row-1;
	column=loc_column-1;
	while(row>=0&&column>=0)                                                    //move towards lower right corner 
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;
		row--;
		column--;
	}
}
function getPossibleMovesForRook()
{
	var row=loc_row+1,column=loc_column;

	while(row<=7)
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;

		row++;
	}
	row=loc_row-1;
	while(row>=0)
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;
		row--;
	}
	row=loc_row;
	column=loc_column+1;
	while(column<=7)
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;
		column++;
	}
	column=loc_column-1;
	while(column>=0)
	{
		checkMove(row,column,row*8+column);
		if(board[row][column]!=0)
			break;
		column--;
	}

}
function getPossibleMovesForPawn()
{
	
	if(loc_row==1)
	{
		if(board[loc_row+2][loc_column]==0 && board[loc_row+1][loc_column]==0)
		{	
			checkMove(loc_row+2,loc_column,loc+16);
		}
	}
	if(loc_row+1<=7)
	{
		if(board[loc_row+1][loc_column]==0)
		{	
			checkMove(loc_row+1,loc_column,loc+8);
		}
	}	
	if(loc_row+1<=7)
	{
		if(loc_column+1<=7)
		{
			if(board[loc_row+1][loc_column+1]<0)
			{
				checkMove(loc_row+1,loc_column+1,(loc_row+1)*8+loc_column+1);
			}
		}
		if(loc_column-1>=0)
		{
			if(board[loc_row+1][loc_column-1]<0)
			{
				checkMove(loc_row+1,loc_column-1,(loc_row+1)*8+loc_column-1);
			}
		}
	}
}
function getPossibleMovesForKnight()
{    												//represent possible loc where we can move
	var dest;
	var i=0;                                                    //represent count of possible moves
	var row,column;               
	while(true)
	{

        if(i==0)
		{
			row=loc_row+2;
			column=loc_column-1;
			dest=row*8+column;
		}
		if(i==1)
		{	
			row=loc_row+2;
			column=loc_column+1;
			dest=row*8+column;
		}
		if(i==2)
		{
			row=loc_row-2;
			column=loc_column+1;
			dest=row*8+column;
		}
		if(i==3)
		{	
			row=loc_row-2;
			column=loc_column-1;
			dest=row*8+column;
		}
		if(i==4)
		{	
			row=loc_row+1;
			column=loc_column-2;
			dest=row*8+column;
		}
		if(i==5)
		{
			row=loc_row+1;
			column=loc_column+2;
			dest=row*8+column;
		}
		if(i==6)
		{
			row=loc_row-1;
			column=loc_column-2;
			dest=row*8+column;
		}	
		if(i==7)
		{	
			row=loc_row-1;
			column=loc_column+2;
			dest=row*8+column;
		}
		if(i==8)
			break;
			
		if(!(row>7||column>7||row<0||column<0))   //check if destination loc is not out of board
				checkMove(row,column,dest);

		i++;
	}

}
function checkMove(row,column,dest)
{	
	
	if(board[row][column]<=0)
	{		
		var temp=board[row][column];
		board[loc_row][loc_column]=0;
		board[row][column]=piece;
		if(piece==6)
			kings_loc=row*8+column;


		if(!checkIfKingUnderAttack(kings_loc))
		{
			
			possible_moves[dest]=true;
		}
		if(piece==6)
			kings_loc=loc_row*8+loc_column;
		board[loc_row][loc_column]=piece;
		board[row][column]=temp;	
		
	}
	
}

function checkIfKingUnderAttack(kings_loc)    //check if move causes king under attack of other pieces
{
//	document.write(kings_loc);

	var king_row,king_column,row,column;
	
	king_row=Math.floor(kings_loc/8);
	king_column=kings_loc%8;
	
	row=king_row+1;
	column=king_column;
	

	while(row<=7)                                                         //check presence of rook or queen in same column 
	{
		if(board[row][column]==-4||board[row][column]==-5)
			return true;
		
		if(board[row][column]!=0)
			break;
		row++;
	}
	
	row=king_row;
	column=king_column+1;
	while(column<=7)                                                    //check presence of rook or queen in same row 
	{
		
		if(board[row][column]==-4||board[row][column]==-5)
			return true;
		if(board[row][column]!=0)
			break;
		column++;
	}
	
	
	column=king_column+1;
	row=king_row+1;	
	
	
	while(row<=7&&column<=7)															 //check presence of bishop or queen in same diagonal 
	{
		
		if(board[row][column]==-2||board[row][column]==-5)
			return true;
		
		if(board[row][column]!=0)
			break;
		
		column++;
		row++;
	}
	
	
	row=king_row-1;
	column=king_column;
	
	while(row>=0)
	{
		if(board[row][column]==-4||board[row][column]==-5)
			return true;
		
		if(board[row][column]!=0)
			break;
		row--;
	}
	
	
	row=king_row;
	column=king_column-1;
	while(column>=0)
	{
		if(board[row][column]==-4||board[row][column]==-5)
			return true;
		if(board[row][column]!=0)
			break;
		column--;
	}
	
	column=king_column-1;
	row=king_row-1;
	while(row>=0&&column>=0)
	{
		if(board[row][column]==-2||board[row][column]==-5)
			return true;
		
		if(board[row][column]!=0)
			break;
		
		column--;
		row--;
	}
	
	column=king_column-1;
	row=king_row+1;
	while(row<=7&&column>=0)
	{
		if(board[row][column]==-2||board[row][column]==-5)
			return true;
		
		if(board[row][column]!=0)
			break;
		
		column--;
		row++;
	}

	column=king_column+1;
	row=king_row-1;
	while(row>=0&&column<=7)
	{
		if(board[row][column]==-2||board[row][column]==-5)
			return true;
		
		if(board[row][column]!=0)
			break;
		
		column++;
		row--;
	}

	if(king_row+1<=7)
	{	
		if(king_column-1>=0)
		{
			if(board[king_row+1][king_column-1]==-1)             //check presence of pawn next to king
				return true;
		}
		if(king_column+1<=7)
		{
			if(board[king_row+1][king_column+1]==-1)             //check presence of pawn next to king
				return true;
		}
	}
	
	var i=0;

	while(true)
	{

		if(i==0)
		{
			row=king_row+2;
			column=king_column-1;
			dest=row*8+column;
		}
		if(i==1)
		{	
			row=king_row+2;
			column=king_column+1;
		}
		if(i==2)
		{
			row=king_row-2;
			column=king_column+1;
		}
		if(i==3)
		{	
			row=king_row-2;
			column=king_column-1;
		}
		if(i==4)
		{	
			row=king_row+1;
			column=king_column-2;
		}
		if(i==5)
		{
			row=king_row+1;
			column=king_column+2;
		}
		if(i==6)
		{
			row=king_row-1;
			column=king_column-2;
		}	
		if(i==7)
		{	
			row=king_row-1;
			column=king_column+2;
		}
		if(i==8)
			break;


		if(!(row>7||column>7||row<0||column<0))
		{	
			if(board[row][column]==-3)
				return true;
		}
		i++;
	}
	
	i=0;
	row=king_row;
	column=king_column;

	while(true)
	{

		if(i==0)
		{
			row=king_row+1;
		}
		if(i==1)
		{	
			row=king_row-1;
		}
		if(i==2)
		{
			column=king_column+1;
		}
		if(i==3)
		{	
			column=king_column-1;
		}
		if(i==4)
		{	
			row=king_row+1;
			column=king_column+1;
		}
		if(i==5)
		{
			row=king_row+1;
			column=king_column-1;
		}
		if(i==6)
		{
			row=king_row-1;
			column=king_column-1;
		}	
		if(i==7)
		{	
			row=king_row-1;
			column=king_column+1;
		}
		if(i==8)
			break;


		if(!(row>7||column>7||row<0||column<0))
		{	
			if(board[row][column]==-6)
				return true;
		}
		i++;
	}

	return false;

	
}