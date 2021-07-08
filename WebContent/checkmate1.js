var enemy_kings_loc;
var enemy_king_moved=false;
var enemy_left_rook_moved=false;
var enemy_right_rook_moved=false;


if(side=="white")
	 enemy_kings_loc=59;
else
	 enemy_kings_loc=60;

function isCheckMate()                                       //check if oppositon king mate
{
	for(var i=0;i<8;i++)
	{
		for(var j=0;j<8;j++)
		{
			if(board[i][j]==-6)
			{
				
				if(canKingMove(i*8+j))
				{
					return false;			
				}
					
			}
			if(board[i][j]==-2)
			{
				
				if(canBishopMove(i*8+j))
				{
					
					return false;			
				}
			}
			if(board[i][j]==-4)
			{
				
				if(canRookMove(i*8+j))
				{
					return false;			
				}
			}
			if(board[i][j]==-5)
			{
				
				if(canQueenMove(i*8+j))
				{
					
					return false;			
				}
			}
			if(board[i][j]==-1)
			{
				
				if(canPawnMove(i*8+j))
				{
					
					return false;			
				}
			}
			if(board[i][j]==-3)
			{
				
				if(canKnightMove(i*8+j))
				{
					return false;			
				}
			}
		}
	}
	return true;
}

function canBishopMove(loc)
{
	var loc_row=Math.floor(loc/8);
	var loc_column=loc%8;

	var row=loc_row+1,column=loc_column+1;
	
	while(row<=7&&column<=7)                                              //move towards left corner
	{
		if(isLegalMove(row,column,row*8+column,loc))
			return true;
		if(board[row][column]!=0)
			break;

		row++;
		column++;
	}
	
	row=loc_row+1;
	column=loc_column-1;
	
	while(row<=7&&column>=0)                                               //move towards right corner
	{
		if(isLegalMove(row,column,row*8+column,loc))
			return true;

		if(board[row][column]!=0)
			break;

		row++;
		column--;
	}

	row=loc_row-1;
	column=loc_column+1;
	while(row>=0&&column<=7)                                                    //move towards lower left corner 
	{
		if(isLegalMove(row,column,row*8+column,loc))
			return true;
		if(board[row][column]!=0)
			break;

		row--;
		column++;
	}
	
	row=loc_row-1;
	column=loc_column-1;
	while(row>=0&&column>=0)                                                    //move towards lower right corner 
	{
		if(isLegalMove(row,column,row*8+column,loc))
			return true;
		if(board[row][column]!=0)
			break;
		row--;
		column--;
	}
	return false;
}
function canRookMove(loc)
{
	var loc_row=Math.floor(loc/8);
	var loc_column=loc%8;


	var row=loc_row+1,column=loc_column;

	while(row<=7)
	{
		if(isLegalMove(row,column,row*8+column,loc))
			return true;
		
		if(board[row][column]!=0)
			break;

		row++;
	}		
	row=loc_row-1;
	
	while(row>=0)
	{
		if(isLegalMove(row,column,row*8+column,loc))
			return true;
		
		if(board[row][column]!=0)
			break;
		row--;
	}
	
	row=loc_row;
	column=loc_column+1;
	
	while(column<=7)
	{
		if(isLegalMove(row,column,row*8+column,loc))
			return true;
		if(board[row][column]!=0)
			break;
		column++;
	}
	column=loc_column-1;
	while(column>=0)
	{
		if(isLegalMove(row,column,row*8+column,loc))
			return true;

		if(board[row][column]!=0)
			break;
		column--;
	}
	return false;

}
function canQueenMove(loc)
{
	
	if(canRookMove(loc))
		return true;
	
	return(canBishopMove(loc));
}
function canKnightMove(loc)
{
	
	var dest;
	var i=0;                      //represent count of possible moves
	
	var row,column;   
	
	var loc_row=Math.floor(loc/8);
	var loc_column=loc%8;
	

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
		{
			if(isLegalMove(row,column,dest,loc))
				return true;
		}

		i++;
	}
	return false;
}
function canPawnMove(loc)
{
	row=Math.floor(loc/8);
	column=loc%8;

	if(row==6)
	{
		if(board[row-2][column]==0 && board[row-1][column]==0)
		{	
			if(isLegalMove(row-2,column,loc-16,loc))
				return true;
		}
	}
	if(row-1>=0)
	{
		if(board[row-1][column]==0)
		{	
			if(isLegalMove(row-1,column,loc-8,loc))
				return true;
		}
	}	
	if(row-1>=0)
	{
		if(column+1<=7)
		{
			if(board[row-1][column+1]>0)
			{
				if(isLegalMove(row-1,column+1,(row-1)*8+column+1,loc))
					return true;
			}
		}
		if(column-1>=0)
		{
			if(board[row-1][column-1]>0)
			{
				if(isLegalMove(row-1,column-1,(row-1)*8+column-1,loc))
					return true;
			}
		}
	}
	return false;
}
function canKingMove(loc)
{	
	var loc_row=Math.floor(loc/8);
	var loc_column=loc%8;
	var row=loc_row,column=loc_column;
	
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
		
		
		if(!(row>7||column>7||row<0||column<0))
		{	
			if(isLegalMove(row,column,row*8+column,loc))
			{	
				
				return true;
			}
		}	
		i++;
	}	
	
	if(checkEnemyCastling())
		return true;

	return false;
}
function checkEnemyCastling()
{
	if(!enemy_king_moved)
	{		
		if(!enemy_left_rook_moved)
		{
			if(castlingAllowed1(1))
			{
				return true;	
			}
		}
		if(!enemy_right_rook_moved)
		{
			
			if(castlingAllowed1(-1))
			{
				return true;
			}
		}
	}

	return false;	
}
function castlingAllowed1(side)                                         //side indicate to which side we want to do castling    1 means left and -1 means right
{
	
	var row=Math.floor(enemy_kings_loc/8);
	
	var column=(enemy_kings_loc%8)+side;
	
	
	while(column>=1&&column<=6)
	{
		if(board[row][column]!=0)
			return false;
		
		column+=side;
		
		
	}
	
	
	if(checkIfEnemyKingUnderAttack(enemy_kings_loc+side))
		return false;
	
	if(checkIfEnemyKingUnderAttack(enemy_kings_loc+side+side))
		return false;

	return true;
	
}

