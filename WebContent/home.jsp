<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.*" import="project.Match"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/bootstrap.min.css">
<title>home</title>
</head>
<body>
<style>
 .jumbotron 
 {
      background-color: #f4511e;
      text-align: center;
      color: #fff;
  }
  </style>

<div class="jumbotron">
  <h1>ChessMaster</h1>      
</div>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">ChessMaster</a>
    </div>
    <ul class="nav navbar-nav">
    <li>
    	<form method="post" action="MatchSetterServlet">
    	<input type="submit" class="btn">
    	</form>    
      </li>
      <li><a href="#">Play With BOT</a></li>
      <li><a href="learngame.html">Start Learning</a></li>
      <li><a href="puzzles.html">Puzzles</a></li>
      <li><a href="http://www.thechesswebsite.com/famous-chess-games">Famous Games</a></li>
 	  <li><a href="Logout">Log Out</a></li>
    </ul>
  </div>
</nav>

<div class="container">
<h2>10 Online Matches That Are Being Played Online</h2>
  <p>Select The Match You Want To View</p>
  <div class="list-group">
  <a href="MatchViewerServlet?number=1" class="list-group-item list-group-item-success">firstmatch</a>
    <a href="#" class="list-group-item list-group-item-info">Second item</a>
    <a href="#" class="list-group-item list-group-item-warning">Third item</a>
    <a href="#" class="list-group-item list-group-item-danger">Fourth item</a>
    <a href="#" class="list-group-item list-group-item-success">Fifth item</a>
    <a href="#" class="list-group-item list-group-item-info">Sixth item</a>
    <a href="#" class="list-group-item list-group-item-warning">Seventh item</a>
    <a href="#" class="list-group-item list-group-item-danger">Eighth item</a>
    <a href="#" class="list-group-item list-group-item-success">Ninth item</a>
    <a href="#" class="list-group-item list-group-item-info">Tenth item</a>
  </div>
  </div>
</body>
</html>