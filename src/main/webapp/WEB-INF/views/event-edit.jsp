<%@ page import="static com.workfront.internship.event_management.common.DateParser.getDateStringFromDate" %>
<%@ page import="static com.workfront.internship.event_management.common.DateParser.getTimeStringFromDate" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection" %>
<%@ page import="com.workfront.internship.event_management.model.*" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hermine
  Date: 8/23/16
  Time: 11:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Event Details| Event Management</title>

    <script src="<c:url value="/resources/js/lib/jquery-3.1.0.min.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.validate.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.validate-additional-methods.js" />"></script>
    <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/additional-methods.min.js"></script>

    <script src="<c:url value="/resources/js/lib/jquery-ui.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.timepicker.js" />"></script>
    <script src="<c:url value="/resources/js/lib/bootstrap.min.js" />"></script>

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.css">
    <script src="//cdnjs.cloudflare.com/ajax/libs/timepicker/1.3.5/jquery.timepicker.min.js"></script>

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/jquery-ui.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/jquery.timepicker.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/map.css" />" rel="stylesheet">


</head>
<%
    Event event = (Event) request.getAttribute("event");
    User sessionUser = (User) session.getAttribute("user");
    String action = (String) request.getAttribute("action");

    boolean emptyEvent = true;
    boolean hasImage = false;
    int categoryId = 0;
    if (event.getId() != 0) {
        emptyEvent = false;
        categoryId = event.getCategory().getId();
    }

    //check if is ALL DAY event
    boolean isAllDay = false;
    if(action.equals("edit-event")) {
        String startTimeString = getTimeStringFromDate(event.getStartDate());
        String endTimeString = getTimeStringFromDate(event.getEndDate());

        if (event.getStartDate() != null && event.getEndDate() != null &&
                startTimeString.equals("00:00") && endTimeString.equals("23:59")) {
            isAllDay = true;
        }
    }

    String imageSrc = "#";
    String imgPreviewClass = "";
    if (event.getImageName() != null){
        hasImage = true;
        imgPreviewClass = "visible_thumb";
        imageSrc = "/resources/uploads/events/images/" + event.getImageName();
    }
