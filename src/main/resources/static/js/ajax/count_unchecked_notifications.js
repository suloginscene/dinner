window.addEventListener('load', function () {

    const logUncheckedCount = function (count) {
        const $count = $('#notification-count');
        if (count !== 0) {
            $count.text(count);
        } else {
            $count.text("");
        }
    };


    $.ajax({
        type: 'get',
        url: '/api/notifications/count-unchecked',
        success: logUncheckedCount
    });

});
