<%@ page language="java" pageEncoding="UTF-8" session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Upload</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">

    <h2>Condidate photo</h2>

    <form action="<c:url value='/photo.upload?id=${candidate.id}'/>" method="post" enctype="multipart/form-data">

        <img src="<c:url value='/photo.download?name=${candidate.photo}'/>"/><br/>

        <a href="<c:url value='/photo.download?name=${candidate.photo}'/>">Download</a><br/>

        <div class="checkbox">
            <input type="file" name="file">
        </div>

        <button type="submit" class="btn btn-default">Submit</button>

    </form>

</div>

</body>
</html>