%>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->
    <!-- Content Section -->
    <section class="content_section">
        <div class="content content clearfix">
            <div class="form_header">
                <div class="main_title upper">
                    <h2>
                        <%=emptyEvent ? "New Event" : event.getTitle()%>
                    </h2>
                </div>
            </div>
            <form id="event_form" enctype="multipart/form-data">
                <input type="hidden" name="eventId" value="<%=event.getId()%>">
                <div class="form_row clearfix">
                    <div class="form_col_full">
                        <label for="eventTitle">
                            <span class="field_name">Title</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="eventTitle" id="eventTitle" type="text"
                               value="<%=event.getTitle()%>">
                    </div>
                </div>
                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="start_date">
                            <span class="field_name">Start date</span>
                            <span class="required_star">*</span>
                        </label>
                        <input type="text" name="startDate" id="start_date" class="date_picker_input"
                               value="<%=(event.getStartDate() != null) ? getDateStringFromDate(event.getStartDate()) : ""%>">
                        <input type="text" name="startTime" id="start_time" class="time_picker_input"
                               value="<%=(event.getStartDate() != null) ? getTimeStringFromDate(event.getStartDate()) : ""%>">
                    </div>
                    <div class="form_col_half">
                        <label for="end_date">
                            <span class="field_name">End date</span>
                            <span class="required_star">*</span>
                        </label>
                        <input type="text" name="endDate" id="end_date" class="date_picker_input"
                               value="<%=(event.getEndDate() != null) ? getDateStringFromDate(event.getEndDate()) : ""%>">
                        <input type="text" name="endTime" id="end_time" class="time_picker_input"
                                value="<%=(event.getEndDate() != null) ? getTimeStringFromDate(event.getEndDate()) : ""%>">
                    </div>
                    <input type="checkbox" id="check-all-day" <%=isAllDay ? "checked" : "" %>> All day
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_full ">
                        <label for="short_desc">
                            <span class="field_name">Short description</span>
                        </label>
                        <textarea form="event_form" name="shortDesc" id="short_desc" rows="5"><%=event.getShortDescription()%></textarea>
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_full ">
                        <label for="short_desc">
                            <span class="field_name">Full description</span>
                        </label>
                        <textarea form="event_form" name="fullDesc" id="full_desc" rows="7"><%=event.getFullDescription()%></textarea>
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_full">
                        <label for="location-input">
                            <span class="field_name">Location</span>
                            <span class="required_star">*</span>
                        </label>
                        <input type="hidden" id="lat" name="lat" value="<%=event.getLat()%>" />
                        <input type="hidden" id="lng" name="lng" value="<%=event.getLng()%>"/>
                        <input class="input_text" name="location" id="location-input" type="text"
                               value="<%=event.getLocation()%>" placeholder="Enter a location" >
                        <div id="map"></div>
                    </div>
                </div>
                <%
                    List<Category> categoryList = (List<Category>) request.getAttribute("categories");
                    if (!categoryList.isEmpty()) { %>
                <div class="form_row clearfix">
                    <div class="form_col_full">
                        <label for="category_select">
                            <span class="field_name">Select Category</span>
                        </label>
                        <select name="categoryId" id="category_select">
                            <% for (Category category : categoryList) { %>
                            <option class="cat_option" value="<%=category.getId()%>"
                                    <%= (categoryId == category.getId()) ? "selected" : ""%>>
                                <%=category.getTitle() %>
                            </option>
                            <% } %>
                        </select>
                        <i class="icon-angle-down"></i>

                    </div>
                </div>
                <%} %>

                <div class="form_row clearfix">
                    <div class="form_col_full">
                        <label for="event_image">
                            <span class="field_name">Attachment</span>
                        </label>
                        <div class="form_col_half">
                            <div class="file_button_wrapper">
                                <div class="fileUpload btn btn-primary">
                                    <i class="icon-upload"></i>
                                    <span class="btn_title" >Choose image</span>
                                    <input type="file" class="input_file" name="eventImage" id="event_image"/>
                                </div>
                                <div id="img_prev" class="<%=imgPreviewClass%>" >
                                        <img id="selected_img" src="<%=imageSrc%>"  class="thumb-img"/>
                                        <button id="delete-img" class="thumb" onclick="" type="button">
                                            <i class="icon-delete"></i>
                                        </button>
                                </div>
                            </div>
                        </div>
                        <div class="form_col_half">
                            <div class="file_button_wrapper">
                                <% if (event.getFileName() != null) { %>
                                <div >
                                    <i class="icon-pdf"></i>
                                    <button id="edit-file" class="thumb" onclick="">
                                        <i class="icon-pencil"></i>
                                    </button>
                                    <button id="delete-file" class="thumb" onclick="">
                                        <i class="icon-delete"></i>
                                    </button>
                                </div>
                                <% } else { %>
                                <div class="fileUpload btn btn-primary">
                                    <i class="icon-upload"></i>
                                    <span class="btn_title">Choose file</span>
                                    <input type="file" class="input_file" name="eventFile" id="event_file"/>
                                </div>

                                <% } %>

                            </div>
                        </div>
                    </div>
                </div>
                <div class="form_row clearfix">
                    <div class="form_col_full">
                        <label>
                            <span class="field_name">Permissions</span>
                        </label>
                    </div>
                    <div class="form_col_half">
                        <div class="file_button_wrapper">
                            Visibility
                            <div class="radio_wrapper">
                                <input type="radio" class="event_radio" name="publicAccessed"
                                       value="1"  <%= (event.isPublicAccessed())? "checked": ""%>><span>Public</span>
                                <input type="radio" class="event_radio" name="publicAccessed"
                                       value="0" <%= (!event.isPublicAccessed())? "checked": ""%>><span>Private</span>
                            </div>
                        </div>
                    </div>
                    <div class="form_col_half">
                        Guests allowed
                        <div class="radio_wrapper">
                            <input type="radio" class="event_radio" name="guestsAllowed"
                                   value="1"  <%= (event.isGuestsAllowed())? "checked": ""%>><span>Yes</span>
                            <input type="radio" class="event_radio" name="guestsAllowed"
                                   value="0" <%= (!event.isGuestsAllowed())? "checked": ""%>><span>No</span>
                        </div>
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_full" id="invitation_email">
                        <label for="invitation-email">
                            <span class="field_name">Invitations</span>
                        </label>
                        <input class="input_text" name="invitationEmail" id="invitation-email" type="text"
                               placeholder="Enter guest email address" autocomplete="off">
                        <button id="add_invitation_btn" class="btn" type="button">Add</button>
                    </div>
                </div>
                <div id="suggested_emails"></div>

                <!-- Invitees -->
                <div class="form_row clearfix">
                    <div class="form_col_full" id="guests_list">
                        <h4>Guests</h4>
                        <div id="invitation_list">

                    <%  if(!isEmptyCollection(event.getInvitations())) {
                            for(Invitation invitation: event.getInvitations()) { %>
                            <div class='invitation_item clearfix' >
                                <i class='icon-response-<%= invitation.getUserResponse().getId()%>'></i>
                                <div class='invitation_item_email'><%=invitation.getUser().getEmail()%></div>
                                <span class ='remove_email'>x</span>

                                <% if(invitation.getUser().getId() == event.getOrganizer().getId()){%>
                                    <span class="organizer"> (Organizer)</span>
                                <% }%>
                            </div>
                        <% }
                    } %></div>

                    </div>
                </div>
                <div class="form_row clearfix btn_row">

                    <% if(action.equals("create-event")) { %>
                        <input type="hidden" name="action" value="create">
                        <button class="btn" id="event_submit">
                            <i class="icon-check"></i>
                            <span>Save</span>
                        </button>

                    <% } else if(action.equals("edit-event")) { %>
                        <input type="hidden" name="action" value="edit">
                        <button type="submit" class="btn" id="event_save">
                            <i class="icon-check"></i>
                            <span>Save</span>
                        </button>
                    <% } %>

                    <button class="btn " id="event_discard" type="button">
                        <i class="icon-check"></i>
                        <span>Cancel</span>
                    </button>
                </div>
            </form>
        </div>

    </section>
    <!-- End Content Section -->
    <script >
        function initAutocomplete() {
            var map = new google.maps.Map(document.getElementById('map'), {
                center: {lat: <%=event.getLat() != 0 ? event.getLat() : 40.1833%>,
                    lng: <%=event.getLng() != 0 ? event.getLng() : 44.5167 %>}, //show Yerevan lat, lng in create eent page
                zoom: 13,
                mapTypeId: 'roadmap'
            });

            // Create the search box and link it to the UI element.
            var input = document.getElementById('location-input');
            var searchBox = new google.maps.places.SearchBox(input);
            map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

            // Bias the SearchBox results towards current map's viewport.
            map.addListener('bounds_changed', function() {
                searchBox.setBounds(map.getBounds());
            });

            var markers = [];
            // Listen for the event fired when the user selects a prediction and retrieve
            // more details for that place.
            searchBox.addListener('places_changed', function() {
                var places = searchBox.getPlaces();

                if (places.length == 0) {
                    return;
                }

                // Clear out the old markers.
                markers.forEach(function(marker) {
                    marker.setMap(null);
                });
                markers = [];

                // For each place, get the icon, name and location.
                var bounds = new google.maps.LatLngBounds();
                places.forEach(function(place) {
                    if (!place.geometry) {
                        console.log("Returned place contains no geometry");
                        return;
                    }
                    debugger;
                    var address = place.formatted_address;
                    var latitude = place.geometry.location.lat();
                    var longitude = place.geometry.location.lng();

                    //set lat lng to hidden input elements
                    $("#lat").val(latitude);
                    $("#lng").val(longitude);

                    var icon = {
                        url: place.icon,
                        size: new google.maps.Size(71, 71),
                        origin: new google.maps.Point(0, 0),
                        anchor: new google.maps.Point(17, 34),
                        scaledSize: new google.maps.Size(25, 25)
                    };

                    // Create a marker for each place.
                    markers.push(new google.maps.Marker({
                        map: map,
                        icon: icon,
                        title: place.name,
                        position: place.geometry.location
                    }));

                    if (place.geometry.viewport) {
                        // Only geocodes have viewport.
                        bounds.union(place.geometry.viewport);

                    } else {
                        bounds.extend(place.geometry.location);
                    }
                });
                map.fitBounds(bounds);
            });
        }

    </script>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBp1mWG670cx7hxNYJ1hlXuXFVKCQDnOQY&libraries=places&callback=initAutocomplete"
                async defer></script>
    </div>
<!-- Footer -->
<jsp:include page="footer.jsp"/>
<!-- End Footer -->
<script src="<c:url value="/resources/js/event-edit.js" />"></script>
</body>
</html>
