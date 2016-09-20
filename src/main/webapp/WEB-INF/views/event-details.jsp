<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.event_management.model.Category" %>
<%@ page import="com.workfront.internship.event_management.model.Event" %>
<%@ page import="com.workfront.internship.event_management.service.CategoryService" %>
<%@ page import="com.workfront.internship.event_management.service.CategoryServiceImpl" %>
<%@ page import="com.workfront.internship.event_management.service.EventService" %>
<%@ page import="com.workfront.internship.event_management.service.EventServiceImpl" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection" %>
<%@ page import="com.workfront.internship.event_management.model.Invitation" %>
<%@ page import="com.workfront.internship.event_management.model.UserRole" %><%--
  Created by IntelliJ IDEA.
  User: Inmelet
  Date: 8/8/2016
  Time: 10:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Event | Event Management</title>

    <script src="<c:url value="/resources/js/lib/jquery-3.1.0.min.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.validate.js" />"></script>
    <script src="<c:url value="/resources/js/lib/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/events.js" />"></script>

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">

</head>
<body class="events_page">
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->

    <!-- Content Section -->

    <section class="content_section">
        <div class="content clearfix">

            <%
                Event event = (Event) request.getAttribute("event");
            %>
            <div class="content_block">
                <div class="event_list clearfix" id="event_list">
                    <div class="list_item">
                        <div class="list_content">
                            <h6 class="title">
                                <a href="#"><%=event.getTitle()%>
                                </a>
                            </h6>
                            <span class="meta">
                                   <span class="meta_part">
                                       <a href="#">
                                           <i class="ev_icon icon-clock"></i>
                                           <span><%=event.getStartDate() %></span>
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
                            <% if(event.getImageName() != null) {%>
                                <img src = "/events/images/<%=event.getImageName()%>" />
                            <% } %>
                                <p class="desc"><%=event.getShortDescription()%></p>

                                <p class="desc"><%=event.getFullDescription()%>
                                </p>

                                <% List<Invitation> invitations = event.getInvitations();
                                    if(!isEmptyCollection(invitations)) { %>
                            <h4>Invitations</h4>
                                <div class="invitees">
                                    <% for(Invitation invitation :invitations) { %>
                                        <div class="invitees_email">
                                            <i class='icon-user'></i>

                                            <%=invitation.getUser().getEmail()%>

                                            <i class='icon-response-<%= invitation.getUserResponse().getTitle()%>'></i>
                                            <% if(invitation.getUserRole().equals(UserRole.ORGANIZER)){%>
                                                <span class="organizer"> (Organizer)</span>
                                            <% }%>

                                        </div>
                                    <% }%>
                                </div>
                            <% }%>
                            </div>
                    <%
                        String action = (String) request.getAttribute("action");
                        if(action == "invitation-respond"){
                    %>
                    <div class="respond_wrapper">
                        <h4>Respond to invitation</h4>
                            <div class="radio_wrapper">
                                <input type="radio" class="event_radio" name="guestsAllowed"
                                       value="yes"  <%= (event.isGuestsAllowed())? "checked": ""%>><span>Yes</span>
                                <input type="radio" class="event_radio" name="guestsAllowed"
                                       value="no" <%= (!event.isGuestsAllowed())? "checked": ""%>><span>No</span>
                                <input type="radio" class="event_radio" name="guestsAllowed"
                                       value="maybe" <%= (!event.isGuestsAllowed())? "checked": ""%>><span>Maybe</span>
                            </div>

                            <div class="btn_wrapper">
                                <a class="btn" href="">Save</a>
                                <a class="btn" href="/events">Discard changed</a>
                            </div>
                        <div>
                            </div>>
                        <% } %>
                        </div>
                    </div>
                </div>
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
