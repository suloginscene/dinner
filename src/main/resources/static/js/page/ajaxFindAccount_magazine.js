const ajaxFindAccount_magazine = function () {

    const template = document.querySelector("#account-template").innerHTML;
    const position = $('#found-position');

    const render = function (account) {
        return account ?
            template.replace("{username}", account.username).replace("{email}", account.email)
            : "<div>사용자가 존재하지 않습니다.</div>";
    };

    const replace = function (element) {
        position.empty();
        position.append(element);
    };

    const findAccount = function (e) {
        e.preventDefault();
        const username = $("#username-form").serialize().substring("username=".length);

        $.ajax({
            type: 'get',
            url: '/api/accounts/' + username,
            success: function (account) {
                replace(render(account));
            },
            error: function () {
                replace(render());
            }
        });
    };


    $("#findByUsername").click(findAccount);

};


window.addEventListener('load', ajaxFindAccount_magazine);
