const paper_alert = function (buttonSelector, message) {

    const doAlert = function () {
        alert(message);
    };


    $(buttonSelector).click(doAlert);

};
