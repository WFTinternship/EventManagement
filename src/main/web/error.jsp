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

    <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="css/reset.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="css/icon_font.css">

    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.structure.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.theme.min.css">

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">

    <script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="js/homePage.js"></script>

</head>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="/header.jsp"/>
    <!-- End Main Header -->
    <!-- Content Section -->

    <!-- Main Header -->
    <section class="content_section">
        <div class="container clearfix">
            <div class="content centered">
                <div class="main_desc ">
                    <p><b>Ooopps.!</b> The Page you were looking for doesn't exist</p>
                </div>
                <div class="page404">
                    <span>404<span class="face404"></span></span>
                </div>
                <div class="centered">
                    <a href="/index.jsp" target="_self" class="btn_a">
                        <span><i class="footer_icon icon-home"></i> <span>Back To Home Page</span></span>
                    </a>
                </div>

            </div>
        </div>
    </section>
</div>

<!-- Footer -->
<jsp:include page="/footer.jsp"/>
<!-- End Footer -->
</body>
</html>
