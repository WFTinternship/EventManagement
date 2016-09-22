<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.event_management.common.DateParser" %>
<%@ page import="com.workfront.internship.event_management.model.Event" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection" %>
<%@ page import="java.util.List" %>
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
    <script src="<c:url value="/resources/js/lib/bootstrap-notify.js" />"></script>
    <script src="<c:url value="/resources/js/lib/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/my-account.js" />"></script>
    <script src="<c:url value="/resources/js/events.js" />"></script>

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/flexslider.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/my-account.css" />" rel="stylesheet">

    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">

</head>
<body class="my-account">
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->
    <!-- Content Section -->

    <section class="content_section">
        <div class="content">
            <div class="content_block">
                <a id="new-event-btn" class="btn upper" href="/new-event"><span><i class="icon-pencil"></i></span>New event</a>

                <div class="hm-tabs tabs2 uppper is-ended"><nav class="clearfix">
                    <ul class="tabs-navi">
                        <li><a data-content="my-events" class="selected" href="#"><span><i class="icon-calendar"></i></span>Events I'm hosting</a></li>
                        <li><a data-content="all-invitations" href="#"><span><i class="icon-envelope"></i></span>Invites</a></li>
                        <li><a data-content="accepted-invitations" href="#"><span><i class="icon-accepted"></i></span>Accepted Invitation</a></li>
                        <li><a data-content="pending-invitations" href="#"><span><i class="icon-question"></i></span>Pending Invitations</a></li>
                        <li><a data-content="participated-events" href="#"><span><i class="icon-group"></i></span>Participated Events</a></li>
                    </ul>
                    <!-- tabs-navi --></nav>
                    <ul style="height: auto;" class="tabs-body">
                        <li data-content="my-events" class="selected clearfix" id="organized-events-tab">
                            <% List<Event> userOrganizedEvents = (List<Event>)session.getAttribute("userOrganizedEvents");
                            if(!isEmptyCollection(userOrganizedEvents)) { %>

                            <div class="list" id="organized-events-list">
                                <% for (Event event : userOrganizedEvents) {
                                %>
                                <div class="list_item" id="event_<%=event.getId()%>">
                                    <div class="list_content">
                                        <h6 class="title">
                                            <a href="#"><%=event.getTitle() %></a>
                                        </h6>
                                        <a id="edit-event" class="change-event-btn" href="/events/<%=event.getId()%>/edit">
                                            <i class="icon-pencil"></i>
                                            <span>Edit</span>
                                        </a>
                                        <button id="delete-event" class="change-event-btn" onclick="deleteEvent(<%=event.getId()%>)">
                                            <i class="icon-delete"></i>
                                            <span>Delete</span>
                                        </button>
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
                                <% } %>
                                </div>
                                <% } else { %>

                            <div class="no-events">No organized events.</div>
                            <% } %>

                        </li>
                        <li data-content="all-invitations">
                        </li>
                        <li data-content="pending-invitations">
                        </li>
                        <li data-content="accepted-invitations">
                        </li>
                        <li data-content="participated-events">
                        </li>
                    </ul>
                    <!-- Tabs Content --></div>
            </div>
        </div>
    </section>
    <!-- End Content Section -->

    <!-- Footer -->
    <jsp:include page="footer.jsp"/>
    <!-- End Footer -->
</div>
</body>
</html>
