<%@ page import="com.workfront.internship.event_management.model.Event" %>
<%@ page import="com.workfront.internship.event_management.service.EventService" %>
<%@ page import="com.workfront.internship.event_management.service.EventServiceImpl" %>
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
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="css/reset.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="css/icon_font.css">
    <link rel="stylesheet" type="text/css" href="css/flexslider.css">

    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.structure.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.theme.min.css">

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">

    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/jquery.validate.js"></script>
    <script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="js/jquery.flexslider.js"></script>

    <script type="text/javascript" src="js/home.js"></script>

</head>
<body class="home_page">
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="/header.jsp"/>
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
            <div class="content_block">
                <div class="event_list clearfix" id="event_list">
                    <div class="main_title centered upper">
                        <h2><span class="line"><i class="icon-calendar"></i></span>Upcoming Events</h2>
                    </div>
                    <div class="list">

                        <%
                            EventService eventService = new EventServiceImpl();
                            List<Event> eventList = eventService.getAllEvents();

                            if (!eventList.isEmpty()) {
                                for (Event event : eventList) {
                        %>
                        <div class="list_item">
                            <div class="list_content">
                                <h6 class="title">
                                    <a href="#"><%=event.getTitle() %>
                                    </a>
                                </h6>
                                <span class="meta">
                                       <span class="meta_part ">
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
                                               <span>Event Organizer</span>
                                           </a>
                                       </span>
                                   </span>
                                <p class="desc"><%=event.getShortDescription()%>
                                </p>

                                <a class="btn" href="#"><span>Details</span></a>
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
    <jsp:include page="/footer.jsp"/>
    <!-- End Footer -->
</div>
</body>
</html>
