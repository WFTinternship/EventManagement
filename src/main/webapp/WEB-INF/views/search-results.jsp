<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.event_management.common.DateParser" %>
<%@ page import="com.workfront.internship.event_management.model.Event" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection" %>
<%@ page import="java.util.List" %>
<%@ page import="com.workfront.internship.event_management.model.User" %>
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
    <title>Search results | Event Management</title>

    <script src="<c:url value="/resources/js/lib/jquery-3.1.0.min.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.validate.js" />"></script>
    <script src="<c:url value="/resources/js/lib/bootstrap-notify.js" />"></script>
    <script src="<c:url value="/resources/js/lib/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/my-account.js" />"></script>
    <script src="<c:url value="/resources/js/header.js" />"></script>

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">

</head>
<%
    User sessionUser = (User) session.getAttribute("user");
    int userId = 0;
    if(sessionUser != null) {
        userId = sessionUser.getId();
    }
    String keyword = (String) request.getAttribute("keyword");
    List<Event> eventList = (List<Event>) request.getAttribute("events");
%>
<body class="search-results-page" onload="highlightKeywordOnload('<%=keyword%>')">
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->
    <!-- Content Section -->

    <section class="content_section">
        <div class="content">
            <div class="main_title centered upper">
                <h2><span class="line"><i class="icon-search"></i></span>Search results</h2>
            </div>
           <div class="search_header">
               Events matching keyword <span class="keyword">"<%=keyword%>"</span>
           </div>
            <div class="list">
                <%
                    if (!eventList.isEmpty()) {
                        for (Event event : eventList) {
                %>
                <div class="list_item" id="event_<%=event.getId()%>">
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
                        <p class="desc"><%=event.getShortDescription()%></p>

                        <a class="btn" href="/events/<%=event.getId()%>"><span>Details</span></a>
                    </div>
                </div>
                <% }
                }  else {%>
                <div> Your search "<%=keyword%>" did not match any event. </div>
                <% } %>
            </div>
        </div>
    </section>
    <!-- End Content Section -->

    <!-- Footer -->
    <!-- End Footer -->
</div>
<jsp:include page="footer.jsp"/>

</body>
</html>
