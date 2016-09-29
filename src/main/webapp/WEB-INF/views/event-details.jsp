<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.event_management.service.CategoryService" %>
<%@ page import="com.workfront.internship.event_management.service.CategoryServiceImpl" %>
<%@ page import="com.workfront.internship.event_management.service.EventService" %>
<%@ page import="com.workfront.internship.event_management.service.EventServiceImpl" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection" %>
<%@ page import="com.workfront.internship.event_management.common.DateParser" %>
<%@ page import="com.workfront.internship.event_management.model.*" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyString" %>
<%@ page import="static com.workfront.internship.event_management.common.DateParser.getTimeStringFromDate" %>
<%@ page import="static com.workfront.internship.event_management.common.DateParser.getDateStringFromDate" %>
<%@ page import="java.util.*" %><%--
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
    <script src="<c:url value="/resources/js/lib/bootstrap-notify.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.validate.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.validate-additional-methods.js" />"></script>
    <script src="<c:url value="/resources/js/lib/lightgallery.js" />"></script>
    <script src="<c:url value="/resources/js/lib/lightgallery.min.js" />"></script>
    <script src="<c:url value="/resources/js/event-details.js" />"></script>

    <script src="<c:url value="/resources/js/header.js" />"></script>


    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap-notify.animate.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/lightgallery.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/lightgallery.min.css" />" rel="stylesheet">

    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">

    <%
        Event event = (Event) request.getAttribute("event");
        User sessionUser = (User) session.getAttribute("user");
        String action = request.getAttribute("action") != null ? (String) request.getAttribute("action") : " ";

        //check if is ALL DAY event
        boolean isAllDay = false;
        String startTimeString = getTimeStringFromDate(event.getStartDate());
        String endTimeString = getTimeStringFromDate(event.getEndDate());

        if (event.getStartDate() != null && event.getEndDate() != null &&
                startTimeString.equals("00:00") && endTimeString.equals("23:59")){
            isAllDay = true;
        }

        //check if event is passed
        Date today = new Date();
        boolean isPassedEvent = false;
        if(today.after(event.getStartDate()) && event.getStartDate().before(today)) {
            isPassedEvent = true;
        }

    %>
    <!-- FB  share settings -->
    <meta property="og:type"          content="article" />
    <meta property="og:title"         content="<%=event.getTitle()%>" />
    <meta property="og:description"   content="<%=event.getShortDescription()%>" />
    <meta property="og:image"         content="http://www.127.0.0.1:8085/resources/uploads/events/images/<%=event.getImageName()%>" />
</head>
</head>

