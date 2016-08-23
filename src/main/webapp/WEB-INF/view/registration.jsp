<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Registration | Event Management</title>

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


    <%--<link rel="stylesheet" type="text/css" href="./css/jquery-ui.min.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="./css/jquery-ui.structure.min.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="./css/jquery-ui.theme.min.css">--%>

    <script>
        $(document).ready(function () {

            //validate and submit registration form
            $('#registration_form').validate({
                rules: {
                    firstName: "required",
                    lastName: "required",
                    email: {
                        required: true,
                        email: true,
                        remote: {
                            url: "/check-email",
                            type: "POST",
                            data: {
                                email: function () {
                                    return $("#email").val();
                                }
                            }
                        }
                    },
                    confirmEmail: {
                        required: true,
                        equalTo: "#email"
                    },
                    password: {
                        required: true,
                        minlength: 6
                    },
                    confirmPassword: {
                        required: true,
                        equalTo: "#password"
                    }
                },

                messages: {
                    firstName: "Please enter your first name.",
                    lastName: "Please enter your last name.",
                    password: {
                        required: "Please provide a password.",
                        minlength: "Password must be at least 6 characters."
                    },
                    confirmPassword: {
                        required: "Please confirm password.",
                        equalTo: "Passwords do not match."
                    },
                    email: {
                        required: "Please provide an email address.",
                        email: "Please enter a valid email address.",
                        remote: "Email already in use."
                    },
                    confirmEmail: {
                        required: "Please confirm email address.",
                        equalTo: "Emails do not match."
                    }
                },

                submitHandler: function (form) {
                    var formData = new FormData($('#registration_form')[0]);

                    $.ajax({
                        url: '/register',
                        type: 'POST',
                        processData: false,
                        contentType: false,
                        data: formData,
                        success: function (result) {
                            if (result.status == "SUCCESS") {
                                window.location = "/";
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {

                        }
                    })
                }
            })
        })
    </script>

</head>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="/WEB-INF/view/header.jsp"/>
    <!-- End Main Header -->

    <!-- Content Section -->
    <section class="content_section">
        <div class="content">
            <div class="form_header">
                <div class="main_title upper">
                    <h2>
                        New user?
                        <span class="highlighted"> Register Now.</span>
                    </h2>
                </div>
            </div>
            <form id="registration_form" enctype="multipart/form-data">
                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="firstName">
                            <span class="field_name">First Name</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="firstName" id="firstName" type="text">
                    </div>
                    <div class="form_col_half ">
                        <label for="lastName">
                            <span class="field_name">Last Name</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="lastName" id="lastName" type="text">
                    </div>
                </div>
                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="email">
                            <span class="field_name">Email</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="email" id="email" type="text">
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
                        <span>Register</span>
                    </button>
                </div>

            </form>
        </div>
    </section>
    <!-- End Content Section -->


    <!-- Footer -->
    <jsp:include page="/WEB-INF/view/footer.jsp"/>
    <!-- End Footer -->
</div>
</body>
</html>
