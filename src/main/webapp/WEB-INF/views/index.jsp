<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.event_management.model.Event" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.event_management.model.User" %>
<%@ page import="com.workfront.internship.event_management.common.DateParser" %>
<%--
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

    <script src="<c:url value="/resources/js/lib/jquery-3.1.0.min.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.validate.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.flexslider.js" />"></script>
    <script src="<c:url value="/resources/js/lib/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/index.js" />"></script>

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/flexslider.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">

</head>
<%
    User sessionUser = (User) session.getAttribute("user");
    int userId = 0;
    if(sessionUser != null) {
        userId = sessionUser.getId();
    }
%>
<body class="home_page">
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->

    <div class="flexslider">
        <ul class="slides">
            <li>
                <img src="<c:url value="/resources/img/slide2.jpg" />"/>
            </li>
            <li>
                <img src="<c:url value="/resources/img/slide2.jpg" />"/>
            </li>
        </ul>
    </div>

    <!-- Content Section -->

    <section class="content_section">
        <div class="content">
            <div class="content_block">
                <div class="event_list clearfix" id="event_list">
                    <div class="main_title centered upper">
                        <h2><span class="line"><i class="icon-calendar"></i></span>Upcoming Events</h2>
                    </div>
                    <div class="list">
                        <%
                            List<Event> eventList = (List<Event>) request.getAttribute("events");
                            if (!eventList.isEmpty()) {
                                for (Event event : eventList) {
                        %>
                        <div class="list_item">
                            <div class="list_content">
                                <h6 class="title">
                                    <a href="#"><%=event.getTitle() %>
                                    </a>
                                </h6>
                                <% if(userId == event.getOrganizer().getId()) { %>
                                <a id="edit-event" class="change-event-btn" href="/events/<%=event.getId()%>/edit">
                                    <i class="icon-pencil"></i>
                                    <span>Edit</span>
                                </a>
                                <button id="delete-event" class="change-event-btn" onclick="deleteEvent(<%=event.getId()%>)">
                                    <i class="icon-delete"></i>
                                    <span>Delete</span>
                                </button>
                                <%} %>
                                <span class="meta">
                                       <span class="meta_part ">
                                           <a href="#">
                                               <i class="ev_icon icon-clock"></i>
                                               <span><%=DateParser.parseDateToString(event.getStartDate()) %></span>
                                           </a>
                                       </span>
                                   <span class="meta_part">
                                       <a href="#">
                                           <i class="ev_icon icon-map-marker"></i>
                                           <span>
                                               <%=event.getLocation()%>
                                           </span>
                                       </a>
                                   </span>
                                       <span class="meta_part">
                                           <i class="ev_icon icon-folder"></i>
                                           <span>
                                               <a href="#">
                                                   <%= event.getCategory().getTitle()%>
                                               </a>
                                           </span>
                                       </span>
                                       <span class="meta_part">
                                           <a href="#">
                                               <i class="ev_icon icon-user"></i>
                                               <span><%=event.getOrganizer().getFirstName() %> <%=event.getOrganizer().getLastName() %></span>
                                           </a>
                                       </span>
                                   </span>
                                <p class="desc"><%=event.getShortDescription()%>
                                </p>

                                <a class="btn" href="/events/<%=event.getId()%>"><span>Details</span></a>
                            </div>
                        </div>
                        <% }
                        } %>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- End Content Section -->

    <!-- Footer -->
    <jsp:include page="footer.jsp"/>
    <!-- End Footer -->
    <%--<% if(response.getAttribute("redirect_url") != null) { %>--%>
        <%--<script type="text/javascript" >alert("kuku");</script>--%>
    <%--<% } %>--%>
</div>
</body>
</html>
