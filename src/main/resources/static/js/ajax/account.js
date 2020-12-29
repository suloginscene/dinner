window.addEventListener('load', function () {

    const template = document.querySelector("#account-template").innerHTML;
    const position = $('#found-position');

    const onSuccess = function (account) {
        const el = template
            .replace("{username}", account.username)
            .replace("{username}", account.username)
            .replace("{username}", account.username);

        position.empty();
        position.append(el);
    };

    const onError = function () {
        const el = "<div>사용자가 존재하지 않습니다.</div>";

        position.empty();
        position.append(el);
    };

    const findAccount = function (e) {
        e.preventDefault();

        const queryString = $("#username-form").serialize();
        const username = queryString.substring("username=".length);

        $.ajax({
            type: 'get',
            url: '/api/accounts/' + username,
            success: onSuccess,
            error: onError
        });
    };


    $("#findByUsername").click(findAccount);

});
