<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.event_management.model.User" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyString" %>

<jsp:include page="login.jsp"/>

<header id="header">
    <div id="topbar">
        <div class="content">
            <% User user = (User) session.getAttribute("user");
                if (user == null) {
            %>
            <div id="login-reg-menu">
            <span class="top_registration">
                    <i class="top_icon icon-user"></i>
                    <a class="upper" href="/registration">Registration</a>
                </span>
                <span class="top_login">
                    <i class="top_icon icon-lock"></i>
                    <button id="login_button" class="login_button upper">Login</button>
                </span>
            </div>
            <% } else { %>
            <div class="dropdown">
                <span class="dropbtn">
                    <% if(!isEmptyString(user.getAvatarPath())) { %>
                        <img class="avatar_icon" src="/resources/uploads/avatars/<%=user.getAvatarPath()%>">
                   <% } else { %>
                    <i class="top_icon icon-user"></i>
                    <% }%>
                    Hi, <%=user.getFirstName()%>
                    <i class="icon-angle-down"></i>
                </span>
                <div class="dropdown-content">
                    <a href="/my-events">My Events</a>
                    <a href="/my-account">My Account</a>
                </div>
            </div>
            <span><a href="/logout" class="upper" id="logout_button" >Logout</a></span>
            <% }; %>
        </div>
    </div>


    <div id="navigation_bar">
        <div class="content">
            <div id="logo">
                <a href="/"><img src="<c:url value="/resources/img/logo.jpg" />" height='45'>
                </a>
            </div>

            <nav id="main_nav">
                <div id="nav_menu">
                    <ul>
                        <li>
                            <a href="/"><span>Home</span></a>
                        </li>
                        <li>
                            <a href="/events" id="all_events"><span >All Events</span></a>
                        </li>
                        <li>
                            <a href="/upcoming-events" id="upcoming_events"><span>Upcoming events</span></a>
                        </li>
                        <li>
                            <a href="/past-events" id="past-events"><span>Past events</span></a>
                        </li>
                        <li>
                            <a href="#" id="gallery"><span>Gallery</span></a>
                        </li>
                        <li>
                            <a href="#" id="contacts"><span>Contacts</span></a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Search Section -->
            <form class="top_search clearfix" id="search_form" action="/search">
                <div class="top_search_con">
                    <input class="s" id="search_input" placeholder="Search for event ..." type="text" name="keyword">
                    <span class="top_search_icon"><i class="icon-search"></i></span>
                    <input class="top_search_submit" type="submit">
                </div>
            </form>
            <!-- End Search Section -->

            <div class="clear"></div>

        </div>
    </div>
</header>