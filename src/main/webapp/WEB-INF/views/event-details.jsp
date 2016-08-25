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
                        <input class="input_text" name="event_title" id="event_title" type="text">
                    </div>
                    <div class="form_col_half ">
                        <label for="short_desc">
                            <span class="field_name">Short description</span>
                        </label>
                        <input class="input_text" name="short_desc" id="short_desc" type="text">
                    </div>
                </div>
                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="location">
                            <span class="field_name">Location</span>
                        </label>
                        <input class="input_text" name="location" id="location" type="text">
                    </div>
                    <div class="form_col_half">
                        <label for="confirmEmail">
                            <span class="field_name">Confirm Email</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="confirmEmail" id="confirmEmail" type="text">
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="password">
                            <span class="field_name">Password</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="password" id="password" type="password">
                    </div>
                    <div class="form_col_half">
                        <label for="confirmPassword">
                            <span class="field_name">Confirm Password</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="confirmPassword" id="confirmPassword" type="password">
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="phone">
                            <span class="field_name">Phone Number</span>
                        </label>
                        <input class="input_text" name="phone" id="phone" type="text">
                    </div>
                    <div class="form_col_half">
                        <label for="avatar">
                            <span class="field_name">Avatar</span>
                        </label>
                        <div class="file_button_wrapper">
                            <input class="input_file" name="avatar" id="avatar" type="file">
                        </div>
                    </div>
                </div>
                <div class="form_row clearfix">
                    <button type="submit" class="btn full_button" id="reg_submit">
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
