<%@ page import="com.workfront.internship.event_management.model.Category" %>
<%@ page import="com.workfront.internship.event_management.model.Event" %>
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

</head>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->
    <% Event event = (Event) request.getAttribute("event");
        boolean emptyEvent = true;
        int categoryId = 0;


        if (event.getId() != 0) {
            emptyEvent = false;
            categoryId = event.getCategory().getId();
        }
    %>

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
                               value="<%=(event.getStartDate() != null) ? event.getStartDate() : ""%>">
                        <input type="text" name="startTime" id="start_time" class="time_picker_input">
                    </div>
                    <div class="form_col_half">
                        <label for="end_date">
                            <span class="field_name">End date</span>
                            <span class="required_star">*</span>
                        </label>
                        <input type="text" name="endDate" id="end_date" class="date_picker_input"
                               value="<%=(event.getEndDate() != null) ? event.getEndDate() : ""%>">
                        <input type="text" name="endTime" id="end_time" class="time_picker_input">
                    </div>
                    <input type="checkbox" id="check-all-day"> All day
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_full ">
                        <label for="short_desc">
                            <span class="field_name">Short description</span>
                        </label>
                        <textarea form="event_form" name="shortDesc" id="short_desc" rows="5">
                        <%=event.getShortDescription()%></textarea>
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_full ">
                        <label for="short_desc">
                            <span class="field_name">Full description</span>
                        </label>
                        <textarea form="event_form" name="fullDesc" id="full_desc" rows="7">
                        <%=event.getFullDescription()%></textarea>
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_full">
                        <label for="location">
                            <span class="field_name">Location</span>
                        </label>
                        <input class="input_text" name="location" id="location" type="text"
                               value="<%=event.getLocation()%>">
                    </div>
                </div>
                <%
                    List<Category> categoryList = (List<Category>) request.getAttribute("categories");
                    if (!categoryList.isEmpty()) { %>
                <div class="form_row clearfix">
                    <div class="form_col_full">
                        <label for="location">
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
                                Event image
                                <input class="input_file" name="eventImage" id="event_image" type="file">
                            </div>
                        </div>
                        <div class="form_col_half">
                            <div class="file_button_wrapper">
                                Attach a file
                                <input type="file" class="input_file" name="eventFile" id="event_file">
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
                        <button id="add_invitation_btn" class="btn">Add</button>
                    </div>
                </div>
                <div id="suggested_emails"></div>

                <div class="form_row clearfix">
                    <div class="form_col_full" id="guests_list">
                        <h4>Guests</h4>
                        <div id="invitation_list">
                        </div>
                    </div>
                </div>

                <div class="form_row clearfix">
                    <button type="submit" class="btn full_button" id="event_submit">
                        <i class="icon-check"></i>
                        <span>Save</span>
                    </button>
                </div>

            </form>

            <form id="check-invitation-email">
                <%--<div class="form_row clearfix">--%>
                <%--<div class="form_col_full" id="invitation_email">--%>
                <%--<label for="invitation-email">--%>
                <%--<span class="field_name">Invitations</span>--%>
                <%--</label>--%>
                <%--<input class="input_text" name="invitationEmail" id="invitation-email" type="text"--%>
                <%--placeholder="Enter guest email address" autocomplete="off">--%>
                <%--<input type="button" value="Add" id="add_invitation_btn">--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--<div id="suggested_emails"></div>--%>

                <%--<div class="form_row clearfix">--%>
                <%--<div class="form_col_full" id="guests_list">--%>
                <%--<h4>Guests</h4>--%>
                <%--<div id="invitation_list">--%>
                <%--</div>--%>
                <%--</div>--%>
                <%--</div>--%>
            </form>

        </div>
    </section>
    <!-- End Content Section -->


    <!-- Footer -->
    <jsp:include page="footer.jsp"/>
    <!-- End Footer -->
</div>

<script src="<c:url value="/resources/js/event-edit.js" />"></script>
</body>
</html>