function isLegalMove(row,column,dest,loc)
{	

	var is_legal=false;
	var p;
	
	l_row=Math.floor(loc/8);
	l_column=loc%8;

	p=board[l_row][l_column];

	if(board[row][column]>=0)
	{	
		
		var temp=board[row][column];
		board[l_row][l_column]=0;
		board[row][column]=p;
		if(p==-6)
			enemy_kings_loc=dest;
		is_legal= (!checkIfEnemyKingUnderAttack(enemy_kings_loc));
	
		if(p==-6)
			enemy_kings_loc=loc;

		board[l_row][l_column]=p;
		board[row][column]=temp;	
		
	}
	return is_legal;
	
}

function checkIfEnemyKingUnderAttack(enemy_kings_loc)         //check if there is any check on opposition king;
{
	var king_row,king_column,row,column;
	
	king_row=Math.floor(enemy_kings_loc/8);
	king_column=enemy_kings_loc%8;
	
	row=king_row+1;
	column=king_column;

	while(row<=7)                                                         //check presence of rook or queen in same column 
	{
		if(board[row][column]==4||board[row][column]==5)
			return true;
		if(board[row][column]!=0)
			break;
		row++;
	}
	
	row=king_row;
	column=king_column+1;
	while(column<=7)                                                    //check presence of rook or queen in same row 
	{
		
		if(board[row][column]==4||board[row][column]==5)
			return true;		 
		
		if(board[row][column]!=0)
			break;
		column++;
	}
	
	
	column=king_column+1;
	row=king_row+1;	
	
	
	while(row<=7&&column<=7)															 //check presence of bishop or queen in same diagonal 
	{
		
		if(board[row][column]==2||board[row][column]==5)
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
		if(board[row][column]==4||board[row][column]==5)
					return true;
			 
		
		
		if(board[row][column]!=0)
			break;
		row--;
	}
	
	
	row=king_row;
	column=king_column-1;
	while(column>=0)
	{
		if(board[row][column]==4||board[row][column]==5)
			return true;
			 
		
		if(board[row][column]!=0)
			break;
		column--;
	}
	
	column=king_column-1;
	row=king_row-1;
	while(row>=0&&column>=0)
	{
		if(board[row][column]==2||board[row][column]==5)
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
		if(board[row][column]==2||board[row][column]==5)
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
		if(board[row][column]==2||board[row][column]==5)
			return true;
		
		if(board[row][column]!=0)
			break;
		
		column++;
		row--;
	}

	if(king_row-1>=0)
	{	
		if(king_column-1>=0)
		{
			if(board[king_row-1][king_column-1]==1)             //check presence of pawn next to king
				return true;
		}
		if(king_column+1<=7)
		{
			if(board[king_row-1][king_column+1]==1)             //check presence of pawn next to king
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
			if(board[row][column]==3)
				return true;
		}
		i++;
	}

	return false;	
}
