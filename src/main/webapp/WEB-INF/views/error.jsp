<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hermine
  Date: 8/8/16
  Time: 6:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error | Event Management</title>

    <script src="<c:url value="/resources/js/lib/jquery-3.1.0.min.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.validate.js" />"></script>
    <script src="<c:url value="/resources/js/lib/bootstrap.min.js" />"></script>

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">

</head>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->
    <!-- Content Section -->

    <!-- Main Header -->
    <section class="content_section">
        <div class="container clearfix">
            <div class="content centered clearfix">
                <div class="main_desc ">
                    <div class="error-message"><%=request.getAttribute("message")%>
                    </div>
                    <% if(request.getAttribute("additionalErrorInfo") != null) { %>
                    <p><%=request.getAttribute("additionalErrorInfo")%></p>
                    <% } else { %>
                    <p>An error occurred while processing your request.</p>
                    <p>Please, try again later.</p>
                    <% } %>
                </div>
            </div>
        </div>
    </section>

<!-- Footer -->
<jsp:include page="footer.jsp"/>
<!-- End Footer -->

</div>
</body>
</html>
