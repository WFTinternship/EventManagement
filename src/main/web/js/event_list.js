$(document).ready(function () {
    var new_item = $('#template_item').clone();
    new_item.removeAttr('id');

    $('#event_list').append(new_item);

});