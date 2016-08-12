<%@ page import="com.workfront.internship.event_management.model.User" %>

<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.validate.js"></script>
<script>
    $(document).ready(function () {

        $("#login_button").click(function () {
            $("#login_modal").modal();
        });

        $('#login_form').validate({
            rules: {
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true,
                    minlength: 6
                },
            },

            submitHandler: function (form) {
                // e.preventDefault();
                var email = $('#email').val();
                var password = $('#password').val();


                $.ajax({
                    type: 'POST',
                    data: {
                        action: 'LOGIN',
                        email: email,
                        password: password
                    },
                    url: '/account-controller',
                    success: function (result) {
                        window.location = "/index.jsp";
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        //if (jqXHR.responseText !== '') {
                        //     alert(textStatus + ": " + jqXHR.responseText);
                        //  } else {
                        alert(textStatus + ": " + errorThrown);
                        //  }
                    }
                })
            }
        })
    })
</script>

<div class="container">

    <!-- Modal -->
    <div class="modal fade " id="login_modal" role="dialog">
        <div class="login_dialog">
            <div class="modal_header">
                <div class="lf_user_row">
                    <span class="lf_header">Login to your Account</span>
                </div>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <form id="login_form" method="POST" action="/login">

                <div class="lf_user_row">
                    <label for="email">
                        <i class="lf_icon icon-user"></i>
                        <input name="email" id="email" type="text">
                    </label>
                </div>
                <div class="lf_user_row">
                    <label for="password">
                        <i class="lf_icon icon-lock"></i>
                        <input name="password" id="password" type="password">
                    </label>
                </div>
                <div class="lf_user_row clearfix">
                    <div class="form_col_half">
                        <label for="rememberme">
                    <span class="remember-box">
                        <input id="rememberme" name="rememberme" type="checkbox">
                        <span>Remember me</span>
                    </span>
                        </label>
                    </div>
                    <div class="form_col_half clearfix">
                        <button name="login" class="btn f_right" id="login">
                            Sign in
                        </button>
                    </div>
                </div>
                <a class="lf_forget_pass" href="#">Forgot Your Password?</a>
            </form>
            </div>
    </div>
</div>

<header id="header">

    <div id="topbar">
        <div class="content">

            <% User user = (User) session.getAttribute("user");
                if (user == null) {
            %>
            <span class="top_registration">
                    <i class="top_icon icon-user"></i>
                    <a class="upper" href="registration.jsp">Registration</a>
                </span>
            <span class="top_login">
                    <i class="top_icon icon-lock"></i>
                    <button id="login_button" class="login_button upper">Login</button>
                <!--<a class="upper" href="../login.jsp">Login</a> -->

                </span>
            <% } else { %>
            <span class="top_registration">
                        <i class="top_icon icon-user"></i>
                        Hi, <%=user.getFirstName()%>
                </span>
            <form action="/account-controller" method="POST" id="logout_form">
                <input type="hidden" name="action" value="LOGOUT"/>
                <input class="upper" type="submit" value="Logout" id="logout_button"/>
            </form>

            <% }
                ; %>
        </div>
    </div>


    <div id="navigation_bar">
        <div class="content">
            <div id="logo">
                <a href="/"><img src="/img/logo.jpg" height='45'>
                </a>
            </div>

            <nav id="main_nav">
                <div id="nav_menu">
                    <ul>
                        <li>
                            <a href=""><span>Home</span></a>
                        </li>
                        <li>
                            <a href=""><span>All Events</span></a>
                        </li>

                        <li>
                            <a href=""><span>Upcomming Events</span></a>
                        </li>
                        <li>
                            <a href=""><span>Past Events</span></a>
                        </li>
                        <li>
                            <a href=""><span>Event Categories</span></a>
                        </li>
                        <li>
                            <a href=""><span>Gallery</span></a>
                        </li>

                    </ul>
                </div>
            </nav>

            <!-- Search Section -->
            <form class="top_search clearfix">
                <div class="top_search_con">
                    <input class="s" placeholder="Search Here ..." type="text">
                    <span class="top_search_icon"><i class="icon-search"></i></span>
                    <input class="top_search_submit" type="submit">
                </div>
            </form>
            <!-- End Search Section -->

            <div class="clear"></div>

        </div>
    </div>
</header>