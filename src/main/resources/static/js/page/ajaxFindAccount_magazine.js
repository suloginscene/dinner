const ajaxFindAccount_magazine = function () {

    const template = document.querySelector("#account-template").innerHTML;
    const position = $('#found-position');

    const render = function (account) {
        return account ?
            template
                .replace("{name}", account.username)
                .replace("{email}", account.email)
                .replace("{username}", account.username)
            : "<div>사용자가 존재하지 않습니다.</div>";
    };

    const replace = function (element) {
        position.empty();
        position.append(element);
    };

    const findAccount = function (e) {
        e.preventDefault();

        const queryString = $("#username-form").serialize();
        const current = queryString.split('&')[0].substring("current=".length);
        const username = queryString.split('&')[1].substring("username=".length);

        if (username.length < 2 || username.length > 16) {
            replace("<div>사용자 이름은 2자에서 16자 사이입니다.</div>");
            return;
        }

        if (username === current) {
            replace("<div>매거진 관리자는 허가 없이 글을 쓸 수 있습니다.</div>");
            return;
        }

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
