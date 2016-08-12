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


    // Specify the validation error messages
    /*  messages: {
     firstname: "Please enter your first name",
     lastname: "Please enter your last name",
     password: {
     required: "Please provide a password",
     minlength: "Your password must be at least 5 characters long"
            },
     email: "Please enter a valid email address",
     agree: "Please accept our policy"
     },

     });*/




});