<body class="event_details_page">
<!-- FB Share -->
<div id="fb-root"></div>
<script>
    (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.7";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));
</script>
<!-- END FB Share -->

<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->

    <!-- Content Section -->

    <section class="content_section">
        <div class="content clearfix">
            <div class="content_block">
                <div class="event_list clearfix" id="event_list">
                    <div class="list_item">
                        <div class="list_content">
                            <%
                                if (action.equals("invitation-responded")) {
                            %>
                            <script type="text/javascript">
                                showResponseSavedMessage();
                            </script>
                            <% } %>
                            <h6 class="title">
                                <a href="#"><%=event.getTitle()%>
                                </a>
                            </h6>
                            <% if(sessionUser != null && event.getOrganizer().getId() == sessionUser.getId()) { %>
                            <a id="edit-event" class="change-event-btn" href="/events/<%=event.getId()%>/edit">
                                <i class="icon-pencil"></i>
                                <span>Edit</span>
                            </a>
                            <button id="delete-event" class="change-event-btn" onclick="deleteEvent(<%=event.getId()%>)">
                                <i class="icon-delete"></i>
                                <span>Delete</span>
                            </button>
                            <% } %>

                           <div class="sharing">
                            <!-- FB share button -->
                            <div class="fb-share-button"
                                 data-size="large" data-type="button_count"></div>
                            <!-- Share via email button -->
                            <a class="btn" href="javascript:emailCurrentPage()">
                                <i class="icon-envelope"></i>
                            </a>
                        </div>
                            <% if (event.getImageName() != null) {%>
                                <img class="event_img" src = "/resources/uploads/events/images/<%=event.getImageName()%>" />
                            <% } %>
                            <div class="meta-row clearfix">
                            <div class="col_half">
                                <div>
                                    <span class="meta_header">Start:</span>
                                    <i class="ev_icon icon-clock"></i>
                                    <%= isAllDay ? getDateStringFromDate(event.getStartDate()) : DateParser.parseDateToString(event.getStartDate()) %>
                                </div>
                                <div>
                                    <span class="meta_header">End:</span>
                                    <i class="ev_icon icon-clock"></i>
                                    <span>
                                        <%= isAllDay ? getDateStringFromDate(event.getEndDate()): DateParser.parseDateToString(event.getEndDate()) %></span>
                                </div>
                                <div>
                                    <span class="meta_header">Guests allowed:</span>
                                    <i class="ev_icon icon-<%= event.isGuestsAllowed() ? "group" : "user"%>"></i>
                                    <span><%= event.isGuestsAllowed() ? "Yes" : "No"%></span>
                                </div>
                                <div>
                                    <span class="meta_header">Location:</span>
                                    <i class="ev_icon icon-map-marker"></i>
                                   <span><%=event.getLocation()%></span>
                                </div>

                            </div>
                            <div class="col_half">
                                <div>
                                    <span class="meta_header">Created on:</span>
                                    <i class="ev_icon icon-clock"></i>
                                    <span><%=DateParser.parseDateToString(event.getCreationDate()) %></span>
                                </div>
                                <div>
                                    <span class="meta_header">Organized by:</span>
                                    <i class="ev_icon icon-user"></i>
                                    <span><%=event.getOrganizer().getFirstName() %> <%=event.getOrganizer().getLastName() %></span>
                                </div>
                                <div>
                                    <span class="meta_header">Event category:</span>
                                    <i class="ev_icon icon-folder"></i>
                                   <span><%= event.getCategory().getTitle()%></span>
                                </div>
                                <div>
                                    <span class="meta_header">Privacy:</span>
                                    <i class="ev_icon icon-<%= event.isPublicAccessed() ? "unlock" : "lock"%>"></i>
                                    <span><%= event.isPublicAccessed() ? "Public" : "Private"%></span>

                                </div>
                            </div>
                            </div>
                            <div id="map"></div>

                            <% if (!isEmptyString(event.getShortDescription())) { %>
                                <div class="desc">
                                    <div class="desc_header">Short description:</div>
                                    <%=event.getShortDescription()%>
                                </div>
                            <% }

                                if (!isEmptyString(event.getFullDescription())) { %>
                             <div class="desc">
                                <div class="desc_header">Full description:</div>
                                <%=event.getFullDescription()%>

                             </div>
                            <% } %>
                                <% List<Invitation> invitations = event.getInvitations();
                                    if(!isEmptyCollection(invitations)) { %>
                                    <div class="guest_list">
                                    <h4>Guests</h4>
                                    <div class="invitees">
                                        <% for(Invitation invitation :invitations) { %>
                                            <div class="invitees_email">
                                                <div class='invitation_item clearfix' >
                                                    <i class='icon-response-<%= invitation.getUserResponse().getId()%>'></i>
                                                    <div class='invitation_item_email'><%=invitation.getUser().getEmail()%></div>

                                                    <% if(invitation.getUser().getId() == event.getOrganizer().getId()){%>
                                                    <span class="organizer"> (Organizer)</span>
                                                    <% }%>
                                                    <% if (sessionUser != null && sessionUser.getId() == invitation.getUser().getId() && !isPassedEvent){  %>
                                                    <div id="change_response_wrapper">
                                                        <button id="change_response" class="">
                                                            <i class="icon-pencil change-response-icon"></i>
                                                            <span>Change response</span>
                                                        </button>

                                                        <div id="respond_wrapper"  >
                                                            <div>Respond to invitation</div>
                                                            <div class="radio_wrapper">
                                                                <input type="radio" class="event_radio" name="response"
                                                                       value="1"  <%=(invitation.getUserResponse().getId() == 1)? "checked": ""%>><span>Yes</span>
                                                                <input type="radio" class="event_radio" name="response"
                                                                       value="2" <%=(invitation.getUserResponse().getId() == 2)? "checked": ""%>><span>No</span>
                                                                <input type="radio" class="event_radio" name="response"
                                                                       value="3" <%=(invitation.getUserResponse().getId() == 3)?  "checked": ""%>><span>Maybe</span>
                                                            </div>

                                                            <div class="btn_wrapper btn_wrapper clearfix">
                                                                <button class="btn" id="respond" onclick="saveResponse(this, <%=event.getId()%>)" >Save</button>
                                                                <button class="btn" id="cancel_response">Cancel</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                        <% } %>
                                                </div>

                                            </div>
                                        <% }%>
                                    </div>
                                </div>
                            <% }%>


                        <%
                            List<Media> mediaList = event.getMedia();
                            if(!isEmptyCollection(mediaList)) { %>
                            <div class="photos_wrapper">
                                <div class="desc_header">Photos:</div>



                        <% Map<String, List<Media>> userMediaMap = new HashMap<>();
                                for (Media media: mediaList) {
                                    User user = media.getUploader();
                                    String userName = user.getFirstName() + " " + user.getLastName() +
                                            " ( "+ user.getEmail() + " )";

                                    List<Media> currentUserMediaList = userMediaMap.get(userName);
                                    if(currentUserMediaList == null) {
                                        currentUserMediaList = new ArrayList<>();
                                        currentUserMediaList.add(media);
                                    } else {
                                        currentUserMediaList.add(media);
                                    }

                                    userMediaMap.put(userName, currentUserMediaList);
                                }
                        %>


                        <%
                            for (Map.Entry<String, List<Media>> entry : userMediaMap.entrySet()) {
                                List<Media> userMediaList = entry.getValue();
                                String userName = entry.getKey();
                        %>
                        <div class="uploader_name"><%=userName%></div>
                                <div class="animated-thumbnials">

                                <%
                                for ( Media media: userMediaList ) {
                                    User user = media.getUploader();
                                    String imagePath = "/resources/uploads/events/event" +
                                            event.getId() + "/user" + user.getId() + "/" + media.getName();
                            %>
                                <a data-src="<%=imagePath%>">
                                    <img class="photo-item" src="<%=imagePath%>">
                                </a>
                            <% } %>

                            <% } %>
                            </div>
                            </div>
                            <% } %>

                        <% if (sessionUser != null && isPassedEvent) { %>

                            <form id="upload_images" enctype="multipart/form-data">
                            <input type="hidden" name="eventId" value="<%=event.getId()%>">
                                <div class="desc_header">Add photos:</div>
                                <div class="fileUpload btn btn-primary">
                                        <i class="icon-img"></i>
                                        <span class="btn_title" >Choose photos</span>
                                        <input onchange="preview(this);" type="file" class="input_file" name="eventImages" id="event_images" multiple/>
                                    </div>
                                <input id="upload_photos_btn" type="submit" Value="Upload" class="btn">
                                <div id="img_prev_photos" ></div>
                            </form>
                        <% } %>
                    </div>
                    </div>
                </div>
            </div>
        </div>

    </section>


    <!-- End Content Section -->

</div>

<!-- MAP -->
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBp1mWG670cx7hxNYJ1hlXuXFVKCQDnOQY"></script>
<script>
    var map;
    function initialize() {
        var mapOptions = {
            zoom: 13,
            center: {lat: <%=event.getLat()%>, lng: <%=event.getLng()%>}
        };
        map = new google.maps.Map(document.getElementById('map'),
                mapOptions);

        var marker = new google.maps.Marker({
            position: {lat: <%=event.getLat()%>, lng: <%=event.getLng()%>},
            map: map
        });

        var infowindow = new google.maps.InfoWindow({
            content: '<p>Marker Location:' + marker.getPosition() + '</p>'
        });

        google.maps.event.addListener(marker, 'click', function() {
            infowindow.open(map, marker);
        });
    }

    google.maps.event.addDomListener(window, 'load', initialize);
</script>
<!-- Footer -->
<jsp:include page="footer.jsp"/>
<!-- End Footer -->
</body>
</html>
