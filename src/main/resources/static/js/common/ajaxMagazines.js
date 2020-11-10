const ajaxMagazines = function () {

    const onSuccess = function (data) {
        console.log("success!")
        console.log(data)
    }

    console.log("get!")

    $.ajax({
        type: 'get',
        url: '/api/magazines',
        success: onSuccess,
    })

}

window.addEventListener('load', ajaxMagazines);
