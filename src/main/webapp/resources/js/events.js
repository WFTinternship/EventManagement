/**
 * Created by hermine on 8/12/16.
 */
$(document).ready(function () {
    //alert('aaa');
    //  loadAllEvents();
});

function getEventsByCategory(categoryId) {

    $.getJSON("/events-ajax?categoryId=" + categoryId, function (data) {
        if (data.status == "SUCCESS") {
            var events = [];
            $.each(data.result, function (key, event) {
                var eventHTML = createEventItem(event)
                events.push(eventHTML);
            });

            if (events != "") {
                $("#event_list").html(events.join(""));
            } else {
                $("#event_list").html("There are no events in this category.");
            }
        }
    });

    return true;
}

function loadAllEvents() {

    $.getJSON("/event-controller", function (data) {
        var events = [];
        $.each(data, function (key, event) {
            var eventHTML = createEventItem(event)
            events.push(eventHTML);
        });

        $("<div/>", {
            "class": "list",
            html: events.join("")
        }).appendTo("#event_list");
    });
}

function createEventItem(event) {

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

    return eventHTML;
}