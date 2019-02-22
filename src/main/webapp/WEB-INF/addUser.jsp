<%--
  Created by IntelliJ IDEA.
  User: Natali
  Date: 20.02.2019
  Time: 16:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri ="http://java.sun.com/jstl/core" prefix="c" %>
<%@taglib uri ="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Add new user</title>
</head>
<body>
<form method="post" action="controller" name="addUser">
    <%Username:" <input type = "text" name ="uname" value="c:out value = "${user.name}"/> readonly = "readonly"/>
</form>
</body>
</html>
