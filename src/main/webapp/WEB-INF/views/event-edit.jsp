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

    <script src="<c:url value="/resources/js/jquery-3.1.0.min.js" />"></script>
    <script src="<c:url value="/resources/js/jquery.validate.js" />"></script>
    <script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">
</head>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->
    <% Event event = (Event) request.getAttribute("event");
        int categoryId = 0;
        if (event.getCategory() != null) {
            categoryId = event.getCategory().getId();
        }
    %>
    <!-- Content Section -->
    <section class="content_section">
        <div class="content">
            <div class="form_header">
                <div class="main_title upper">
                    <h2>
                        New Event
                    </h2>
                </div>
            </div>
            <form id="event_form" enctype="multipart/form-data">
                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="event_title">
                            <span class="field_name">Title</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="event_title" id="event_title" type="text"
                               value="<%=event.getTitle()%>">
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_half ">
                        <label for="short_desc">
                            <span class="field_name">Short description</span>
                        </label>
                        <textarea form="event_form" name="short_desc" id="short_desc">
                        <%=event.getShortDescription()%></textarea>
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_half ">
                        <label for="short_desc">
                            <span class="field_name">Full description</span>
                        </label>
                        <textarea form="event_form" name="full_desc" id="full_desc">
                        <%=event.getFullDescription()%></textarea>
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_half">
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
                    <div class="form_col_half">
                        <label for="location">
                            <span class="field_name">Select Category</span>
                        </label>
                        <select name="category_select" id="category_select">
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
                    <div class="form_col_half">
                        <label for="event_image">
                            <span class="field_name">Upload image</span>
                        </label>
                        <div class="file_button_wrapper">
                            <input class="input_file" name="event_image" id="event_image" type="file">
                        </div>
                    </div>
                </div>
                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="event_file">
                            <span class="field_name">Upload file</span>
                        </label>
                        <div class="file_button_wrapper">
                            <input type="file" class="input_file" name="event_file" id="event_file">
                        </div>
                    </div>
                </div>
                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label>
                            <span class="field_name">Visibility</span>
                        </label>
                        <div class="file_button_wrapper">
                            <div class="file_button_wrapper">
                                <input type="radio" class="event_radio" name="public_accessed"
                                       value="1"  <%= (event.isPublicAccessed())? "selected": ""%>>Public
                                <input type="radio" class="event_radio" name="public_accessed"
                                       value="0" <%= (!event.isPublicAccessed())? "selected": ""%>>Private
                            </div>
                        </div>
                    </div>
                    <div class="form_col_half">
                        <label>
                            <span class="field_name">Guests allowed</span>
                        </label>
                        <div class="file_button_wrapper">
                            <input type="radio" class="event_radio" name="guests_allowed"
                                   value="1"  <%= (event.isGuestsAllowed())? "selected": ""%>>Yes
                            <input type="radio" class="event_radio" name="guests_allowed"
                                   value="0" <%= (!event.isGuestsAllowed())? "selected": ""%>>No
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
        </div>
    </section>
    <!-- End Content Section -->


    <!-- Footer -->
    <jsp:include page="footer.jsp"/>
    <!-- End Footer -->
</div>
</body>
</html>
