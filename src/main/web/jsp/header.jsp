<%@ page import="com.workfront.internship.event_management.model.User" %>

<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script>
    $(document).ready(function () {
        $("#login_button").click(function () {
            $("#login_modal").modal();
        });
    });
</script>

<div class="container">

    <!-- Modal -->
    <div class="modal fade " id="login_modal" role="dialog">
        <div class="login_dialog">
            <div class="modal_header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <div class="lf_user_row">
                    <span class="lf_header">Login to your Account</span>
                </div>
            </div>

            <form id="login_form" method="POST" action="/login">

                <div class="lf_user_row">
                    <label for="username">
                        <i class="lf_icon icon-user"></i>
                        <input name="username" id="username" type="text">
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
                    <a class="upper" href="../registration.jsp">Registration</a>
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
            <form action="/logout" method="POST" id="logout_form">
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