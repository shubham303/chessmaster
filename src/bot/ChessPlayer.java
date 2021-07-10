package bot;

import java.util.ArrayList;

public class ChessPlayer 
{
	ChessBoard c_board;
	int val=0;
	Move final_move;
	Move possible_moves[];
	
	public ChessPlayer(ChessBoard c)
	{
		c_board=c;
	}
	
	private Move getBestMove() 
	{
		boolean maxplayer=true;
		ChessBoard state=new ChessBoard(c_board);
		getBestMove(state,5,-50000,+50000,maxplayer);
		return final_move;
	}
	private int getBestMove(ChessBoard state, int depth,int alpha, int beta, boolean maxplayer) 
	{
		int val;
		
		if(depth==0)
			return evaluateState(state);
		
		if(maxplayer==true)
		{	
			possible_moves=getPossibleMoves(state,true);
			
			for(int i=0;i<possible_moves.length;i++)
			{
				val=getBestMove(makeMove(state,possible_moves[i],true),depth-1,alpha,beta,!maxplayer);
				if(val>alpha)
				{
					final_move=possible_moves[i];
					alpha=val;
				}
					if(alpha>=beta)
					break;
			}
			return alpha;	
		}
		else
		{
			possible_moves=getPossibleMoves(state,false);
			
			for(int i=0;i<possible_moves.length;i++)
			{
				val=getBestMove(makeMove(state,possible_moves[i],false),depth-1,alpha,beta,!maxplayer);
				
				if(val>beta)
				{
					final_move=possible_moves[i];
					beta=val;
				}
				if(alpha>=beta)
					break;			
			}
			return beta;	
		}
	}
	
