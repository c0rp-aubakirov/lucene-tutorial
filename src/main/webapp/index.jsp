<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html lang="ru">
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
          integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
            integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
            crossorigin="anonymous"></script>
    <script src="http://twitter.github.io/typeahead.js/releases/latest/typeahead.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/typeahead.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/typeahead.css" type="text/css"/>
</head>
<body class="container">
<div class="col-md-4 col-md-offset-3 form-group" id="remote">
    <label for="search" class="control-label"></label>
    <input type="text" class="form-control typeahead"
           autocomplete="off" spellcheck="false"
           value='<c:out value="${search}"/>'
           id="search" name="search" placeholder="Поиск событий">
</div>
</body>
</html>

