/**
 * Created by hermine on 8/27/16.
 */
$(document).ready(function () {

    $("#start_date").datepicker({
        dateFormat: 'dd/mm/yy'
        //dateFormat: 'yy-mm-dd'
    });
    $("#end_date").datepicker({
        dateFormat: 'dd/mm/yy'
    });
    $('#start_time').timepicker({
        timeFormat: 'HH:mm',
        startTime: new Date(0, 0, 0, 9, 0, 0),
        interval: 15
    });
    $('#end_time').timepicker({
        timeFormat: 'HH:mm',
        startTime: new Date(0, 0, 0, 9, 0, 0),
        interval: 15
    });

    $('#add_invitation_btn').click(function (e) {

        var email = $('#invitation_input').val();
        $("#guests_list").css("display", "block");

        var str = "<i class='lf_icon icon-user'></i><div class='invitation-item'>" + email + "</div>";

        $('#invitation_list').append(str);
        // var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    });

})