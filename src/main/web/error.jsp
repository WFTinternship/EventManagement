<%--
  Created by IntelliJ IDEA.
  User: hermine
  Date: 8/8/16
  Time: 6:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Event Management</title>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="css/reset.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">

    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.structure.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.theme.min.css">

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/event_list.js"></script>


</head>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <section class="content_section">
        <div class="container row_spacer clearfix">
            <div class="content">
                <div class="main_desc centered">
                    <p><b>Ooopps.!</b> The Page you were looking for doesn't exist</p>
                </div>
                <div class="my_col_third on_the_center">
                    <div class="search_block large_search">
                        <form class="widget_search" method="get" action="">
                            <input class="serch_input" name="s" id="s" placeholder="Search for Other Pages..."
                                   type="search">
                            <button type="submit" id="searchsubmit" class="search_btn">
                                <i class="ico-search2"></i>
                            </button>
                            <div class="clear"></div>
                        </form>
                    </div>
                </div>
                <div class="page404">
                    <span>404<span class="face404"></span></span>
                </div>
                <div class="centered">
                    <a href="#" target="_self" class="btn_a medium_btn bottom_space">
                        <span><i class="in_left ico-home5"></i><span>Back To Home Page</span><i
                                class="in_right ico-home5"></i></span>
                    </a>
                </div>
            </div>
        </div>
    </section>
</div>
</body>
</html>
