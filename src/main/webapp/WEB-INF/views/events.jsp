<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.event_management.model.Category" %>
<%@ page import="com.workfront.internship.event_management.model.Event" %>
<%@ page import="com.workfront.internship.event_management.service.CategoryService" %>
<%@ page import="com.workfront.internship.event_management.service.CategoryServiceImpl" %>
<%@ page import="com.workfront.internship.event_management.service.EventService" %>
<%@ page import="com.workfront.internship.event_management.service.EventServiceImpl" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.event_management.common.DateParser" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection" %>
<%@ page import="com.workfront.internship.event_management.model.User" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyString" %><%--
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
    <script src="<c:url value="/resources/js/lib/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/lib/bootstrap-notify.js" />"></script>
    <script src="<c:url value="/resources/js/my-events.js" />"></script>
    <script src="<c:url value="/resources/js/header.js" />"></script>


    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/my-account.css" />" rel="stylesheet">

</head>
<%
    User sessionUser = (User) session.getAttribute("user");
    int userId = 0;
    if(sessionUser != null) {
        userId = sessionUser.getId();
    }
    List<Category> categoryList = (List<Category>) request.getAttribute("categories");
    List<Event> eventList = (List<Event>) request.getAttribute("events");


    String listHeader = (String) request.getAttribute("listHeader");
    String currentCategoryId = (String) request.getAttribute("categoryId");

%>
<body class="events_page">
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->

    <!-- Content Section -->
    <section class="content_section clearfix">
        <div class="content">
            <%
                if (!isEmptyCollection(categoryList)) { %>
            <aside class="left_sidebar">

                <div class="cat_menu">
                    <h6 class="title">Categories</h6>
                    <ul class="cat_list">
                        <%
                            for (Category category : categoryList) {
                            if (!isEmptyString(currentCategoryId) && category.getId() == Integer.parseInt(currentCategoryId)) {
                                listHeader = category.getTitle();
                            }
                        %>
                        <li>
                            <a class="cat_item <%= (!isEmptyString(currentCategoryId) && category.getId() == Integer.parseInt(currentCategoryId ) )? "highlight": ""%>"
                                href="/events?categoryId=<%=category.getId()%>"><%=category.getTitle() %>
                            </a>
                            <span class="num_events">
                                <% /*= eventService.getEventsByCategory(category.getId()).size()*/%>
                            </span>
                        </li>
                        <% } %>

                    </ul>
                </div>
            </aside>
            <% } %>

            <div class="content_block">
                <div class="event_list clearfix" id="event_list">
                    <div class="main_title centered upper">
                        <h2><span class="line"><i class="icon-calendar"></i></span>
                            <span class="list_header"><%=listHeader %></span>
                        </h2>
                    </div>
                    <div class="list">
                        <%
                            if (!isEmptyCollection(eventList)) {
                                for (Event event : eventList) {
                        %>
                        <div class="list_item" id="event_<%=event.getId()%>">
                            <div class="list_content">
                                <h6 class="title">
                                    <a href="/events/<%=event.getId()%>"><%=event.getTitle() %>
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
                                    <% if (event.getLocation() != null) { %>

                               <span class="meta_part">
                                   <a href="#">
                                       <i class="ev_icon icon-map-marker"></i>
                                       <span>
                                           <%=event.getLocation()%>
                                       </span>
                                   </a>
                               </span>
                                <% } %>
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
                        } else { %>
                        <div> There are no events in this category. </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- End Content Section -->
</div>
<!-- Footer -->
<jsp:include page="footer.jsp"/>
<!-- End Footer -->
</body>
</html>
