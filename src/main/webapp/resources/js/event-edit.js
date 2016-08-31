/**
 * Created by hermine on 8/27/16.
 */
$(document).ready(function () {
    $("#start_date").datepicker({
        dateFormat: 'dd:mm:yyyy'
    });
    $("#end_date").datepicker({
        dateFormat: 'dd:mm:yyyy'
    });
    $('#start_time').timepicker();
    $('#end_time').timepicker();
})