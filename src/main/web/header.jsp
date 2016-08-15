<%@ page import="com.workfront.internship.event_management.model.User" %>

<jsp:include page="login.jsp"/>

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
            <form action="/logout" method="GET" id="logout_form">
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
                            <a href="/events.jsp"><span id="all_events">All Events</span></a>
                        </li>
                        <li>
                            <a href="/events.jsp"><span>Upcoming events</span></a>
                        </li>
                        <li>
                            <a href="/events.jsp"><span>Past events</span></a>
                        </li>
                        <% if (session.getAttribute("user") != null) { %>
                        <li>
                            <a href=""><span>Create Event</span></a>
                        </li>
                        <% }
                            ; %>
                        <li>
                            <a href=""><span>Gallery</span></a>
                        </li>
                        <li>
                            <a href=""><span>Contacts</span></a>
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