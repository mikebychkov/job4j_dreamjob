<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.dream.model.Candidate" %>
<%@ page import="com.dream.store.Store" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
<%--    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"--%>
<%--            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>--%>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" crossorigin="anonymous"></script>

    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

    <script>
        function validate() {
            let fields = [];
            fields.push("name");
            for (let i = 0; i < fields.length; i++) {
                let val = $('#' + fields[i]).val();
                if (val === '') {
                    alert('Field: ' + fields[i] + ' is empty!');
                    return false;
                }
            }
            return true;
        }
        function loadCityList() {
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/dreamjob/city.list',
                dataType: 'json'
            }).done(function(data) {
                let opts = "";
                for (let k in data) {
                    opts += "<option value=\"" + k + "\">" + data[k] + "</option>";
                }
                $('#city').html(opts);
            }).fail(function(err) {
                alert("Error loading city list (" + err.status + ")");
            });
        }
        loadCityList();
    </script>

    <title>Работа мечты</title>
</head>
<body>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <c:if test="${candidate.id == 0}">Новый кандидат.</c:if>
                <c:if test="${candidate.id != 0}">Редактирование кандидата.</c:if>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/candidates.do?id=${candidate.id}" method="post">
                    <div class="form-group">
                        <label for="name">Имя</label>
                        <input type="text" class="form-control" name="name" id="name" value="${candidate.name}">
                    </div>
                    <div class="form-group">
                        <label for="city">Город</label>
                        <select name="city" id="city" class="form-control">
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="return validate()">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>