$(document).ready(function () {

    $.getJSON("/home", function (data) {
        var events = [];
        $.each(data, function (key, event) {

            var eventHTML = '<div class="list_item"><div class="list_content">' +
                '<h6 class="title"><a href="#">' + event.title + '</a></h6>' +
                '<span class="meta">' +
                '<span class="meta_part "><a href="#"><i class="ev_icon icon-clock"></i><span>' + event.startDate +
                '</span></a></span>' +
                '<span class="meta_part"><a href="#"><i class="ev_icon icon-map-marker"></i><span>' + event.location +
                '</span></a></span>' +
                '<span class="meta_part"><i class="ev_icon icon-folder"></i><span><a href="#">' + event.category.title +
                '</a></span></span>' +
                '<span class="meta_part"><a href="#"><i class="ev_icon icon-user"></i><span>Event Organizer' +
                '</span></a></span>' +
                '</span>' +
                '<p class="desc">' + event.shortDescription + '</p>' +
                '<a class="btn" href="#"><span>Details</span></a>' +
                '</div></div>';

            events.push(eventHTML);
        });
        $("<div/>", {
            "class": "my-new-list",
            html: events.join("")
        }).appendTo("#event_list");
    });

    $('#login_form').on('submit', function (e) {
        e.preventDefault();
        var username = $('#username').val();
        var password = $('#password').val();

        $.ajax({
            type: 'POST',
            data: {
                username: username,
                password: password
            },
            url: '/login',
            success: function (result) {
                window.location = "/index.jsp";
            },
            error: function () {
            }
        })
    })


});