	private ChessBoard makeMove(ChessBoard state, Move move,boolean our_turn) 
	{
		// TODO Auto-generated method stub
		ChessBoard c=new ChessBoard(state);
		
		if(move.our_turn)
		{
			if(move.left_castling)
			{
				c.board[0][3]=0;
				c.board[0][5]=6;
				c.board[0][4]=4;
				c.board[0][7]=0;
				
				c.left_castling_allowed=true;
				c.right_castling_allowed=true;
				c.piece_locations[7]=4;
				c.piece_locations[3]=5;
				
			}
			else
			{	
				if(move.right_castling)
				{
					c.board[0][3]=0;
					c.board[0][1]=6;
					c.board[0][2]=4;
					c.board[0][0]=0;
					
					c.enemy_left_castling_allowed=true;
					c.enemy_right_castling_allowed=true;
					
					c.piece_locations[0]=2;
					c.piece_locations[3]=1;
				}
				else
				{
					int start_row=move.start/8;
					int start_column=move.start%8;
					int end_row=move.end/8;
					int end_column=move.end%8;
					
					short temp=c.board[start_row][start_column];
					c.board[start_row][start_column]=0;
					c.board[end_row][end_column]=temp;

					for(int i=0;i<c.piece_locations.length;i++)
					{
						if(c.piece_locations[i]==move.end)
						{
							c.piece_locations[i]=-1;
						}
						if(c.piece_locations[i]==move.start)
						{
							c.piece_locations[i]=move.end;
								break;
						}
						
					}
					if(c.board[end_row][end_column]==1)
						c.board[end_row][end_column]=5;
					
					
				}
			}
		}
		else
		{
			if(move.left_castling)
			{
				c.board[7][3]=0;
				c.board[7][5]=6;
				c.board[7][4]=4;
				c.board[7][7]=0;
				
				c.enemy_piece_locations[7]=60;
				c.enemy_piece_locations[3]=61;
				
			}
			else
			{	
				if(move.right_castling)
				{
					c.board[7][3]=0;
					c.board[7][1]=6;
					c.board[7][2]=4;
					c.board[7][0]=0;
					
					c.enemy_piece_locations[0]=58;
					c.enemy_piece_locations[3]=57;
				}
				else
				{
					int start_row=move.start/8;
					int start_column=move.start%8;
					int end_row=move.end/8;
					int end_column=move.end%8;
					
					short temp=c.board[start_row][start_column];
					c.board[start_row][start_column]=0;
					c.board[end_row][end_column]=temp;
					
					for(int i=0;i<c.enemy_piece_locations.length;i++)
					{
						if(c.enemy_piece_locations[i]==move.end)
						{
							c.enemy_piece_locations[i]=-1;
						}
						if(c.enemy_piece_locations[i]==move.start)
						{
							c.enemy_piece_locations[i]=move.end;
								break;
						}	
					}
					if(c.board[end_row][end_column]==-1)
						c.board[end_row][end_column]=-5;
				}
				
			}
		}
		return c;
		
	}
	private int evaluateState(ChessBoard c) 
	{
		int king=0,queen=0,pawn=0,knight=0,bishop=0,rook=0;
		int row,column;
		int multiplier;
		int value;
		
		//checkIfKingUnderAttack(c.piece_locations[3])
		for(int i=0;i<64;i++)	
		{
			row=i/8;
			column=i%8;
			if(row==0||row==7||column==0||column==7)
				multiplier=4;
			if(row==1||row==6||column==1||column==6)
				multiplier=3;
			if(row==2||row==5||column==2||column==5)
				multiplier=2;
			if(row==3||row==4||column==3||column==4)
				multiplier=1;
			
			
			if(c.board[row][column]==0)
				continue;
			
			if(c.board[row][column]==1)
			{
				pawn=pawn+pawn*multiplier;													//here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==-1)
			{
				pawn--;													//here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==4)
			{
				rook++;                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==3)
			{
				knight++;                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==2)
			{
				bishop++;                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==6)
			{
				king++;                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==5)
			{
				queen++;                      //here true means it is bots turn to make move
				continue;
			}
			
			if(c.board[row][column]==-4)
			{
				rook--;                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==-3)
			{
				knight--;                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==-2)
			{
				bishop--;                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==-6)
			{
				king--;                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==-5)
			{
				queen--;                      //here true means it is bots turn to make move
				continue;
			}
			
				
		}
	}
	Move[] getPossibleMoves(ChessBoard c,boolean our_turn)
	{
		int row,column;
		int side;
		
		ArrayList<Move> list=new ArrayList<Move>();
		
		if(our_turn)
		{
			side=1;
		}
		else
			side=-1;
		
		for(int i=0;i<64;i++)
		{
			row=i/8;
			column=i%8;
			
			if(c.board[row][column]==0)
				continue;
			
			if(c.board[row][column]==5*side)
			{
				getPossibleMovesForQueen(c,list, our_turn,(short)i);                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==4*side)
			{
				getPossibleMovesForRook(c,list, our_turn,(short)i);                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==3*side)
			{
				getPossibleMovesForKnight(c,list, our_turn,(short)i);                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==2*side)
			{
				getPossibleMovesForBishop(c,list, our_turn,(short) i);                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==6*side)
			{
				getPossibleMovesForKing(c,list, our_turn,(short)i);                      //here true means it is bots turn to make move
				continue;
			}
			if(c.board[row][column]==1*side)
			{
				getPossibleMovesForPawn(c,list, our_turn,(short)i);                      //here true means it is bots turn to make move
				continue;
			}
			
		}
		return((Move[])list.toArray());
		
	}
	
	private void getPossibleMovesForKing(ChessBoard c, ArrayList<Move> list,boolean our_turn,short loc) 
	{
		short board[][]=c.getBoard();
		int direction=1;                                 // specify to which direction pawn will move 
		
		int loc_row=loc/8;
		int loc_column=loc%8;
		
		int row=loc_row;
		int column=loc_column;;
		int i=0;
		
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
			{
				if(checkMove(c,(short)row,(short)column,row*8+column,loc,our_turn))
					list.add(new Move(loc,(short)(row*8+column)));
			}
			
		}
		checkCastling(c,list,our_turn);	
	}
	
	private void checkCastling(ChessBoard c, ArrayList<Move> list, boolean our_turn) 
	{
			
		if(c.left_castling_allowed)
		{				
			if(castlingAllowed(c,our_turn,1))
			{
				list.add(new Move(true,our_turn));	
			}
		}
		if(c.right_castling_allowed)
		{
			if(castlingAllowed(c,our_turn,-1))
			{
				list.add(new Move(false,our_turn));
			}
		}
								
	}
	
	private boolean castlingAllowed(ChessBoard c, boolean our_turn,int side) 
	{
	
		int kings_loc;
		int row=0;
		int column;
		short[][] board=c.getBoard();
		
		if(our_turn)
		{
			kings_loc=3;
			row=0;
			column=3+side;
		}
		else
		{
			kings_loc=59;
			row=7;
			column=3+side;
		}
		
		while(column>=1&&column<=6)
		{	
			if(board[row][column]!=0)
				return false;			
			column+=side;
		}
		if(checkIfKingUnderAttack(board,(short)kings_loc,our_turn))
			return false;
		
		if(checkIfKingUnderAttack(board,(short)(kings_loc+side),our_turn))
			return false;
		
		if(checkIfKingUnderAttack(board,(short)(kings_loc+side+side),our_turn))
			return false;

		return true;
	}
	private void getPossibleMovesForPawn(ChessBoard c, ArrayList<Move> list,boolean our_turn,short loc) 
	{
		short board[][]=c.getBoard();
		int direction=1;                                 // specify to which direction pawn will move 
		
		
		int loc_row=loc/8; 
		int loc_column=loc%8;
		int row;
		int column;
		
		for(int i=1;i<8;i++)
		{
			row=loc_row;
			column=loc_column;
			
			if(row==1&&our_turn||row==6&&!our_turn)
			{
				row=row+2*direction;
				if(board[row][column]==0 && board[row-1*direction][column]==0)
				{	
					if(checkMove(c,(short)row,(short)column,row*8+column,loc,our_turn))
						list.add(new Move(loc,(short)(row*8+column)));
				}
				
			}
			row=loc_row+direction;
			if(row<=7 && row>=0)
			{
				if(board[row][column]==0)
				{	
					if(checkMove(c,(short)(row),(short)column,row*8+column,loc,our_turn))
						list.add(new Move(loc,(short)(loc+8*direction)));
				}
			}	
			
			column=loc_column+1;
			if(row<=7&&row>=0)
			{
				if(column<=7)
				{
					if(board[row][column]*direction<0)
					{
						if(checkMove(c,(short)(row),(short)column,row*8+column,loc,our_turn))
							list.add(new Move(loc,(short)(row*8+column)));
					}
				}
				column=loc_column-1;
				
				if(column>=0)
				{
					if(board[row][column]*direction<0)
					{
						if(checkMove(c,(short)(row),(short)column,row*8+column,loc,our_turn))
							list.add(new Move(loc,(short)(row*8+column)));

					}
				}
			}
			loc+=i;
			loc_row=loc/8;
			loc_column=loc%8;
		}
	}
	
	private void getPossibleMovesForKnight(ChessBoard c, ArrayList<Move> list,boolean our_turn,short loc) 
	{
		
		int loc_row=loc/8;
		int loc_column=loc%8;
		int dest=0;
		int row=0,column=0;
		
		for(int j=0;j<2;j++)
		{
			int i=0;
			
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
					if(checkMove(c,(short)row,(short)column,row*8+column,loc,our_turn))
						list.add(new Move(loc,(short) (row*8+column)));
				}
				i++;
			}
			if(our_turn)
				 loc=c.getPiece_locations()[6];
			else
				loc=c.getEnemy_piece_locations()[6];
			
			loc_row=loc/8;
			loc_column=loc%8;
		}
		
	}
	private void getPossibleMovesForBishop(ChessBoard c, ArrayList<Move> list,boolean our_turn,short loc) 
	{
		
		short loc_row=(short) (loc/8);
		short loc_column=(short) (loc%8);
		short[][] board=c.getBoard();
		short row=loc_row;
		short column=loc_column;
		for(int i=0;i<2;i++)
		{
			row=(short) (loc_row+1);
			column=(short) (loc_column+1);
			while(row<=7&&column<=7)                                              //move towards left corner
			{
				if(checkMove(c,row,column,row*8+column,loc,our_turn))
					list.add(new Move(loc,(short) (row*8+column)));
				
				if(board[row][column]!=0)
					break;
	
				row++;
				column++;
			}
			
			row=(short) (loc_row+1);
			column=(short) (loc_column-1);
			
			while(row<=7&&column>=0)                                               //move towards right corner
			{
				if(checkMove(c,row,column,row*8+column,loc,our_turn))
					list.add(new Move(loc,(short) (row*8+column)));
				
				if(board[row][column]!=0)
					break;
	
				row++;
				column--;
			}
	
			row=(short) (loc_row-1);
			column=(short) (loc_column+1);
			while(row>=0&&column<=7)                                                    //move towards lower left corner 
			{
				if(checkMove(c,row,column,row*8+column,loc,our_turn))
					list.add(new Move(loc,(short) (row*8+column)));
				
				if(board[row][column]!=0)
					break;
	
				row--;
				column++;
			}
			
			row=(short) (loc_row-1);
			column=(short) (loc_column-1);
			
			while(row>=0&&column>=0)                                                    //move towards lower right corner 
			{
				if(checkMove(c,row,column,row*8+column,loc,our_turn))
					list.add(new Move(loc,(short) (row*8+column)));
				
				if(board[row][column]!=0)
					break;
				row--;
				column--;
			}
			if(our_turn)
				loc=c.getPiece_locations()[5];
			else
				loc=c.getEnemy_piece_locations()[5];
			
			loc_row=(short) (loc/8);
			loc_column=(short) (loc%8);
			board=c.getBoard();
			row=loc_row;
		    column=loc_column;
		}
	}
	private void getPossibleMovesForRook(ChessBoard c, ArrayList<Move> list,boolean our_turn,short loc) 
	{
		
		
		short loc_row=(short) (loc/8);
		short loc_column=(short) (loc%8);
		short[][] board=c.getBoard();
		short row=loc_row;
		short column=loc_column;
		
		for(int i=0;i<2;i++)
		{
			while(row<=7)
			{
				if(checkMove(c,row,column,row*8+column,loc,our_turn))
					list.add(new Move(loc,(short) (row*8+column)));
			
				if(board[row][column]!=0)
					break;

				row++;
			}
			row=(short) (loc_row-1);
			
			while(row>=0)
			{
				if(checkMove(c,row,column,row*8+column,loc,our_turn))
					list.add(new Move(loc,(short) (row*8+column)));
				
				if(board[row][column]!=0)
					break;
				row--;
			}
			row=loc_row;
			column=(short) (loc_column+1);
			while(column<=7)
			{
				if(checkMove(c,row,column,row*8+column,loc,our_turn))
					list.add(new Move(loc,(short) (row*8+column)));
				
				if(board[row][column]!=0)
					break;
				column++;
			}
			column=(short) (loc_column-1);
			while(column>=0)
			{
				if(checkMove(c,row,column,row*8+column,loc,our_turn))
					list.add(new Move(loc,(short) (row*8+column)));
				
				if(board[row][column]!=0)
					break;
				column--;
			}
			
			if(our_turn)
				loc=c.getPiece_locations()[7];
			else
				loc=c.getEnemy_piece_locations()[7];
			
			loc_row=(short) (loc/8);
			loc_column=(short) (loc%8);
			board=c.getBoard();
			row=loc_row;
		    column=loc_column;
		}		
		
	}
	
	private void getPossibleMovesForQueen(ChessBoard c, ArrayList<Move> list,boolean our_turn,short loc) 
	{
		
		short loc_row=(short) (loc/8);
		short loc_column=(short) (loc%8);
		short[][] board=c.getBoard();
		short row=loc_row;
		short column=loc_column;
		
		while(row<=7)
		{
			if(checkMove(c,row,column,row*8+column,loc,our_turn))
				list.add(new Move(loc,(short) (row*8+column)));
			
			if(board[row][column]!=0)
				break;

			row++;
		}
		row=(short) (loc_row-1);
		
		while(row>=0)
		{
			if(checkMove(c,row,column,row*8+column,loc,our_turn))
				list.add(new Move(loc,(short) (row*8+column)));
			
			if(board[row][column]!=0)
				break;
			row--;
		}
		row=loc_row;
		column=(short) (loc_column+1);
		while(column<=7)
		{
			if(checkMove(c,row,column,row*8+column,loc,our_turn))
				list.add(new Move(loc,(short) (row*8+column)));
			
			if(board[row][column]!=0)
				break;
			column++;
		}
		column=(short) (loc_column-1);
		while(column>=0)
		{
			if(checkMove(c,row,column,row*8+column,loc,our_turn))
				list.add(new Move(loc,(short) (row*8+column)));
			
			if(board[row][column]!=0)
				break;
			column--;
		}
		
		row=(short) (loc_row+1);
		column=(short) (loc_column+1);
		while(row<=7&&column<=7)                                              //move towards left corner
		{
			if(checkMove(c,row,column,row*8+column,loc,our_turn))
				list.add(new Move(loc,(short) (row*8+column)));
			
			if(board[row][column]!=0)
				break;

			row++;
			column++;
		}
		
		row=(short) (loc_row+1);
		column=(short) (loc_column-1);
		
		while(row<=7&&column>=0)                                               //move towards right corner
		{
			if(checkMove(c,row,column,row*8+column,loc,our_turn))
				list.add(new Move(loc,(short) (row*8+column)));
			
			if(board[row][column]!=0)
				break;

			row++;
			column--;
		}

		row=(short) (loc_row-1);
		column=(short) (loc_column+1);
		while(row>=0&&column<=7)                                                    //move towards lower left corner 
		{
			if(checkMove(c,row,column,row*8+column,loc,our_turn))
				list.add(new Move(loc,(short) (row*8+column)));
			
			if(board[row][column]!=0)
				break;

			row--;
			column++;
		}
		
		row=(short) (loc_row-1);
		column=(short) (loc_column-1);
		
		while(row>=0&&column>=0)                                                    //move towards lower right corner 
		{
			if(checkMove(c,row,column,row*8+column,loc,our_turn))
				list.add(new Move(loc,(short) (row*8+column)));
			
			if(board[row][column]!=0)
				break;
			row--;
			column--;
		}
	}
	private boolean checkMove(ChessBoard c,short row, short column, int dest,short loc,boolean our_turn) 
	{
		short board[][]=c.getBoard();
		int enemy_kings_loc;
		int direction;
		//short enemy_kings_location=c.getGs().getEnemy_piece_locations()[59];
		
		boolean is_legal=false;
		short p;
		
		short l_row=(short) (loc/8);
		short l_column=(short) (loc%8);
		short kings_loc;
		
		p=board[l_row][l_column];
		
		if(our_turn)
		{
			direction=1;
			kings_loc=c.getPiece_locations()[3];
		}		
		else
		{
			direction=-1;
			kings_loc=c.getEnemy_piece_locations()[3];
		}
		if(board[row][column]*direction>=0)
		{	
			short temp=board[row][column];
			board[l_row][l_column]=0;
			board[row][column]=p;
			if(p==6||p==-6)
				kings_loc=(short)dest;
			
			is_legal=(!checkIfKingUnderAttack(board,kings_loc,our_turn));	
		}
			
		
		
		return is_legal;
	}
	private boolean checkIfKingUnderAttack(short[][] board, short kings_loc,boolean our_turn) 
	{
		
		int  king_row,king_column,row,column;
		int king=6,queen=5,rook=4,pawn=1,knight=3,bishop=2;
		if(our_turn)
		{
			 king=-6;queen=-5;rook=-4;pawn=-1;knight=-3;bishop=-2;
		}
		
		king_row=(short) (kings_loc/8);
		king_column=(short) (kings_loc%8);
		
		row=king_row+1;
		column=king_column;
		
		while(row<=7)                                                         //check presence of rook or queen in same column 
		{
			if(board[row][column]==rook||board[row][column]==queen)
				return true;
			
			if(board[row][column]!=0)
				break;
			row++;
		}
		
		row=king_row;
		column=king_column+1;
		
		while(column<=7)                                                    //check presence of rook or queen in same row 
		{
			if(board[row][column]==rook||board[row][column]==queen)
				return true;
			if(board[row][column]!=0)
				break;
			column++;
		}
		
		
		column=king_column+1;
		row=king_row+1;	
		
		
		while(row<=7&&column<=7)															 //check presence of bishop or queen in same diagonal 
		{
			
			if(board[row][column]==bishop||board[row][column]==queen)
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
			if(board[row][column]==rook||board[row][column]==queen)
				return true;
			
			if(board[row][column]!=0)
				break;
			row--;
		}
		
		
		row=king_row;
		column=king_column-1;
		while(column>=0)
		{
			if(board[row][column]==rook||board[row][column]==queen)
				return true;
			if(board[row][column]!=0)
				break;
			column--;
		}
		
		column=king_column-1;
		row=king_row-1;
		while(row>=0&&column>=0)
		{
			if(board[row][column]==bishop||board[row][column]==queen)
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
			if(board[row][column]==bishop||board[row][column]==queen)
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
			if(board[row][column]==bishop||board[row][column]==queen)
				return true;
			
			if(board[row][column]!=0)
				break;
			
			column++;
			row--;
		}

		if(our_turn)
		{
			if(king_row+1<=7)
			{	
				if(king_column-1>=0)
				{
					if(board[king_row+1][king_column-1]==pawn)             //check presence of pawn next to king
						return true;
				}
				if(king_column+1<=7)
				{
					if(board[king_row+1][king_column+1]==pawn)             //check presence of pawn next to king
						return true;
				}
			}
		}
		else
		{
			if(king_row-1>=0)
			{	
				if(king_column-1>=0)
				{
					if(board[king_row-1][king_column-1]==pawn)             //check presence of pawn next to king
						return true;
				}
				if(king_column+1<=7)
				{
					if(board[king_row-1][king_column+1]==pawn)             //check presence of pawn next to king
						return true;
				}
			}
		}
		
		int i=0;

		while(true)
		{

			if(i==0)
			{
				row=king_row+2;
				column=king_column-1;
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
				if(board[row][column]==knight)
					return true;
			}
			i++;
		}
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
				if(board[row][column]==king)
					return true;
			}
			i++;
		}
		
		
		

		return false;

	}
}

