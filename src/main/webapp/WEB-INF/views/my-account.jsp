<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.workfront.internship.event_management.model.Category" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.workfront.internship.event_management.service.util.Validator.isEmptyCollection" %>
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
    <title>Home | Event Management</title>

    <script src="<c:url value="/resources/js/lib/jquery-3.1.0.min.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.validate.js" />"></script>
    <script src="<c:url value="/resources/js/lib/jquery.flexslider.js" />"></script>
    <script src="<c:url value="/resources/js/lib/bootstrap.min.js" />"></script>
    <script src="<c:url value="/resources/js/my-account.js" />"></script>

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">

    <link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/bootstrap.min.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/lib/flexslider.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">
    <link href="<c:url value="/resources/css/my-account.css" />" rel="stylesheet">

    <link href="<c:url value="/resources/css/icon_font.css" />" rel="stylesheet">

</head>
<body class="home_page">
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="header.jsp"/>
    <!-- End Main Header -->

    <div class="flexslider">
        <ul class="slides">
            <li>
                <img src="<c:url value="/resources/img/slide2.jpg" />"/>
            </li>
            <li>
                <img src="<c:url value="/resources/img/slide2.jpg" />"/>
            </li>
        </ul>
    </div>

    <!-- Content Section -->

    <section class="content_section">
        <div class="content">
            <%
                List<Category> categoryList = (List<Category>) request.getAttribute("categories");
                if (!isEmptyCollection(categoryList)) { %>
            <aside class="left_sidebar">

                <div class="cat_menu">
                    <h6 class="title">Categories</h6>
                    <ul class="cat_list">
                        <%
                            for (Category category : categoryList) { %>
                        <li>
                            <div class="cat_item"
                                 onclick='getEventsByCategory("<%=category.getId()%>", this);'><%=category.getTitle() %>
                            </div>
                            <span class="num_events">
                                <% /*= eventService.getEventsByCategory(category.getId()).size()*/%>
                            </span>
                        </li>
                        <% } %>

                    </ul>
                </div>
            </aside>
            <% } %>

            <div class="content_block">
                <div class="hm-tabs tabs2 uppper is-ended"><nav class="clearfix">
                    <ul class="tabs-navi">
                        <li><a data-content="my-events" class="selected" href="#"><span><i class="icon-pencil"></i></span>My Events</a></li>
                        <li><a data-content="all-invitations" href="#"><span><i class="icon-calendar"></i></span>All invitations</a></li>
                        <li><a data-content="accepted-invitations" href="#"><span><i class="icon-accepted"></i></span>Accepted Invitation</a></li>
                        <li><a data-content="pending-invitations" href="#"><span><i class="icon-question"></i></span>Pending Invitations</a></li>
                        <li><a data-content="participated-events" href="#"><span><i class="icon-group"></i></span>Participated Events</a></li>
                    </ul>
                    <!-- tabs-navi --></nav>
                    <ul style="height: auto;" class="tabs-body">
                        <li data-content="my-events" class="selected clearfix">
                            <h6>Why Choose Us ?</h6>
                            <p>Inbox Lorem ipsum dolor sit amet, consectetur adipisicing elit. Earum recusandae rem animi accusamus quisquam reprehenderit sed voluptates, numquam, quibusdam velit dolores repellendus tempora corrupti accusantium obcaecati voluptate totam eveniet laboriosam?</p>
                            <p>Inbox Lorem ipsum dolor sit amet, consectetur adipisicing elit. Earum recusandae rem animi accusamus quisquam reprehenderit sed voluptates, numquam, quibusdam velit dolores repellendus tempora corrupti accusantium obcaecati voluptate totam eveniet laboriosam?</p>
                        </li>
                        <li data-content="all-invitations">
                            <p>New Lorem ipsum dolor sit amet, consectetur adipisicing elit. Non a voluptatibus, ex odit totam cumque nihil eos asperiores ea, labore rerum. Doloribus tenetur quae impedit adipisci, laborum dolorum eaque ratione quaerat, eos dicta consequuntur atque ex facere voluptate cupiditate incidunt.</p>
                            <p>New Lorem ipsum dolor sit amet, consectetur adipisicing elit. Non a voluptatibus, ex odit totam cumque nihil eos asperiores ea, labore rerum. Doloribus tenetur quae impedit adipisci, laborum dolorum eaque ratione quaerat, eos dicta consequuntur atque ex facere voluptate cupiditate incidunt.</p>
                        </li>
                        <li data-content="pending-invitations">
                            <p>Gallery Lorem ipsum dolor sit amet, consectetur adipisicing elit. Cumque tenetur aut, cupiditate, libero eius rerum incidunt dolorem quo in officia.</p>
                            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. A ipsa vero, culpa doloremque voluptatum consectetur mollitia, atque expedita unde excepturi id, molestias maiores delectus quos molestiae. Ab iure provident adipisci eveniet quisquam ratione libero nam inventore error pariatur optio facilis assumenda sint atque cumque, omnis perspiciatis. Maxime minus quam voluptatum provident aliquam voluptatibus vel rerum. Soluta nulla tempora aspernatur maiores! Animi accusamus officiis neque exercitationem dolore ipsum maiores delectus asperiores reprehenderit pariatur placeat, quaerat sed illum optio qui enim odio temporibus, nulla nihil nemo quod dicta consectetur obcaecati vel. Perspiciatis animi corrupti quidem fugit deleniti, atque mollitia labore excepturi ut.</p>
                        </li>
                        <li data-content="technology">
                            <p>Store Lorem ipsum dolor sit amet, consectetur adipisicing elit. Earum recusandae rem animi accusamus quisquam reprehenderit sed voluptates, numquam, quibusdam velit dolores repellendus tempora corrupti accusantium obcaecati voluptate totam eveniet laboriosam?</p>
                            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Eligendi, enim, pariatur. Ab assumenda, accusantium! Consequatur magni placeat quae eos dicta, cum expedita sunt facilis est impedit possimus dolorum sequi nostrum nobis sit praesentium molestias nulla laudantium fugit corporis nam ut saepe harum ipsam? Debitis accusantium, omnis repudiandae modi, distinctio illo voluptatibus aperiam odio veritatis, quam perferendis eaque ullam. Temporibus tempore ad voluptates explicabo ea sit deleniti ipsum quos dolores tempora odio, ab corporis molestiae, eaque optio, perferendis! Cumque libero quia modi! Totam magni rerum id iusto explicabo distinctio, magnam, labore sed nemo expedita velit quam, perspiciatis non temporibus sit minus voluptatum. Iste, cumque sunt suscipit facere iusto asperiores, ullam dolorum excepturi quidem ea quibusdam deserunt illo. Nesciunt voluptates repellat ipsam.</p>
                        </li>
                        <li data-content="participated-events">
                            <p>Settings Lorem ipsum dolor sit amet, consectetur adipisicing elit. Laboriosam nam magni, ullam nihil a suscipit, ex blanditiis, adipisci tempore deserunt maiores. Nostrum officia, ratione enim eaque nihil quis ea, officiis iusto repellendus. Animi illo in hic, maxime deserunt unde atque a nesciunt? Non odio quidem deserunt animi quod impedit nam, voluptates eum, voluptate consequuntur sit vel, et exercitationem sint atque dolores libero dolorem accusamus ratione iste tenetur possimus excepturi. Accusamus vero, dignissimos beatae tempore mollitia officia voluptate quam animi vitae.</p>
                            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Similique ipsam eum reprehenderit minima at sapiente ad ipsum animi doloremque blanditiis unde omnis, velit molestiae voluptas placeat qui provident ab facilis.</p>
                        </li>
                    </ul>
                    <!-- Tabs Content --></div>
            </div>
        </div>
    </section>
    <!-- End Content Section -->

    <!-- Footer -->
    <jsp:include page="footer.jsp"/>
    <!-- End Footer -->
</div>
</body>
</html>
