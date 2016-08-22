<%@ page import="com.workfront.internship.event_management.model.Category" %>
<%@ page import="com.workfront.internship.event_management.model.Event" %>
<%@ page import="com.workfront.internship.event_management.service.CategoryService" %>
<%@ page import="com.workfront.internship.event_management.service.CategoryServiceImpl" %>
<%@ page import="com.workfront.internship.event_management.service.EventService" %>
<%@ page import="com.workfront.internship.event_management.service.EventServiceImpl" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Inmelet
  Date: 8/8/2016
  Time: 10:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home | Event Management</title>
    <link rel="stylesheet" type="text/css" href="../../css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="../../css/reset.css">
    <link rel="stylesheet" type="text/css" href="../../css/main.css">
    <link rel="stylesheet" type="text/css" href="../../css/icon_font.css">
    <link rel="stylesheet" type="text/css" href="../../css/flexslider.css">

    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.structure.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.theme.min.css">

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">

    <script type="text/javascript" src="../../js/bootstrap.min.js"></script>
    <script type="text/javascript" src="../../js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../../js/jquery.validate.js"></script>
    <script type="text/javascript" src="../../js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="../../js/jquery.flexslider.js"></script>

    <script type="text/javascript" src="../../js/events.js"></script>

</head>
<body class="events_page">
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="/WEB-INF/view/header.jspew/header.jsp"/>
    <!-- End Main Header -->

    <div class="flexslider">
        <ul class="slides">
            <li>
                <img src="/img/slide2.jpg"/>
            </li>
            <li>
                <img src="/img/slide2.jpg"/>
            </li>
        </ul>
    </div>

    <!-- Content Section -->

    <section class="content_section">
        <div class="content">
            <aside class="left_sidebar">

                <div class="cat_menu">
                    <h6 class="title">Categories</h6>
                    <ul class="cat_list">
                        <%
                            List<Category> categoryList = (List<Category>) request.getAttribute("categories");
                            if (!categoryList.isEmpty()) {
                                for (Category category : categoryList) { %>
                        <li>
                            <div class="cat_item"
                                 onclick='getEventsByCategory("<%=category.getId()%>");'><%=category.getTitle() %>
                            </div>
                            <span class="num_events">
                                <% /*= eventService.getEventsByCategory(category.getId()).size()*/%>
                            </span>
                        </li>
                        <% }
                        } %>
                    </ul>
                </div>
            </aside>

            <div class="content_block">
                <div class="event_list clearfix" id="event_list">
                    <div class="main_title centered upper">
                        <h2><span class="line"><i class="icon-calendar"></i></span>
                            <span class="list_header">All Events</span>
                        </h2>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- End Content Section -->

    <!-- Footer -->
    <jsp:include page="/WEB-INF/view/footer.jspew/footer.jsp"/>
    <!-- End Footer -->
</div>
</body>
</html>
